import { Component, Input, Output, EventEmitter, OnInit } from "@angular/core";
import { NG_VALUE_ACCESSOR } from "@angular/forms";
import { ObjectUtils } from "../../utils";

@Component({
	selector: "app-picklist",
	templateUrl: "./picklist.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: PicklistComponent,
		multi: true
	}]
})
export class PicklistComponent implements OnInit {

	@Input()
	public propertyNameToDisplay: string;

	@Input()
	public showControls: boolean = false;

	@Input()
	public readonly: boolean = false;

	@Output()
	private selectionChanged: EventEmitter<any[]>;

	public _sourceItems: any[];
	public _targetItems: any[];
	public _propertyNameToFilter: string;
	public _showSourceFilter: boolean;
	public _showTargetFilter: boolean;
	
	private onChange = (values: any[]) => {};
	private onTouch = () => {};
	
	public constructor() {
		this.selectionChanged = new EventEmitter<any[]>();
		this.init();
	}
	
	private init(): void {
		this.reset();
	}

	private reset(): void {
		this._sourceItems = [];
		this._targetItems = [];
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.propertyNameToDisplay)) {
			throw Error("Input property [propertyNameToDisplay] cannot be null or undefined.");
		}
	}

	@Input()
	public set sourceItems(sourceItems: any[]) {
		this._sourceItems = sourceItems;
	}

	@Input()
	public set targetItems(targetItems: any[]) {
		this._targetItems = targetItems;
	}

	@Input()
	public set propertyNameToFilter(propertyNameToFilter: string) {
		this._propertyNameToFilter = propertyNameToFilter;
	}

	@Input()
	public set showSourceFilter(showSourceFilter: boolean) {
		this._showSourceFilter = showSourceFilter;
	}

	@Input()
	public set showTargetFilter(showTargetFilter: boolean) {
		this._showTargetFilter = showTargetFilter;
	}

	public onTargetChanged($event): void {
		this.selectionChanged.emit(this._targetItems);
		this.propagateValue();
	}
	
	private propagateValue() {
		this.onChange(this._targetItems);
		this.onTouch();
	}

	public writeValue(values: any[]): void {
		this._targetItems = values;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouch = fn;
	}
}
