import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { NomenclatorService, DocumentTypeService, DocumentSearchService, DocumentSelectionSearchResultViewModel, DocumentSearchResultsViewModel, BaseWindow } from "@app/shared";
import { PageConstants, DocumentSelectionSearchFilterModel, DocumentLocationModel} from "@app/shared";
import { ObjectUtils, ArrayUtils, TranslateUtils, StringUtils, MessageDisplayer } from "@app/shared";
import { AppError } from "@app/shared";
import { DocumentTypeModel } from "@app/shared";
import { DocumentWindowInputData } from "@app/client/common/document-window/document-window-input-data";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-document-selection-window",
	templateUrl: "./document-selection-window.component.html"
})
export class DocumentSelectionWindowComponent extends BaseWindow implements OnInit {
	
	@Input()
	public selectionMode: "single" | "multiple";

	@Input()
	public inputData: DocumentSelectionWindowInputData;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public valuesSelected: EventEmitter<SelectedDocument | SelectedDocument[]>;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private documentTypeService: DocumentTypeService;
	private documentSearchService: DocumentSearchService;

	public windowVisible: boolean = false;
	public documentsVisible: boolean = false;
	public title: string;
	public pageSize: number;
	public first: number;

	public okActionEnabled: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public documentType: DocumentTypeModel;
	public documents: DocumentSelectionSearchResultViewModel[];
	public selectedDocuments: any; // DocumentSelectionSearchResultViewModel or DocumentSelectionSearchResultViewModel[];

	public viewDocumentWindowVisible: boolean;
	public viewDocumentWindowInputData: DocumentWindowInputData;
	public viewDocumentWindowMode: string;
	
	public scrollHeight: string;

	public constructor(documentTypeService: DocumentTypeService, documentSearchService: DocumentSearchService,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils,
			formBuilder: FormBuilder) {
		super();
		this.documentTypeService = documentTypeService;
		this.documentSearchService = documentSearchService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter();
		this.valuesSelected = new EventEmitter();
		this.init();
	}

	private init(): void {
		this.lock();
		this.windowVisible = false;
		this.selectionMode = "single";
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.first = 0;
		this.okActionEnabled = false;
		this.scrollHeight = (window.innerHeight - 300) + "px";
	}

