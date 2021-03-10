import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { AppError, MetadataDefinitionModel, ArrayUtils, ObjectUtils, DateUtils, DateConstants } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-metadata-date-time-field",
	template: `
		<input *ngIf="readonly" 
				type="text"
				[(ngModel)]="fieldValueForView"
				pInputText
				readonly="true"
				(blur)="onBlured()">
		<p-calendar *ngIf="!readonly" 
				[(ngModel)]="fieldValue" 
				showIcon="true" 
				[readonlyInput]="false" 
				[dateFormat]="dateTimeFormat"
				[showTime]="true"
				hourFormat="24"
				utc="false"
				monthNavigator="true" 
				yearNavigator="true"
				[yearRange]="yearRange"
				showButtonBar="true"
				appendTo="body" 
				[dataType]="'date'"
				(onSelect)="onSelected($event)"
				(onClearClick)="onCleared()"
				(onBlur)="onBlured()">
		</p-calendar>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataDateTimeFieldComponent, multi: true }
	]
})
export class MetadataDateTimeFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	public dateTimeFormat: string;
	public yearRange: string;

	public fieldValueForView: string;
	public fieldValue: Date;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.dateTimeFormat = DateConstants.DATE_TIME_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRangeForMetadata();
	}

	public ngOnInit(): void {
		this.prepareForViewIfApplicable();
	}

	private prepareForViewIfApplicable(): void {
		if (this.readonly) {
			if (ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
				this.fieldValueForView = DateUtils.formatDateTimeForDisplay(this.fieldValue);
			}
		}
	}

	public onSelected(event: any): void {
		this.propagateValue();
	}

	public onBlured(event: any): void {
		this.propagateValue();
	}

	public onCleared(): void {
		this.propagateValue();
	}

	private propagateValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public writeValue(value: Date): void {
		this.fieldValue = value;	
		this.prepareForViewIfApplicable();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}