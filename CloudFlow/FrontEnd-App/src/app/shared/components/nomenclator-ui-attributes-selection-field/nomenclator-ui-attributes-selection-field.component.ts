import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { StringUtils, ObjectUtils, ArrayUtils } from "./../../utils";
import { NomenclatorService } from "./../../service";
import { NomenclatorAttributeModel } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";

@Component({
	selector: "app-nomenclator-ui-attributes-selection-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-autoComplete 
			[(ngModel)]="selectedAttributes" 
			[suggestions]="filteredAttributes"
        	[minLength]="1" 
			placeholder="" 
			field="name"
			dataKey="name"
			[dropdown]="true"
			[multiple]="true"
			(completeMethod)="filterAttributes($event)"
			(onSelect)="onSelect($event)"
			(onUnselect)="onUnselect($event)"
			(onBlur)="onBlur($event)"
			appendTo="body">
    	</p-autoComplete>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: NomenclatorUiAttributesSelectionFieldComponent, multi: true }
	]
})
export class NomenclatorUiAttributesSelectionFieldComponent implements ControlValueAccessor {

	private nomenclatorAttributes: NomenclatorAttributeModel[];

	public selectedAttributes: NomenclatorAttributeModel[];
	public filteredAttributes: NomenclatorAttributeModel[];

	public loading: boolean;

	private fieldValue: ValueOfNomenclatorUiAttributesSelectionField;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor() {
		this.filteredAttributes = [];
	}

	private applyValue(): void {
		this.nomenclatorAttributes = [];
		this.filteredAttributes = [];
		this.selectedAttributes = [];
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.nomenclatorAttributes = this.fieldValue.candidateAttributes;
		if (ArrayUtils.isNotEmpty(this.fieldValue.values)) {
			this.fieldValue.values.forEach((value: string) => {
				this.nomenclatorAttributes.forEach((nomenclatorAttribute: NomenclatorAttributeModel) => {
					if (nomenclatorAttribute.name === value) {
						this.selectedAttributes.push(nomenclatorAttribute);
					}
				});
			});
		}
		
	}

	private prepareAndPropagateValue(): void {

		let newFieldValue = new ValueOfNomenclatorUiAttributesSelectionField();
		newFieldValue.candidateAttributes = this.fieldValue.candidateAttributes;
		if (ArrayUtils.isEmpty(this.selectedAttributes)) {
			newFieldValue.values = null;
		} else {
			newFieldValue.values = [];
			this.selectedAttributes.forEach((model: NomenclatorAttributeModel)=> {
				if (!ArrayUtils.elementExists(newFieldValue.values, model.name)) {
					newFieldValue.values.push(model.name);
				}			
			});
		}
		this.fieldValue = newFieldValue;

		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public filterAttributes(event: any): void {
		let query = event.query;
		this.filteredAttributes = [];
		if (ArrayUtils.isNotEmpty(this.nomenclatorAttributes)) {
			this.nomenclatorAttributes.forEach((model: NomenclatorAttributeModel) => {
				if (StringUtils.containsIgnoreCase(model.name, query)) {
					this.filteredAttributes.push(model);
				}
			});
		}
	}

	public onSelect(event: any): void {
		this.prepareAndPropagateValue();
	}

	public onUnselect(event: any): void {
		this.prepareAndPropagateValue();
	}

	public onBlur(event: any): void {
		this.prepareAndPropagateValue();
	}

	public writeValue(value: ValueOfNomenclatorUiAttributesSelectionField): void {
		this.fieldValue = value;
		this.applyValue();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public getNomeclatorUiAttributeNames(): NomenclatorAttributeModel[] {
		return this.selectedAttributes;
	}
}

export class ValueOfNomenclatorUiAttributesSelectionField {

	public candidateAttributes: NomenclatorAttributeModel[];
	public values: string[];
}