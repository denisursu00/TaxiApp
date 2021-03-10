import { Component, Input, OnInit } from "@angular/core";
import { Validators } from "@angular/forms";
import { FormGroup } from "@angular/forms";
import { DateUtils, BooleanUtils, ObjectUtils, StringUtils, ArrayUtils, NomenclatorUtils } from "./../../../utils";
import { ValueOfNomenclatorValueField } from "./../../../components/nomenclator-value-field";
import { AppError, AttributeTypeEnum, NomenclatorAttributeModel } from "./../../../model";
import { DateConstants } from "./../../../constants";
import { AttributeEventMediator, AttributeEventName, AttributeEvent } from "./attribute-event-mediator";
import { NomenclatorFilter } from "./../../../model/nomenclator/nomenclator-filter";
import { NomenclatorAttributeSelectionFilterModel, NomenclatorMultipleFilter } from "./../../../model/nomenclator";
import { NomenclatorService } from "./../../../service";
import { CustomNomenclatorSelectionFiltersRequestModel, CustomNomenclatorSelectionFiltersResponseModel } from "./../../../model/nomenclator";
import { NomenclatorValueModel } from "./../../../model";
import { MessageDisplayer } from "./../../../message-displayer";
import { NomenclatorValidators } from "./../../../validators";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-nomenclator-attribute",
	templateUrl: "./nomenclator-attribute.component.html"
})
export class NomenclatorAttributeComponent implements OnInit {

	@Input()
	public formGroup: FormGroup;
	
	@Input()
	public attribute: NomenclatorAttributeModel;

	public readonly: boolean;

	public visible: boolean = false;
	private visibleByExpression: boolean  = true;

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public eventMediator: AttributeEventMediator;
	
	private allAttributesReady: boolean = false;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public required: boolean;

	public dateFormat: string;
	public yearRange: string;

	public nomenclatorCustomFilters: NomenclatorMultipleFilter[] = [];
	public nomenclatorCustomFilterByValueIds: number[] = [];
	public nomenclatorFilterValuesFromAttributes: object = {};
	public nomenclatorFieldDisabled: boolean = false;
	private customNomenclatorSelectionFiltersResponseModel: CustomNomenclatorSelectionFiltersResponseModel;

	public loading: boolean = false;

