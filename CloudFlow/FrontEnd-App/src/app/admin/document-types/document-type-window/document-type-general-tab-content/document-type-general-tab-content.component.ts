import { Component, OnInit, ViewChild} from "@angular/core";
import { AppError, MimeTypeModel, ObjectUtils, DocumentTemplateModel, DocumentLocationModel, DocumentLocationService, MessageDisplayer, ArrayUtils, FolderTreeNode, FolderModel, FolderService, StringUtils, FormUtils, BooleanUtils, PDialogMinimizer } from "@app/shared";
import { DocumentTypeModel } from "@app/shared";
import { DocumentTypeEditInfo } from "./../document-type-edit-info";
import { DocumentTypeTabContent } from "./../document-type-tab-content";
import { FormGroup, FormBuilder, Validators, AbstractControl } from "@angular/forms";
import { DocumentTemplateComponent } from "@app/shared/components/document-template/document-template.component";
import { TreeNode, Dialog } from "primeng/primeng";
import { Message } from "@app/shared";
import { FolderSelectorComponent } from "@app/shared/components/folder-selector";

@Component({
	selector: "app-document-type-general-tab-content",
	templateUrl: "./document-type-general-tab-content.component.html"
})
export class DocumentTypeGeneralTabContentComponent extends DocumentTypeTabContent {

	private static readonly NOT_AVAILABLE: string = "(N/A)";

	@ViewChild(DocumentTemplateComponent)
	private documentTemplateComponent: DocumentTemplateComponent;

	@ViewChild(FolderSelectorComponent)
	private folderSelectorComponent: FolderSelectorComponent;

	private documentLocationService: DocumentLocationService;
	private folderService: FolderService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;
	
	public selectedDocumentLocation: DocumentLocationModel = new DocumentLocationModel();
	public uploadedDocumentTemplates: DocumentTemplateModel[] = [];
	public selectedFolder: FolderModel = new FolderModel();
	public allowedDocumentTemplateTypes: MimeTypeModel[] = [];
	public documentLocations: DocumentLocationModel[];

	public documentLocationAndFolderSelectorWindowVisible: boolean = false;
	public documentTemplateDescription: string;
	public documentTypeId: string;

	private messages: Message[];

	private pDialogMinimizer: PDialogMinimizer;

