import { Component, OnInit, Output, Input, EventEmitter, OnChanges, SimpleChanges } from "@angular/core";
import { ArrayUtils, ObjectUtils, FormUtils, BooleanUtils, StringUtils, NomenclatorAttributeModel, MessageDisplayer,  } from "@app/shared";
import { AppError, Message, DocumentTypeModel, MetadataDefinitionModel, ListMetadataItemModel } from "@app/shared";
import { StringValidators, TranslateUtils, NomenclatorService } from "@app/shared";
import { Validators, FormControl, FormGroup, FormBuilder, ValidatorFn, AbstractControl } from "@angular/forms";
import { MetadataDefinitionInputData } from "./metadata-definition-input-data";
import { MetadataDefinitionsOfMetadataCollectionWindowInputData } from "./../../metadata-definitions-of-metadata-collection-window/metadata-definitions-of-metadata-collection-window-input-data";
import { MetadataDefinitionValidators } from "./metadata-definition-validators";
import { ValueOfMetadataDefaultValueField } from "./metadata-definition-default-value-field.component";
import { SelectItem } from "primeng/primeng";
import { ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField } from "./nomenclator-metadata-definition-value-selection-filters-field.component";

@Component({
	selector: "app-metadata-definition",
	templateUrl: "./metadata-definition.component.html"
})
export class MetadataDefinitionComponent implements OnChanges {

	private static readonly SELECT_ITEM_NONE: SelectItem = {
		value: null,
		label: null
	};

	@Input()
	public usageMode: "documentType" | "metadataCollection";

	@Input()
	public inputData: MetadataDefinitionInputData;

	@Output()
	public dataSaved: EventEmitter<MetadataDefinitionModel>;

	@Output()
	public canceled: EventEmitter<void>;

	private translateUtils: TranslateUtils;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	
	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public saveActionLabelCode: string;
	
	public metadataNameForAutoCompleteWithMetadataSelectItems: SelectItem[];
	public typeOfAutoCompleteWithMetadataSelectItems: SelectItem[];
	public nomenclatorAttributeKeyForAutoCompleteWithMetadataSelectItems: SelectItem[];
	
	public constructor(formBuilder: FormBuilder, translateUtils: TranslateUtils, 
			nomenclatorService: NomenclatorService,  messageDisplayer: MessageDisplayer) {
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.nomenclatorService = nomenclatorService;
		this.dataSaved = new EventEmitter();
		this.canceled = new EventEmitter();
		this.initForm();
	}
	
