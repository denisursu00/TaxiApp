import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel, ArrayUtils, TranslateUtils, ObjectUtils, StringUtils, BooleanUtils } from "@app/shared";

@Component({
	selector: "app-metadata-calendar-field",
	template: `
		<app-calendar-selection-field
			[(ngModel)]="fieldValue"
			[readonly]="readonly"
			(selectionChanged)="onSelectionChanged()">
		</app-calendar-selection-field>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataCalendarFieldComponent, multi: true }
	]
})
export class MetadataCalendarFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	@Output()
	public valueChanged: EventEmitter<number>;

	public fieldValue: number;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.valueChanged = new EventEmitter();
	}

	public ngOnInit(): void {		
	}

	public onSelectionChanged(): void {
		this.propagateValue();
		this.valueChanged.emit(this.fieldValue);
	}

	private propagateValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public writeValue(calendarId: number): void {
		this.fieldValue = calendarId;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}