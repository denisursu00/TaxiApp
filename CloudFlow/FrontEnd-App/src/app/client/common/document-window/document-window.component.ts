import { Component, OnInit, ViewChild, Input, Output, EventEmitter, AfterViewInit } from "@angular/core";
import { 
	AppError, 
	MessageDisplayer, 
	TranslateUtils,
	IconConstants, 
	DocumentTypeService, 
	DocumentTypeModel, 
	MetadataDefinitionModel, 
	ListMetadataItemModel, 
	DocumentService, 
	DocumentAddBundleModel, 
	DocumentTemplateModel,
	DocumentModel,
	DocumentViewOrEditBundleModel,
	ObjectUtils,
	ArrayUtils,
	BooleanUtils,
	DocumentViewVersionBundleModel,
	SendDocumentToWorkflowRequestModel,
	WorkflowInstanceResponseModel,
	NavigationUtils,
	UrlBuilder,
	Message,
	ServletPathConstants,
	DocumentValidationResponseModel,
	DocumentValidationRequestModel,
	DownloadUtils,
	ExportType,
	SendDocumentToWorkflowResponseModel,
	ConfirmationCallback,
	ConfirmationWindowFacade,
	BaseWindow,
	DocumentConstants,
	ProjectService,
	MetadataInstanceModel
} from "@app/shared";
import { DocumentGeneralTabContentComponent } from "./document-general-tab-content/document-general-tab-content.component";
import { DocumentAttachmentsTabContentComponent } from "./document-attachments-tab-content/document-attachments-tab-content.component";
import { DocumentSecurityTabContentComponent } from "./document-security-tab-content/document-security-tab-content.component";
import { DocumentVersionsTabContentComponent } from "./document-versions-tab-content/document-versions-tab-content.component";
import { DocumentHistoryTabContentComponent } from "./document-history-tab-content/document-history-tab-content.component";
import { DocumentWorkflowGraphTabContentComponent } from "./document-workflow-graph-tab-content/document-workflow-graph-tab-content.component";
import { DocumentWindowInputData } from "./document-window-input-data";
import { DocumentAddInfo } from "./document-add-info";
import { DocumentViewOrEditInfo } from "./document-view-or-edit-info";
import { DocumentTabContent } from "./document-tab-content";
import { environment } from "@app/../environments/environment";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";
import { Dialog } from "primeng/primeng";
import { exists } from "fs";

@Component({
	selector: "app-document-window",
	templateUrl: "./document-window.component.html"
})
export class DocumentWindowComponent extends BaseWindow implements OnInit {

	private static readonly GENERAL_TAB_INDEX: number = 0;
	private static readonly ATTACHMENTS_TAB_INDEX: number = 1;
	private static readonly SECURITY_TAB_INDEX: number = 2;
	private static readonly HISTORY_TAB_INDEX: number = 3;
	private static readonly VERSIONS_TAB_INDEX: number = 4;
	private static readonly WORKFLOW_GRAPH_TAB_INDEX: number = 5;

	@Input()
	public inputData: DocumentWindowInputData;

	@Input()
	public mode: "add" | "viewOrEdit" | "viewVersion";
	
	@Output()
	private windowClosed: EventEmitter<void>;

	@ViewChild(DocumentGeneralTabContentComponent)
	private generalTabContent: DocumentGeneralTabContentComponent;

	@ViewChild(DocumentAttachmentsTabContentComponent)
	private attachmentsTabContent: DocumentAttachmentsTabContentComponent;

	@ViewChild(DocumentSecurityTabContentComponent)
	private securityTabContent: DocumentSecurityTabContentComponent;

	@ViewChild(DocumentVersionsTabContentComponent)
	private versionsTabContent: DocumentVersionsTabContentComponent;

	@ViewChild(DocumentHistoryTabContentComponent)
	private historyTabContent: DocumentHistoryTabContentComponent;
	
	@ViewChild(DocumentWorkflowGraphTabContentComponent)
	private workflowGraphTabContent: DocumentWorkflowGraphTabContentComponent;

	private documentTypeService: DocumentTypeService;
	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private projectService: ProjectService;

	public windowVisible: boolean = false;

	public title: string;

	public activeTabIndex: number;
	
	public tabContentVisible: boolean = false;
	public tabContentMode: string;
	public tabContentInputData: DocumentAddInfo | DocumentViewOrEditInfo;
	public tabContentReadonly: boolean;