	public constructor(formBuilder: FormBuilder, documentLocationService: DocumentLocationService, folderService: FolderService, messageDisplayer: MessageDisplayer) {
		super();
		this.formBuilder = formBuilder;
		this.documentLocationService = documentLocationService;
		this.folderService = folderService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	private init(): void {
		this.prepareForm();
	}

	protected doWhenNgOnInit(): void {
		let mimeTypeZip: MimeTypeModel = new MimeTypeModel();
		mimeTypeZip.extension = "zip";
		this.allowedDocumentTemplateTypes.push(mimeTypeZip);
		let mimeTypeDocX: MimeTypeModel = new MimeTypeModel();
		mimeTypeDocX.extension = "docx";
		this.allowedDocumentTemplateTypes.push(mimeTypeDocX);
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: ["", Validators.required],
			description: [""],
			keepAllVersions: [false]
		});
	}

	protected prepareForAdd(): void {
		this.selectedDocumentLocation.name = DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE;
		this.selectedFolder.name = DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE;
	}
	
	protected prepareForEdit(): void {
		let folderId: string = this.inputData.documentType.folderIdForDefaultLocation;
		let documentLocationRealName: string = this.inputData.documentType.parentDocumentLocationRealNameForDefaultLocation;

		if (StringUtils.isNotBlank(folderId)) {
			this.folderService.getFolderById(folderId, documentLocationRealName, {
				onSuccess: (folderModel: FolderModel) => {
					this.selectedFolder = folderModel;
					if (StringUtils.isNotBlank(documentLocationRealName)) {
						this.getDocumentLocationByRealName(documentLocationRealName);
					}
				},
				onFailure: (appError: AppError) => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		} else {
			this.onResetDefaultLocation();
		}
		this.documentTypeId = this.inputData.documentType.id + "";
		this.form.patchValue({
			name: this.inputData.documentType.name,
			description: this.inputData.documentType.description,
			keepAllVersions: this.inputData.documentType.keepAllVersions
		});
		this.uploadedDocumentTemplates = this.inputData.documentType.templates;
	}

	public populateDocumentType(documentType: DocumentTypeModel): void {
		if (this.isEdit()) {
			documentType.id = this.inputData.documentType.id;
		}		
		documentType.name = this.nameFormControl.value.trim();
		documentType.description = this.descriptionFormControl.value;
		documentType.keepAllVersions = this.keepAllVersionsFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedFolder)) {
			if (this.selectedFolder.name !== DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE) {
				documentType.folderIdForDefaultLocation = this.selectedFolder.id;
			} else if (this.selectedFolder.name === DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE) {
				documentType.folderIdForDefaultLocation = "";
			}
		}
		if (ObjectUtils.isNotNullOrUndefined(this.selectedDocumentLocation)) {
			if (this.selectedDocumentLocation.name !== DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE) {
				documentType.parentDocumentLocationRealNameForDefaultLocation = this.selectedDocumentLocation.documentLocationRealName;
			} else if (this.selectedDocumentLocation.name === DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE) {
				documentType.parentDocumentLocationRealNameForDefaultLocation = "";
			}
		}
		documentType.namesForTemplatesToDelete = this.documentTemplateComponent.getNamesForTemplatesToDelete();
		documentType.templates = this.documentTemplateComponent.getUploadedDocumentTemplates();
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = this.form.valid;
		if (!isValid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED"));
		}
		return isValid;
	}
	
	public get nameFormControl(): AbstractControl {
		return this.form.controls["name"];
	}
	
	public get descriptionFormControl(): AbstractControl {
		return this.form.controls["description"];
	}
	
	public get keepAllVersionsFormControl(): AbstractControl {
		return this.form.controls["keepAllVersions"];
	}

	public onSelectDefaultLocation(): void {
		this.documentLocationService.getAllDocumentLocations({
			onSuccess: (documentLocations: DocumentLocationModel[]) => {
				if (ArrayUtils.isEmpty(documentLocations)) {
					return;
				}
				this.documentLocations = documentLocations;
				this.documentLocationAndFolderSelectorWindowVisible = true;
			},
			onFailure: (applicationError: AppError) => {
				this.messageDisplayer.displayAppError(applicationError);
			}
		});
	}

	public onResetDefaultLocation(): void {
		this.selectedDocumentLocation = new DocumentLocationModel();
		this.selectedDocumentLocation.name = DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE;
		this.selectedFolder = new FolderModel();
		this.selectedFolder.name = DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE;
	}

	public onBlur(documentTemplateDescriptionFormControl: AbstractControl): void {
		this.documentTemplateDescription = documentTemplateDescriptionFormControl.value;
	}
	
	public onHideDocumentLocationAndFolderSelectorWindow(event: any): void {
		this.closeDocumentLocationAndFolderSelectorWindow();
	}

	public onShowDocumentLocationAndFolderSelectorWindow(event: any, pDialog: Dialog): void {
		this.pDialogMinimizer = new PDialogMinimizer(pDialog);
		pDialog.center();
		setTimeout(() => {
			pDialog.maximize();
		}, 0);
	}

	public onToggleMinimizeDocumentLocationAndFolderSelectorWindow(pDialog: Dialog): void {
		this.pDialogMinimizer.toggleMinimize();
	}

	public get documentLocationAndFolderSelectorWindowMinimized(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.pDialogMinimizer)) {
			return false;
		}
		return this.pDialogMinimizer.minimized;
	}

	public onOkAction(event: any): void {
		if (this.selectedFolder.name !== DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE) {
			this.closeDocumentLocationAndFolderSelectorWindow();
		}
	}

	public onCancelAction(event: any): void {
		this.closeDocumentLocationAndFolderSelectorWindow();
	}

	private closeDocumentLocationAndFolderSelectorWindow(): void {
		this.documentLocationAndFolderSelectorWindowVisible = false;
	}

	public onFolderSelected(folderTreeNode: FolderTreeNode<FolderModel | DocumentLocationModel>): void {
		if (folderTreeNode.isFolder()) {
			this.selectedFolder = <FolderModel> folderTreeNode.data;
			this.getDocumentLocationByRealName(this.selectedFolder.documentLocationRealName);
		} else {
			this.selectedFolder = new FolderModel();
			this.selectedFolder.name = DocumentTypeGeneralTabContentComponent.NOT_AVAILABLE;
		}
	}

	private getDocumentLocationByRealName(documentLocationRealName: string): void {
		this.documentLocationService.getDocumentLocationByRealName(documentLocationRealName, {
			onSuccess: (documentLocationModel: DocumentLocationModel) => {
				this.selectedDocumentLocation = documentLocationModel;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}