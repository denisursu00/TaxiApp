import { Component, Input, OnInit } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel, ListMetadataItemModel, ArrayUtils, ObjectUtils, StringUtils } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-metadata-list-field",
	template: `
		<p-dropdown *ngIf="isSingleSelection" 
			[options]="selectItems" 
			[(ngModel)]="selectedItemValues"
			[style]="{'width':'100%'}"
			[placeholder]="(allowCustomValue ? 'LABELS.SELECT_OR_WRITE_CUSTOM_VALUE' : 'LABELS.SELECT') | translate" 
			[readonly]="readonly" 
			[editable]="allowCustomValue"
			(onChange)="onSelectionValueChanged($event)" 
			(onBlur)="onSelectionBlured($event)"
			filter="true"
			appendTo="body">
		</p-dropdown>
		<div *ngIf="isMultipleSelection" class="ui-grid-row">
			<div [ngClass]="allowCustomValue ? 'ui-grid-col-6' : 'ui-grid-col-12'">
				<p-multiSelect 
					[options]="selectItems" 
					[(ngModel)]="selectedItemValues"
					[maxSelectedLabels]="1000"
					[defaultLabel]="'LABELS.SELECT' | translate"
					[style]="{'width':'100%'}" 
					[disabled]="readonly"
					(onChange)="onSelectionValueChanged($event)"
					(onBlur)="onSelectionBlured($event)"
					appendTo="body">
				</p-multiSelect>
			</div>
			<div *ngIf="allowCustomValue" class="ui-grid-col-6">
				<input type="text" 					
					style="width: 100%; margin-left: 2px;"
					[placeholder]="'LABELS.CUSTOM_VALUE' | translate" 
					[(ngModel)]="multipleSelectionCustomValue"
					(input)="onMultipleSelectionCustomValueChanged($event)"
					(blur)="onMultipleSelectionCustomValueBlured($event)"
					pInputText>
			</div>
		</div>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataListFieldComponent, multi: true }
	]
})
export class MetadataListFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	private innerValues: string[];

	public selectItems: SelectItem[];
	public selectedItemValues: string | string[];

	public multipleSelectionCustomValue: string = null;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public ngOnInit(): void {
		this.loadSelectItems();
	}

	private loadSelectItems(): void {
		this.selectItems = [];
		if (this.isSingleSelection && !this.isRequired) {
			this.selectItems.push({
				value: null,
				label: null,
			});
		}
		this.metadataDefinition.listItems.forEach((item: ListMetadataItemModel) => {
			let selectItem: SelectItem = {
				value: item.value,
				label: item.label
			};
			this.selectItems.push(selectItem);
		});
	}

	public onSelectionValueChanged(event: any): void {
		this.prepareAndPropagateInnerValues();
	}

	public onMultipleSelectionCustomValueChanged(event: any): void {
		this.prepareAndPropagateInnerValues();
	}

	private prepareAndPropagateInnerValues(): void {
		this.prepareInnerValues();
		this.propagateInnerValues();
	}

	private prepareInnerValues(): void {
		this.innerValues = [];
		if (ObjectUtils.isNotNullOrUndefined(this.selectedItemValues)) {
			if (this.selectedItemValues instanceof Array) {
				this.selectedItemValues.forEach((selectedValue: string) => {
					this.innerValues.push(selectedValue);
				});
			} else {
				if (StringUtils.isNotBlank(this.selectedItemValues)) {
					let finalValue: string = this.selectedItemValues;
					this.selectItems.forEach((selItem: SelectItem) => {
						if (selItem.label !== null) {
							if ((selItem.label === this.selectedItemValues.toString()) || (selItem.label.trim() === this.selectedItemValues.toString().trim())) {
								finalValue = selItem.value;
							}
						}						
					});
					this.innerValues.push(finalValue);
				}
			}
		}
		if (ObjectUtils.isNotNullOrUndefined(this.multipleSelectionCustomValue)) {
			if (StringUtils.isBlank(this.multipleSelectionCustomValue)) {
				return;
			}
			let found: boolean = false;
			this.innerValues.forEach((innerValue: string) => {
				if (innerValue === this.multipleSelectionCustomValue.trim()) {
					found = true;
				}
			});
			if (!found) {
				this.innerValues.push(this.multipleSelectionCustomValue.trim());
			}
		}
	}

	public onMultipleSelectionCustomValueBlured(event: any): void {
		this.onTouched();
	}

	public onSelectionBlured(event: any): void {
		if (this.isRequired) {			
			// Timeout e pus pentru a rezolva o chestie de finete ca sa nu apara mesajul
			// de invaliditate pe formular cand se incearca selectarea unui item.		
			setTimeout(() => {
				this.onTouched();
			}, 200);
		} else {
			this.onTouched();
		}
	}

	private propagateInnerValues(): void {
		this.onChange(this.innerValues);
		this.onTouched();
	}

	private adjustSelectItemsWithInnerValues(): void {
		if (this.isMultipleSelection) {
			let customSelectItemValue : string = null;
			this.innerValues.forEach((innerValue: string) => {
				let innerValueFound: boolean = false;
				if (this.selectItemsContainValue(innerValue)) {
					innerValueFound = true;
				}
				if (!innerValueFound) {
					customSelectItemValue = innerValue;
				}
			});
			if (this.readonly) {
				this.selectItems.push({
					value: customSelectItemValue,
					label: customSelectItemValue
				});
			} else {
				this.multipleSelectionCustomValue = customSelectItemValue;
			}
		}
	}

	private selectItemsContainValue(theValue: string): boolean {
		let contain: boolean = false;
		this.selectItems.forEach((item: SelectItem) => {
			if (item.value === theValue) {
				contain = true;
			}
		});
		return contain;
	}

	private prepareSelectedItemValuesFromInnerValues(): void {
		if (this.isMultipleSelection) {
			this.selectedItemValues = [];
			this.innerValues.forEach((theValue: string) => {
				if (this.selectItemsContainValue(theValue)) {
					(<string[]>this.selectedItemValues).push(theValue);
				}
			});
		} else {
			if (ArrayUtils.isNotEmpty(this.innerValues)) {
				this.selectedItemValues = this.innerValues[0];
			}
		}
	}

	public writeValue(values: string[]): void {

		if (this.isSingleSelection && ArrayUtils.isNotEmpty(values) && values.length > 1) {
			throw new Error("Tipul metadatei nu permite mai multe valori");
		}
		if (ArrayUtils.isNotEmpty(values) && this.isSingleSelection && !this.allowCustomValue) {
			let found: boolean = false;
			this.selectItems.forEach((selectItem: SelectItem) => {
				if (selectItem.value === values[0]) {
					found = true;
				}
			});
			if (!found) {
				throw new Error("Valoarea '" + values[0] + "' nu este compatibila cu lista de selectie");
			}
		}

		this.innerValues = values;
		this.adjustSelectItemsWithInnerValues();
		this.prepareSelectedItemValuesFromInnerValues();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public get isRequired(): boolean {
		return this.metadataDefinition.mandatory;
	}

	public get allowCustomValue(): boolean {
		return this.metadataDefinition.extendable ? true : false;
	}

	public get isMultipleSelection(): boolean {
		return this.metadataDefinition.multipleSelection;
	}

	public get isSingleSelection(): boolean {
		return !this.isMultipleSelection;
	}
}