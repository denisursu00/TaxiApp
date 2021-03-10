import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { WorkflowStateModel } from "./../../model/bpm/workflow-state.model";
import { StringUtils, ObjectUtils, ArrayUtils } from "./../../utils";

@Component({
	selector: "app-workflow-states-selection-field",
	templateUrl: "./workflow-states-selection-field.component.html",
	styleUrls: ["./workflow-states-selection-field.component.css"],
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: WorkflowStatesSelectionFieldComponent, multi: true }
	]
})
export class WorkflowStatesSelectionFieldComponent implements ControlValueAccessor, OnInit {
	
	@Input()
	public selectionMode: "single" | "multiple";
	
	@Input()
	public possibleStates: WorkflowStateModel[];

	@Output()
	public selectionChanged: EventEmitter<string>;

	private stateNameByCodeMap: object;

	public workflowStatesSelectionWindowVisible: boolean;
	public workflowStatesSelectionWindowPossibleStates: WorkflowStateModel[];
	public workflowStatesSelectionWindowSelectedStateCodes: string;

	private innerValue: string;

	public selectActionEnabled: boolean;
	public selectedStatesForDisplay: string;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor() {
		this.init();
	}

	private init(): void {
		this.selectionMode = "multiple";
		this.workflowStatesSelectionWindowVisible = false;
		this.selectedStatesForDisplay = "";
		this.selectActionEnabled = false;
		this.stateNameByCodeMap = {};
		this.selectionChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		this.selectActionEnabled = ArrayUtils.isNotEmpty(this.possibleStates);
		if (ArrayUtils.isNotEmpty(this.possibleStates)) {
			this.possibleStates.forEach((workflowState: WorkflowStateModel) => {
				this.stateNameByCodeMap[workflowState.code] = workflowState.name;
			});
		}
	}

	private updateSelectedStatesForDisplay(): void {
		this.selectedStatesForDisplay = "";
		if (ObjectUtils.isNotNullOrUndefined(this.innerValue)) {
			let stateCodesAsArray: string[] = this.innerValue.split(";");
			stateCodesAsArray.forEach((stateCode: string)=> {
				let stateName: string = this.stateNameByCodeMap[stateCode.trim()];
				if (StringUtils.isNotBlank(stateName)) {
					if (StringUtils.isNotBlank(this.selectedStatesForDisplay)) {
						this.selectedStatesForDisplay = this.selectedStatesForDisplay + ", ";
					}
					this.selectedStatesForDisplay = this.selectedStatesForDisplay + stateName;
				}
			});
		}
	}

	public onSelectAction(event: any): void {
		this.workflowStatesSelectionWindowPossibleStates = this.possibleStates;
		this.workflowStatesSelectionWindowSelectedStateCodes = this.innerValue;
		this.workflowStatesSelectionWindowVisible = true;
	}

	public onWorkflowStatesSelectionWindowStatesSelected(stateCodes: string): void {
		this.innerValue = null;
		if (StringUtils.isNotBlank(stateCodes)) {
			this.innerValue = stateCodes;
		}
		this.updateSelectedStatesForDisplay();
		this.propagateValue();
		this.selectionChanged.emit(this.innerValue);
		this.workflowStatesSelectionWindowVisible = false;
	}

	public onWorkflowStatesSelectionWindowWindowClosed(event: any): void {
		this.workflowStatesSelectionWindowVisible = false;
	}

	private propagateValue(): void {
		this.onChange(this.innerValue);
	}

	public writeValue(stateCodes: string): void {
		this.innerValue = stateCodes;
		this.updateSelectedStatesForDisplay();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}