	public historyTabEnabled: boolean;
	public versionsTabEnabled: boolean;
	public workflowGraphTabEnabled: boolean;

	public editActionEnabled: boolean;
	public closeActionEnabled: boolean;
	public saveAndCloseActionEnabled: boolean;
	public saveActionEnabled: boolean;
	public sendActionEnabled: boolean;
	public exportActionEnabled: boolean;
	public cancelActionEnabled: boolean;

	public documentMessagesWindowVisible: boolean = false;
	public documentMessagesWindowMessages: Message[];

	public chooseWorkflowDestinationUserWindowVisible: boolean = false;
	public chooseWorkflowTransitionWindowVisible: boolean = false;
	public chooseWorkflowTransitionWindowTransitions: string[];

	public chooseDocumentTemplateWindowVisible: boolean = false;
	public chooseDocumentTemplateWindowTemplates: DocumentTemplateModel[];

	public viewDocumentVersionWindowVisible: boolean;
	public viewDocumentVersionWindowInputData: DocumentWindowInputData;
	public viewDocumentVersionWindowMode: string;
	
	private sendDocumentToWorkflowRequest: SendDocumentToWorkflowRequestModel = null;

	private businessValidationMessages: Message[];

	private chosenExportType: ExportType;
	
	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(documentTypeService: DocumentTypeService, documentService: DocumentService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, projectService: ProjectService) {
		super();
		this.documentTypeService = documentTypeService;
		this.documentService = documentService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.activeTabIndex = DocumentWindowComponent.GENERAL_TAB_INDEX;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.projectService = projectService;
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareByMode();
	}
	
	private reset(): void {
		this.tabContentVisible = false;
		this.chooseWorkflowTransitionWindowTransitions = [];
		this.sendDocumentToWorkflowRequest = null;
	}

	private closeWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	private openWindow(): void {
		this.unlock();
		this.windowVisible = true;
	}

	public onTabChanged(event: any): void {
		this.activeTabIndex = event.index;
	}
	
