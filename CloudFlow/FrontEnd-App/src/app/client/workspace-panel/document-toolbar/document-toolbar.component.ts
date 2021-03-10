import { Component, Output, EventEmitter } from "@angular/core";
import { MenuItem } from "primeng/components/common/menuitem";
import { DocumentTypeService, DocumentTypeModel, AppError, MessageDisplayer, StringUtils, DocumentService, FolderModel, ObjectUtils, ConfirmationWindowFacade, ClientPermissionEnum } from "@app/shared";
import { DocumentWindowInputData } from "./../../common/document-window";
import { AdminUpdateDocumentWindowInputData } from "@app/client/common/admin-update-document-window/admin-update-document-window-input-data";
import { AdminUpdateDocumentWindowTabContent } from "@app/client/common/admin-update-document-window/admin-update-document-window-tab-content";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-document-toolbar",
	templateUrl: "document-toolbar.component.html",
	styleUrls: [ "document-toolbar.component.css" ]
})
export class DocumentToolbarComponent {

	@Output()
	private documentActionPerformed: EventEmitter<void>;

	private documentTypeService: DocumentTypeService;
	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;

	private folder: FolderModel;
	private documentId: string;
	private documentLocationRealName: string;

	public isDisabledForAdd: boolean;
	public isDisabledForEdit: boolean;
	public isDisabledForDelete: boolean;

	public documentTypeMenuItems: MenuItem[];

	public documentWindowVisible: boolean;
	public documentWindowInputData: DocumentWindowInputData;
	public documentWindowMode: string;
	
	public adminUpdateDocumentWindowVisible: boolean;
	public adminUpdateDocumentWindowInputData: AdminUpdateDocumentWindowInputData;

	public confirmationWindow: ConfirmationWindowFacade;

	private authManager: AuthManager;

	public constructor(documentTypeService: DocumentTypeService, documentService: DocumentService, messageDisplayer: MessageDisplayer, 
			authManager: AuthManager) {
		this.documentTypeService = documentTypeService;
		this.documentService = documentService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.authManager = authManager;
		this.init();
	}
	
	private init(): void {
		this.documentWindowVisible = false;
		this.documentActionPerformed = new EventEmitter();
		this.disableAllActions();
	}

	public prepareDocumentTypeMenuItems(): void {
		this.documentTypeMenuItems = [];
		this.documentTypeService.getAvailableDocumentTypes({
			onSuccess: (documentTypes: DocumentTypeModel[]): void => {
				documentTypes.forEach(documentType => {
					this.addDocumentTypeMenuItem(documentType);
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private addDocumentTypeMenuItem(documentType: DocumentTypeModel): void {
		if (ObjectUtils.isNotNullOrUndefined(this.folder.documentTypeId)) {
			if (this.folder.documentTypeId === documentType.id) {
				this.documentTypeMenuItems.push(
					this.buildDocumentTypeMenuItem(documentType)
				);
			}
		} else {
			this.documentTypeMenuItems.push(
				this.buildDocumentTypeMenuItem(documentType)
			);
		}
	}

	private buildDocumentTypeMenuItem(documentType: DocumentTypeModel): MenuItem {
		let menuItem: MenuItem = {
			id: String(documentType.id),
			label: documentType.name,
			command: this.onDocumentTypeClick.bind(onclick, documentType, this)
		};
		return menuItem;
	}

	private onDocumentTypeClick(documentType: DocumentTypeModel, thisDocumentToolbar: DocumentToolbarComponent): void {
		thisDocumentToolbar.documentWindowMode = "add";		
		thisDocumentToolbar.documentWindowInputData = new DocumentWindowInputData();
		thisDocumentToolbar.documentWindowInputData.documentTypeId = documentType.id;
		thisDocumentToolbar.documentWindowInputData.documentLocationRealName = thisDocumentToolbar.documentLocationRealName;
		thisDocumentToolbar.documentWindowInputData.parentFolderId = thisDocumentToolbar.folder.id;		
		thisDocumentToolbar.documentWindowVisible = true;
	}

	public onDocumentDelete(): void {
		this.confirmationWindow.confirm({
			approve: () => {
				this.confirmationWindow.hide();
				this.deleteDocument();
			},
			reject: () => {
				this.confirmationWindow.hide();
			}
		}, "DELETE_CONFIRM");		
	}

	private deleteDocument(): void {
		this.documentService.deleteDocument(this.documentId, this.documentLocationRealName, {
			onSuccess: (): void => {
				this.documentActionPerformed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onDocumentEdit(event: any): void {
		this.documentWindowMode = "viewOrEdit";		
		this.documentWindowInputData = new DocumentWindowInputData();
		this.documentWindowInputData.documentId = this.documentId;
		this.documentWindowInputData.documentLocationRealName = this.documentLocationRealName;
		this.documentWindowVisible = true;
	}

	public onAdminUpdateDocument(): void {	
		this.adminUpdateDocumentWindowInputData = new AdminUpdateDocumentWindowInputData();
		this.adminUpdateDocumentWindowInputData.documentId = this.documentId;
		this.adminUpdateDocumentWindowInputData.documentLocationRealName = this.documentLocationRealName;
		this.adminUpdateDocumentWindowVisible = true;
	}

	private disableAllActions(): void {
		this.isDisabledForAdd = true;
		this.isDisabledForEdit = true;
		this.isDisabledForDelete = true;
	}

	private changePerspective(): void {
		this.disableAllActions();
		if (ObjectUtils.isNotNullOrUndefined(this.folder)) {
			this.isDisabledForAdd = false;
		}
		if (StringUtils.isNotBlank(this.documentId) && StringUtils.isNotBlank(this.documentLocationRealName)) {
			this.isDisabledForEdit = false;
			this.isDisabledForDelete = false;
		}
	}

	public setFolder(folder: FolderModel): void {
		this.folder = folder;
		if (ObjectUtils.isNotNullOrUndefined(folder)) {
			this.documentLocationRealName = folder.documentLocationRealName;
			this.prepareDocumentTypeMenuItems();
		}
		this.changePerspective();
	}

	public setDocumentIdAndDocumentaLocationRealName(documentId: string, documentLocationRealName: string): void {
		this.documentId = documentId;
		this.documentLocationRealName = documentLocationRealName;
		this.changePerspective();
	}

	public onDocumentWindowClosed(event: any): void {
		this.documentActionPerformed.emit();
		this.documentWindowVisible = false;
	}

	public onAdminUpdateDocumentWindowClosed(event: any): void {
		this.documentActionPerformed.emit();
		this.adminUpdateDocumentWindowVisible = false;
	}

	public get isAdminUpdateDocumentPermissionAllowed(): boolean {
		return this.authManager.hasPermission(ClientPermissionEnum.ADMIN_UPDATE_DOCUMENT);
	}
	
}