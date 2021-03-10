import { Component, Input, OnInit, OnChanges, SimpleChanges} from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { AppError, TranslateUtils, DateConstants, ArrayUtils, ObjectUtils, StringUtils, AttributeTypeEnum } from "@app/shared";
import { ValueOfNomenclatorValueField, DateUtils } from "@app/shared";
import { MessageDisplayer } from "@app/shared";

@Component({
	selector: "app-nomenclator-attribute-definition-default-value-field",
	template: `
		<input *ngIf="attributeType === 'TEXT'"
			type="text"
			[(ngModel)]="valueAsString"
			pInputText
			(input)="onValueChanged($event)"
			(blur)="onValueBlured($event)">

		<input *ngIf="attributeType === 'NUMERIC'"
			type="text"
			[(ngModel)]="valueAsString"
			pInputText
			pKeyFilter="num"
			(input)="onValueChanged($event)"
			(blur)="onValueBlured($event)">

		<p-calendar *ngIf="attributeType === 'DATE'"
			[(ngModel)]="valueAsDate"
			[showIcon]="true" 
			[readonlyInput]="false" 
			[dateFormat]="dateFormat" 
			[appendTo]="body" 
			utc="false"
			[monthNavigator]="true" 
			[yearNavigator]="true"
			[yearRange]="yearRange" 
			[showButtonBar]="true" 
			[dataType]="'date'"
			(onSelect)="onValueChanged($event)"
			(onBlur)="onValueBlured($event)"			
			(onClearClick)="onValueChanged($event)"
			(onTodayClick)="onValueChanged($event)">
		</p-calendar>

		<p-checkbox *ngIf="attributeType === 'BOOLEAN'"
			[(ngModel)]="valueAsBoolean" 
			binary="true"
			(onChange)="onValueChanged($event)">
		</p-checkbox>

		<app-nomenclator-value-field *ngIf="attributeType === 'NOMENCLATOR'"
			[(ngModel)]="valueAsNomenclatorValue"
			(valueChanged)="onNomenclatorValueChanged($event)">
		</app-nomenclator-value-field>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: NomenclatorAttributeDefinitionDefaultValueFieldComponent, multi: true }
	]
})
export class NomenclatorAttributeDefinitionDefaultValueFieldComponent implements ControlValueAccessor, OnChanges {

	public attributeType: string;
	public nomenclatorId: number;

	public dateFormat: string;
	public yearRange: string;

	public valueAsString: string;
	public valueAsDate: Date;
	public valueAsBoolean: boolean = false;
	public valueAsNomenclatorValue: ValueOfNomenclatorValueField;

	private fieldValue: ValueOfNomenclatorAttributeDefinitionDefaultValueField;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer) {
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
	}

	public ngOnChanges(simpleChanges: SimpleChanges): void {
		// Nothing now.
	}

	public onValueChanged(event: any): void {
		this.prepareAndPropagateFieldValue();
	}

	public onValueBlured(event: any): void {
		this.prepareAndPropagateFieldValue();
	}

	private prepareAndPropagateFieldValue(): void {
		this.prepareFieldValue();
		this.propagateFieldValue();
	}

	private prepareFieldValue(): void {	
		this.fieldValue = new ValueOfNomenclatorAttributeDefinitionDefaultValueField(this.attributeType);
		if (this.attributeType === AttributeTypeEnum.DATE) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsDate)) {
				this.fieldValue.value = DateUtils.formatForStorage(this.valueAsDate);
			}
		} else if (this.attributeType === AttributeTypeEnum.BOOLEAN) {
			this.fieldValue.value = (this.valueAsBoolean ? "true" : "false");
		} else if (this.attributeType === AttributeTypeEnum.NOMENCLATOR) {
			this.fieldValue.nomenclatorId = this.nomenclatorId;
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsNomenclatorValue)) {
				if (ObjectUtils.isNotNullOrUndefined(this.valueAsNomenclatorValue.value)) {
					this.fieldValue.value = this.valueAsNomenclatorValue.value.toString();
				}
			}
		} else {
			this.fieldValue.value = this.valueAsString;
		}
	}

	private propagateFieldValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	private clearSpecificValues(): void {
		this.valueAsString = null;
		this.valueAsBoolean = false;
		this.valueAsNomenclatorValue = null;
		this.attributeType = null;
		this.nomenclatorId = null;
	}

	private applySpecificValueFromFieldValue(): void {
		this.clearSpecificValues();
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			throw new Error("fieldValue cannot be null here");
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue.attributeType)) {
			throw new Error("attributeType cannot be null");
		}
		this.attributeType = this.fieldValue.attributeType;
		
		if (this.attributeType === AttributeTypeEnum.DATE) {
			if (StringUtils.isNotBlank(this.fieldValue.value)) {
				this.valueAsDate = DateUtils.parseFromStorage(this.fieldValue.value);
			}
		} else if (this.attributeType === AttributeTypeEnum.BOOLEAN) {
			this.valueAsBoolean = (this.fieldValue.value === "true");
		} else if (this.attributeType === AttributeTypeEnum.NOMENCLATOR) {
			this.nomenclatorId = this.fieldValue.nomenclatorId;
			if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorId)) {
				this.valueAsNomenclatorValue = new ValueOfNomenclatorValueField(this.nomenclatorId);
				if (StringUtils.isNotBlank(this.fieldValue.value)) {
					this.valueAsNomenclatorValue.value = parseInt(this.fieldValue.value, 0);
				}
			}						
		} else {
			this.valueAsString = this.fieldValue.value;
		}
	}

	public onNomenclatorValueChanged(valueOfNomenclatorValueField: ValueOfNomenclatorValueField): void {
		this.prepareAndPropagateFieldValue();
	}

	public writeValue(value: ValueOfNomenclatorAttributeDefinitionDefaultValueField): void {
		this.fieldValue = value;
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			this.clearSpecificValues();
			return;
		}		
		this.applySpecificValueFromFieldValue();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}

export class ValueOfNomenclatorAttributeDefinitionDefaultValueField {

	public value: string;
	private _attributeType: string;

	public nomenclatorId: number; // NOMENCLATOR

	public constructor(attributeType: string) {
		if (StringUtils.isBlank(attributeType)) {
			throw new Error("attributeType cannot be blank");
		}
		this._attributeType = attributeType;
		
	}

	public get attributeType() {
		return this._attributeType;
	}
}