import { Component, Input, OnInit } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel } from "@app/shared";

@Component({
	selector: "app-metadata-numeric-field",
	template: `
		<input type="text" 
			[readonly]="readonly" 
			[(ngModel)]="inputValue"
			pInputText
			pKeyFilter="num"
			(input)="onInput($event)"
			(blur)="onBlured($event)">
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataNumericFieldComponent, multi: true }
	]
})
export class MetadataNumericFieldComponent implements ControlValueAccessor {

	// TODO - Trebuie revenit si cizelat acest field.

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	private innerValue: string;

	public inputValue: string;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public onInput(event: any): void {
		this.innerValue = this.inputValue;
		this.propagateValue();	
	}

	public onBlured(event: any) {
		this.propagateValue();
	}

	private propagateValue(): void {
		this.onChange(this.innerValue);
		this.onTouched();
	}

	public writeValue(value: string): void {
		this.innerValue = value;
		this.inputValue = this.innerValue;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}