	private initForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("name", new FormControl(null, []));
		this.formGroup.addControl("label", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.formGroup.addControl("type", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("mandatory", new FormControl(false, []));
		this.formGroup.addControl("mandatoryInStates", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("restrictedOnEdit", new FormControl(false, []));
		this.formGroup.addControl("restrictedOnEditInStates", new FormControl({value: null, disabled: true}, [Validators.required]));
		this.formGroup.addControl("invisible", new FormControl(false, []));
		this.formGroup.addControl("invisibleInStates", new FormControl({value: null}, []));
		this.formGroup.addControl("orderNumber", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("representative", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("indexed", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("multipleSelection", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("extendable", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("listItems", new FormControl({value: false, disabled: true}, [Validators.required]));
		this.formGroup.addControl("onlyUsersFromGroup", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("idOfGroupOfPermittedUsers", new FormControl({value: null, disabled: false}, [Validators.required]));
		this.formGroup.addControl("autoCompleteWithCurrentUser", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("autoCompleteWithCurrentUserState", new FormControl({value: false, disabled: true}, [Validators.required]));
		this.formGroup.addControl("prefix", new FormControl({value: null, disabled: true}, [Validators.required, StringValidators.blank()]));
		this.formGroup.addControl("numberLength", new FormControl({value: null, disabled: true}, [Validators.required, MetadataDefinitionValidators.numberLengthValidator({
			getAutoNumberPrefix: (): string => {
				return this.prefixFormControl.value;
			}
		})]));		
		this.formGroup.addControl("defaultValue", new FormControl({value: null, disabled: true}, [MetadataDefinitionValidators.defaultMetadataValueValidator({
			getMetadataType: (): string => {
				return this.typeFormControl.value;
			}
		})]));
		this.formGroup.addControl("metadataDefinitionsOfMetadataCollection", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("nomenclatorId", new FormControl({value: null, disabled: false}, [Validators.required]));
		this.formGroup.addControl("nomenclatorValueSelectionFilters", new FormControl({value: null, disabled: false}, []));
		this.formGroup.addControl("autoCompleteWithCurrentDate", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("autoCompleteWithCurrentDateTime", new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("autoCompleteWithCurrentMonth", new FormControl({value: false, disabled: true}, []));

		this.formGroup.addControl("metadataNameForAutoCompleteWithMetadata", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("typeOfAutoCompleteWithMetadata", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("nomenclatorAttributeKeyForAutoCompleteWithMetadata", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("classNameForAutoCompleteWithMetadata", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("metadataDocumentTypeId", new FormControl({value: null, disabled: true}, [Validators.required]));
		this.formGroup.addControl("multipleDocumentsSelection",  new FormControl({value: false, disabled: true}, []));
		this.formGroup.addControl("multipleProjectsSelection",  new FormControl({value: false, disabled: true}, []));

		this.invisibleFormControl.setValidators([MetadataDefinitionValidators.invisibleValidator({
			getInvisibleInStates: (): string => {
				return this.invisibleInStatesFormControl.value;
			},
			getMandatoryInStates: (): string => {
				return this.mandatoryInStatesFormControl.value;
			},
			isMandatory: (): boolean => {
				return this.mandatoryFormControl.value;
			}
		})]);
		this.invisibleInStatesFormControl.setValidators([MetadataDefinitionValidators.invisibleInStatesValidator({
			getMandatoryInStates: (): string => {
				return this.mandatoryInStatesFormControl.value;
			},
			isMandatory: (): boolean => {
				return this.mandatoryFormControl.value;
			}
		})]);
	}

	public ngOnChanges(simpleChanges: SimpleChanges): void {
		this.formGroup.reset();
		this.populateMetadataNamesForAutoCompleteWithMetadata();
		this.updateValidators();		
		if (ObjectUtils.isNullOrUndefined(this.inputData.editMetadataDefinition)) {
			this.prepareForAdd();
		} else {
			this.prepareForEdit();
		}
		this.updateValueAndValidityForControlsIfNeed();
	}

	private isMetadataTypeCandidateForAutoCompleteWithMetadata(metadataType: string): boolean {
		if (StringUtils.isBlank(metadataType)) {
			return false;
		}
		return metadataType === MetadataDefinitionModel.TYPE_USER 
				|| metadataType === MetadataDefinitionModel.TYPE_NOMENCLATOR
				|| metadataType === MetadataDefinitionModel.TYPE_TEXT
				|| metadataType === MetadataDefinitionModel.TYPE_TEXT_AREA
				|| metadataType === MetadataDefinitionModel.TYPE_DOCUMENT;
	}

	private populateTypeOfAutoCompleteWithMetadata(): void {
		this.typeOfAutoCompleteWithMetadataSelectItems = [];
		this.typeOfAutoCompleteWithMetadataSelectItems.push(MetadataDefinitionComponent.SELECT_ITEM_NONE);
		let metadataName: string = this.metadataNameForAutoCompleteWithMetadataFormControl.value;
		if (StringUtils.isBlank(metadataName)) {
			return;
		}
		this.inputData.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
			if (md.name === metadataName) {
				this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_METADATA_VALUE));
				this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_DELETE_VALUE));
				this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS));
				if (md.type === MetadataDefinitionModel.TYPE_USER) {
					this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_TITLE));
					this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_EMAIL));
				} else if (md.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
					this.typeOfAutoCompleteWithMetadataSelectItems.push(this.createSelectItemForTypeOfAutoCompleteWithMetadata(MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE));
				}
			}
		});
	}

	private createSelectItemForTypeOfAutoCompleteWithMetadata(type: string): SelectItem {
		return {
			value: type,
			label: this.translateUtils.translateLabel("TYPE_OF_AUTO_COMPLETE_WITH_METADATA_" + type)
		};
	}

	private populateMetadataNamesForAutoCompleteWithMetadata(): void {
		this.metadataNameForAutoCompleteWithMetadataSelectItems = [];
		this.metadataNameForAutoCompleteWithMetadataSelectItems.push(MetadataDefinitionComponent.SELECT_ITEM_NONE);
		if (ArrayUtils.isNotEmpty(this.inputData.metadataDefinitions)) {
			this.inputData.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
				let canAdd: boolean = false;
				if (this.isMetadataTypeCandidateForAutoCompleteWithMetadata(md.type)) {
					if (ObjectUtils.isNullOrUndefined(this.inputData.editMetadataDefinition) || this.inputData.editMetadataDefinition.name !== md.name) {
						canAdd = true;
					}
				}
				if (canAdd) {
					this.metadataNameForAutoCompleteWithMetadataSelectItems.push({
						value: md.name,
						label: md.label
					});
				}			
			});
		}
	}

	private updateValidators(): void {
		this.nameFormControl.clearValidators();
		this.nameFormControl.setValidators([MetadataDefinitionValidators.metadataNameValidator(this.getTakenMetadataNames())]);
	}

	private getTakenMetadataNames(): string[] {
		let takenMetadataNames: string[] = [];
		if (ArrayUtils.isNotEmpty(this.inputData.metadataDefinitions)) {
			this.inputData.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
				takenMetadataNames.push(md.name);
			});
		}
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.editMetadataDefinition)) {
			ArrayUtils.removeElement(takenMetadataNames, this.inputData.editMetadataDefinition.name);
		}		
		return takenMetadataNames;
	}
	
	private updateValueAndValidityForControlsIfNeed() {
		this.nameFormControl.updateValueAndValidity();
		this.invisibleFormControl.updateValueAndValidity();
		this.invisibleInStatesFormControl.updateValueAndValidity();
	}

	public onMetadataTypeChanged(metadataType: string): void {
		this.changePespective();
		this.autoCompleteWithCurrentDateFormControl.reset();
		this.autoCompleteWithCurrentDateTimeFormControl.reset();
		this.autoCompleteWithCurrentMonthFormControl.reset();
		this.prefixFormControl.reset();
		this.numberLengthFormControl.reset();
		this.multipleSelectionFormControl.reset();
		this.extendableFormControl.reset();
		this.listItemsFormControl.reset();
		this.onlyUsersFromGroupFormControl.reset();
		this.idOfGroupOfPermittedUsersFormControl.reset();
		this.autoCompleteWithCurrentUserFormControl.reset();
		this.autoCompleteWithCurrentUserStateFormControl.reset();
		this.nomenclatorIdFormControl.reset();
		this.nomenclatorValueSelectionFiltersFormControl.reset();
		this.metadataNameForAutoCompleteWithMetadataFormControl.reset();
		this.typeOfAutoCompleteWithMetadataFormControl.reset();
		this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl.reset();
		this.classNameForAutoCompleteWithMetadataFormControl.reset();
		this.metadataDocumentTypeIdFormControl.reset();
		this.multipleDocumentsSelectionFormControl.reset();
		this.multipleProjectsSelectionFormControl.reset();
		this.defaultValueFormControl.reset();
		if (this.isApplicableDefaultValue(this.typeFormControl.value)) {
			let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(metadataType);
			this.defaultValueFormControl.setValue(valueOfDefaultValue);
		}		
	}

	private isApplicableDefaultValue(metadataType: string): boolean {
		if (ObjectUtils.isNullOrUndefined(metadataType)) {
			return false;
		}
		return (metadataType !== MetadataDefinitionModel.TYPE_AUTO_NUMBER 
				&& metadataType !== MetadataDefinitionModel.TYPE_METADATA_COLLECTION 
				&& metadataType !== MetadataDefinitionModel.TYPE_DOCUMENT
				&& metadataType !== MetadataDefinitionModel.TYPE_PROJECT);
	}

	private prepareForAdd(): void {
		this.formGroup.patchValue({
			orderNumber: this.getProposedOerderNumber()
		});
		this.changePespective();
		this.saveActionLabelCode = "ADD";
	}

	private getLastOrderNumber(): number {
		let lastMetadataOrderNumber: number = 0;
		if (ArrayUtils.isNotEmpty(this.inputData.metadataDefinitions)) {
			this.inputData.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
				if (metadataDefinition.orderNumber > lastMetadataOrderNumber) {
					lastMetadataOrderNumber = metadataDefinition.orderNumber;
				}
			});
		}
		return lastMetadataOrderNumber;
	}

	private getProposedOerderNumber(): number {
		let lastOrderNumber: number = this.getLastOrderNumber();
		return (lastOrderNumber + 1);
	}

	private prepareForEdit(): void {
		this.populateFormValues(this.inputData.editMetadataDefinition);		
		this.changePespective();
		this.saveActionLabelCode = "SAVE_METADATA";
	}

	private populateFormValues(metadataDefinition: MetadataDefinitionModel): void {

		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(metadataDefinition.type);
		valueOfDefaultValue.value = metadataDefinition.defaultValue;

		this.formGroup.patchValue({
			name: metadataDefinition.name,
			label: metadataDefinition.label,
			type: metadataDefinition.type,
			mandatory: metadataDefinition.mandatory,
			mandatoryInStates: metadataDefinition.mandatoryStates,
			restrictedOnEdit: metadataDefinition.restrictedOnEdit,
			restrictedOnEditInStates: metadataDefinition.restrictedOnEditStates,
			invisible: metadataDefinition.invisible,
			invisibleInStates: metadataDefinition.invisibleInStates,
			orderNumber: metadataDefinition.orderNumber
		});
		
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
			this.formGroup.patchValue({
				autoCompleteWithCurrentDate: BooleanUtils.isTrue(metadataDefinition.autoCompleteWithCurrentDate)
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
			this.formGroup.patchValue({
				autoCompleteWithCurrentDateTime: BooleanUtils.isTrue(metadataDefinition.autoCompleteWithCurrentDateTime)
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_MONTH) {
			this.formGroup.patchValue({
				autoCompleteWithCurrentMonth: BooleanUtils.isTrue(metadataDefinition.autoCompleteWithCurrentMonth)
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
			this.formGroup.patchValue({
				prefix: metadataDefinition.prefix,
				numberLength: metadataDefinition.numberLength
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
			this.formGroup.patchValue({
				metadataDefinitionsOfMetadataCollection: metadataDefinition.metadataDefinitions
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
			let listItems: ListMetadataItemModel[] = null;
			let defaultListItems: ListMetadataItemModel[] = null;
			if (ArrayUtils.isNotEmpty(metadataDefinition.listItems)) {
				listItems = [];
				defaultListItems = [];
				metadataDefinition.listItems.forEach((listItem: ListMetadataItemModel) => {
					listItems.push(listItem.clone());
					defaultListItems.push(listItem.clone());
				});
			}
			this.formGroup.patchValue({
				multipleSelection: BooleanUtils.isTrue(metadataDefinition.multipleSelection),
				extendable: BooleanUtils.isTrue(metadataDefinition.extendable),
				listItems: listItems
			});
			valueOfDefaultValue.listItems = defaultListItems;
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
			this.formGroup.patchValue({
				onlyUsersFromGroup: BooleanUtils.isTrue(metadataDefinition.onlyUsersFromGroup),
				idOfGroupOfPermittedUsers: metadataDefinition.idOfGroupOfPermittedUsers,
				autoCompleteWithCurrentUser: BooleanUtils.isTrue(metadataDefinition.autoCompleteWithCurrentUser),
				autoCompleteWithCurrentUserState: metadataDefinition.autoCompleteWithCurrentUserStateCode
			});
			valueOfDefaultValue.idOfGroupOfPermittedUsers = metadataDefinition.idOfGroupOfPermittedUsers;
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			let valueOfSelectionFilters: ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField = new ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField(metadataDefinition.nomenclatorId);
			valueOfSelectionFilters.selectionFilters = metadataDefinition.valueSelectionFilters;
			this.formGroup.patchValue({
				nomenclatorId: metadataDefinition.nomenclatorId,
				nomenclatorValueSelectionFilters: valueOfSelectionFilters
			});
			valueOfDefaultValue.nomenclatorId = metadataDefinition.nomenclatorId;
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DOCUMENT) {
			this.formGroup.patchValue({
				metadataDocumentTypeId: metadataDefinition.metadataDocumentTypeId,
				multipleDocumentsSelection: BooleanUtils.isTrue(metadataDefinition.multipleDocumentsSelection)
			});
		}

		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_PROJECT) {
			this.formGroup.patchValue({
				multipleProjectsSelection: BooleanUtils.isTrue(metadataDefinition.multipleProjectsSelection)
			});
		}
		
		let isNotMetadataCollection: boolean = (metadataDefinition.type !== MetadataDefinitionModel.TYPE_METADATA_COLLECTION);
		if (isNotMetadataCollection) {
			this.formGroup.patchValue({
				representative: metadataDefinition.representative,
				indexed: metadataDefinition.indexed
			});
		}

		if (this.isAutoCompleteWithMetadataApplicableForMetadataType(metadataDefinition.type)) {
			this.formGroup.patchValue({
				metadataNameForAutoCompleteWithMetadata: metadataDefinition.metadataNameForAutoCompleteWithMetadata,
				typeOfAutoCompleteWithMetadata: metadataDefinition.typeOfAutoCompleteWithMetadata,
				nomenclatorAttributeKeyForAutoCompleteWithMetadata: metadataDefinition.nomenclatorAttributeKeyForAutoCompleteWithMetadata,
				classNameForAutoCompleteWithMetadata: metadataDefinition.classNameForAutoCompleteWithMetadata
			});
			this.populateTypeOfAutoCompleteWithMetadata();
			this.populateNomenclatorAttributeKeyForAutoCompleteWithMetadata();
		}

		if (this.isApplicableDefaultValue(metadataDefinition.type)) {
			this.formGroup.patchValue({		
				defaultValue: valueOfDefaultValue
			});
		}		
	}

	private changePespective(): void {
		
		let metadataType: string = this.typeFormControl.value;

		this.enableOrDisableFormControl(this.mandatoryInStatesFormControl, this.mandatoryFormControl.value);
		this.enableOrDisableFormControl(this.restrictedOnEditInStatesFormControl, this.restrictedOnEditFormControl.value);
		this.enableOrDisableFormControl(this.invisibleInStatesFormControl, this.invisibleFormControl.value);

		let isNotTypeNullOrMetadataCollection: boolean = (ObjectUtils.isNotNullOrUndefined(metadataType) && metadataType !== MetadataDefinitionModel.TYPE_METADATA_COLLECTION);
		this.enableOrDisableFormControl(this.representativeFormControl, (this.isUsageModeDocumentType() && isNotTypeNullOrMetadataCollection));
		this.enableOrDisableFormControl(this.indexedFormControl, isNotTypeNullOrMetadataCollection);
						
		let isTypeDate: boolean = (metadataType === MetadataDefinitionModel.TYPE_DATE);
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentDateFormControl, isTypeDate);

		let isTypeDateTime: boolean = (metadataType === MetadataDefinitionModel.TYPE_DATE_TIME);
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentDateTimeFormControl, isTypeDateTime);

		let isTypeMonth: boolean = (metadataType === MetadataDefinitionModel.TYPE_MONTH);
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentMonthFormControl, isTypeMonth);

		let isTypeAutoNumber: boolean = (metadataType === MetadataDefinitionModel.TYPE_AUTO_NUMBER);
		this.enableOrDisableFormControl(this.prefixFormControl, isTypeAutoNumber);
		this.enableOrDisableFormControl(this.numberLengthFormControl, isTypeAutoNumber);

		let isTypeUser: boolean = (metadataType === MetadataDefinitionModel.TYPE_USER);
		this.enableOrDisableFormControl(this.onlyUsersFromGroupFormControl, isTypeUser);
		this.enableOrDisableFormControl(this.idOfGroupOfPermittedUsersFormControl, (isTypeUser && this.onlyUsersFromGroupFormControl.value));
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentUserFormControl, isTypeUser);
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentUserStateFormControl, (isTypeUser && this.autoCompleteWithCurrentUserFormControl.value));
		
		let isTypeList: boolean = (metadataType === MetadataDefinitionModel.TYPE_LIST);
		this.enableOrDisableFormControl(this.multipleSelectionFormControl, (this.isUsageModeDocumentType() && isTypeList));
		this.enableOrDisableFormControl(this.extendableFormControl, (this.isUsageModeDocumentType() && isTypeList));
		this.enableOrDisableFormControl(this.listItemsFormControl, isTypeList);

		let enableNomenclatorFields: boolean = (MetadataDefinitionModel.TYPE_NOMENCLATOR === metadataType);
		this.enableOrDisableFormControl(this.nomenclatorIdFormControl, enableNomenclatorFields);
		this.enableOrDisableFormControl(this.nomenclatorValueSelectionFiltersFormControl, enableNomenclatorFields);

		let enableDocumentFields: boolean = (MetadataDefinitionModel.TYPE_DOCUMENT === metadataType);
		this.enableOrDisableFormControl(this.metadataDocumentTypeIdFormControl, enableDocumentFields);
		this.enableOrDisableFormControl(this.multipleDocumentsSelectionFormControl, enableDocumentFields);

		let enableProjectFields: boolean = (MetadataDefinitionModel.TYPE_PROJECT === metadataType);
		this.enableOrDisableFormControl(this.multipleProjectsSelectionFormControl, enableProjectFields);

		let enableMetadataCollectionFields: boolean = (this.isUsageModeDocumentType() && MetadataDefinitionModel.TYPE_METADATA_COLLECTION === metadataType);
		this.enableOrDisableFormControl(this.metadataDefinitionsOfMetadataCollectionFormControl, enableMetadataCollectionFields);
		
		let enableDefaultValueField = this.isApplicableDefaultValue(metadataType);
		if (isTypeDate) {
			enableDefaultValueField = BooleanUtils.isFalse(this.autoCompleteWithCurrentDateFormControl.value);
		}
		if (isTypeDateTime) {
			enableDefaultValueField = BooleanUtils.isFalse(this.autoCompleteWithCurrentDateTimeFormControl.value);
		}
		if (isTypeMonth) {
			enableDefaultValueField = BooleanUtils.isFalse(this.autoCompleteWithCurrentMonthFormControl.value);
		}
		if (isTypeUser) {
			enableDefaultValueField = BooleanUtils.isFalse(this.autoCompleteWithCurrentUserFormControl.value);
		}

		let isAutoCompleteWithMetadataApplicable: boolean = this.isAutoCompleteWithMetadataApplicableForMetadataType(metadataType);
		this.enableOrDisableFormControl(this.metadataNameForAutoCompleteWithMetadataFormControl, isAutoCompleteWithMetadataApplicable);
		this.enableOrDisableFormControl(this.typeOfAutoCompleteWithMetadataFormControl, StringUtils.isNotBlank(this.metadataNameForAutoCompleteWithMetadataFormControl.value));
		// tslint:disable-next-line:max-line-length
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl, (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE));
		this.enableOrDisableFormControl(this.classNameForAutoCompleteWithMetadataFormControl, (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS));
		
		this.enableOrDisableFormControl(this.defaultValueFormControl, enableDefaultValueField);
	}

	private isUsageModeDocumentType(): boolean {
		return (this.usageMode === "documentType");
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	public onSaveAction(event: any): void {
		if (!this.isValid()) {
			return;
		}
		let metadataDefinition: MetadataDefinitionModel = this.prepareMetadataDefinitionModel();
		this.dataSaved.emit(metadataDefinition);
	}

	public onCancelAction(event: any): void {
		this.canceled.emit();
	}

	private prepareMetadataDefinitionModel(): MetadataDefinitionModel {
		let metadataDefinition: MetadataDefinitionModel = new MetadataDefinitionModel();
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.editMetadataDefinition)) {
			metadataDefinition.id = this.inputData.editMetadataDefinition.id;
		}
		let metadataType: string = this.typeFormControl.value;
		metadataDefinition.type = metadataType;
		metadataDefinition.name = this.nameFormControl.value.trim();
		metadataDefinition.label = this.labelFormControl.value.trim();
		let mandatory: boolean = BooleanUtils.isTrue(<boolean> this.mandatoryFormControl.value);
		metadataDefinition.mandatory = mandatory;
		if (mandatory) {
			metadataDefinition.mandatoryStates = this.mandatoryInStatesFormControl.value;
		}
		let restrictedOnEdit: boolean = BooleanUtils.isTrue(<boolean> this.restrictedOnEditFormControl.value);
		metadataDefinition.restrictedOnEdit = restrictedOnEdit;
		if (restrictedOnEdit) {
			metadataDefinition.restrictedOnEditStates =  this.restrictedOnEditInStatesFormControl.value;
		}		
		let invisible: boolean = BooleanUtils.isTrue(<boolean> this.invisibleFormControl.value);
		metadataDefinition.invisible = invisible;
		if (invisible) {
			metadataDefinition.invisibleInStates = this.invisibleInStatesFormControl.value;
		}
		metadataDefinition.orderNumber = parseInt(<string> this.orderNumberFormControl.value, 0);
		this.populateSpecificPropertiesByMetadataType(metadataType, metadataDefinition);
		return metadataDefinition;
	}

	private populateSpecificPropertiesByMetadataType(metadataType: string, metadataDefinition: MetadataDefinitionModel): void {
		
		// TODO - In acesta metoda s-ar putea face o populare in functie si de usageMode ( -- Dar trebuie sa fie gata! --).

		let isNotMetadataCollection: boolean = (metadataType !== MetadataDefinitionModel.TYPE_METADATA_COLLECTION);
		if (isNotMetadataCollection) {
			metadataDefinition.representative = BooleanUtils.isTrue(<boolean>this.representativeFormControl.value);
			metadataDefinition.indexed = BooleanUtils.isTrue(<boolean>this.indexedFormControl.value);		
		}

		if (metadataType === MetadataDefinitionModel.TYPE_DATE) {
			metadataDefinition.autoCompleteWithCurrentDate = BooleanUtils.isTrue(this.autoCompleteWithCurrentDateFormControl.value);
		} else if (metadataType === MetadataDefinitionModel.TYPE_DATE_TIME) {
			metadataDefinition.autoCompleteWithCurrentDateTime = BooleanUtils.isTrue(this.autoCompleteWithCurrentDateTimeFormControl.value);
		} else if (metadataType === MetadataDefinitionModel.TYPE_MONTH) {
			metadataDefinition.autoCompleteWithCurrentMonth = BooleanUtils.isTrue(this.autoCompleteWithCurrentMonthFormControl.value);
		} else if (metadataType === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
			metadataDefinition.prefix = this.prefixFormControl.value.trim();			
			if (ObjectUtils.isNumber(this.numberLengthFormControl.value)) {
				metadataDefinition.numberLength = this.numberLengthFormControl.value;
			} else {
				metadataDefinition.numberLength = parseInt(this.numberLengthFormControl.value.trim(), 0);
			}
		} else if (metadataType === MetadataDefinitionModel.TYPE_LIST) {
			metadataDefinition.multipleSelection = BooleanUtils.isTrue(this.multipleSelectionFormControl.value);
			metadataDefinition.extendable = BooleanUtils.isTrue(this.extendableFormControl.value);
			metadataDefinition.listItems = this.listItemsFormControl.value;
		} else if (metadataType === MetadataDefinitionModel.TYPE_USER) {
			metadataDefinition.onlyUsersFromGroup = BooleanUtils.isTrue(this.onlyUsersFromGroupFormControl.value);
			metadataDefinition.idOfGroupOfPermittedUsers = this.idOfGroupOfPermittedUsersFormControl.value;
			metadataDefinition.autoCompleteWithCurrentUser = BooleanUtils.isTrue(this.autoCompleteWithCurrentUserFormControl.value);
			metadataDefinition.autoCompleteWithCurrentUserStateCode = this.autoCompleteWithCurrentUserStateFormControl.value;
		} else if (metadataType === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
			metadataDefinition.metadataDefinitions = this.metadataDefinitionsOfMetadataCollectionFormControl.value;
		} else if (metadataType === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			metadataDefinition.nomenclatorId = this.nomenclatorIdFormControl.value;
			let fieldValue: ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField = this.nomenclatorValueSelectionFiltersFormControl.value;
			if (ObjectUtils.isNotNullOrUndefined(fieldValue)) {
				metadataDefinition.valueSelectionFilters = fieldValue.selectionFilters;
			}
		} else if (metadataType === MetadataDefinitionModel.TYPE_DOCUMENT) {
			metadataDefinition.metadataDocumentTypeId = this.metadataDocumentTypeIdFormControl.value;
			metadataDefinition.multipleDocumentsSelection = BooleanUtils.isTrue(this.multipleDocumentsSelectionFormControl.value);
		} else if (metadataType === MetadataDefinitionModel.TYPE_PROJECT) {
			metadataDefinition.multipleProjectsSelection = BooleanUtils.isTrue(this.multipleProjectsSelectionFormControl.value);
		}
		if (this.isApplicableDefaultValue(metadataType)) {
			if (ObjectUtils.isNotNullOrUndefined(this.defaultValueFormControl.value)) {
				metadataDefinition.defaultValue = (<ValueOfMetadataDefaultValueField> this.defaultValueFormControl.value).value;
			}
		}
		if (this.isAutoCompleteWithMetadataApplicableForMetadataType(metadataType)) {
			metadataDefinition.metadataNameForAutoCompleteWithMetadata = this.metadataNameForAutoCompleteWithMetadataFormControl.value;
			metadataDefinition.typeOfAutoCompleteWithMetadata = this.typeOfAutoCompleteWithMetadataFormControl.value;
			if (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE) {			
				metadataDefinition.nomenclatorAttributeKeyForAutoCompleteWithMetadata = this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl.value;
			} else if (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS) {			
				metadataDefinition.classNameForAutoCompleteWithMetadata = this.classNameForAutoCompleteWithMetadataFormControl.value;
			}
		}
	}

	public onMandatoryChanged(checked: boolean): void {
		this.mandatoryInStatesFormControl.reset();
		this.enableOrDisableFormControl(this.mandatoryInStatesFormControl, checked);
		this.updateValidityForInvisibleAndInvisibileInStates();
	}

	public onMandatoryInStatesSelectionChanged(mandatoryInStatecodesCodes: string): void {
		this.updateValidityForInvisibleAndInvisibileInStates();
	}
	
	private updateValidityForInvisibleAndInvisibileInStates(): void {
		this.invisibleFormControl.updateValueAndValidity();
		this.invisibleFormControl.markAsDirty();
		this.invisibleFormControl.markAsTouched();
		this.invisibleInStatesFormControl.updateValueAndValidity();
		this.invisibleInStatesFormControl.markAsDirty();
		this.invisibleInStatesFormControl.markAsTouched();
	}

	public onRestrictedOnEditChanged(checked: boolean): void {
		this.restrictedOnEditInStatesFormControl.reset();
		this.enableOrDisableFormControl(this.restrictedOnEditInStatesFormControl, checked);
	}

	public onInvisibleChanged(checked: boolean): void {
		this.invisibleInStatesFormControl.reset();
		this.enableOrDisableFormControl(this.invisibleInStatesFormControl, checked);
		this.invisibleInStatesFormControl.updateValueAndValidity();
	}

	public onInvisibleInStatesSelectionChanged(invisibleInStatecodesCodes: string): void {
		this.invisibleFormControl.updateValueAndValidity();
	}

	public onOnlyUsersFromGroupChanged(checked: boolean): void {
		this.enableOrDisableFormControl(this.idOfGroupOfPermittedUsersFormControl, checked);
		if (BooleanUtils.isFalse(checked)) {
			this.idOfGroupOfPermittedUsersFormControl.reset();
		}
		let defaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		if (BooleanUtils.isTrue(checked)) {
			if (StringUtils.isNotBlank(<string> this.idOfGroupOfPermittedUsersFormControl.value)) {
				defaultValue.idOfGroupOfPermittedUsers = <string> this.idOfGroupOfPermittedUsersFormControl.value;
			}
		}
		this.defaultValueFormControl.setValue(defaultValue);
	}

	public onIdOfGroupOfPermittedUsersSelectionChanged(groupId: string): void {
		let defaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		defaultValue.idOfGroupOfPermittedUsers = groupId;
		this.defaultValueFormControl.setValue(defaultValue);
	}

	public onAutoCompleteWithCurrentUserChanged(checked: boolean): void {
		this.enableOrDisableFormControl(this.autoCompleteWithCurrentUserStateFormControl, checked);
		let defaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		if (BooleanUtils.isTrue(this.onlyUsersFromGroupFormControl.value)) {
			if (StringUtils.isNotBlank(<string> this.idOfGroupOfPermittedUsersFormControl.value)) {
				defaultValue.idOfGroupOfPermittedUsers = <string> this.idOfGroupOfPermittedUsersFormControl.value;
			}
		}
		this.defaultValueFormControl.setValue(defaultValue);		
		this.enableOrDisableFormControl(this.defaultValueFormControl, !checked);
	}

	public onNomenclatorIdSelectionChanged(nomenclatorId: number): void {

		let nomenclatorValueSelectionFiltersValue: ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField= null;
		if (ObjectUtils.isNotNullOrUndefined(nomenclatorId)) {
			nomenclatorValueSelectionFiltersValue = new ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField(nomenclatorId);
		}
		this.nomenclatorValueSelectionFiltersFormControl.setValue(nomenclatorValueSelectionFiltersValue);

		this.defaultValueFormControl.reset();
		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		valueOfDefaultValue.nomenclatorId = nomenclatorId;
		this.defaultValueFormControl.setValue(valueOfDefaultValue);
	}

	public onMetadataListItemsValueChanged(listItems: ListMetadataItemModel[]): void {
		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		let currentValueOfDefaultValue: ValueOfMetadataDefaultValueField = <ValueOfMetadataDefaultValueField> this.defaultValueFormControl.value;
		valueOfDefaultValue.listItems = [];
		let selectedItemValue: string = null;
		if (ArrayUtils.isNotEmpty(listItems)) {
			listItems.forEach((listItem: ListMetadataItemModel) => {
				valueOfDefaultValue.listItems.push(listItem.clone());
				if (ObjectUtils.isNotNullOrUndefined(currentValueOfDefaultValue) && listItem.value === currentValueOfDefaultValue.value) {
					selectedItemValue = listItem.value;
				}
			});
		}
		valueOfDefaultValue.value = selectedItemValue;
		this.defaultValueFormControl.setValue(valueOfDefaultValue);
	}

	public onPrefixChanged(): void {
		this.numberLengthFormControl.updateValueAndValidity({onlySelf: false});
	}

	public onAutoCompleteWithCurrentDateChanged(checked: boolean): void {
		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		this.defaultValueFormControl.setValue(valueOfDefaultValue);
		this.enableOrDisableFormControl(this.defaultValueFormControl, BooleanUtils.isFalse(checked));
	}

	public onAutoCompleteWithCurrentDateTimeChanged(checked: boolean): void {
		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		this.defaultValueFormControl.setValue(valueOfDefaultValue);
		this.enableOrDisableFormControl(this.defaultValueFormControl, BooleanUtils.isFalse(checked));
	}

	public onAutoCompleteWithCurrentMonthChanged(checked: boolean): void {
		let valueOfDefaultValue: ValueOfMetadataDefaultValueField = new ValueOfMetadataDefaultValueField(this.typeFormControl.value);
		this.defaultValueFormControl.setValue(valueOfDefaultValue);
		this.enableOrDisableFormControl(this.defaultValueFormControl, BooleanUtils.isFalse(checked));
	}

	public onMetadataNameForAutoCompleteWithMetadataChanged(): void {
		let typeOfAutoCompleteWithMetadataEnabled: boolean = StringUtils.isNotBlank(this.metadataNameForAutoCompleteWithMetadataFormControl.value);
		this.enableOrDisableFormControl(this.typeOfAutoCompleteWithMetadataFormControl, typeOfAutoCompleteWithMetadataEnabled);
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl, false);
		this.enableOrDisableFormControl(this.classNameForAutoCompleteWithMetadataFormControl, false);
		this.typeOfAutoCompleteWithMetadataFormControl.reset();
		this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl.reset();
		this.classNameForAutoCompleteWithMetadataFormControl.reset();
		this.populateTypeOfAutoCompleteWithMetadata();
	}

	public onTypeOfAutoCompleteWithMetadataChanged(): void {
		let nomenclatorAttributeKeyForAutoCompleteWithMetadataVisible: boolean = false;
		let classNameForAutoCompleteWithMetadataVisible: boolean = false;
		if (StringUtils.isNotBlank(this.typeOfAutoCompleteWithMetadataFormControl.value)) {
			if (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE) {
				nomenclatorAttributeKeyForAutoCompleteWithMetadataVisible = true;
			} else if (this.typeOfAutoCompleteWithMetadataFormControl.value === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS) {
				classNameForAutoCompleteWithMetadataVisible = true;
			}
		}	
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl, nomenclatorAttributeKeyForAutoCompleteWithMetadataVisible);
		if (nomenclatorAttributeKeyForAutoCompleteWithMetadataVisible) {
			this.populateNomenclatorAttributeKeyForAutoCompleteWithMetadata();
		}
		this.enableOrDisableFormControl(this.classNameForAutoCompleteWithMetadataFormControl, classNameForAutoCompleteWithMetadataVisible);
		
	}

	public onMetadataDocumentTypeIdChanged(): void {
		// nothing now.
	}

	public populateNomenclatorAttributeKeyForAutoCompleteWithMetadata(): void {

		this.nomenclatorAttributeKeyForAutoCompleteWithMetadataSelectItems = [];
		this.nomenclatorAttributeKeyForAutoCompleteWithMetadataSelectItems.push(MetadataDefinitionComponent.SELECT_ITEM_NONE);	

		let nomenclatorId: number = null;
		let metadataName: string = this.metadataNameForAutoCompleteWithMetadataFormControl.value;
		this.inputData.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
			if (md.name === metadataName) {
				nomenclatorId = md.nomenclatorId;
			}
		});
		if (ObjectUtils.isNullOrUndefined(nomenclatorId)) {
			return;
		}
		this.nomenclatorService.getNomenclatorAttributesByNomenclatorId(nomenclatorId, {
			onSuccess: (attributes: NomenclatorAttributeModel[]): void => {
				if (ArrayUtils.isEmpty(attributes)) {
					return;
				}
				attributes.forEach((attribute: NomenclatorAttributeModel) => {							
					this.nomenclatorAttributeKeyForAutoCompleteWithMetadataSelectItems.push({
						value: attribute.key,
						label: attribute.name
					});
				});
				let attributeKey: number = this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl.value;
				this.nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl.setValue(attributeKey);
			}, 
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private enableOrDisableFormControl(formControl: FormControl, enable: boolean): void {
		FormUtils.enableOrDisableFormControl(formControl, enable);
	}

	private isAutoCompleteWithMetadataApplicableForMetadataType(metadataType: string): boolean {
		if (StringUtils.isBlank(metadataType)) {
			return false;
		}
		return (metadataType === MetadataDefinitionModel.TYPE_TEXT 
				|| metadataType === MetadataDefinitionModel.TYPE_TEXT_AREA 
				|| metadataType === MetadataDefinitionModel.TYPE_NOMENCLATOR
				|| metadataType === MetadataDefinitionModel.TYPE_METADATA_COLLECTION);
	}

	public get isAutoCompleteWithMetadataApplicable(): boolean {
		return this.isAutoCompleteWithMetadataApplicableForMetadataType(this.typeFormControl.value);
	}

	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.get(controlName);
	}

	public get nameFormControl(): FormControl { 
		return this.getFormControlByName("name"); 
	}
	
	public get labelFormControl(): FormControl { 
		return this.getFormControlByName("label"); 
	}

	public get typeFormControl(): FormControl { 
		return this.getFormControlByName("type"); 
	}

	public get orderNumberFormControl(): FormControl { 
		return this.getFormControlByName("orderNumber"); 
	}

	public get mandatoryFormControl(): FormControl { 
		return this.getFormControlByName("mandatory"); 
	}

	public get mandatoryInStatesFormControl(): FormControl { 
		return this.getFormControlByName("mandatoryInStates"); 
	}

	public get restrictedOnEditFormControl(): FormControl { 
		return this.getFormControlByName("restrictedOnEdit"); 
	}

	public get restrictedOnEditInStatesFormControl(): FormControl { 
		return this.getFormControlByName("restrictedOnEditInStates"); 
	}

	public get invisibleFormControl(): FormControl { 
		return this.getFormControlByName("invisible");
	}

	public get invisibleInStatesFormControl(): FormControl { 
		return this.getFormControlByName("invisibleInStates");
	}

	public get representativeFormControl(): FormControl { 
		return this.getFormControlByName("representative"); 
	}

	public get indexedFormControl(): FormControl { 
		return this.getFormControlByName("indexed"); 
	}

	public get metadataDefinitionsOfMetadataCollectionFormControl(): FormControl { 
		return this.getFormControlByName("metadataDefinitionsOfMetadataCollection");
	}

	public get prefixFormControl(): FormControl { 
		return this.getFormControlByName("prefix");
	}

	public get numberLengthFormControl(): FormControl { 
		return this.getFormControlByName("numberLength");
	}

	public get multipleSelectionFormControl(): FormControl { 
		return this.getFormControlByName("multipleSelection");
	}

	public get extendableFormControl(): FormControl { 
		return this.getFormControlByName("extendable");
	}

	public get listItemsFormControl(): FormControl { 
		return this.getFormControlByName("listItems");
	}

	public get onlyUsersFromGroupFormControl(): FormControl { 
		return this.getFormControlByName("onlyUsersFromGroup");
	}

	public get idOfGroupOfPermittedUsersFormControl(): FormControl { 
		return this.getFormControlByName("idOfGroupOfPermittedUsers");
	}

	public get autoCompleteWithCurrentUserFormControl(): FormControl { 
		return this.getFormControlByName("autoCompleteWithCurrentUser");
	}

	public get autoCompleteWithCurrentUserStateFormControl(): FormControl { 
		return this.getFormControlByName("autoCompleteWithCurrentUserState");
	}

	public get defaultValueFormControl(): FormControl { 
		return this.getFormControlByName("defaultValue");
	}

	public get nomenclatorIdFormControl(): FormControl { 
		return this.getFormControlByName("nomenclatorId");
	}

	public get nomenclatorValueSelectionFiltersFormControl(): FormControl { 
		return this.getFormControlByName("nomenclatorValueSelectionFilters");
	}

	public get autoCompleteWithCurrentDateFormControl(): FormControl { 
		return this.getFormControlByName("autoCompleteWithCurrentDate");
	}

	public get autoCompleteWithCurrentDateTimeFormControl(): FormControl { 
		return this.getFormControlByName("autoCompleteWithCurrentDateTime");
	}

	public get autoCompleteWithCurrentMonthFormControl(): FormControl { 
		return this.getFormControlByName("autoCompleteWithCurrentMonth");
	}

	public get metadataNameForAutoCompleteWithMetadataFormControl(): FormControl { 
		return this.getFormControlByName("metadataNameForAutoCompleteWithMetadata");
	}

	public get typeOfAutoCompleteWithMetadataFormControl(): FormControl { 
		return this.getFormControlByName("typeOfAutoCompleteWithMetadata");
	}

	public get nomenclatorAttributeKeyForAutoCompleteWithMetadataFormControl(): FormControl { 
		return this.getFormControlByName("nomenclatorAttributeKeyForAutoCompleteWithMetadata");
	}

	public get classNameForAutoCompleteWithMetadataFormControl(): FormControl { 
		return this.getFormControlByName("classNameForAutoCompleteWithMetadata");
	}

	public get metadataDocumentTypeIdFormControl(): FormControl { 
		return this.getFormControlByName("metadataDocumentTypeId");
	}

	public get multipleDocumentsSelectionFormControl(): FormControl { 
		return this.getFormControlByName("multipleDocumentsSelection");
	}

	public get multipleProjectsSelectionFormControl(): FormControl { 
		return this.getFormControlByName("multipleProjectsSelection");
	}
}