import { Component, Output, EventEmitter, OnInit, Input } from "@angular/core";
import { WorkflowStateModel } from "./../../model/bpm";
import { ArrayUtils, BooleanUtils, ObjectUtils, StringUtils } from "./../../utils";
import { Dialog } from "primeng/primeng";
import { BaseWindow } from "./../../base-window";

@Component({
	selector: "app-workflow-states-selection-window",
	templateUrl: "./workflow-states-selection-window.component.html",
	styleUrls: ["./workflow-states-selection-window.component.css"]
})
export class WorkflowStatesSelectionWindowComponent extends BaseWindow implements OnInit {

	private static readonly WORKFLOW_STATES_STORAGE_SEPARATOR: string = ";";
	
	@Input()
	public selectionMode: "single" | "multiple";

	@Input()
	public possibleStates: WorkflowStateModel[] = [];

	@Input()
	public selectedStateCodes: string = "";

	@Output()
	private statesSelected: EventEmitter<string>;

	@Output()
	private windowClosed: EventEmitter<void>;
	
	public selectedStates: WorkflowStateModel | WorkflowStateModel[];
	public visible: boolean = false;

	public constructor() {
		super();
		this.init();
	}

	private init(): void {
		this.visible = true;
		this.selectionMode = "multiple";
		this.windowClosed = new EventEmitter<void>();
		this.statesSelected = new EventEmitter<string>();
	}

	public ngOnInit(): void {
		this.prepareSelectedStates();
	}

	private prepareSelectedStates(): void {
		if (StringUtils.isBlank(this.selectedStateCodes) || ArrayUtils.isEmpty(this.possibleStates)) {
			return;
		}
		let selectedStatesAsArray: WorkflowStateModel[] = [];
		let selectedStateCodesAsArray: string[] = this.selectedStateCodes.split(WorkflowStatesSelectionWindowComponent.WORKFLOW_STATES_STORAGE_SEPARATOR);
		for (let selectedStateCode of selectedStateCodesAsArray) {
			this.possibleStates.forEach((possibleState: WorkflowStateModel) => {
				if (selectedStateCode === possibleState.code) {
					selectedStatesAsArray.push(possibleState);
				}					
			});
		}
		if (ArrayUtils.isNotEmpty(selectedStatesAsArray)) {
			if (this.selectionMode === "single") {
				this.selectedStates = selectedStatesAsArray[0];
			} else if (this.selectionMode === "multiple") {
				this.selectedStates = selectedStatesAsArray;
			} else {
				throw new Error("selectionMode invalid [" + this.selectionMode + "]");
			}
		}
	}

	public get isMultiple() {
		return (this.selectionMode === "multiple");
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onOkAction(event: any): void {
		let seletectedStateCode: string = null;		
		if (this.selectionMode === "single") {
			if (ObjectUtils.isNotNullOrUndefined(this.selectedStates)) {
				seletectedStateCode = (<WorkflowStateModel> this.selectedStates).code;
			}
		} else if (this.selectionMode === "multiple") {
			let selectedStateCodesAsArray: string[] = [];
			if (ArrayUtils.isNotEmpty(<Array<WorkflowStateModel>>this.selectedStates)) {
				for (let selectedState of <Array<WorkflowStateModel>> this.selectedStates) {
					selectedStateCodesAsArray.push(selectedState.code);
				}
				selectedStateCodesAsArray.sort();
				seletectedStateCode = selectedStateCodesAsArray.join(WorkflowStatesSelectionWindowComponent.WORKFLOW_STATES_STORAGE_SEPARATOR);
			}			
		} else {
			throw new Error("selectionMode necunoscut [" + this.selectionMode + "]");
		}
		this.statesSelected.emit(seletectedStateCode);
		this.windowClosed.emit();
	}
}