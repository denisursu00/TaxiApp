import { Component } from "@angular/core";
import { SearchesAndReportsTabContent } from "../searches-and-reports-tab-content";
import { DocumentSearchCriteriaModel } from "@app/shared/model/document-search-criteria.model";
import { FormBuilder, FormGroup, AbstractControl, FormControl } from "@angular/forms";
import { SelectItem } from "primeng/primeng";
import { Statement } from "@angular/compiler";
import * as moment from "moment";
import {
	DateConstants,
	DocumentTypeService,
	DocumentTypeModel,
	AppError,
	MessageDisplayer,
	MetadataDefinitionModel,
	DocumentModel,
	DocumentLocationModel,
	ObjectUtils,
	ArrayUtils,
	WorkflowStateModel,
	MetadataSearchCriteriaModel,
	DateUtils
} from "@app/shared";
import { SearchDocumentsValidators } from "../search-documents-validators";

@Component({
	selector: "app-advanced-search-tab-content",
	templateUrl: "./advanced-search-tab-content.component.html",
	styleUrls: ["./advanced-search-tab-content.component.css"]
})
export class AdvancedSearchTabContentComponent extends SearchesAndReportsTabContent {
	
	private documentTypeService: DocumentTypeService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public documentTypeSelectItems: SelectItem[];
	public metadataDefinitions: MetadataDefinitionModel[];

	public dateFormat: String;
	public yearRange: String;

