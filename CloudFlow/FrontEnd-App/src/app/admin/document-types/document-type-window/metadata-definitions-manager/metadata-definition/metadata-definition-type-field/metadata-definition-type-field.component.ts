import { Component, Input, OnInit, Output, EventEmitter} from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel, TranslateUtils, ArrayUtils, ObjectUtils, StringUtils, MetadataTypeConstants } from "@app/shared";
import { SelectItem } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-metadata-definition-type-field",
	template: `
		<p-dropdown
			[options]="selectItems" 
			[(ngModel)]="selectedItemValue"
			[style]="{'width':'100%'}"
			[placeholder]="'LABELS.SELECT' | translate" 
			[readonly]="false" 
			[editable]="false"
			(onChange)="onSelectionValueChanged($event)" 
			(onBlur)="onSelectionBlured($event)"
			filter="true"
			appendTo="body">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataDefinitionTypeFieldComponent, multi: true }
	]
})
export class MetadataDefinitionTypeFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public usageMode: "documentType" | "metadataCollection";

	@Output()
	public selectionChanged: EventEmitter<string>;

	private innerValue: string;

	public selectItems: SelectItem[];
	public selectedItemValue: string;

	private translateUtils: TranslateUtils;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(translateUtils: TranslateUtils) {
		this.translateUtils = translateUtils;
		this.selectionChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.usageMode)) {
			throw new Error("usageMode cannot be null");
		}
		this.loadSelectItems();
	}
	
	private loadSelectItems(): void {
		this.selectItems = [];
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_TEXT));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_NUMERIC));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_AUTO_NUMBER));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_DATE));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_DATE_TIME));
		if (this.isUsageModeDocumentType()) {
			this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_MONTH));			
		}
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_LIST));		
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_USER));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_GROUP));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_TEXT_AREA));
		this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_NOMENCLATOR));		
		if (this.isUsageModeDocumentType()) {	
			this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_CALENDAR));
			this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_DOCUMENT));
			this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_PROJECT));
			this.selectItems.push(this.createSelectItem(MetadataDefinitionModel.TYPE_METADATA_COLLECTION));
		}

		ListItemUtils.sortByLabel(this.selectItems);
	}

	private isUsageModeDocumentType(): boolean {
		return (this.usageMode === "documentType");
	}

	private createSelectItem(type: string): SelectItem {
		let metadataTypeLabelCodeByType: object = MetadataTypeConstants.getMetadataTypeLabelCodeByTypeMap();
		let labelCode: string = metadataTypeLabelCodeByType[type];
		return {
			value: type,
			label: this.translateUtils.translateLabel(labelCode)
		};
	}

	public onSelectionValueChanged(event: any): void {
		this.prepareAndPropagateInnerValue();
		this.selectionChanged.emit(this.innerValue);
	}

	public onSelectionBlured(event: any): void {
		this.onTouched();
	}

	private prepareAndPropagateInnerValue(): void {
		this.prepareInnerValue();
		this.propagateInnerValue();
	}

	private prepareInnerValue(): void {
		this.innerValue = this.selectedItemValue;
	}

	private propagateInnerValue(): void {
		this.onChange(this.innerValue);
		this.onTouched();
	}

	public writeValue(value: string): void {
		this.innerValue = value;
		this.selectedItemValue = this.innerValue;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}