import { Component, Input, Output, OnInit, OnChanges, SimpleChanges, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { TranslateUtils, BooleanUtils, ObjectUtils, ArrayUtils, NomenclatorUtils } from "./../../utils";
import { NomenclatorService } from "./../../service";
import { AppError, NomenclatorFilter, NomenclatorSortedAttribute } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";

@Component({
	selector: "app-nomenclator-value-field",
	template: `
		<p-table *ngIf="selectionMode == 'multiple'"
				[value]="nomenclatorUiValues" 
				[(selection)]="selectedRows" 
				[responsive]="true"
				[resizableColumns]="true"
				[scrollable]="true"
				[loading]="loading"
				columnResizeMode="fit"
				selectionMode="multiple" 
				scrollHeight="250px"
				(onRowSelect)="onRowSelect($event)"
				(onRowUnselect)="onRowUnSelect($event)">
			<ng-template pTemplate="caption">
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12" style="text-align: left;">
							<p-button [disabled]="!selectNomenclatorValueEnabled" (onClick)="onSelectNomenclatorValue()" label="..."></p-button>
							<p-button [disabled]="!removeActionEnabled" (onClick)="onRemoveAction($event)" [label]="'LABELS.DELETE' | translate"></p-button>
						</div>
					</div>
				</div>
			</ng-template>
			<ng-template pTemplate="body" let-rowData>
				<tr [pSelectableRow]="rowData">
					<td>
						{{rowData["uiValue"]}}
					</td>
				</tr>
			</ng-template>
			<ng-template pTemplate="emptymessage">
				<tr>
					<td>
						{{ 'LABELS.NO_RECORDS_FOUND' | translate }}
					</td>
				</tr>
			</ng-template>
		</p-table>
		<div class="ui-inputgroup" *ngIf="selectionMode == 'single'">
			<input type="text" pInputText [(ngModel)]="nomenclatorUiValue" [placeholder]="placeholder" [readonly]="true">
			<button pButton type="button" *ngIf="clearNomenclatorValueEnabled" icon="fa fa-close" class="ui-button-secondary" (click)="onClearNomenclatorValue()"></button>			
			<button pButton type="button" label="..." [disabled]="!selectNomenclatorValueEnabled" (click)="onSelectNomenclatorValue()"></button> 
		</div>
		<app-nomenclator-values-selection-window *ngIf="nomenclatorValuesSelectionWindowVisible"
			[selectionMode]="nomenclatorValuesSelectionWindowSelectionMode"
			[nomenclatorId]="nomenclatorValuesSelectionWindowNomenclatorId"
			[customFilters]="nomenclatorValuesSelectionWindowCustomFilters"
			[customSortedAttributes]="nomenclatorValuesSelectionWindowCustomSortedAttributes"
			[customFilterByValueIds]="nomenclatorValuesSelectionWindowCustomFilterByValueIds"
			(valuesSelected)="onNomenclatorValuesSelectionWindowValuesSelected($event)"
			(windowClosed)="onNomenclatorValuesSelectionWindowWindowClosed($event)">
		</app-nomenclator-values-selection-window>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: NomenclatorValueFieldComponent, multi: true }
	]
})
export class NomenclatorValueFieldComponent implements ControlValueAccessor, OnInit, OnChanges {

	// Mecanismul de aici ar putea fi implementat ca numai daca nomenclatorUiValue s-a afisat 
	// atunci sa i se seteze valoarea in fieldValue (si sa se propage). Momentan se seteaza 
	// fieldValue si apoi se incearca incarcarea valorii ui. Aceasta permite ca daca valoarea 
	// de ui nu se poate incarca (sau dureaza) totusi formularul sa fie valid si sa se permita 
	// alte actiuni pe formular - intrucat in pasul de selectare valoare nomenclator utilizatorul 
	// a vazut ce a selectat si nu au fost probleme.

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public selectionMode: string = "single" || "multiple";

	@Input()
	public readonly: boolean;

	@Input()
	public customFilters: NomenclatorFilter[];

	@Input()
	public customFilterByValueIds: number[];

	@Input()
	public customSortedAttributes: NomenclatorSortedAttribute[];

	@Output()
	public valueChanged: EventEmitter<ValueOfNomenclatorValueField>;

	private fieldValue: ValueOfNomenclatorValueField;

	public placeholder: string;
	public nomenclatorUiValue: string;
	public nomenclatorUiValues: MultipleValueView[];

	public selectNomenclatorValueEnabled: boolean;
	public clearNomenclatorValueEnabled: boolean;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public nomenclatorValuesSelectionWindowVisible: boolean;
	public nomenclatorValuesSelectionWindowCustomFilters: NomenclatorFilter[];
	public nomenclatorValuesSelectionWindowCustomSortedAttributes: NomenclatorSortedAttribute[];
	public nomenclatorValuesSelectionWindowCustomFilterByValueIds: number[];
	public nomenclatorValuesSelectionWindowNomenclatorId: number;
	public nomenclatorValuesSelectionWindowSelectionMode: string;

	public selectedRows: MultipleValueView[];
	public removeActionEnabled: boolean;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.placeholder = this.EMPTY_TEXT;
		this.nomenclatorUiValue = this.EMPTY_TEXT;
		this.nomenclatorUiValues = [];
		this.selectedRows = [];
		this.selectNomenclatorValueEnabled = false;
		this.clearNomenclatorValueEnabled = false;
		this.nomenclatorValuesSelectionWindowVisible = false;
		this.selectionMode = "single";
		this.valueChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		this.updatePerspective();
	}

	public ngOnChanges(changes: SimpleChanges): void {
		this.updatePerspective();	
	}

	public onSelectNomenclatorValue(): void {
		this.nomenclatorValuesSelectionWindowNomenclatorId = this.fieldValue.nomenclatorId;
		this.nomenclatorValuesSelectionWindowCustomFilters = this.customFilters;
		this.nomenclatorValuesSelectionWindowCustomSortedAttributes = this.customSortedAttributes;
		this.nomenclatorValuesSelectionWindowCustomFilterByValueIds = this.customFilterByValueIds;
		this.nomenclatorValuesSelectionWindowSelectionMode = this.selectionMode;
		this.nomenclatorValuesSelectionWindowVisible = true;
	}

	public onClearNomenclatorValue(): void {
		this.clearValue();
		this.valueChanged.emit(this.fieldValue);
	}

	private clearValue(): void {		
		if (ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
			this.fieldValue = new ValueOfNomenclatorValueField(this.fieldValue.nomenclatorId);
		}
		this.nomenclatorUiValue = this.EMPTY_TEXT;
		this.nomenclatorUiValues = [];
		this.selectedRows = [];
		this.placeholder = this.EMPTY_TEXT;
		this.clearNomenclatorValueEnabled = false;
		this.propagateValue();		
	}

	public onNomenclatorValuesSelectionWindowValuesSelected(nomenclatorValueIds: number[]): void {
		if (ArrayUtils.isNotEmpty(nomenclatorValueIds)) {
			if (this.selectionMode === "multiple") {
				this.fieldValue = new ValueOfNomenclatorValueField(this.fieldValue.nomenclatorId);
				this.fieldValue.values = nomenclatorValueIds;
				this.propagateValue();
				this.loadNomenclatorUiValue();
				this.valueChanged.emit(this.fieldValue);
			} else if (this.selectionMode === "single") {
				let currentNomValueId: number = this.fieldValue.value;
				if (currentNomValueId !== nomenclatorValueIds[0]) {
					this.fieldValue = new ValueOfNomenclatorValueField(this.fieldValue.nomenclatorId);
					this.fieldValue.value = nomenclatorValueIds[0];
					this.propagateValue();
					this.loadNomenclatorUiValue();
					this.valueChanged.emit(this.fieldValue);
				}
			} else {
				throw new Error("Nomenclator value field selection mode has to be 'single' or 'multiple'. Value of selection mode is: [" + this.selectionMode + "]");
			}
		} else {
			this.onTouched();
		}
	}

	public onNomenclatorValuesSelectionWindowWindowClosed(event: any): void {
		this.nomenclatorValuesSelectionWindowVisible = false;
		this.onTouched();
	}

	private loadNomenclatorUiValue(): void {
		if (this.selectionMode === "single") {
			if (!NomenclatorUtils.fieldValueHasValue(this.fieldValue)) {
				return;
			}
			this.nomenclatorUiValue = this.EMPTY_TEXT;
		} else if (this.selectionMode === "multiple") {
			if (!NomenclatorUtils.fieldValueHasValues(this.fieldValue)) {
				return;
			}
			this.nomenclatorUiValues = [];
			this.selectedRows = [];
		} else {
			throw new Error("Nomenclator value field selection mode has to be 'single' or 'multiple'. Value of selection mode is: [" + this.selectionMode + "]");
		}

		this.placeholder = this.translateUtils.translateMessage("LOADING");
		this.selectNomenclatorValueEnabled = false;
		this.clearNomenclatorValueEnabled = false;
		
		this.nomenclatorService.getUiAttributeValues(this.fieldValue.values, {
			onSuccess: (uiValueByNomenclatorValueId: object) => {
				if (this.selectionMode === "multiple") {
					this.fieldValue.values.forEach(nomenclatorValueId => {
						if (ObjectUtils.isNotNullOrUndefined(uiValueByNomenclatorValueId) 
								&& ObjectUtils.isNotNullOrUndefined(uiValueByNomenclatorValueId[nomenclatorValueId])) {
							
							let multipleValueView = new MultipleValueView();
							multipleValueView.id = nomenclatorValueId;
							multipleValueView.uiValue = uiValueByNomenclatorValueId[nomenclatorValueId];
							this.nomenclatorUiValues.push(multipleValueView);
						}
					});
				} else {
					this.nomenclatorUiValue = uiValueByNomenclatorValueId[this.fieldValue.value];
				}
				this.placeholder = this.EMPTY_TEXT;
				this.updatePerspective();
			}, 
			onFailure: (error: AppError) => {
				this.placeholder = this.EMPTY_TEXT;
				this.updatePerspective();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private propagateValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	private updatePerspective(): void {
		this.selectNomenclatorValueEnabled = false;
		this.clearNomenclatorValueEnabled = false;
		if (BooleanUtils.isFalse(this.readonly) && ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
			this.selectNomenclatorValueEnabled = ObjectUtils.isNotNullOrUndefined(this.fieldValue.nomenclatorId);
			if (this.selectionMode === "single") {
				this.clearNomenclatorValueEnabled = ObjectUtils.isNotNullOrUndefined(this.fieldValue.value);
			} else if (this.selectionMode === "multiple") {
				this.removeActionEnabled = ArrayUtils.isNotEmpty(this.selectedRows);
			} else {
				throw new Error("Nomenclator value field selection mode has to be 'single' or 'multiple'. Value of selection mode is: [" + this.selectionMode + "]");
			}
		}
	}

	public writeValue(value: ValueOfNomenclatorValueField): void {
		this.fieldValue = value;
		this.updatePerspective();
		if (this.selectionMode === "single") {
			if (!NomenclatorUtils.fieldValueHasValue(this.fieldValue)) {
				this.clearValue();
			} else {
				this.loadNomenclatorUiValue();
			}
		} else if (this.selectionMode === "multiple") {
			if (!NomenclatorUtils.fieldValueHasValues(this.fieldValue)) {
				this.clearValue();
			} else {
				this.loadNomenclatorUiValue();
			}
		} else {
			throw new Error("Nomenclator value field selection mode has to be 'single' or 'multiple'. Value of selection mode is: [" + this.selectionMode + "]");
		}
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public onRowSelect(event: any): void {
		this.updatePerspective();
	}

	public onRowUnSelect(event: any): void {
		this.updatePerspective();
	}

	public onRemoveAction(event: any): void {
		let currentValues: number[] = this.fieldValue.values;
		this.fieldValue = new ValueOfNomenclatorValueField(this.fieldValue.nomenclatorId);
		for (let nomenclatorUiValueToRemove of this.selectedRows) {
			this.nomenclatorUiValues = [...this.nomenclatorUiValues.filter(nomenclatorUiValueAndId => nomenclatorUiValueAndId.id !== nomenclatorUiValueToRemove.id)];
			currentValues = [...currentValues.filter(currentValueOfFieldValue => currentValueOfFieldValue !== nomenclatorUiValueToRemove.id)];
		}
		this.fieldValue.values = currentValues;
		if (ArrayUtils.isEmpty(this.nomenclatorUiValues)) {
			this.clearValue();
		}
		this.propagateValue();
		this.selectedRows = [];
		this.updatePerspective();
	}
}

export class ValueOfNomenclatorValueField {

	private _nomenclatorId: number;
	private _values: number[];

	public constructor(nomenclatorId: number) {
		if (ObjectUtils.isNullOrUndefined(nomenclatorId)) {
			throw new Error("nomenclatorId cannot be null/undefined");
		}
		this._nomenclatorId = nomenclatorId;
	}

	public get nomenclatorId(): number {
		return this._nomenclatorId;
	}

	public get value(): number {
		if (ArrayUtils.isEmpty(this._values)) {
			return undefined;
		}
		if (this._values.length > 1) {
			throw new Error("A single value was asked for, but multiple values found: " + this._values);
		}
		return this._values[0];
	}

	public set value(value: number) {
		this._values = [value];
	}

	public get values(): number[] {
		return this._values;
	}

	public set values(values: number[]) {
		this._values = values;
	}
}

export class MultipleValueView {

	public id: number;
	public uiValue: string;

}