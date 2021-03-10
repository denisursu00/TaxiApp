import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel, ArrayUtils, TranslateUtils, ObjectUtils, StringUtils, BooleanUtils, ValueOfNomenclatorValueField, NomenclatorFilter } from "@app/shared";

@Component({
	selector: "app-metadata-nomenclator-field",
	template: `
		<app-nomenclator-value-field
			[(ngModel)]="nomenclatorValue"
			[readonly]="readonly"
			[customFilters]="customFilters"
			(valueChanged)="onValueChanged()">
		</app-nomenclator-value-field>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataNomenclatorFieldComponent, multi: true }
	]
})
export class MetadataNomenclatorFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	@Input()
	public customFilters: NomenclatorFilter[];

	@Output()
	public valueChanged: EventEmitter<number>;

	public nomenclatorValue: ValueOfNomenclatorValueField;
	private fieldValue: number;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.valueChanged = new EventEmitter();
	}

	public ngOnInit(): void {		
	}

	public onValueChanged(): void {
		this.fieldValue = null;
		if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorValue)) {
			this.fieldValue = this.nomenclatorValue.value;
		}
		this.propagateValue();
		this.valueChanged.emit(this.fieldValue);
	}

	private propagateValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public writeValue(nomeclatorValueId: number): void {
		this.fieldValue = nomeclatorValueId;		
		let newNomenclatorValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.metadataDefinition.nomenclatorId);
		newNomenclatorValue.value = nomeclatorValueId;
		this.nomenclatorValue = newNomenclatorValue;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}