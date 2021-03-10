import { Component, Output, EventEmitter, Input, OnInit, OnChanges, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { NomenclatorAttributeModel, ObjectUtils, AttributeTypeEnum, BooleanUtils, FormUtils, TranslateUtils, StringUtils, NomenclatorAttributeSelectionFilterModel, ArrayUtils, NomenclatorService, AppError, MessageDisplayer } from "@app/shared";
import { SelectItem } from "primeng/primeng";
import { ValueOfNomenclatorAttributeDefinitionDefaultValueField } from "./nomenclator-attribute-definition-default-value-field/nomenclator-attribute-definition-default-value-field.component";
import { ValueOfNomenclatorAttributeSelectionFilterField } from "./nomenclator-attribute-definition-selection-filters-field.component";
import { ValidatorFn } from "@angular/forms";
import { AbstractControl } from "@angular/forms";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-nomenclator-attribute-definition-component",
	templateUrl: "./nomenclator-attribute-definition.component.html"
})
export class NomenclatorAttributeDefinitionComponent implements OnChanges, OnInit {

	@Input()
	public inputData: NomenclatorAttributeDefinitionInputData;

	@Output()
	public canceled: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<NomenclatorAttributeModel>;

	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;
	private nomenclatorService: NomenclatorService;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public attributeTypeSelectItems: SelectItem[] = [];
	public attributeKeyForAutocompleteSelectItems: SelectItem[] = [];
	public autocompleteTypeSelectItems: SelectItem[] = [];
	public nomenclatorAttributeKeyForAutocompleteSelectItems: SelectItem[];

	public constructor(formBuilder: FormBuilder, translateUtils: TranslateUtils, 
			messageDisplayer: MessageDisplayer, nomenclatorService: NomenclatorService) {
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.nomenclatorService = nomenclatorService;
		this.canceled = new EventEmitter();
		this.dataSaved = new EventEmitter<NomenclatorAttributeModel>();
		this.init();
	}

	private init(): void {
		this.prepareForm();
		this.buildAttributeTypeSelectItems();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group([]);
		this.form.addControl("name", new FormControl(null, [Validators.required, AttributeDefinitionValidators.attributeNameValidator()]));
		this.form.addControl("key", new FormControl(null, []));
		this.form.addControl("type", new FormControl(null, [Validators.required]));
		this.form.addControl("required", new FormControl(false, []));
		this.form.addControl("requiredCheckExpression", new FormControl(false, []));
		this.form.addControl("invisibleCheckExpression", new FormControl());
		this.form.addControl("orderNumber", new FormControl(null, [Validators.required]));
		this.form.addControl("nomenclatorId", new FormControl({value: null, disabled: true}, [Validators.required]));
		this.form.addControl("nomenclatorSelectionFilters", new FormControl());
		this.form.addControl("nomenclatorSelectionFiltersCustomClass", new FormControl());
		this.form.addControl("nomenclatorSelectionFiltersCustomClassAttributeKeys", new FormControl());
		this.form.addControl("defaultValue", new FormControl({value: null, disabled: true}));
		this.form.addControl("readonlyOnAdd", new FormControl(false, []));		
		this.form.addControl("readonlyOnEdit", new FormControl(false, []));		
		this.form.addControl("attributeKeyForAutocomplete", new FormControl({value: null, disabled: true}, []));
		this.form.addControl("autocompleteType", new FormControl({value: null, disabled: true}, []));
		this.form.addControl("nomenclatorAttributeKeyForAutocomplete", new FormControl({value: null, disabled: true}, []));
	}

	private buildAttributeTypeSelectItems(): void {
		this.attributeTypeSelectItems = [
			{ label: this.translateUtils.translateLabel("TEXT"), value: AttributeTypeEnum.TEXT },
			{ label: this.translateUtils.translateLabel("NUMERIC"), value: AttributeTypeEnum.NUMERIC },
			{ label: this.translateUtils.translateLabel("DATE"), value: AttributeTypeEnum.DATE },
			{ label: this.translateUtils.translateLabel("BOOLEAN"), value: AttributeTypeEnum.BOOLEAN },
			{ label: this.translateUtils.translateLabel("NOMENCLATOR"), value: AttributeTypeEnum.NOMENCLATOR }
		];

		ListItemUtils.sortByLabel(this.attributeTypeSelectItems);
	}

