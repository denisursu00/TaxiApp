import { Component, Output, EventEmitter } from "@angular/core";
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from "@angular/forms";
import { SelectItem } from "primeng/api";
import { ReportConstants } from "../../constants/report.constants";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
    selector: "app-tip-sedinta-cd-pvg-selector",
    template: `
        <p-dropdown 
            [options]="selectItems"
            [(ngModel)]="selectedItemValue"
			[style]="{'width':'100%'}"
			[placeholder]="'LABELS.SELECT' | translate" 
			[readonly]="false" 
			[editable]="false"
			(onChange)="onSelectionValueChanged($event)" 
            filter="true"
			(onBlur)="onSelectionBlured($event)"
			appendTo="body">
        </p-dropdown>`,
    providers: [
        { provide: NG_VALUE_ACCESSOR, useExisting: TipSedintaCdPvgComponent, multi: true }
    ]
})
export class TipSedintaCdPvgComponent implements ControlValueAccessor {

    @Output()
    public selectionChanged: EventEmitter<string>;

    private innerValue: string;

    public selectItems: SelectItem[];
    public selectedItemValue: string;

    private onChange: any = () => { };
    private onTouched: any = () => { };
    
    public constructor() {
        this.selectionChanged = new EventEmitter();
        this.init();
    }
    
    private init(): void {
        this.selectItems = [
            { value: ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_CD_VALUE, label: ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_CD_LABEL },
            { value: ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_PVG_VALUE, label: ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_PVG_LABEL }
        ];

        ListItemUtils.sortByLabel(this.selectItems);
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