	private prepareByMode() {
		this.reset();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isViewOrEdit()) {
			this.prepareForViewOrEdit();
		} else if (this.isViewVersion()) {
			this.prepareForViewVersion();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isViewOrEdit(): boolean {
		return this.mode === "viewOrEdit";
	}

	private isViewVersion(): boolean {
		return this.mode === "viewVersion";
	}

	private prepareForAdd(): void {
		this.lock();
		this.documentService.getDocumentAddBundle(this.inputData.documentTypeId.toString(), {
			onSuccess: (documentAddBundle: DocumentAddBundleModel): void => {
				this.tabContentMode = this.mode;
				this.tabContentInputData = {	
					documentType: documentAddBundle.documentType,
					documentLocationRealName: this.inputData.documentLocationRealName,
					parentFolderId: this.inputData.parentFolderId,
					workflow: documentAddBundle.workflow,
					currentState: documentAddBundle.currentState,
					canUserSend: documentAddBundle.canUserSend
				};
				this.tabContentReadonly = false;
				this.updatePerspectiveForAdd();
				this.tabContentVisible = true;
				this.openWindow();
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow();
			}
		});
	}

	private prepareForViewOrEdit(): void {
		this.lock();
		this.documentService.getDocumentViewOrEditBundle(this.inputData.documentId, this.inputData.documentLocationRealName, {
			onSuccess: (documentViewOrEditBundle: DocumentViewOrEditBundleModel) => {
				this.tabContentMode = this.mode;
				this.tabContentInputData = {
					documentType: documentViewOrEditBundle.documentType,
					document: documentViewOrEditBundle.document,
					workflow: documentViewOrEditBundle.workflow,
					currentState: documentViewOrEditBundle.workflowState,
					canUserSend: documentViewOrEditBundle.canUserSend,
					canUserEdit: documentViewOrEditBundle.canUserEdit,
					canUserAccessLockedDocument: documentViewOrEditBundle.canUserAccessLockedDocument
				};
				this.tabContentReadonly = true;
				if (this.tabContentInputData.canUserAccessLockedDocument) {
					this.tabContentReadonly = false;
				}
				this.updatePerspectiveForViewOrEdit();
				this.tabContentVisible = true;
				this.openWindow();		
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow();
			}
		});
	}

	private prepareForViewVersion(): void {
		this.lock();
		this.documentService.getDocumentViewVersionBundle(this.inputData.documentLocationRealName, this.inputData.documentId, this.inputData.versionNr, {
			onSuccess: (documentViewVersionBundle: DocumentViewVersionBundleModel) => {				
				this.tabContentMode = "viewOrEdit";
				this.tabContentInputData = {
					documentType: documentViewVersionBundle.documentType,
					document: documentViewVersionBundle.document,
					workflow: documentViewVersionBundle.workflow,
					currentState: documentViewVersionBundle.workflowState
				};
				this.tabContentReadonly = true;
				this.updatePerspectiveForViewVersion();
				this.tabContentVisible = true;
				this.openWindow();				
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow();
			}
		});
	}

	private setTitle(documentName: string): void {
		this.title = this.translateUtils.translateLabel("DOCUMENT") + ": " + documentName;		
	}

	private updatePerspectiveForAdd(): void {

		let documentAddInfo: DocumentAddInfo = <DocumentAddInfo> this.tabContentInputData;		
		this.setTitle(documentAddInfo.documentType.name);

		this.historyTabEnabled = false;
		this.versionsTabEnabled = false;
		this.workflowGraphTabEnabled = ObjectUtils.isNotNullOrUndefined(documentAddInfo.workflow);

		this.disableAllActions();
		this.closeActionEnabled = true;
		this.saveAndCloseActionEnabled = true;
		this.saveActionEnabled = true;
		this.sendActionEnabled = ObjectUtils.isNotNullOrUndefined(documentAddInfo.workflow) && documentAddInfo.canUserSend;	
		this.tabContentReadonly = false;
	}

	private updatePerspectiveForViewOrEdit(): void {

		let documentViewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.tabContentInputData;
		this.setTitle(documentViewOrEditInfo.document.documentName);

		this.historyTabEnabled = true;
		this.versionsTabEnabled = BooleanUtils.isTrue(documentViewOrEditInfo.documentType.keepAllVersions);
		this.workflowGraphTabEnabled = ObjectUtils.isNotNullOrUndefined(documentViewOrEditInfo.workflow);

		this.disableAllActions();
		this.closeActionEnabled = true;
		if (documentViewOrEditInfo.canUserAccessLockedDocument) {
			this.saveAndCloseActionEnabled = true;
			this.saveActionEnabled = true;
			this.sendActionEnabled = ObjectUtils.isNotNullOrUndefined(documentViewOrEditInfo.workflow) && documentViewOrEditInfo.canUserSend;
			this.exportActionEnabled = ArrayUtils.isNotEmpty(documentViewOrEditInfo.documentType.templates);
			this.cancelActionEnabled = BooleanUtils.isTrue(documentViewOrEditInfo.document.hasStableVersion);
			this.tabContentReadonly = false;
		} else {
			this.editActionEnabled = documentViewOrEditInfo.canUserEdit;
			this.sendActionEnabled = ObjectUtils.isNotNullOrUndefined(documentViewOrEditInfo.workflow) && documentViewOrEditInfo.canUserSend;
			this.exportActionEnabled = ArrayUtils.isNotEmpty(documentViewOrEditInfo.documentType.templates);	
			this.tabContentReadonly = true;
		}
	}

	private updatePerspectiveForViewVersion(): void {
		
		let documentViewVersionInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.tabContentInputData;
		this.setTitle(documentViewVersionInfo.document.documentName + " [" + documentViewVersionInfo.document.versionNumber + "]");

		this.historyTabEnabled = false;
		this.versionsTabEnabled = false;
		this.workflowGraphTabEnabled = ObjectUtils.isNotNullOrUndefined(documentViewVersionInfo.workflow);

		this.disableAllActions();
		this.closeActionEnabled = true;
		this.tabContentReadonly = true;
	}

	private disableAllActions() {
		this.editActionEnabled = false;
		this.closeActionEnabled = false;
		this.saveAndCloseActionEnabled = false;
		this.saveActionEnabled = false;
		this.sendActionEnabled = false;
		this.exportActionEnabled = false;
		this.cancelActionEnabled = false;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private prepareDocument(): DocumentModel {

		let document: DocumentModel = new DocumentModel();		
		if (this.isAdd()) {
			let documentAddInfo: DocumentAddInfo = <DocumentAddInfo> this.tabContentInputData;
			document.id = null;
			document.documentTypeId = documentAddInfo.documentType.id;
			document.documentLocationRealName = documentAddInfo.documentLocationRealName;
			document.parentFolderId = documentAddInfo.parentFolderId;
			document.workflowStateId = ObjectUtils.isNotNullOrUndefined(documentAddInfo.currentState) ? documentAddInfo.currentState.id : null;
		} else if (this.isViewOrEdit()) {		
			let documentViewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.tabContentInputData;	
			document.id = documentViewOrEditInfo.document.id;			
			document.documentTypeId = documentViewOrEditInfo.documentType.id;
			document.documentLocationRealName = documentViewOrEditInfo.document.documentLocationRealName;
			document.parentFolderId = documentViewOrEditInfo.document.parentFolderId;			
			document.workflowStateId = documentViewOrEditInfo.document.workflowStateId;
			document.author = documentViewOrEditInfo.document.author;
			document.created = documentViewOrEditInfo.document.created;
		} else {
			throw new Error("Valoarea pentru mode [" + this.mode + "] este invalida/incompatibila acestei metode.");
		}

		this.generalTabContent.populateDocument(document);
		this.attachmentsTabContent.populateDocument(document);		
		this.securityTabContent.populateDocument(document);
		if (ObjectUtils.isNotNullOrUndefined(this.historyTabContent)) {
			this.historyTabContent.populateDocument(document);
		}		
		if (ObjectUtils.isNotNullOrUndefined(this.versionsTabContent)) {
			this.versionsTabContent.populateDocument(document);
		}
		if (ObjectUtils.isNotNullOrUndefined(this.workflowGraphTabContent)) {
			this.workflowGraphTabContent.populateDocument(document);
		}
		
		return document;
	}

	private areTabsValid(): boolean {		
		let isValid: boolean = true;
		const isWorkflowGraphTabContentValid = (ObjectUtils.isNotNullOrUndefined(this.workflowGraphTabContent) ? this.workflowGraphTabContent.isValid() : true);
		if (!isWorkflowGraphTabContentValid) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.WORKFLOW_GRAPH_TAB_INDEX;
		}
		const isVersionsTabContentValid = (ObjectUtils.isNotNullOrUndefined(this.versionsTabContent) ? this.versionsTabContent.isValid() : true);
		if (!isVersionsTabContentValid) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.VERSIONS_TAB_INDEX;
		}
		const isHistoryTabContentValid = (ObjectUtils.isNotNullOrUndefined(this.historyTabContent) ? this.historyTabContent.isValid() : true);
		if (!isHistoryTabContentValid) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.HISTORY_TAB_INDEX;
		}
		if (!this.securityTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.SECURITY_TAB_INDEX;
		}
		if (!this.attachmentsTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.ATTACHMENTS_TAB_INDEX;
		}
		if (!this.generalTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}

