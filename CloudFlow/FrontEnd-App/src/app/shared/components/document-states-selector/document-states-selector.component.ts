import { Component, Input, Output, EventEmitter } from "@angular/core";
import { WorkflowStateModel } from "../../model/bpm/workflow-state.model";
import { WorkflowService } from "../../service/workflow.service";
import { AppError } from "../../model/app-error";
import { MessageDisplayer } from "../../message-displayer";
import { DocumentTypeIdsRequestModel } from "../../model/document-type-ids-request.model";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ArrayUtils } from "../../utils/array-utils";

@Component({
	selector: "app-document-states-selector",
	templateUrl: "./document-states-selector.component.html",
	styleUrls: ["./document-states-selector.component.css"],
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: DocumentStatesSelectorComponent,
		multi: true
	}]
})
export class DocumentStatesSelectorComponent implements ControlValueAccessor {

	@Input()
	public showControls: boolean = true;

	@Input()
	public readonly: boolean = false;

	@Output()
	private selectionChanged: EventEmitter<WorkflowStateModel[]>;

	private workflowService: WorkflowService;
	private messageDisplayer: MessageDisplayer;

	public sourceStates: WorkflowStateModel[];
	public targetStates: WorkflowStateModel[];
	
	private onChange = (workflowStateModels: WorkflowStateModel[]) => {};
	private onTouch = () => {};
	
	public constructor(workflowService: WorkflowService, messageDisplayer: MessageDisplayer) {
		this.workflowService = workflowService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<[WorkflowStateModel]>();
		this.init();
	}
	
	private init(): void {
		this.reset();
	}

	private reset(): void {
		this.sourceStates = [];
		this.targetStates = [];
	}

	private loadSourceStates(documentTypeIds: number[]): void {
		if (ArrayUtils.isEmpty(documentTypeIds)) {
			return;
		}
		this.workflowService.getStatesByDocumentTypeIds(this.buildDocumentTypeIdsRequestModel(documentTypeIds), {
			onSuccess: (states: WorkflowStateModel[]): void => {
				this.sourceStates = states;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private buildDocumentTypeIdsRequestModel(ids: number[]): DocumentTypeIdsRequestModel {
		let documentTypeIdsRequestModel = new DocumentTypeIdsRequestModel();
		ids.forEach(id => {
			documentTypeIdsRequestModel.ids.push(id);
		});
		return documentTypeIdsRequestModel;
	}

	@Input()
	public set documentTypeIds(documentTypeIds: number[]) {
		this.reset();
		this.loadSourceStates(documentTypeIds);
	}

	public onTargetChanged($event): void {
		this.selectionChanged.emit(this.targetStates);
		this.propagateValue();
	}
	
	private propagateValue() {
		this.onChange(this.targetStates);
		this.onTouch();
	}

	public writeValue(defaultStates: WorkflowStateModel[]): void {
		this.targetStates = defaultStates;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouch = fn;
	}
}
