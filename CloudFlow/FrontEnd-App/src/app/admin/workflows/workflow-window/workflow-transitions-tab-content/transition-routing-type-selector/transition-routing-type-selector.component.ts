import { Component, Input, Output, EventEmitter, OnInit } from "@angular/core";
import { TransitionRoutingTypeModel } from "@app/shared/model/bpm/transition-routing-type.model";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ObjectUtils } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-transition-routing-type-selector",
	templateUrl: "./transition-routing-type-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: TransitionRoutingTypeSelectorComponent,
		multi: true
	}]
})
export class TransitionRoutingTypeSelectorComponent implements ControlValueAccessor, OnInit {

	@Input()
	public routingType: string;

	@Output()
	private selectionChanged: EventEmitter<string>;
	
	public routingTypesSelectItems: SelectItem[];
	public selectedRoutingType: string;
	
	private onChange = (transitionRoutingType: string) => {};
	private onTouch = () => {};

	public constructor() {
		this.selectionChanged = new EventEmitter<string>();
		this.init();
	}

	private init(): void {
		let routingTypes: TransitionRoutingTypeModel[] = TransitionRoutingTypeModel.getRoutingTypes();
		this.routingTypesSelectItems = [];
		routingTypes.forEach((routingType: TransitionRoutingTypeModel) => {
			this.routingTypesSelectItems.push({label: routingType.labelCode, value: routingType.value});
		});
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.routingType)) {
			this.selectedRoutingType = this.routingType;
		}
	}

	public onRoutingTypeChanged(event: any): void {
		this.selectionChanged.emit(this.selectedRoutingType);
		this.propagateValue();
	}
	
	private propagateValue(): void {
		this.onChange(this.selectedRoutingType);
		this.onTouch();
	}

	public writeValue(transitionRoutingType: string): void {
		this.selectedRoutingType = transitionRoutingType;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouch = fn;
	}
}