	private displayValidationMessages(): void {
		this.documentMessagesWindowMessages = [];	
		this.addMessagesFromTabContent(this.generalTabContent);
		this.addMessagesFromTabContent(this.attachmentsTabContent);
		this.addMessagesFromTabContent(this.securityTabContent);
		this.addMessagesFromTabContent(this.workflowGraphTabContent);
		this.addMessagesFromTabContent(this.historyTabContent);
		this.addMessagesFromTabContent(this.versionsTabContent);
		if (ArrayUtils.isNotEmpty(this.businessValidationMessages)) {
			this.businessValidationMessages.forEach((message: Message) => {
				this.documentMessagesWindowMessages.push(message);
			});
		}
		this.documentMessagesWindowVisible = true;
	}

	private addMessagesFromTabContent(documentTabContent: DocumentTabContent) {
		if (ObjectUtils.isNullOrUndefined(documentTabContent)) {
			return;
		}
		let tabMessages: Message[] = documentTabContent.getMessages();
		if (ArrayUtils.isEmpty(tabMessages)) {
			return;
		}
		tabMessages.forEach((tabMessage: Message) => {
			this.documentMessagesWindowMessages.push(tabMessage);
		});
	}

	public onDocumentMessagesWindowClosed(event: any) {
		this.documentMessagesWindowVisible = false;
	}

	public onViewDocumentVersion(versionNr: string): void {		
		this.viewDocumentVersionWindowMode = "viewVersion";		
		this.viewDocumentVersionWindowInputData = new DocumentWindowInputData();
		this.viewDocumentVersionWindowInputData.documentId = this.inputData.documentId;
		this.viewDocumentVersionWindowInputData.documentLocationRealName = this.inputData.documentLocationRealName;
		this.viewDocumentVersionWindowInputData.versionNr = versionNr;
		this.viewDocumentVersionWindowVisible = true;
	}