	public ngOnInit(): void {		
		if (ObjectUtils.isNullOrUndefined(this.inputData) || ObjectUtils.isNullOrUndefined(this.inputData.documentTypeId)) {
			throw new Error("document type Id cannot be null");
		}		
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("documentLocation", new FormControl());
		this.formGroup.addControl("documentName", new FormControl());
		this.documentTypeService.getDocumentTypeById(this.inputData.documentTypeId, {
			onSuccess: (documentType: DocumentTypeModel): void => {
				this.documentType = documentType;
				this.title = this.translateUtils.translateLabel("DOCUMENT_SELECTION") + ": " + documentType.name;
				
				// TODO - metadate

				this.unlock();
				this.windowVisible = true;
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.windowClosed.emit();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private isSingleSelection(): boolean {
		return this.selectionMode === "single";
	}

	private isMultipleSelection(): boolean {
		return this.selectionMode === "multiple";
	}

	private updatePerspective(): void {
		this.okActionEnabled = false;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedDocuments)) {
			if (ObjectUtils.isArray(this.selectedDocuments)) {
				this.okActionEnabled = ArrayUtils.isNotEmpty(this.selectedDocuments);
			} else {
				this.okActionEnabled = true;
			}
		}		
	}

	public onSearchAction(): void {
		
		this.lock();
		
		let filterModel: DocumentSelectionSearchFilterModel = new DocumentSelectionSearchFilterModel();
		filterModel.documentLocationRealName = (<DocumentLocationModel> this.documentLocationFormControl.value).documentLocationRealName;
		filterModel.documentTypeId = this.documentType.id;
		filterModel.documentName = this.documentNameFormControl.value;

		this.documentSearchService.findDocumentsForSelection(filterModel, {
			onSuccess: (results: DocumentSelectionSearchResultViewModel[]): void => {
				this.first = 0;
				this.documentsVisible = true;
				this.documents = results;
				this.unlock();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onOKAction(): void {

		if (ObjectUtils.isNullOrUndefined(this.selectedDocuments)) {		
			return;
		}

		let selectedDocs: SelectedDocument[] = [];
		if (ObjectUtils.isNotNullOrUndefined(this.selectedDocuments)) {		
			if (ObjectUtils.isArray(this.selectedDocuments)) {
				if (ArrayUtils.isNotEmpty(this.selectedDocuments)) {
					this.selectedDocuments.forEach((selectedDocument: DocumentSelectionSearchResultViewModel) => {
						let sd: SelectedDocument = new SelectedDocument(selectedDocument.documentLocationRealName, selectedDocument.documentId);
						sd.documentName = selectedDocument.documentName;
						selectedDocs.push(sd);
					});
				}
			} else {
				let selectedDocument: DocumentSelectionSearchResultViewModel = <DocumentSelectionSearchResultViewModel> this.selectedDocuments;
				let sd: SelectedDocument = new SelectedDocument(selectedDocument.documentLocationRealName, selectedDocument.documentId);
				sd.documentName = selectedDocument.documentName;			
				selectedDocs.push(sd);
			}
		}

		if (this.isSingleSelection()) {
			if (ArrayUtils.isNotEmpty(selectedDocs)) {
				this.valuesSelected.emit(selectedDocs[0]);
			}
		} else if (this.isMultipleSelection()) {
			this.valuesSelected.emit(selectedDocs);
		} else {
			throw new Error("wrong selection");
		}
		this.windowVisible = false;
	}

	public onCloseAction(): void {
		this.windowClosed.emit();
		this.windowVisible = false;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onDocumentSelected(event: any) {
		this.updatePerspective();
	}

	public onDocumentUnselected(event: any) {
		this.updatePerspective();
	}

	public get nrOfDocuments(): number {
		if (ArrayUtils.isNotEmpty(this.documents)) {
			return this.documents.length;
		}
		return 0;
	}

	public onPage(event: any): void {
		this.first = event.first;
	}
	
	public onViewDocumentAction(document: DocumentSelectionSearchResultViewModel): void {
		if (ObjectUtils.isNullOrUndefined(document)) {
			return;
		}
		this.viewDocumentWindowMode = "viewOrEdit";
		this.viewDocumentWindowInputData = new DocumentWindowInputData();
		this.viewDocumentWindowInputData.documentLocationRealName = document.documentLocationRealName;
		this.viewDocumentWindowInputData.documentId = document.documentId;
		this.viewDocumentWindowVisible = true;
	}

	public onViewDocumentWindowClosed(event: any): void {
		this.viewDocumentWindowVisible = false;
	}

	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.get(controlName);
	}

	public get documentLocationFormControl(): FormControl { 
		return this.getFormControlByName("documentLocation"); 
	} 

	public get documentNameFormControl(): FormControl { 
		return this.getFormControlByName("documentName"); 
	} 
}

export class SelectedDocument {

	private _documentLocationRealName: string;
	private _documentId: string;
	public documentName: string;

	public constructor(documentLocationRealName: string, documentId: string) {
		if (StringUtils.isBlank(documentLocationRealName)) {
			throw new Error("documentLocationRealName cannot be null");
		}
		if (StringUtils.isBlank(documentId)) {
			throw new Error("documentId cannot be null");
		}
		this._documentLocationRealName = documentLocationRealName;
		this._documentId = documentId;
	}

	public get documentLocationRealName() {
		return this._documentLocationRealName;
	}

	public get documentId() {
		return this._documentId;
	}
}

export class DocumentSelectionWindowInputData {

	public documentTypeId: number;
}