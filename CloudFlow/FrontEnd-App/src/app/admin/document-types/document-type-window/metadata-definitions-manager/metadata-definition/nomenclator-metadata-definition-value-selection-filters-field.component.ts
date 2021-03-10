import { Component, Input, OnInit, OnChanges, SimpleChanges} from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { AppError, TranslateUtils, DateConstants, ArrayUtils, ObjectUtils, StringUtils, AttributeTypeEnum, NomenclatorService, NomenclatorModel, NomenclatorAttributeModel, FormUtils, NomenclatorUtils, NomenclatorMetadataDefinitionValueSelectionFilterModel } from "@app/shared";
import { ValueOfNomenclatorValueField, DateUtils } from "@app/shared";
import { MessageDisplayer } from "@app/shared";
import { SelectItem } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-nomenclator-metadata-definition-value-selection-filters-field",
	template: `
		<app-loading *ngIf="loadingVisible"></app-loading>
		<p-table [columns]="columns" 
				[value]="filterViews" 
				[(selection)]="selectedFilterView"  
				[responsive]="true"
				[resizableColumns]="true"
				columnResizeMode="fit"
				[scrollable]="true"
				selectionMode="single" 
				scrollHeight="250px"
				(onRowSelect)="onFilterRowSelect($event)"
				(onRowUnselect)="onFilterRowUnselect($event)">
			<ng-template pTemplate="colgroup" let-columns>
				<colgroup>
					<col style="width: 50px">
					<col *ngFor="let column of columns" style="width: auto">
				</colgroup>
			</ng-template>
			<ng-template pTemplate="caption">
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12" style="text-align: left;">
							<p-button [disabled]="!addActionEnabled" (onClick)="onAddAction()" [label]="'LABELS.ADD' | translate"></p-button>
							<p-button [disabled]="!removeActionEnabled" (onClick)="onRemoveAction()" [label]="'LABELS.REMOVE' | translate"></p-button>
						</div>
					</div>
				</div>
			</ng-template>
			<ng-template pTemplate="header" let-columns>
				<tr>
					<th pResizableColumn>
						{{"LABELS.NO_CRT" | translate}}
					</th>
					<th pResizableColumn *ngFor="let column of columns">
						{{column.header}}
					</th>
				</tr>
			</ng-template>
			<ng-template pTemplate="body" let-rowData let-columns="columns" let-index="rowIndex">
				<tr [pSelectableRow]="rowData">
					<td>{{index + 1}}</td>
					<td *ngFor="let column of columns">
						{{rowData[column.field]}}
					</td>
				</tr>
			</ng-template>
		</p-table>

		<p-dialog [(visible)]="dialogVisible" 
				[modal]="true"
				[maximizable]="true" 
				[closable]="true" 
				[showHeader]="true"
				[contentStyle]="{'height':'60vh'}" [style]="{'width':'80vw','height':'auto'}"
				appendTo="body"
				(onHide)="onDialogHide($event)">				
			<p-header>Add filter</p-header>
			<form [formGroup]="form">
				
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							Attribute *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<p-dropdown 
								[options]="filterAttributeSelectItems"
								formControlName="filterAttribute"
								[placeholder]="'LABELS.SELECT' | translate" 
								filter="true"
								(onChange)="onFilterAttributeSelectionChanged($event)"
								appendTo="body">
							</p-dropdown>
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div class="ui-message ui-messages-error ui-corner-all" *ngIf="filterAttributeFormControl.invalid && (filterAttributeFormControl.dirty || filterAttributeFormControl.touched)">
								<span class="fa fa-close"></span>					
								<span *ngIf="filterAttributeFormControl.errors['required']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
				
				<div *ngIf="defaultFilterValueFormControl.enabled" class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							Default filter value:
						</div>
					</div>
					<div class="ui-grid-row">
						<div [ngSwitch]="filterAttributeFormControl.value.type" class="ui-grid-col-12">

							<!-- Attribute TEXT -->
							<input *ngSwitchCase="'TEXT'"
									type="text" 
									formControlName="defaultFilterValue"
									pInputText>

							<!-- Attribute NUMERIC -->
							<input *ngSwitchCase="'NUMERIC'"
									type="text" 
									formControlName="defaultFilterValue"
									pInputText
									pKeyFilter="num">

							<!-- Attribute DATE -->
							<p-calendar *ngSwitchCase="'DATE'" 
									formControlName="defaultFilterValue"
									showIcon="true" 
									[readonlyInput]="false" 
									[dateFormat]="dateFormat"
									monthNavigator="true" 
									yearNavigator="true"
									[yearRange]="yearRange"
									showButtonBar="true"
									appendTo="body" 
									[dataType]="'date'">
							</p-calendar>

							<!-- Attribute BOOLEAN -->
							<p-dropdown *ngSwitchCase="'BOOLEAN'" 
								[options]="booleanValueSelectItems"
								[placeholder]="'LABELS.SELECT' | translate"
								formControlName="defaultFilterValue"
								filter="true"
								appendTo="body">
							</p-dropdown>

							<!-- Attribute NOMENCLATOR -->
							<app-nomenclator-value-field *ngSwitchCase="'NOMENCLATOR'" 
								formControlName="defaultFilterValue">
							</app-nomenclator-value-field>

						</div>
					</div>
				</div>
				<div class="ui-grid-row">
					<div class="ui-grid-col-12">
						<div class="ui-message ui-messages-error ui-corner-all" *ngIf="defaultFilterValueFormControl.invalid && (defaultFilterValueFormControl.dirty || defaultFilterValueFormControl.touched)">
							<span class="fa fa-close"></span>					
							<span *ngIf="defaultFilterValueFormControl.errors['required']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
						</div>
					</div>
				</div>

				<div *ngIf="metadataNameForAutocompleteFilterValueFormControl.enabled" class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							Metadata name for autocomplete filter value:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="metadataNameForAutocompleteFilterValue" pInputText>
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div class="ui-message ui-messages-error ui-corner-all" *ngIf="metadataNameForAutocompleteFilterValueFormControl.invalid && (metadataNameForAutocompleteFilterValueFormControl.dirty || metadataNameForAutocompleteFilterValueFormControl.touched)">
								<span class="fa fa-close"></span>					
								<span *ngIf="metadataNameForAutocompleteFilterValueFormControl.errors['required']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>

			</form>
			<p-footer>
				<p-button (onClick)="onDialogOKAction($event)" label="OK"></p-button>
				<p-button (onClick)="onDialogCancelAction($event)" [label]="'LABELS.CANCEL' | translate"></p-button>
			</p-footer>
		</p-dialog>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: NomenclatorMetadataDefinitionValueSelectionFiltersFieldComponent, multi: true }
	]
})
export class NomenclatorMetadataDefinitionValueSelectionFiltersFieldComponent implements ControlValueAccessor, OnChanges, OnInit {

	private fieldValue: ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField;
	
	public addActionEnabled: boolean;
	public removeActionEnabled: boolean;
	
	public columns: any[];

	public filterViews: SelectionFilterView[];
	public selectedFilterView: SelectionFilterView;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private nomenclatorService: NomenclatorService;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	private nomenclatorModel: NomenclatorModel;

	public filterAttributeSelectItems: SelectItem[];
	public booleanValueSelectItems: SelectItem[];

	public dialogVisible: boolean = false;
	public loadingVisible: boolean = false;

	public dateFormat: string;
	public yearRange: string;
	
	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer, 
			nomenclatorService: NomenclatorService, formBuilder: FormBuilder) {
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.nomenclatorService = nomenclatorService;
		this.formBuilder = formBuilder;

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
	}

	public ngOnInit(): void {
		
		this.columns = []; 

		this.booleanValueSelectItems = [];
		this.booleanValueSelectItems.push({ value: "true", label: "True" });
		this.booleanValueSelectItems.push({ value: "false", label: "False" });

		// TODO De tradus
		this.columns.push({header: "Filter attribute", field: "filterAttributeName"});			
		this.columns.push({header: "Default filter value", field: "defaultFilterValue"});	
		this.columns.push({header: "Metadata name for autocomplete filter value", field: "metadataNameForAutocompleteFilterValue"});	
		
		this.form = this.formBuilder.group([]);
		this.form.addControl("filterAttribute", new FormControl(null, [Validators.required]));
		this.form.addControl("defaultFilterValue", new FormControl({value:null, disabled: true}));
		this.form.addControl("metadataNameForAutocompleteFilterValue", new FormControl({value:null, disabled: true}));
		
		this.changePerspective();
	}

	public ngOnChanges(simpleChanges: SimpleChanges): void {
		// Nothing now.
	}

	private prepareFilterViews(): void {
		this.filterViews = [];
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.loadNomenclatorModel(() => {
			if (ObjectUtils.isNullOrUndefined(this.nomenclatorModel)) {
				return;
			}
			if (ArrayUtils.isEmpty(this.fieldValue.selectionFilters)) {
				return;
			}
			this.fieldValue.selectionFilters.forEach((filter: NomenclatorMetadataDefinitionValueSelectionFilterModel, index: number) => {				
				let filterAttribute: NomenclatorAttributeModel = this.getAttributeById(filter.filterAttributeId);
				let filterView: SelectionFilterView = new SelectionFilterView();
				filterView.indexOfModel = index;
				filterView.filterAttributeName = filterAttribute.name;
				if (ObjectUtils.isNotNullOrUndefined(filter.defaultFilterValue)) {
					if (filterAttribute.type === AttributeTypeEnum.NOMENCLATOR) {
						this.nomenclatorService.getUiAttributeValues([Number(filter.defaultFilterValue)], {
							onSuccess: (uiAttributes: any): void => {
								filterView.defaultFilterValue = uiAttributes[filter.defaultFilterValue];
							},
							onFailure: (error: AppError): void => {
								this.messageDisplayer.displayAppError(error);
							}
						});
					} else {
						filterView.defaultFilterValue = filter.defaultFilterValue;
					}
				}				
				filterView.metadataNameForAutocompleteFilterValue = filter.metadataNameForAutocompleteFilterValue;
				this.filterViews.push(filterView);
			});
		});
	}

	private getAttributeNameById(attributeId: number): string {
		let attributeName: string = null;
		this.nomenclatorModel.attributes.forEach((attribute: NomenclatorAttributeModel) => {
			if (attribute.id === attributeId) {
				attributeName = attribute.name;
			}
		});
		return attributeName;
	}

	private getAttributeById(attributeId: number): NomenclatorAttributeModel {
		let attribute: NomenclatorAttributeModel = null;
		this.nomenclatorModel.attributes.forEach((attributeModel: NomenclatorAttributeModel) => {
			if (attributeModel.id === attributeId) {
				attribute = attributeModel;
			}
		});
		return attribute;
	}

	private loadNomenclatorModel(callback: () => any): void {
		this.nomenclatorModel = null;
		if (ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
			this.loadingVisible = true;
			this.nomenclatorService.getNomenclator(this.fieldValue.nomenclatorId, {
				onSuccess: (nomenclator: NomenclatorModel): void => {
					this.nomenclatorModel = nomenclator;
					callback();
					this.loadingVisible = false;
				},
				onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					callback();
					this.loadingVisible = false;
				}
			});
		} else {
			callback();
		}
	}

	private propagateFieldValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	private changePerspective(): void {
		this.addActionEnabled = true;
		this.removeActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedFilterView);
	}

	public onAddAction(): void {
		this.filterAttributeSelectItems = [];
		this.form.reset();
		this.nomenclatorModel.attributes.forEach((attribute: NomenclatorAttributeModel) => {
			this.filterAttributeSelectItems.push({
				value: attribute,
				label: attribute.name
			});
		});
		
		ListItemUtils.sortByLabel(this.filterAttributeSelectItems);
		
		this.dialogVisible = true;
	}
	
	public onRemoveAction(): void {
		ArrayUtils.removeElementByIndex(this.fieldValue.selectionFilters, this.selectedFilterView.indexOfModel);
		this.selectedFilterView = null;
		this.changePerspective();
		this.prepareFilterViews();
	}

	public onFilterRowSelect(event: any): void {
		this.changePerspective();
	}

	public onFilterRowUnselect(event: any): void {
		this.changePerspective();
	}

	public writeValue(value: ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField): void {
		this.fieldValue = value;
		this.prepareFilterViews();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public onDialogHide(event: any): void {
		this.doWhenCloseDialog();
	}

	public onDialogOKAction(event: any): void {

		FormUtils.validateAllFormFields(this.form);
		if (this.form.invalid) {
			return;
		}

		if (ArrayUtils.isEmpty(this.fieldValue.selectionFilters)) {
			this.fieldValue.selectionFilters = [];
		}		
		let newFilter: NomenclatorMetadataDefinitionValueSelectionFilterModel = new NomenclatorMetadataDefinitionValueSelectionFilterModel();
		newFilter.filterAttributeId = this.filterAttributeFormControl.value.id;
		newFilter.defaultFilterValue = this.getDefaultFilterValueAsString();
		newFilter.metadataNameForAutocompleteFilterValue = ObjectUtils.isNotNullOrUndefined(this.metadataNameForAutocompleteFilterValueFormControl.value) ? this.metadataNameForAutocompleteFilterValueFormControl.value.trim() : null;
		this.fieldValue.selectionFilters.push(newFilter);
		
		this.doWhenCloseDialog();
		this.prepareFilterViews();
	}

	private getDefaultFilterValueAsString(): string {
		let defaultValueAsString: string = null;
		let rawDefaultValue: any = this.defaultFilterValueFormControl.value;
		if (ObjectUtils.isNullOrUndefined(rawDefaultValue)) {
			return null;
		}
		if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.TEXT) {
			defaultValueAsString = rawDefaultValue;
		} else if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.NUMERIC) {
			defaultValueAsString = String(rawDefaultValue);
		} else if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.BOOLEAN) {
			defaultValueAsString = rawDefaultValue;
		} else if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.DATE) {
			defaultValueAsString = DateUtils.formatForStorage(rawDefaultValue);
		} else if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.NOMENCLATOR) {
			if (NomenclatorUtils.fieldValueHasValue(rawDefaultValue)) {
				defaultValueAsString = (<ValueOfNomenclatorValueField> rawDefaultValue).value.toString();
			}
		} else {
			throw new Error("type necunoscut");
		}
		return defaultValueAsString;
	}

	public onDialogCancelAction(event: any): void {		
		this.doWhenCloseDialog();
	}

	private doWhenCloseDialog(): void {
		this.dialogVisible = false;
		this.defaultFilterValueFormControl.disable();
		this.metadataNameForAutocompleteFilterValueFormControl.disable();
	}

	public onFilterAttributeSelectionChanged(event: any): void {
		let enableFilterValueFields: boolean = ObjectUtils.isNotNullOrUndefined(this.filterAttributeFormControl.value);
		FormUtils.enableOrDisableFormControl(this.defaultFilterValueFormControl, enableFilterValueFields);
		FormUtils.enableOrDisableFormControl(this.metadataNameForAutocompleteFilterValueFormControl, enableFilterValueFields);

		if (enableFilterValueFields) {
			if (this.filterAttributeFormControl.value.type === AttributeTypeEnum.NOMENCLATOR) {
				let value: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.filterAttributeFormControl.value.typeNomenclatorId);
				this.defaultFilterValueFormControl.setValue(value);
			}
		}
	}

	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.form.get(controlName);
	}

	public get filterAttributeFormControl(): FormControl { 
		return this.getFormControlByName("filterAttribute"); 
	}

	public get defaultFilterValueFormControl(): FormControl { 
		return this.getFormControlByName("defaultFilterValue"); 
	}	

	public get metadataNameForAutocompleteFilterValueFormControl(): FormControl { 
		return this.getFormControlByName("metadataNameForAutocompleteFilterValue"); 
	}
}

export class ValueOfNomenclatorMetadataDefinitionValueSelectionFiltersField {

	public selectionFilters: NomenclatorMetadataDefinitionValueSelectionFilterModel[];
	public _nomenclatorId: number; // NOMENCLATOR

	public constructor(nomenclatorId: number) {
		if (ObjectUtils.isNullOrUndefined(nomenclatorId)) {
			throw new Error("nomenclatorId cannot be null/undefined");
		}
		this._nomenclatorId = nomenclatorId;
	}

	public get nomenclatorId() {
		return this._nomenclatorId;
	}
}

class SelectionFilterView {
	
	public indexOfModel: number;

	public filterAttributeName: string;
	public defaultFilterValue: string;
	public metadataNameForAutocompleteFilterValue: string;
}