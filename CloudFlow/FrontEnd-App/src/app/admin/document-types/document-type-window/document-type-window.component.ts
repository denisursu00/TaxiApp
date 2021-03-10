import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from "@angular/core";
import { 
	AppError, 
	MessageDisplayer, 
	TranslateUtils,
	DocumentTypeService, 
	DocumentTypeModel, 
	ObjectUtils,
	ArrayUtils,
	BooleanUtils,
	WorkflowService,
	DocumentService,
	WorkflowStateModel,
	MetadataDefinitionModel,
	MetadataNomenclatorUiAttributeModel,
	ConfirmationWindowFacade,
	BaseWindow
} from "@app/shared";
import { DocumentTypeGeneralTabContentComponent } from "./document-type-general-tab-content/document-type-general-tab-content.component";
import { DocumentTypeInitiatorsTabContentComponent } from "./document-type-initiators-tab-content/document-type-initiators-tab-content.component";
import { DocumentTypeAttachmentsTabContentComponent } from "./document-type-attachments-tab-content/document-type-attachments-tab-content.component";
import { DocumentTypeMetadataTabContentComponent } from "./document-type-metadata-tab-content/document-type-metadata-tab-content.component";
import { DocumentTypeEditInfo } from "./document-type-edit-info";
import { DocumentTypeTabContent } from "./document-type-tab-content";
import { Message } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-document-type-window",
	templateUrl: "./document-type-window.component.html"
})
export class DocumentTypeWindowComponent extends BaseWindow implements OnInit {

	private static readonly GENERAL_TAB_INDEX: number = 0;
	private static readonly INITIATORS_TAB_INDEX: number = 1;
	private static readonly ATTACHMENTS_TAB_INDEX: number = 2;
	private static readonly METADATA_TAB_INDEX: number = 3;

	@Input()
	public documentTypeId: number;

	@Input()
	public mode: "add" | "edit";
	
	@Output()
	private windowClosed: EventEmitter<void>;

	@Output()
	private dataSaved: EventEmitter<void>;

	@ViewChild(DocumentTypeGeneralTabContentComponent)
	private generalTabContent: DocumentTypeGeneralTabContentComponent;

	@ViewChild(DocumentTypeInitiatorsTabContentComponent)
	private initiatorsTabContent: DocumentTypeInitiatorsTabContentComponent;

	@ViewChild(DocumentTypeAttachmentsTabContentComponent)
	private attachmentsTabContent: DocumentTypeAttachmentsTabContentComponent;

	@ViewChild(DocumentTypeMetadataTabContentComponent)
	private metadataTabContent: DocumentTypeMetadataTabContentComponent;

	private documentTypeService: DocumentTypeService;
	private documentService: DocumentService;
	private workflowService: WorkflowService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;

	public title: string;

	public tabContentVisible: boolean = false;
	public tabContentMode: string;
	public tabContentInputData: DocumentTypeEditInfo;

	public saveActionEnabled: boolean;
	public cancelActionEnabled: boolean;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public activeTabIndex: number;
	
	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(documentTypeService: DocumentTypeService, documentService: DocumentService,
			workflowService: WorkflowService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		super();
		this.documentTypeService = documentTypeService;
		this.documentService = documentService;
		this.workflowService = workflowService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.activeTabIndex = DocumentTypeWindowComponent.GENERAL_TAB_INDEX;
		this.confirmationWindow = new ConfirmationWindowFacade();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareByMode();
	}

	private reset(): void {
		this.tabContentVisible = false;
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
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.tabContentMode = this.mode;
		this.tabContentInputData = null;
		this.tabContentVisible = true;
		this.setTitle("Untitled");
		this.openWindow();
	}

	private prepareForEdit(): void {
		// TODO - N-ar fi rau de facut o metoda care sa ia toate aceste informatii dintr-un singur apel api.
		this.lock();		
		this.documentTypeService.getDocumentTypeById(this.documentTypeId, {
			onSuccess: (documentType: DocumentTypeModel) => {
				this.workflowService.getWorkflowStatesByDocumentType(documentType.id, {
					onSuccess: (workflowStates: WorkflowStateModel[]) => {
						this.tabContentMode = this.mode;
						this.tabContentInputData = {
							documentType: documentType,
							workflowStates: workflowStates
						};
						this.tabContentVisible = true;
						this.setTitle(documentType.name);
						this.openWindow();
					},
					onFailure: (error: AppError) => {
						this.messageDisplayer.displayAppError(error);
						this.closeWindow();
					}
				});
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow();
			}
		});
	}