	public ngOnInit(): void {
	}

	public get keyFieldReadonly(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.inputData.editAttributeModel) && ObjectUtils.isNotNullOrUndefined(this.inputData.editAttributeModel.id);
	}

	public ngOnChanges(): void {
		this.form.reset();
		this.populateAttributeKeysForAutocomplete();
		this.updateValidators();
		if (this.isAdd()) {
			let orderNumber: number = this.prepareDefaultUiOrderNumber();
			this.orderNumberFormControl.setValue(orderNumber);
		} else if (this.isEdit()) {
			this.populateFormValuesFromModel();
		}		
		this.changePerspective();
	}

	private updateValidators(): void {
		this.keyFormControl.clearValidators();
		this.keyFormControl.setValidators([AttributeDefinitionValidators.attributeKeyValidator(this.getTakenAttributeKeys())]);
	}

	private getTakenAttributeKeys(): string[] {
		let takenAttributeKeys: string[] = [];
		if (ArrayUtils.isNotEmpty(this.inputData.attributeModels)) {
			this.inputData.attributeModels.forEach((md: NomenclatorAttributeModel) => {
				takenAttributeKeys.push(md.key);
			});
		}
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.editAttributeModel)) {
			ArrayUtils.removeElement(takenAttributeKeys, this.inputData.editAttributeModel.key);
		}		
		return takenAttributeKeys;
	}

	private isAttributeTypeCandidateForAutocomplete(attributeType: string): boolean {
		if (StringUtils.isBlank(attributeType)) {
			return false;
		}
		return attributeType === AttributeTypeEnum.NOMENCLATOR || attributeType === AttributeTypeEnum.TEXT || attributeType === AttributeTypeEnum.NUMERIC;
	}

	private isAutocompleteApplicableForAttributeType(attributeType: string): boolean {
		if (StringUtils.isBlank(attributeType)) {
			return false;
		}
		return (attributeType === AttributeTypeEnum.TEXT || attributeType === AttributeTypeEnum.NUMERIC || attributeType === AttributeTypeEnum.NOMENCLATOR);
	}

	public get isAutocompleteApplicable(): boolean {
		return this.isAutocompleteApplicableForAttributeType(this.typeFormControl.value);
	}

	private populateAttributeKeysForAutocomplete(): void {
		this.attributeKeyForAutocompleteSelectItems = [];
		if (ArrayUtils.isNotEmpty(this.inputData.attributeModels)) {
			this.inputData.attributeModels.forEach((attributeModel: NomenclatorAttributeModel) => {
				let canAdd: boolean = false;
				if (this.isAttributeTypeCandidateForAutocomplete(attributeModel.type)) {
					if (ObjectUtils.isNullOrUndefined(this.inputData.editAttributeModel) || this.inputData.editAttributeModel.name !== attributeModel.name) {
						canAdd = true;
					}
				}
				if (canAdd) {
					this.attributeKeyForAutocompleteSelectItems.push({
						value: attributeModel.key,
						label: attributeModel.name
					});
				}			
			});
		}

		ListItemUtils.sortByLabel(this.attributeKeyForAutocompleteSelectItems);
	}

	private populateAutocompleteTypeSelectItems(): void {
		this.autocompleteTypeSelectItems = [];
		let attributeKey: string = this.attributeKeyForAutocompleteFormControl.value;
		if (StringUtils.isBlank(attributeKey)) {
			return;
		}
		this.inputData.attributeModels.forEach((attributeModel: NomenclatorAttributeModel) => {
			if (attributeModel.key === attributeKey) {
				this.autocompleteTypeSelectItems.push(this.createSelectItemForTypeOfAutocomplete(NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_COPY_VALUE));
				this.autocompleteTypeSelectItems.push(this.createSelectItemForTypeOfAutocomplete(NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_DELETE_VALUE));
				if (attributeModel.type === AttributeTypeEnum.NOMENCLATOR) {			
					this.autocompleteTypeSelectItems.push(this.createSelectItemForTypeOfAutocomplete(NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_NOMENCLATOR_ATTRIBUTE));
				}
			}
		});

		ListItemUtils.sortByLabel(this.autocompleteTypeSelectItems);
	}

	private createSelectItemForTypeOfAutocomplete(type: string): SelectItem {
		return {
			value: type,
			label: this.translateUtils.translateLabel("NOMENCLATOR_ATTRIBUTE_AUTOCOMPLETE_TYPE_" + type)
		};
	}


	private prepareDefaultUiOrderNumber(): number {
		let max: number = 0;
		if (ArrayUtils.isNotEmpty(this.inputData.attributeModels)) {
			this.inputData.attributeModels.forEach((attributeModel: NomenclatorAttributeModel) => {
				if (attributeModel.uiOrder > max) {				
					max = attributeModel.uiOrder;
				}
			});
		}
		max++;
		return max;
	}

	private isAdd(): boolean {
		return ObjectUtils.isNullOrUndefined(this.inputData.editAttributeModel);
	}

	private isEdit(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.inputData.editAttributeModel);
	}

	private populateFormValuesFromModel(): void {

		if (ObjectUtils.isNullOrUndefined(this.inputData.editAttributeModel)) {			
			throw new Error("selectedNomenclatorAttribute cannot be null");
		}

		let defaultValue: ValueOfNomenclatorAttributeDefinitionDefaultValueField = new ValueOfNomenclatorAttributeDefinitionDefaultValueField(this.inputData.editAttributeModel.type);
		
		this.form.patchValue({
			name: this.inputData.editAttributeModel.name.trim(),
			key: this.inputData.editAttributeModel.key.trim(),
			type: this.inputData.editAttributeModel.type,
			required: BooleanUtils.isTrue(this.inputData.editAttributeModel.required),
			requiredCheckExpression: this.inputData.editAttributeModel.requiredCheckExpression,
			invisibleCheckExpression: this.inputData.editAttributeModel.invisibleCheckExpression,
			orderNumber: this.inputData.editAttributeModel.uiOrder,
			readonlyOnAdd: BooleanUtils.isTrue(this.inputData.editAttributeModel.readonlyOnAdd),
			readonlyOnEdit: BooleanUtils.isTrue(this.inputData.editAttributeModel.readonlyOnEdit)
		});

		if (this.isAutocompleteApplicableForAttributeType(this.inputData.editAttributeModel.type)) {
			this.form.patchValue({
				attributeKeyForAutocomplete: this.inputData.editAttributeModel.attributeKeyForAutocomplete,
				autocompleteType: this.inputData.editAttributeModel.autocompleteType,
				nomenclatorAttributeKeyForAutocomplete: this.inputData.editAttributeModel.nomenclatorAttributeKeyForAutocomplete
			});
			this.populateAutocompleteTypeSelectItems();
			this.populateNomenclatorAttributeKeyForAutocomplete();
		}

		if (ObjectUtils.isNotNullOrUndefined(this.inputData.editAttributeModel.typeNomenclatorId)) {
			let valueOfNomenclatorSelectionFilters: ValueOfNomenclatorAttributeSelectionFilterField = new ValueOfNomenclatorAttributeSelectionFilterField(this.inputData.editAttributeModel.typeNomenclatorId);
			valueOfNomenclatorSelectionFilters.selectionFilters = this.inputData.editAttributeModel.typeNomenclatorSelectionFilters;
			this.form.patchValue({
				nomenclatorId: this.inputData.editAttributeModel.typeNomenclatorId,
				nomenclatorSelectionFilters: valueOfNomenclatorSelectionFilters,
				nomenclatorSelectionFiltersCustomClass: this.inputData.editAttributeModel.typeNomenclatorSelectionFiltersCustomClass,
				nomenclatorSelectionFiltersCustomClassAttributeKeys: this.inputData.editAttributeModel.typeNomenclatorSelectionFiltersCustomClassAttributeKeys
			});
			defaultValue.nomenclatorId = this.inputData.editAttributeModel.typeNomenclatorId;
		}
		defaultValue.value = this.inputData.editAttributeModel.defaultValue;
		this.form.patchValue({
			defaultValue: defaultValue
		});
	}

	private changePerspective(): void {

		let attributeType: string = this.typeFormControl.value;

		this.enableOrDisableFormControl(this.requiredCheckExpressionFormControl, BooleanUtils.isFalse(this.requiredFormControl.value));

		let enableNomenclatorFields: boolean = (AttributeTypeEnum.NOMENCLATOR === this.typeFormControl.value);
		this.enableOrDisableFormControl(this.nomenclatorIdFormControl, enableNomenclatorFields);

		let enableNomenclatorSelectionFilters: boolean = ObjectUtils.isNotNullOrUndefined(this.nomenclatorIdFormControl.value);
		this.enableOrDisableFormControl(this.nomenclatorSelectionFiltersFormControl, enableNomenclatorSelectionFilters);
		this.enableOrDisableFormControl(this.nomenclatorSelectionFiltersCustomClassFormControl, enableNomenclatorSelectionFilters);
		this.enableOrDisableFormControl(this.nomenclatorSelectionFiltersCustomClassAttributeKeysFormControl, enableNomenclatorSelectionFilters);

		let isAutocompleteApplicable: boolean = this.isAutocompleteApplicableForAttributeType(attributeType);
		this.enableOrDisableFormControl(this.attributeKeyForAutocompleteFormControl, isAutocompleteApplicable);
		this.enableOrDisableFormControl(this.autocompleteTypeFormControl, StringUtils.isNotBlank(this.attributeKeyForAutocompleteFormControl.value));
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutocompleteFormControl, (this.autocompleteTypeFormControl.value === NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_NOMENCLATOR_ATTRIBUTE));
		
		this.enableOrDisableFormControl(this.defaultValueFormControl, StringUtils.isNotBlank(attributeType));
	}

	public populateNomenclatorAttributeKeyForAutocomplete(): void {

		this.nomenclatorAttributeKeyForAutocompleteSelectItems = [];

		let nomenclatorId: number = null;
		let attributeKey: string = this.attributeKeyForAutocompleteFormControl.value;
		this.inputData.attributeModels.forEach((attributemodel: NomenclatorAttributeModel) => {
			if (attributemodel.key === attributeKey) {
				nomenclatorId = attributemodel.typeNomenclatorId;
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
					this.nomenclatorAttributeKeyForAutocompleteSelectItems.push({
						value: attribute.key,
						label: attribute.name
					});
				});
				let attributeKeyValue: any = this.nomenclatorAttributeKeyForAutocompleteFormControl.value;
				this.nomenclatorAttributeKeyForAutocompleteFormControl.setValue(attributeKeyValue);

				ListItemUtils.sortByLabel(this.nomenclatorAttributeKeyForAutocompleteSelectItems);				
			}, 
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onSaveAction(event: any): void {
		if (!this.isValid()) {
			return;
		}
		let nomenclatorAttribute: NomenclatorAttributeModel = this.prepareNomenclatorAttributeModel();
		this.dataSaved.emit(nomenclatorAttribute);
	}

	public onCancelAction(event: any): void {
		this.canceled.emit();
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private prepareNomenclatorAttributeModel(): NomenclatorAttributeModel {
		if (ObjectUtils.isNullOrUndefined(this.inputData.editAttributeModel)) {
			this.inputData.editAttributeModel = new NomenclatorAttributeModel();
		}
		this.inputData.editAttributeModel.name = this.nameFormControl.value.trim();
		this.inputData.editAttributeModel.key = this.keyFormControl.value.trim();
		this.inputData.editAttributeModel.type = this.typeFormControl.value;
		this.inputData.editAttributeModel.required = BooleanUtils.isTrue(this.requiredFormControl.value);
		this.inputData.editAttributeModel.requiredCheckExpression = this.requiredCheckExpressionFormControl.value;
		this.inputData.editAttributeModel.invisibleCheckExpression = this.invisibleCheckExpressionFormControl.value;
		this.inputData.editAttributeModel.uiOrder = parseInt(<string> this.orderNumberFormControl.value, 0);
		this.inputData.editAttributeModel.readonlyOnAdd = BooleanUtils.isTrue(this.readonlyOnAddFormControl.value);
		this.inputData.editAttributeModel.readonlyOnEdit = BooleanUtils.isTrue(this.readonlyOnEditFormControl.value);		
		if (this.typeFormControl.value === AttributeTypeEnum.NOMENCLATOR) {
			this.inputData.editAttributeModel.typeNomenclatorId = this.nomenclatorIdFormControl.value;
			if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorSelectionFiltersFormControl.value)) {
				this.inputData.editAttributeModel.typeNomenclatorSelectionFilters = (<ValueOfNomenclatorAttributeSelectionFilterField> this.nomenclatorSelectionFiltersFormControl.value).selectionFilters;
			}
			this.inputData.editAttributeModel.typeNomenclatorSelectionFiltersCustomClass = this.nomenclatorSelectionFiltersCustomClassFormControl.value;
			this.inputData.editAttributeModel.typeNomenclatorSelectionFiltersCustomClassAttributeKeys = this.nomenclatorSelectionFiltersCustomClassAttributeKeysFormControl.value;
		}
		this.inputData.editAttributeModel.attributeKeyForAutocomplete = this.attributeKeyForAutocompleteFormControl.value;
		this.inputData.editAttributeModel.autocompleteType = this.autocompleteTypeFormControl.value;
		this.inputData.editAttributeModel.nomenclatorAttributeKeyForAutocomplete = this.nomenclatorAttributeKeyForAutocompleteFormControl.value;
		
		let rawDefaultValue: any = this.defaultValueFormControl.value;		
		if (ObjectUtils.isNotNullOrUndefined(rawDefaultValue)) {
			this.inputData.editAttributeModel.defaultValue = (<ValueOfNomenclatorAttributeDefinitionDefaultValueField> this.defaultValueFormControl.value).value;
		}	
		return this.inputData.editAttributeModel;
	}

	public onAttributeTypeSelectionChanged(event: any): void {
		this.changePerspective();		
		this.nomenclatorSelectionFiltersCustomClassFormControl.reset();
		this.autocompleteTypeFormControl.reset();
		this.attributeKeyForAutocompleteFormControl.reset();
		this.nomenclatorAttributeKeyForAutocompleteFormControl.reset();
		let defaultValue: ValueOfNomenclatorAttributeDefinitionDefaultValueField = new ValueOfNomenclatorAttributeDefinitionDefaultValueField(event.value);
		this.defaultValueFormControl.setValue(defaultValue);
	}

	public onRequiredChanged(): void {
		this.enableOrDisableFormControl(this.requiredCheckExpressionFormControl, BooleanUtils.isFalse(this.requiredFormControl.value));
	}

	public onNomenclatorIdSelectionChanged(nomenclatorId: number): void {
		
		let nomenclatorSelectionFiltersValue: ValueOfNomenclatorAttributeSelectionFilterField = null;
		if (ObjectUtils.isNotNullOrUndefined(nomenclatorId)) {
			nomenclatorSelectionFiltersValue = new ValueOfNomenclatorAttributeSelectionFilterField(nomenclatorId);
		}
		this.nomenclatorSelectionFiltersFormControl.setValue(nomenclatorSelectionFiltersValue);
		
		let defaultValue: ValueOfNomenclatorAttributeDefinitionDefaultValueField = new ValueOfNomenclatorAttributeDefinitionDefaultValueField(this.typeFormControl.value);
		defaultValue.nomenclatorId = nomenclatorId;
		this.defaultValueFormControl.setValue(defaultValue);

		this.changePerspective();
	}

	private enableOrDisableFormControl(formControl: FormControl, enable: boolean): void {
		if (BooleanUtils.isTrue(enable)) {
			formControl.enable();
		} else {
			formControl.disable();
		}
	}

	public onAttributeKeyForAutocompleteChanged(): void {
		let typeOfAutocompleteEnabled: boolean = StringUtils.isNotBlank(this.attributeKeyForAutocompleteFormControl.value);
		this.enableOrDisableFormControl(this.autocompleteTypeFormControl, typeOfAutocompleteEnabled);
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutocompleteFormControl, false);
		this.autocompleteTypeFormControl.reset();
		this.nomenclatorAttributeKeyForAutocompleteFormControl.reset();
		this.populateAutocompleteTypeSelectItems();
	}

	public onAutocompleteTypeChanged(): void {
		let nomenclatorAttributeKeyForAutocompleteVisible: boolean = false;
		if (StringUtils.isNotBlank(this.autocompleteTypeFormControl.value)) {
			if (this.autocompleteTypeFormControl.value === NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_NOMENCLATOR_ATTRIBUTE) {
				nomenclatorAttributeKeyForAutocompleteVisible = true;
			}
		}	
		this.enableOrDisableFormControl(this.nomenclatorAttributeKeyForAutocompleteFormControl, nomenclatorAttributeKeyForAutocompleteVisible);
		if (nomenclatorAttributeKeyForAutocompleteVisible) {
			this.populateNomenclatorAttributeKeyForAutocomplete();
		}
	}

	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.form.get(controlName);
	}

	public get nameFormControl(): FormControl { 
		return this.getFormControlByName("name"); 
	}

	public get keyFormControl(): FormControl { 
		return this.getFormControlByName("key"); 
	}

	public get typeFormControl(): FormControl { 
		return this.getFormControlByName("type"); 
	}

	public get requiredFormControl(): FormControl { 
		return this.getFormControlByName("required"); 
	}

	public get requiredCheckExpressionFormControl(): FormControl { 
		return this.getFormControlByName("requiredCheckExpression"); 
	}

	public get invisibleCheckExpressionFormControl(): FormControl { 
		return this.getFormControlByName("invisibleCheckExpression"); 
	}

	public get orderNumberFormControl(): FormControl { 
		return this.getFormControlByName("orderNumber"); 
	}

	public get nomenclatorIdFormControl(): FormControl { 
		return this.getFormControlByName("nomenclatorId");
	}

	public get nomenclatorSelectionFiltersFormControl(): FormControl {
		return this.getFormControlByName("nomenclatorSelectionFilters");
	}

	public get nomenclatorSelectionFiltersCustomClassFormControl(): FormControl {
		return this.getFormControlByName("nomenclatorSelectionFiltersCustomClass");
	}

	public get nomenclatorSelectionFiltersCustomClassAttributeKeysFormControl(): FormControl {
		return this.getFormControlByName("nomenclatorSelectionFiltersCustomClassAttributeKeys");
	}

	public get defaultValueFormControl(): FormControl { 
		return this.getFormControlByName("defaultValue");
	}

	public get readonlyOnAddFormControl(): FormControl { 
		return this.getFormControlByName("readonlyOnAdd");
	}

	public get readonlyOnEditFormControl(): FormControl { 
		return this.getFormControlByName("readonlyOnEdit");
	}

	public get attributeKeyForAutocompleteFormControl(): FormControl { 
		return this.getFormControlByName("attributeKeyForAutocomplete");
	}

	public get autocompleteTypeFormControl(): FormControl { 
		return this.getFormControlByName("autocompleteType");
	}

	public get nomenclatorAttributeKeyForAutocompleteFormControl(): FormControl { 
		return this.getFormControlByName("nomenclatorAttributeKeyForAutocomplete");
	}
}

class AttributeDefinitionValidators {

	public static attributeNameValidator(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			
			let controlValue: string = control.value;
			if (StringUtils.isBlank(controlValue)) {
				return { required: true };
			}

			// TODO - de verificat si duplicated.

			return null;
		};
	}

	public static attributeKeyValidator(takenAttributeKeys: string[]): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			
			let controlValue: string = control.value;
			if (StringUtils.isBlank(controlValue)) {
				return { required: true };
			}
			
			let regExp: RegExp = new RegExp(/^[a-zA-Z0-9_]+$/);
			let isPattern: boolean = regExp.test(controlValue);
			if (!isPattern) {
				return { pattern: true };
			}

			if (ArrayUtils.isEmpty(takenAttributeKeys)) {
				return null;
			}
			let duplicated: boolean = false;
			takenAttributeKeys.forEach((takenValue: string) => {
				if (takenValue === controlValue) {
					duplicated = true;
				}
			});
			return duplicated ? { keyExists: true } : null;
		};
	}
}

export class NomenclatorAttributeDefinitionInputData {

	public editAttributeModel: NomenclatorAttributeModel;

	public attributeModels: NomenclatorAttributeModel[];
}