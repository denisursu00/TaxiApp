import { Component, Input, Output, EventEmitter, OnInit, ViewChild } from "@angular/core";
import { ObjectUtils, TranslateUtils, WorkflowModel, WorkflowService, AppError, MessageDisplayer, DocumentTypeModel, WorkflowStateModel, ConfirmationUtils, BaseWindow } from "@app/shared";
import { WorkflowGeneralTabContentComponent } from "./workflow-general-tab-content";
import { WorkflowStatesTabContentComponent } from "./workflow-states-tab-content";
import { WorkflowTransitionsTabContentComponent } from "./workflow-transitions-tab-content";
import { WorkflowSupervisorsTabContentComponent } from "./workflow-supervisors-tab-content";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-workflow-window",
	templateUrl: "./workflow-window.component.html",
	styleUrls: ["./workflow-window.component.css"]
})
export class WorkflowWindowComponent extends BaseWindow implements OnInit {

	public static readonly GENERAL_TAB_INDEX: number = 0;
	public static readonly STATES_TAB_INDEX: number = 2;
	public static readonly TRANSITION_TAB_INDEX: number = 3;

	@Input()
	public workflowId: number;

	@Input()
	public mode: "add" | "edit";
	
	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

	@ViewChild(WorkflowGeneralTabContentComponent)
	public generalTab: WorkflowGeneralTabContentComponent;

	@ViewChild(WorkflowSupervisorsTabContentComponent)
	public supervisorsTab: WorkflowSupervisorsTabContentComponent;

	@ViewChild(WorkflowStatesTabContentComponent)
	public statesTab: WorkflowStatesTabContentComponent;

	@ViewChild(WorkflowTransitionsTabContentComponent)
	public transitionsTab: WorkflowTransitionsTabContentComponent;

	private workflowService: WorkflowService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;

	public title: string;
	
	public windowVisible: boolean = true;

	public tabContentVisible: boolean = false;
	public tabContentInputData: WorkflowModel;

	public activeTabIndes: number = 0;

	public constructor(workflowService: WorkflowService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
		super();
		this.workflowService = workflowService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.mode)) {
			throw new Error("The input property [mode] cannot be null or undefined.");
		}
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	public onTabChanged(event: any): void {
		this.activeTabIndes = event.index;
		if (event.index === WorkflowWindowComponent.TRANSITION_TAB_INDEX) {
			this.transitionsTab.setDocumentsTypes(this.generalTab.getDocumentTypes());
			this.transitionsTab.setStates(this.statesTab.states);
		} else if (event.index === WorkflowWindowComponent.STATES_TAB_INDEX) {
			this.statesTab.setTransitions(this.transitionsTab.transitions);
		}
	}

	private prepareForAdd(): void {
		this.setTitle(null);
		this.tabContentVisible = true;
		this.unlock();
	}

	private prepareForEdit(): void {
		this.lock();
		this.workflowService.getWorkflowById(this.workflowId, {
			onSuccess: (workflow: WorkflowModel): void => {
				this.setTitle(workflow.name);
				this.tabContentInputData = workflow;
				this.tabContentVisible = true;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private setTitle(workflowName: string): void {
		if (ObjectUtils.isNullOrUndefined(workflowName)) {
			this.title = this.translateUtils.translateLabel("WORKFLOW");
		} else {
			this.title = this.translateUtils.translateLabel("WORKFLOW") + " : " + workflowName;		
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}
	
	private closeWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onCancelAction(event: any): void {
		this.closeWindow();
	}

	public onSaveAction(event: any): void {
		let workflow: WorkflowModel = new WorkflowModel();
		
		this.generalTab.populateForSave(workflow);
		this.supervisorsTab.populateForSave(workflow);
		this.statesTab.populateForSave(workflow);
		this.transitionsTab.populateForSave(workflow);

		if (!this.isValid()) {
			return;
		}
		
		if (this.isEdit()) {
			workflow.id = this.tabContentInputData.id;
			workflow.versionNumber = this.tabContentInputData.versionNumber;
			this.workflowService.hasInstances(workflow.id, {
				onSuccess: (hasActiveInstances: boolean): void => {
					if (hasActiveInstances) {
						this.confirmationUtils.confirm("CONFIRM_EDIT_WORKFLOW_INSTANCES_EXIST", {
							approve: (): void => {
								this.saveWorkflow(workflow);
							},
							reject: (): void => {}
						});
					} else {
						this.saveWorkflow(workflow);
					}
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});

		} else {
			this.saveWorkflow(workflow);
		}
	}

	private saveWorkflow(workflow: WorkflowModel): void {
		this.workflowService.saveWorkflow(workflow, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("WORKFLOW_SAVED");
				this.dataSaved.emit();
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private isValid(): boolean {
		let isValid: boolean = true;
		if (!this.transitionsTab.isValid()) {
			isValid = false;
			this.activeTabIndes = WorkflowWindowComponent.TRANSITION_TAB_INDEX;
		}
		if (!this.generalTab.isValid()) {
			isValid = false;
			this.activeTabIndes = WorkflowWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}
}