	public completionSuggestions: SelectItem[];

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.completionSuggestions = [];
	}

	public ngOnInit(): void {
		
		if (this.isView()) {
			this.readonly = true;
		} else {
			if (this.isAdd()) {
				this.readonly = this.attribute.readonlyOnAdd;
			} else if (this.isEdit()) {
				this.readonly = this.attribute.readonlyOnEdit;
			}
		}		

		if (this.attribute.type === AttributeTypeEnum.BOOLEAN) {
			this.required = false;
		} else {
			if (this.isView()) {
				this.required = false;
			} else {
				this.required = (BooleanUtils.isTrue(this.attribute.required));
			}
		}
		
		// Workaround readonly
		if (this.readonly) {
			if (this.attribute.type === AttributeTypeEnum.DATE || this.attribute.type === AttributeTypeEnum.BOOLEAN) {
				this.attributeFormControl.disable();
			}
		}
		
		this.prepareCompletionSuggestions();

		this.fireAttributeInitializedEvent();
		this.subscribeForEvents();
	}

	public isAdd(): boolean {
		return this.mode === "add";
	}

	public isEdit(): boolean {
		return this.mode === "edit";
	}

	public isView(): boolean {
		return this.mode === "view";
	}

	private isNomenclatorSelectionFilterApplicable(): boolean {
		return (this.attribute.type === AttributeTypeEnum.NOMENCLATOR 
				&& ArrayUtils.isNotEmpty(this.attribute.typeNomenclatorSelectionFilters));	
	}

	private isNomenclatorCustomClassSelectionFilterApplicable(): boolean {
		return (this.attribute.type === AttributeTypeEnum.NOMENCLATOR 
				&& StringUtils.isNotBlank(this.attribute.typeNomenclatorSelectionFiltersCustomClass)
				&& StringUtils.isNotBlank(this.attribute.typeNomenclatorSelectionFiltersCustomClassAttributeKeys));	
	}

	private prepareNomenclatorCustomFilters(callback: () => any): void {		
		this.nomenclatorCustomFilters = [];		
		if (this.isNomenclatorSelectionFilterApplicable()) {
			this.attribute.typeNomenclatorSelectionFilters.forEach((selectFilter: NomenclatorAttributeSelectionFilterModel) => {
				let nomenclatorFilter: NomenclatorMultipleFilter = this.getCustomFilterByAttributeKey(selectFilter.filterAttributeKey);
				if (nomenclatorFilter == null) {
					nomenclatorFilter = new NomenclatorMultipleFilter();
					nomenclatorFilter.attributeKey = selectFilter.filterAttributeKey;
					nomenclatorFilter.values = [];
					this.nomenclatorCustomFilters.push(nomenclatorFilter);
				}
				if (StringUtils.isNotBlank(selectFilter.defaultFilterValue)) {
					nomenclatorFilter.values.push(selectFilter.defaultFilterValue);
				}
				if (StringUtils.isNotBlank(selectFilter.attributeKeyForAutocompleteFilterValue)) {
					let filterValueByAttribute = this.nomenclatorFilterValuesFromAttributes[selectFilter.attributeKeyForAutocompleteFilterValue];
					if (ObjectUtils.isNotNullOrUndefined(filterValueByAttribute)) {
						nomenclatorFilter.values.push(filterValueByAttribute);
					}
				}
			});
		}
		if (this.isNomenclatorCustomClassSelectionFilterApplicable()) {

			let requestModel: CustomNomenclatorSelectionFiltersRequestModel = new CustomNomenclatorSelectionFiltersRequestModel();
			requestModel.nomenclatorId = this.attribute.nomenclatorId;
			requestModel.attributeId = this.attribute.id;
			requestModel.attributeValueByKey = {};
			let attributeKeys: string[] = this.getAttributeKeysForCustomClassSelectionFilter();
			attributeKeys.forEach((attributeKey: string) => {
				requestModel.attributeValueByKey[attributeKey] =  this.nomenclatorFilterValuesFromAttributes[attributeKey] ;
			});
			this.loading = true;
			this.customNomenclatorSelectionFiltersResponseModel = null;
			this.nomenclatorService.getCustomNomenclatorSelectionFilters(requestModel, {
				onSuccess: (responseModel: CustomNomenclatorSelectionFiltersResponseModel): void => {
					this.customNomenclatorSelectionFiltersResponseModel = responseModel;
					this.nomenclatorCustomFilterByValueIds = responseModel.valueIds;
					this.nomenclatorCustomFilters = [];
					if (ObjectUtils.isNotNullOrUndefined(responseModel.attributeValuesByKey)) {
						Object.keys(responseModel.attributeValuesByKey).forEach((attributeKey: string) => {
							let nomenclatorFilter: NomenclatorMultipleFilter = this.getCustomFilterByAttributeKey(attributeKey);
							if (nomenclatorFilter == null) {
								nomenclatorFilter = new NomenclatorMultipleFilter();
								nomenclatorFilter.attributeKey = attributeKey;
								nomenclatorFilter.values = [];
								this.nomenclatorCustomFilters.push(nomenclatorFilter);
							}
							if (ArrayUtils.isNotEmpty(responseModel.attributeValuesByKey[attributeKey])) {
								nomenclatorFilter.values = nomenclatorFilter.values.concat(responseModel.attributeValuesByKey[attributeKey]);
							}
						});
					}						
					this.loading = false;
					callback();
				},
				onFailure: (error: AppError): void => {
					this.loading = false;
					callback();
				}
			});

		} else {
			callback();
		}
	}

	private getAttributeKeysForCustomClassSelectionFilter(): string[] {
		let attributeKeys: string[] = [];
		if (StringUtils.isNotBlank(this.attribute.typeNomenclatorSelectionFiltersCustomClassAttributeKeys)) {
			attributeKeys = this.attribute.typeNomenclatorSelectionFiltersCustomClassAttributeKeys.split(";");
		}
		return attributeKeys;
	}

	private isOtherNomenclatorAttributeCompleted(attributeKey: string): boolean {
		let found: boolean = false;
		Object.keys(this.nomenclatorFilterValuesFromAttributes).forEach((keyAsAttributeKey: string) => {
			if (keyAsAttributeKey === attributeKey && ObjectUtils.isNotNullOrUndefined(this.nomenclatorFilterValuesFromAttributes[keyAsAttributeKey])) {
				found = true;
			}
		});
		return found;
	}

	private getCustomFilterByAttributeKey(attributeKey : string): NomenclatorMultipleFilter {
		let returnFilter: NomenclatorMultipleFilter = null;
		this.nomenclatorCustomFilters.forEach((filter: NomenclatorMultipleFilter) => {
			if (filter.attributeKey === attributeKey) {
				returnFilter = filter;
			}
		});
		return returnFilter;
	}

	private subscribeForEvents(): void {
		if (ObjectUtils.isNullOrUndefined(this.eventMediator)) {
			return;
		}
		this.subscribeForAllAttributesReadyEvent();
		this.subscribeForExpressionsRanEvent();		
		if (this.isView()) {
			return;
		}
		this.subscribeForAttributeValueChangeEvent();
	}

	private subscribeForAllAttributesReadyEvent(): void {		
		this.eventMediator.subscribe({
			eventName: AttributeEventName.ALL_ATTRIBUTES_READY,
			handle: (event: AttributeEvent) => {
				this.allAttributesReady = true;
				this.visible = this.visibleByExpression;
				this.autoCompleteWithDefaultValue();
				if (this.isEdit()) {
					if (this.isNomenclatorSelectionFilterApplicable() || this.isNomenclatorCustomClassSelectionFilterApplicable()) {
						if (ObjectUtils.isNotNullOrUndefined(event.attributeValues)) {
							Object.keys(event.attributeValues).forEach((key: string) => {
								this.updateNomenclatorFilterValuesFromAttributes(key, event.attributeValues[key]);
							});
						}
					}
				}
				this.nomenclatorFieldDisabled = false;
				this.prepareNomenclatorCustomFilters(() => {
					this.changeNomenclatorFieldPerspective();
				});
			}
		});
	}

	private subscribeForExpressionsRanEvent(): void {	
		this.eventMediator.subscribe({
			eventName: AttributeEventName.EXPRESSIONS_RAN,
			handle: (event: AttributeEvent) => {
				let result: any = event.expressionsRanResponse.resultsByAttributeKey[this.attribute.key];
				if (ObjectUtils.isNotNullOrUndefined(result)) {
					if (this.isAdd() || this.isEdit()) {
						let requiredResult: string = result["required"];
						if (ObjectUtils.isNotNullOrUndefined(requiredResult)) {
							let prevRequired: boolean = this.required;
							this.required = ("true" === requiredResult);
							if (this.required) {
								if (!prevRequired) {							
									if (this.attribute.type === AttributeTypeEnum.NOMENCLATOR) {
										this.attributeFormControl.setValidators([NomenclatorValidators.nomenclatorValueRequired()]);
									} else {
										this.attributeFormControl.setValidators([Validators.required]);
									}
									this.attributeFormControl.updateValueAndValidity();
								}
							} else {
								this.attributeFormControl.clearValidators();
								this.attributeFormControl.updateValueAndValidity();
							}
						}
					}

					let invisibleResult: string = result["invisible"];
					if (ObjectUtils.isNotNullOrUndefined(invisibleResult)) {						
						if (this.allAttributesReady) {
							this.visible = !("true" === invisibleResult);
							if (this.visible) {
								this.enableAttributeWhenVisible();					
							} else {
								this.disableAttributeWhenInvisible();
							}
						} else {
							this.visibleByExpression = !("true" === invisibleResult);
							if (this.visibleByExpression) {
								this.enableAttributeWhenVisible();					
							} else {
								this.disableAttributeWhenInvisible();
							}
						}
					}		
				}
			}
		});
	}

	private enableAttributeWhenVisible(): void {
		this.attributeFormControl.enable();	
		if (this.attribute.type === AttributeTypeEnum.NOMENCLATOR) {
			let currentValue: ValueOfNomenclatorValueField = <ValueOfNomenclatorValueField> this.attributeFormControl.value;
			if (ObjectUtils.isNullOrUndefined(currentValue)) {
				this.attributeFormControl.setValue(new ValueOfNomenclatorValueField(this.attribute.typeNomenclatorId));
			}			
		}
	}

	private disableAttributeWhenInvisible(): void {
		this.attributeFormControl.reset();
		this.attributeFormControl.disable();
	}

	private changeNomenclatorFieldPerspective(): void {
		
		if (this.attribute.type !== AttributeTypeEnum.NOMENCLATOR) {
			return;
		}

		if (this.isNomenclatorSelectionFilterApplicable() || this.isNomenclatorCustomClassSelectionFilterApplicable()) {
			
			this.nomenclatorFieldDisabled = false;
			
			let filterValuesByOtherAttributesCompleted: boolean = true;
			if (this.isNomenclatorSelectionFilterApplicable()) {
				this.attribute.typeNomenclatorSelectionFilters.forEach((selectionFilter: NomenclatorAttributeSelectionFilterModel) => {
					if (StringUtils.isNotBlank(selectionFilter.attributeKeyForAutocompleteFilterValue)) {
						if (!this.isOtherNomenclatorAttributeCompleted(selectionFilter.attributeKeyForAutocompleteFilterValue)) {
							filterValuesByOtherAttributesCompleted = false;
						}
					}
				});
			}

			if (this.isNomenclatorCustomClassSelectionFilterApplicable()) {
				this.nomenclatorFieldDisabled = ObjectUtils.isNotNullOrUndefined(this.customNomenclatorSelectionFiltersResponseModel) && !this.customNomenclatorSelectionFiltersResponseModel.selectable;
			}

			if (!filterValuesByOtherAttributesCompleted) {
				this.nomenclatorFieldDisabled = true;
			}
		}
	}

	private subscribeForAttributeValueChangeEvent(): void {

		let attributeKeysForSubscribe: string[] = [];
		if (StringUtils.isNotBlank(this.attribute.attributeKeyForAutocomplete)) {
			attributeKeysForSubscribe.push(this.attribute.attributeKeyForAutocomplete);
		}

		if (ArrayUtils.isNotEmpty(this.attribute.typeNomenclatorSelectionFilters)) {
			this.attribute.typeNomenclatorSelectionFilters.forEach((selectionFilter: NomenclatorAttributeSelectionFilterModel) => {
				if (StringUtils.isNotBlank(selectionFilter.attributeKeyForAutocompleteFilterValue)) {
					attributeKeysForSubscribe.push(selectionFilter.attributeKeyForAutocompleteFilterValue);
				}
			});
		}
		if (StringUtils.isNotBlank(this.attribute.typeNomenclatorSelectionFiltersCustomClassAttributeKeys)) {
			let attributeKeys: string[] = this.attribute.typeNomenclatorSelectionFiltersCustomClassAttributeKeys.split(";");
			attributeKeysForSubscribe = attributeKeysForSubscribe.concat(attributeKeys);
		}
		
		let uniqueAttributeKeysForSubscribe: string[] = attributeKeysForSubscribe.filter((value, index, self) => {
			return self.indexOf(value) === index;
		});

		uniqueAttributeKeysForSubscribe.forEach((attributeKey: string) => {
			this.eventMediator.subscribe({
				eventName: AttributeEventName.ATTRIBUTE_VALUE_CHANGE,
				attributeKey: attributeKey,
				handle: (event: AttributeEvent): void => {
					this.handleAttributeValueChangeEvent(event);
				}
			});
		});
	}

	private handleAttributeValueChangeEvent(event: AttributeEvent): void {
		this.attributeFormControl.reset();	
		if (this.isNomenclatorSelectionFilterApplicable() || this.isNomenclatorCustomClassSelectionFilterApplicable()) {		
			this.attributeFormControl.setValue(new ValueOfNomenclatorValueField(this.attribute.typeNomenclatorId));
			this.updateNomenclatorFilterValuesFromAttributes(event.attributeDefinition.key, event.attributeValue);
			this.prepareNomenclatorCustomFilters(() => {
				this.changeNomenclatorFieldPerspective();
			});
		}
		if (ObjectUtils.isNullOrUndefined(event.attributeValue)) {			
			return;
		}
		if (event.attributeDefinition.type === AttributeTypeEnum.NOMENCLATOR) {
			this.autoCompleteFromNomenclatorAttribute(event.attributeValue);
		} else if (event.attributeDefinition.type === AttributeTypeEnum.TEXT || event.attributeDefinition.type === AttributeTypeEnum.NUMERIC) {
			this.autoCompleteFromSimpleMetadata(event.attributeValue);
		}
	}

	private autoCompleteFromNomenclatorAttribute(attributeValue: ValueOfNomenclatorValueField): void {
		if (NomenclatorUtils.fieldValueHasNotValue(attributeValue)) {
			return;
		}
		this.nomenclatorService.getNomenclatorValue(attributeValue.value, {
			onSuccess: (nomenclatorValueModel: NomenclatorValueModel): void => {
				if (this.attribute.autocompleteType === NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_NOMENCLATOR_ATTRIBUTE) {
					let value: string = nomenclatorValueModel[this.attribute.nomenclatorAttributeKeyForAutocomplete];
					if (ObjectUtils.isNotNullOrUndefined(value)) {
						this.attributeFormControl.setValue(value);
					}
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private autoCompleteFromSimpleMetadata(metadataValue: string): void {
		if (this.attribute.autocompleteType === NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_COPY_VALUE) {
			this.attributeFormControl.setValue(metadataValue);
		} else if (this.attribute.autocompleteType === NomenclatorAttributeModel.AUTOCOMPLETE_TYPE_DELETE_VALUE) {
			this.attributeFormControl.setValue(null);
		}
	}

	private updateNomenclatorFilterValuesFromAttributes(attributeKey: string, attributeValue: any): void {
		this.nomenclatorFilterValuesFromAttributes[attributeKey] = null;
		if (ObjectUtils.isNotNullOrUndefined(attributeValue)) {
			if (attributeValue instanceof ValueOfNomenclatorValueField) {
				if (NomenclatorUtils.fieldValueHasValue(attributeValue)) {
					this.nomenclatorFilterValuesFromAttributes[attributeKey] = (<ValueOfNomenclatorValueField> attributeValue).value;
				}
			} else if (attributeValue instanceof Date) {
				this.nomenclatorFilterValuesFromAttributes[attributeKey] = DateUtils.formatForStorage(attributeValue);
			} else if (ObjectUtils.isString(attributeValue)) {
				if (StringUtils.isNotBlank(attributeValue)) {
					this.nomenclatorFilterValuesFromAttributes[attributeKey] = attributeValue;
				}
			} else if (ObjectUtils.isBoolean(attributeValue)) {
				if (attributeValue) {
					this.nomenclatorFilterValuesFromAttributes[attributeKey] = true;
				} else {
					// TODO - De vazut false....se pune null acum - oarecum se poate pune filtru doar pe true.
				}				
			} else {
				this.nomenclatorFilterValuesFromAttributes[attributeKey] = attributeValue;
			}
		}
	}

	public onAttributeValueChanged(): void {
		this.fireValueChangeEvent();
	}

	private autoCompleteWithDefaultValue(): void {
		
		if (!this.isAdd()) {
			return;
		}

		if (StringUtils.isBlank(this.attribute.defaultValue)) {
			return;
		}

		let defaultValue: any = this.attribute.defaultValue;
		if (this.attribute.type === AttributeTypeEnum.DATE) {			
			defaultValue = DateUtils.parseFromStorage(this.attribute.defaultValue);
		} else if (this.attribute.type === AttributeTypeEnum.BOOLEAN) {			
			defaultValue = ("true" === this.attribute.defaultValue);
		} else if (this.attribute.type === AttributeTypeEnum.NOMENCLATOR) {	
			defaultValue = new ValueOfNomenclatorValueField(this.attribute.typeNomenclatorId);
			defaultValue.value = StringUtils.toNumber(this.attribute.defaultValue);
		}

		if (ObjectUtils.isNotNullOrUndefined(defaultValue)) {
			this.attributeFormControl.setValue(defaultValue);
			this.fireValueChangeEvent();
		}
	}

	private fireValueChangeEvent(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.eventMediator)) {
			this.eventMediator.fireEvent({
				eventName: AttributeEventName.ATTRIBUTE_VALUE_CHANGE,
				attributeDefinition: this.attribute,
				attributeValue: this.attributeFormControl.value
			});
		}
	}

	private fireAttributeInitializedEvent(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.eventMediator)) {
			this.eventMediator.fireEvent({
				eventName: AttributeEventName.ATTRIBUTE_INITIALIZED,
				attributeDefinition: this.attribute,
				attributeValue: this.attributeFormControl.value
			});
		}
	}

	public get isCompletionSuggestion(): boolean {
		if (this.isView()) {
			return false;
		}
		return ObjectUtils.isNotNullOrUndefined(this.attribute.completionSuggestionNomenclatorId) && StringUtils.isNotBlank(this.attribute.completionSuggestionNomenclatorAttributeKey);
	}

	private prepareCompletionSuggestions(): void {
		if (!this.isCompletionSuggestion) {
			return;
		}
		this.completionSuggestions = [];
		if (this.isView()) {
			return;
		}
		this.nomenclatorService.getNomenclatorValuesByNomenclatorId(this.attribute.completionSuggestionNomenclatorId, {
			onSuccess: (nomenclatorValues: NomenclatorValueModel[]) => {
				if (ArrayUtils.isNotEmpty(nomenclatorValues)) {
					nomenclatorValues.forEach((nv: NomenclatorValueModel) => {
						this.completionSuggestions.push({
							value: nv[this.attribute.completionSuggestionNomenclatorAttributeKey],
							label: nv[this.attribute.completionSuggestionNomenclatorAttributeKey]
						});
					});				
				}
				this.sortCompletionSuggestions();
			}, 
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private sortCompletionSuggestions(): void {
		this.completionSuggestions.sort((cs1: SelectItem, cs2: SelectItem): number => {
			if (cs1.value < cs2.value) {
				return -1;
			}
			if (cs1.value > cs2.value) {
				return 1;
			}
			return 0;
		});
	}

	public get attributeFormControl() { 
		return this.formGroup.get(this.attribute.key);	
	}
}