import { Component, OnInit, ViewChild, Input, Output, EventEmitter, AfterViewInit } from "@angular/core";
import { 
	AppError, 
	MessageDisplayer, 
	TranslateUtils,
	IconConstants,
	DocumentTypeModel, 
	MetadataDefinitionModel, 
	ListMetadataItemModel, 
	DocumentService, 
	ObjectUtils,
	ArrayUtils,
	BooleanUtils,
	NavigationUtils,
	UrlBuilder,
	Message,
	DownloadUtils,
	ExportType,
	ConfirmationCallback,
	ConfirmationWindowFacade,
	BaseWindow,
	AdminUpdateDocumentModel,
	AdminUpdateDocumentBundleModel,
	MessageType
} from "@app/shared";
import { AdminUpdateDocumentWindowGeneralTabContentComponent } from "./general-tab-content/general-tab-content.component";
import { AdminUpdateDocumentWindowAttachmentsTabContentComponent } from "./attachments-tab-content/attachments-tab-content.component";
import { AdminUpdateDocumentWindowInputData } from "./admin-update-document-window-input-data";
import { AdminUpdateDocumentWindowTabContent } from "./admin-update-document-window-tab-content";
import { AdminUpdateDocumentWindowTabInputData } from "./admin-update-document-window-tab-input-data";

@Component({
	selector: "app-admin-update-document-window",
	templateUrl: "./admin-update-document-window.component.html"
})
export class AdminUpdateDocumentWindowComponent extends BaseWindow implements OnInit {

	private static readonly GENERAL_TAB_INDEX: number = 0;
	private static readonly ATTACHMENTS_TAB_INDEX: number = 1;

	@Input()
	public inputData: AdminUpdateDocumentWindowInputData;

	@Output()
	private windowClosed: EventEmitter<void>;

	@ViewChild(AdminUpdateDocumentWindowGeneralTabContentComponent)
	private generalTabContent: AdminUpdateDocumentWindowGeneralTabContentComponent;

	@ViewChild(AdminUpdateDocumentWindowAttachmentsTabContentComponent)
	private attachmentsTabContent: AdminUpdateDocumentWindowAttachmentsTabContentComponent;

	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;

	public title: string;

	public activeTabIndex: number;
	
	public tabContentVisible: boolean = false;
	public tabContentInputData: AdminUpdateDocumentWindowTabInputData;

	public documentMessagesWindowVisible: boolean = false;
	public documentMessagesWindowMessages: Message[];

	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(documentService: DocumentService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		super();
		this.documentService = documentService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.activeTabIndex = AdminUpdateDocumentWindowComponent.GENERAL_TAB_INDEX;
		this.confirmationWindow = new ConfirmationWindowFacade();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareForEdit();
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
	
	private prepareForEdit() {
		this.reset();
		this.lock();
		this.documentService.getAdminUpdateDocumentBundle(this.inputData.documentId, this.inputData.documentLocationRealName, {
			onSuccess: (documentBundle: AdminUpdateDocumentBundleModel) => {
				this.tabContentInputData = {
					document: documentBundle.document,
					documentType: documentBundle.documentType
				};
				this.updatePerspective();
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

	private updatePerspective(): void {
		this.setTitle(this.tabContentInputData.document.documentName);
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private prepareDocumentModel(): AdminUpdateDocumentModel {

		let document: AdminUpdateDocumentModel = new AdminUpdateDocumentModel();	
		
		document.id = this.tabContentInputData.document.id;			
		document.documentTypeId = this.tabContentInputData.documentType.id;
		document.documentLocationRealName = this.tabContentInputData.document.documentLocationRealName;

		this.generalTabContent.populateDocument(document);
		this.attachmentsTabContent.populateDocument(document);		
		
		return document;
	}

	private areTabsValid(): boolean {		
		let isValid: boolean = true;
		if (!this.attachmentsTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = AdminUpdateDocumentWindowComponent.ATTACHMENTS_TAB_INDEX;
		}
		if (!this.generalTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = AdminUpdateDocumentWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}

	private displayValidationMessages(): void {
		this.documentMessagesWindowMessages = [];	
		this.addMessagesFromTabContent(this.generalTabContent);
		this.addMessagesFromTabContent(this.attachmentsTabContent);
		this.documentMessagesWindowVisible = true;
	}

	private addMessagesFromTabContent(documentTabContent: AdminUpdateDocumentWindowTabContent) {
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
	
	private validateDocument(callback: (valid: boolean) => void): void {
		if (!this.areTabsValid()) {
			this.displayValidationMessages();
			callback(false);
		} else {
			callback(true);
		}		
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}

	public onSaveAction(event: any): void {
		this.lock();					
		this.validateDocument((valid: boolean): void => {
			if (valid) {
				this.unlock();
				this.confirmationWindow.confirm({
					approve: (): void => {
						
						this.lock();
						let document: AdminUpdateDocumentModel = this.prepareDocumentModel();
						this.documentService.updateDocument(document, {
							onSuccess: (documentId: string): void => {
								this.messageDisplayer.displaySuccess("DOCUMENT_SAVED");				
								this.closeWindow();
							},
							onFailure: (error: AppError): void => {
								this.unlock();
								this.messageDisplayer.displayAppError(error);
							}
						});

					},
					reject: (): void => {
						this.confirmationWindow.hide();
						this.unlock();
					}
				}, "ADMIN_UPDATE_DOCUMENT_CONFIRM_SAVE", true, MessageType.WARN);
			} else {
				this.unlock();
			}
		});
	}
}