	private setTitle(documentTypeName: string): void {
		this.title = this.translateUtils.translateLabel("DOC_TYPE") + ": " + documentTypeName;		
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private prepareDocumentType(): DocumentTypeModel {

		let documentType: DocumentTypeModel = new DocumentTypeModel();

		this.generalTabContent.populateDocumentType(documentType);
		this.initiatorsTabContent.populateDocumentType(documentType);
		this.attachmentsTabContent.populateDocumentType(documentType);
		this.metadataTabContent.populateDocumentType(documentType);
		
		return documentType;
	}

	private isValid(): boolean {
		let isValid: boolean = true;
		if (!this.metadataTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentTypeWindowComponent.METADATA_TAB_INDEX;
		}
		if (!this.attachmentsTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentTypeWindowComponent.ATTACHMENTS_TAB_INDEX;
		}
		if (!this.initiatorsTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentTypeWindowComponent.INITIATORS_TAB_INDEX;
		}
		if (!this.generalTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = DocumentTypeWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}

	public onSaveAction(event: any): void {		
		if (!this.isValid()) {
			this.displayValidationMessages();
			return;
		}
		
		let documentType: DocumentTypeModel = this.prepareDocumentType();

		if (this.isAdd()) {
			this.documentTypeService.existDocumentTypeWithName(documentType.name, {
				onSuccess: (exists: boolean): void => {
					if (BooleanUtils.isTrue(exists)) {
						this.messageDisplayer.displayError("DOCUMENT_TYPE_WITH_SAME_NAME_EXISTS");
					} else {
						this.saveDocumentType(documentType);
					}
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
			
		} else if (this.isEdit()) {
			this.lock();
			this.documentService.existDocumentsOfType(this.documentTypeId, {
				onSuccess: (exist: boolean) => {
					this.unlock();
					if (BooleanUtils.isTrue(exist)) {
						this.confirmationWindow.confirm({
							approve: () => {
								this.confirmationWindow.hide();
								this.saveDocumentType(documentType);
							},
							reject: () => {
								this.confirmationWindow.hide();
							}
						},  "CONFIRM_EDIT_DOCUMENT_TYPE_DOCUMENTS_EXIST");
					} else {
						this.saveDocumentType(documentType);
					}
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
					this.unlock();
				}
			});
		}
	}

	private saveDocumentType(documentType: DocumentTypeModel): void {
		
		this.lock();
		this.documentTypeService.saveDocumentType(documentType, {
			onSuccess: (): void => {
				this.unlock();
				this.messageDisplayer.displaySuccess("DOCUMENT_TYPE_SAVED");				
				this.dataSaved.emit();
				this.closeWindow();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onCancelAction(event: any): void {
		this.closeWindow();
	}

	private displayValidationMessages(): void {
		this.messagesWindowMessages = [];
		this.addMessagesFromTabContent(this.generalTabContent);
		this.addMessagesFromTabContent(this.initiatorsTabContent);
		this.addMessagesFromTabContent(this.attachmentsTabContent);
		this.addMessagesFromTabContent(this.metadataTabContent);
		this.messagesWindowVisible = true;
	}

	private addMessagesFromTabContent(documentTypeTabContent: DocumentTypeTabContent) {
		if (ObjectUtils.isNullOrUndefined(documentTypeTabContent)) {
			return;
		}
		let tabMessages: Message[] = documentTypeTabContent.getMessages();
		if (ArrayUtils.isEmpty(tabMessages)) {
			return;
		}
		tabMessages.forEach((tabMessage: Message) => {
			this.messagesWindowMessages.push(tabMessage);
		});
	}

	public onMessagesWindowClosed(event: any) {
		this.messagesWindowVisible = false;
	}
}