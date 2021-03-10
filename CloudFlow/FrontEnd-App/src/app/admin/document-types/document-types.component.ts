import { Component, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { DocumentTypeModel, PageConstants } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { DocumentTypeService, MessageDisplayer, MimeTypeSelectorComponent } from "@app/shared";

@Component({
	selector: "app-document-types",
	templateUrl: "./document-types.component.html"
})
export class DocumentTypesComponent implements OnInit {
	
	private documentTypeService: DocumentTypeService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public documentTypes: DocumentTypeModel[];
	public selectedDocumentType: DocumentTypeModel;

	public deleteEnabled: boolean;
	public editEnabled: boolean;

	public loading: boolean;
	public pageSize: number;

	public documentTypeWindowVisible: boolean;
	public documentTypeWindowMode: "add" | "edit";
	public documentTypeWindowDocumentTypeId: number;

	private isDocumentTypeSaved: boolean;
	public scrollHeight: string;

	public constructor(documentTypeService: DocumentTypeService, 
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.documentTypeService = documentTypeService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;		
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.documentTypes = [];
		this.isDocumentTypeSaved = false;
		this.documentTypeWindowVisible = false;
		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.changePerspective();
	}

	public ngOnInit(): void {
		this.loadDocumentTypes();
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	private loadDocumentTypes(): void {
		this.lock();
		this.documentTypeService.getAllDocumentTypesForDisplay({
			onSuccess: (documentTypes: DocumentTypeModel[]) => {
				this.documentTypes = documentTypes;
				this.selectedDocumentType = null;
				this.changePerspective();
				this.unlock();
			},
			onFailure: (error: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private changePerspective(): void {
		this.editEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocumentType);
		this.deleteEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocumentType);
	}

	public onAdd(event: any): void {
		this.documentTypeWindowMode = "add";
		this.documentTypeWindowDocumentTypeId = null;
		this.documentTypeWindowVisible = true;
	}

	public onEdit(event: any): void {
		this.documentTypeWindowMode = "edit";
		this.documentTypeWindowDocumentTypeId = this.selectedDocumentType.id;
		this.documentTypeWindowVisible = true;
	}

	public onDelete(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedDocumentType)) {
			return;
		}
		this.confirmationUtils.confirm("CONFIRM_DELETE_DOCUMENT_TYPE", {
			approve: (): void => {
				this.deleteSelectedDocumentType();
			}, 
			reject: (): void => {}
		});
	}

	private deleteSelectedDocumentType(): void {
		this.lock();
		this.documentTypeService.deleteDocumentType(this.selectedDocumentType.id, {
			onSuccess: () => {						
				this.selectedDocumentType = null;
				this.changePerspective();
				this.unlock();
				this.messageDisplayer.displaySuccess("DOCUMENT_TYPE_DELETED");
			},
			onFailure: (error: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private refresh(): void {
		this.loadDocumentTypes();
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	public onDocumentTypeSelected(event: any): void {
		this.changePerspective();
	}

	public onDocumentTypeUnselected(event: any): void {
		this.changePerspective();
	}

	private onDocumentTypeWindowClosed(): void {
		this.documentTypeWindowVisible = false;
		if (this.isDocumentTypeSaved) {
			this.refresh();
		}
	}

	private onDocumentTypeWindowDataSaved(): void {
		this.isDocumentTypeSaved = true;
	}
}