	public documentTypeIds: number[];

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
		this.loadDocumentTypes();
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group({
			documentLocation: null,
			documentCreationDateFrom: null,
			documentCreationDateTo: null,
			documentType: null,
			searchInVersions: false,
			documentStates: null
		}, {
			validator: SearchDocumentsValidators.fromDateGreaterThanToDate("documentCreationDateFrom", "documentCreationDateTo")
		});
	}

	private loadDocumentTypes(): void {
		this.documentTypeService.getAvailableDocumentTypesForSearch({
			onSuccess: (documentTypes: DocumentTypeModel[]): void => {
				this.documentTypeSelectItems = [...this.documentTypeSelectItems, ...this.prepareDocumentTypeSelectItems(documentTypes)];
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareDocumentTypeSelectItems(documentTypes: DocumentTypeModel[]): SelectItem[] {
		let documentTypeSelectItems: SelectItem[] = [];
		documentTypes.forEach(documentType => {
			documentTypeSelectItems.push(this.createDocumentTypeSelectItem(documentType));
		});
		return documentTypeSelectItems;
	}

	private createDocumentTypeSelectItem(documentType: DocumentTypeModel): SelectItem {
		let documentTypeSelectItem: SelectItem = {
			label: documentType.name,
			value: documentType
		};
		return documentTypeSelectItem;
	}

	public onDocumentTypeChange(event): void {
		this.documentTypeIds = [];
		this.documentTypeIds.push(event.value.id);
		this.removeMetadataControls();
		this.loadIndexedMetadatas();
	}

	private loadIndexedMetadatas(): void {
		if (ArrayUtils.isEmpty(this.documentTypeIds)) {
			return;
		}
		this.documentTypeService.getDocumentTypeById(this.documentTypeIds[0], {
			onSuccess: (documentType: DocumentTypeModel): void => {
				this.populateIndexedMetadatas(documentType);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateIndexedMetadatas(documentType: DocumentTypeModel): void {
		documentType.metadataDefinitions.forEach(metadataDefinition => {
			if (metadataDefinition.indexed) {
				if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
					this.formGroup.addControl(metadataDefinition.name, new FormControl([]));
				} else {
					this.formGroup.addControl(metadataDefinition.name, new FormControl(null));
				}
				this.metadataDefinitions.push(metadataDefinition);
			}
		});
	}

	private removeMetadataControls(): void {
		this.metadataDefinitions.forEach(metadata => {
			this.formGroup.removeControl(metadata.name);
		});
		this.metadataDefinitions = [];
	}

	public getControlByName(controlName: string): AbstractControl {
		return this.formGroup.controls[controlName];
	}

	public get documentTypeFormControl(): AbstractControl {
		return this.getControlByName("documentType");
	}

	public get documentCreationDateFromFormControl(): AbstractControl {
		return this.getControlByName("documentCreationDateFrom");
	}

	public get documentCreationDateToFormControl(): AbstractControl {
		return this.getControlByName("documentCreationDateTo");
	}

	public reset(): void {
		this.documentTypeSelectItems = [];
		this.metadataDefinitions = [];
	}

	public isValid(): boolean {
		if (this.formGroup.invalid) {
			this.documentTypeFormControl.markAsTouched();
			return false;
		}
		return true;
	}
	
	public populateForSearch(documentSearchCriteriaModel: DocumentSearchCriteriaModel): void {		
		documentSearchCriteriaModel.documentLocationRealName = (<DocumentLocationModel>this.getControlByName("documentLocation").value).documentLocationRealName;
		
		documentSearchCriteriaModel.createdStart = this.getControlByName("documentCreationDateFrom").value;
		documentSearchCriteriaModel.createdEnd = this.getControlByName("documentCreationDateTo").value;
		
		let searchInVersions: boolean = this.getControlByName("searchInVersions").value;
		if (searchInVersions) {
			documentSearchCriteriaModel.searchInVersions = searchInVersions;
		}

		let documentType: DocumentTypeModel = this.getControlByName("documentType").value;
		if (ObjectUtils.isNotNullOrUndefined(documentType)) {
			documentSearchCriteriaModel.documentTypeIdList.push(
				(<DocumentTypeModel>documentType).id
			);
		}

		this.populateWorkflowStateIdList(documentSearchCriteriaModel);
		this.populateMetadataSearchCriteriaList(documentSearchCriteriaModel);
	}

	private populateWorkflowStateIdList(documentSearchCriteriaModel: DocumentSearchCriteriaModel): void {
		let documentType: DocumentTypeModel = this.getControlByName("documentType").value;
		if (ObjectUtils.isNotNullOrUndefined(documentType)) {
			let documentStates: WorkflowStateModel[] = this.getControlByName("documentStates").value;
			if (ArrayUtils.isNotEmpty(documentStates)) {
				documentStates.forEach(state => {
					documentSearchCriteriaModel.workflowStateIdList.push(state.id);
				});
			}
		}
	}

	private populateMetadataSearchCriteriaList(documentSearchCriteriaModel: DocumentSearchCriteriaModel): void {
		this.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
			let fieldControl: AbstractControl = this.getControlByName(metadataDefinition.name);
			
			if (ObjectUtils.isNotNullOrUndefined(fieldControl.value)) {
				if (metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, fieldControl.value)
					);
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
					fieldControl.value.forEach(fieldValue => {
						documentSearchCriteriaModel.metadataSearchCriteriaList.push(
							this.createMetadataSearchCriteriaModel(metadataDefinition, fieldValue)
						);
					});
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, DateUtils.formatForStorage(fieldControl.value))
					);
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, DateUtils.formatDateTimeForStorage(fieldControl.value))
					);
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_MONTH) {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, DateUtils.formatMonthYearForStorage(fieldControl.value))
					);
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
					let nomenclatorSearchValue: string = (<number> fieldControl.value).toString();
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(this.createMetadataSearchCriteriaModel(metadataDefinition, nomenclatorSearchValue));
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_GROUP) {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, fieldControl.value)
					);
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DOCUMENT) {
					fieldControl.value.forEach(fieldValue => {
						documentSearchCriteriaModel.metadataSearchCriteriaList.push(
							this.createMetadataSearchCriteriaModel(metadataDefinition, fieldValue)
						);
					});
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_CALENDAR) {
					let calendarIdAString: string = (<number> fieldControl.value).toString();
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(this.createMetadataSearchCriteriaModel(metadataDefinition, calendarIdAString));
				} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_PROJECT) {
					fieldControl.value.forEach(fieldValue => {
						documentSearchCriteriaModel.metadataSearchCriteriaList.push(
							this.createMetadataSearchCriteriaModel(metadataDefinition, fieldValue)
						);
					});
				} else {
					documentSearchCriteriaModel.metadataSearchCriteriaList.push(
						this.createMetadataSearchCriteriaModel(metadataDefinition, fieldControl.value)
					);
				}
			}
		});
	}

	private createMetadataSearchCriteriaModel(metadataDefinition: MetadataDefinitionModel, value: string): MetadataSearchCriteriaModel {
		let metadataSearchCriteriaModel: MetadataSearchCriteriaModel = new MetadataSearchCriteriaModel();
		metadataSearchCriteriaModel.metadataDefinitionId = metadataDefinition.id;
		metadataSearchCriteriaModel.value = String(value);
		return metadataSearchCriteriaModel;
	}
}