	private onViewDocumentVersionWindowClosed(event: any) {
		this.viewDocumentVersionWindowVisible = false;
	}
	
	private validateDocument(callback: (valid: boolean) => void): void {
		
		this.businessValidationMessages = [];
		
		if (!this.areTabsValid()) {
			this.displayValidationMessages();
			callback(false);
			return;
		}

		let validationRequest: DocumentValidationRequestModel = new DocumentValidationRequestModel();
		validationRequest.documentId =  this.inputData.documentId;
		if (this.isAdd()) {
			let documentAddInfo: DocumentAddInfo = <DocumentAddInfo> this.tabContentInputData;
			validationRequest.documentTypeId = documentAddInfo.documentType.id;
			validationRequest.documentLocationRealName = documentAddInfo.documentLocationRealName;
		} else if (this.isViewOrEdit()) {		
			let documentViewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.tabContentInputData;			
			validationRequest.documentTypeId = documentViewOrEditInfo.documentType.id;
			validationRequest.documentLocationRealName = documentViewOrEditInfo.document.documentLocationRealName;
		}
		validationRequest.metatadaInstances = this.generalTabContent.getMetadataInstances();
		this.documentService.validateDocument(validationRequest, {
			onSuccess: (validationResponse: DocumentValidationResponseModel) => {				
				if (validationResponse.valid) {
					callback(true);
				} else {
					validationResponse.messages.forEach((message: string) => {
						this.businessValidationMessages.push(Message.createForError(message, false));
					});
					this.activeTabIndex = DocumentWindowComponent.GENERAL_TAB_INDEX;
					this.displayValidationMessages();
					callback(false);
				}
			},
			onFailure: (error: AppError) => {
				this.businessValidationMessages.push(Message.createForError(error.errorCode));
				this.displayValidationMessages();				
				callback(false);
			}
		});
	}

