import { Component, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { SearchesAndReportsTabContent } from "../searches-and-reports-tab-content";
import { DocumentSearchCriteriaModel } from "@app/shared/model/document-search-criteria.model";
import { 
	DocumentLocationModel, 
	DocumentTypeService, 
	AppError, 
	DocumentTypeModel, 
	MessageDisplayer, 
	DocumentStatesSelectorComponent, 
	ArrayUtils, 
	DateConstants, 
	FormUtils,
	DateUtils
} from "@app/shared";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { SearchDocumentsValidators } from "../search-documents-validators";

@Component({
	selector: "app-simple-search-tab-content",
	templateUrl: "./simple-search-tab-content.component.html",
	styleUrls: ["./simple-search-tab-content.component.css"]
})
export class SimpleSearchTabContentComponent extends SearchesAndReportsTabContent {

	private documentTypeService: DocumentTypeService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;

	public createdStart: Date;
	public createdEnd: Date;
	public sourceDocumentTypes: DocumentTypeModel[];
	public targetDocumentTypes: DocumentTypeModel[];
	public searchForm: FormGroup;
	public documentTypeIds: number[];
	public dateFormat: string;
	public yearRange: string;

	public constructor(documentTypeService: DocumentTypeService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.documentTypeService = documentTypeService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.init();
	}
	
	private init(): void {
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.reset();
		this.prepareForm();
		this.populateSourceDocumentTypes();
	}

	private prepareForm(): void {
		this.searchForm = this.formBuilder.group({
			documentLocation: [],
			createdStart: [],
			createdEnd: [],
			documentTypes: [],
			documentStates: []
		}, {
			validator: SearchDocumentsValidators.fromDateGreaterThanToDate("createdStart", "createdEnd")
		});
	}

	private populateSourceDocumentTypes(): void {
		this.documentTypeService.getAvailableDocumentTypesForSearch({
			onSuccess: (documentTypes: DocumentTypeModel[]): void => {
				this.sourceDocumentTypes = documentTypes;
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public reset(): void {
		this.sourceDocumentTypes = [];
		this.targetDocumentTypes = [];
		this.documentTypeIds = [];
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.searchForm);
		return this.searchForm.valid;
	}

	public isValid(): boolean {
		return this.isFormValid();
	}

	public populateForSearch(documentSearchCriteriaModel: DocumentSearchCriteriaModel): void {
		documentSearchCriteriaModel.documentLocationRealName = this.searchForm.controls.documentLocation.value.documentLocationRealName;
		if (this.searchForm.controls.createdStart.value) {
			documentSearchCriteriaModel.createdStart = this.searchForm.controls.createdStart.value;
		}
		if (this.searchForm.controls.createdEnd.value) {
			documentSearchCriteriaModel.createdEnd = this.searchForm.controls.createdEnd.value;
		}
		if (this.searchForm.controls.documentTypes.value) {
			this.searchForm.controls.documentTypes.value.forEach(documentType => {
				documentSearchCriteriaModel.documentTypeIdList.push(documentType.id);
			});
		}
		if (this.searchForm.controls.documentStates.value) {
			this.searchForm.controls.documentStates.value.forEach(documentState => {
				documentSearchCriteriaModel.workflowStateIdList.push(documentState.id);
			});
		}
	}

	public onChangeDocumentTypes(event): void {
		this.searchForm.patchValue({
			documentTypes: this.targetDocumentTypes
		});
		this.documentTypeIds = [];
		if (ArrayUtils.isNotEmpty(this.targetDocumentTypes)) {
			this.targetDocumentTypes.forEach(targetDocumentType => {
				this.documentTypeIds.push(targetDocumentType.id);
			});
			this.documentTypeIds = [...this.documentTypeIds];
		}
	}

	public get createdStartFormControl(): AbstractControl {
		return this.searchForm.controls["createdStart"];
	}

	public get createdEndFormControl(): AbstractControl {
		return this.searchForm.controls["createdEnd"];
	}
}