	public onEditAction(event: any): void {		
		this.lock();
		this.documentService.checkout(this.inputData.documentLocationRealName, this.inputData.documentId, {
			onSuccess: (): void => {			
				this.mode = "viewOrEdit";			
				this.prepareByMode();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});	
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}
	
	public onSaveAndCloseAction(event: any): void {		

		this.lock();
		this.validateDocument((valid: boolean): void => {
			if (valid) {
				let document: DocumentModel = this.prepareDocument();

				this.documentService.checkin(document, {
					onSuccess: (documentId: string): void => {
						this.messageDisplayer.displaySuccess("DOCUMENT_SAVED");
						this.closeWindow();
					},
					onFailure: (error: AppError): void => {
						this.unlock();
						this.messageDisplayer.displayAppError(error);
					}
				});
			} else {
				this.unlock();
			}
		});		
	}

	public onSaveAction(event: any): void {
		this.lock();			
		this.validateDocument((valid: boolean): void => {
			if (valid) {			
				let document: DocumentModel = this.prepareDocument();	

				this.documentService.save(document, {
					onSuccess: (documentId: string): void => {
						this.messageDisplayer.displaySuccess("DOCUMENT_SAVED");			
						this.mode = "viewOrEdit";
						this.inputData.documentId = documentId;				
						this.prepareByMode();
					},
					onFailure: (error: AppError): void => {
						this.unlock();
						this.messageDisplayer.displayAppError(error);
					}
				});	
			} else {
				this.unlock();
			}
		});
	}

	public onSendAction(event: any): void {
		this.lock();		
		this.validateDocument((valid: boolean): void => {
			if (valid) {
				this.sendDocumentToWorkflowRequest = new SendDocumentToWorkflowRequestModel();
				this.sendDocumentToWorkflowRequest.document = this.prepareDocument();
				this.sendDocumentToWorkflowRequest.workflowId = this.tabContentInputData.workflow.id;
				this.sendDocumentToWorkflowRequest.uiSendConfirmed = false;

				this.sendDocumentToWorkflow();
			} else {
				this.unlock();
			}
		});
	}

	private sendDocumentToWorkflow(): void {
		if (!this.isLock()) {
			this.lock();
		}
		this.documentService.sendDocumentToWorkflow(this.sendDocumentToWorkflowRequest, {
			onSuccess: (responseModel: SendDocumentToWorkflowResponseModel): void => {
				this.unlock();
				if (BooleanUtils.isTrue(responseModel.needUiSendConfirmation)) {					
					this.confirmationWindow.confirm({
						approve: () => {
							this.sendDocumentToWorkflowRequest.uiSendConfirmed = true;
							this.confirmationWindow.visible = false;
							this.sendDocumentToWorkflow();
						},
						reject: () => {
							this.sendDocumentToWorkflowRequest.uiSendConfirmed = false;
							this.confirmationWindow.hide();
						}
					}, "SEND_DOCUMENT_TO_WORKFLOW_CONFIRMATION_MESSAGE");
				} else {
					this.sendDocumentToWorkflowRequest.document = responseModel.workflowInstanceResponse.document;	
					this.sendDocumentToWorkflowRequest.manualAssignmentDestinationId = null;
					this.sendDocumentToWorkflowRequest.transitionName = responseModel.workflowInstanceResponse.chosenTransitionName;				
					if (ArrayUtils.isNotEmpty(responseModel.workflowInstanceResponse.candidateTransitionNames)) {
						this.unlock();
						this.chooseWorkflowTransitionWindowTransitions = responseModel.workflowInstanceResponse.candidateTransitionNames;
						this.chooseWorkflowTransitionWindowVisible = true;
					} else if (responseModel.workflowInstanceResponse.manualAssignment) {
						this.unlock();
						this.chooseWorkflowDestinationUserWindowVisible = true;
					} else {
						if (responseModel.workflowInstanceResponse.workflowFinished) {
							this.messageDisplayer.displaySuccess("DOCUMENT_WORKFLOW_FINISHED");
						} else {
							this.messageDisplayer.displaySuccess("DOCUMENT_SENT");
						}
						this.closeWindow();
					}
				}
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});	
	}

	private onChooseWorkflowDestinationUserWindowDestinationUserChose(userId: string): void {
		this.chooseWorkflowDestinationUserWindowVisible = false;
		this.sendDocumentToWorkflowRequest.manualAssignmentDestinationId = userId;
		this.sendDocumentToWorkflow();
	}

	private onChooseWorflowTransitionWindowTransitionChose(transition: string): void {
		this.chooseWorkflowTransitionWindowVisible = false;
		this.sendDocumentToWorkflowRequest.manualAssignmentDestinationId = null;
		this.sendDocumentToWorkflowRequest.transitionName = transition;
		this.sendDocumentToWorkflow();
	}

	public onExportAsDocxAction(event: any): void {
		this.exportDocumentAsDocx();
	}

	private exportDocumentAsDocx(): void {
		this.exportDocument(ExportType.DOCX);
	}

	private exportDocument(exportType: ExportType) {
		let templates: DocumentTemplateModel[] = this.tabContentInputData.documentType.templates;
		if (templates.length === 1) {
			this.exportAndSaveDocument(templates[0].name, exportType);
		} else if (templates.length > 1) {
			this.chosenExportType = exportType;
			this.chooseDocumentTemplateWindowTemplates = templates;
			this.chooseDocumentTemplateWindowVisible = true;
		} else {
			throw new Error("no templates available");
		}
	}

	private exportAndSaveDocument(templateName: string, exportType: ExportType): void {
		this.lock();
		if (ObjectUtils.isNullOrUndefined(exportType)) {
			exportType = ExportType.DOCX;
		}
		this.documentService.exportDocument(this.inputData.documentLocationRealName, this.inputData.documentId, templateName, exportType).subscribe(
			(response: HttpResponse<Blob>) => {
				this.unlock();
				DownloadUtils.saveFileFromResponse(response);
			}, 
			(error: any) => {
				this.unlock();
				this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
			}
		);
	}

	private onChooseDocumentTemplateWindowTemplateChose(template: DocumentTemplateModel): void {
		this.chooseDocumentTemplateWindowVisible = false;
		this.exportAndSaveDocument(template.name, this.chosenExportType);
	}

	private onChooseDocumentTemplateWindowCanceled(event: any) {
		this.chooseDocumentTemplateWindowVisible = false;
	}

	public onCancelAction(event: any): void {
		this.lock();
		this.documentService.undoCheckout(this.inputData.documentLocationRealName, this.inputData.documentId, {
			onSuccess: (): void => {		
				this.mode = "viewOrEdit";			
				this.prepareByMode();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});	
	}
}