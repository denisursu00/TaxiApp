import { Component } from "@angular/core";
import { WorkflowService, WorkflowModel, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils } from "@app/shared";

@Component({
	selector: "app-workflows",
	templateUrl: "./workflows.component.html",
	styleUrls: ["./workflows.component.css"]
})
export class WorkflowsComponent {

	private workflowService: WorkflowService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public workflows: WorkflowModel[];
	public selectedWorkflow: WorkflowModel;

	public isLoading: boolean;
	
	public workflowWindowVisible: boolean = false;
	public workflowWindowMode: "add" | "edit";
	public selectedWorkflowId: number;

	public workflowGraphWindowVisible: boolean = false;
	public scrollHeight: string;

	public constructor(workflowService: WorkflowService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.workflowService = workflowService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init(): void {
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.refresh();
	}

	private refresh(): void {
		this.reset();
		this.populateWorkflows();
	}

	private reset(): void {
		this.isLoading = false;
		this.selectedWorkflow = null;
	}

	private populateWorkflows(): void {
		this.isLoading = true;
		this.workflowService.getAllWorkflows({
			onSuccess: (workflows: WorkflowModel[]): void => {
				this.workflows = workflows;
				this.isLoading = false;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.isLoading = false;
			}
		});
	}

	public onAdd(): void {
		this.workflowWindowMode = "add";
		this.workflowWindowVisible = true;
	}

	public onEdit(): void {
		this.workflowWindowMode = "edit";
		this.selectedWorkflowId = this.selectedWorkflow.id;
		this.showWorkflowWindow();
	}

	public onRemove(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_WORKFLOW", {
			approve: (): void => {
				this.workflowService.deleteWorkflow(this.selectedWorkflow.id, {
					onSuccess: (workflowId: number): void => {
						this.messageDisplayer.displaySuccess("WORKFLOW_DELETED");
						this.refresh();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public onViewGraph(): void {
		this.selectedWorkflowId = this.selectedWorkflow.id;
		this.workflowGraphWindowVisible = true;
	}

	public onAddVersion(): void {
		this.confirmationUtils.confirm("CONFIRM_CREATE_WORKFLOW_VERSION", {
			approve: (): void => {
				this.workflowService.createNewVersion(this.selectedWorkflow.id, {
					onSuccess: (workflowId: number): void => {
						this.workflowWindowMode = "edit";
						this.selectedWorkflowId = workflowId;
						this.showWorkflowWindow();
						this.refresh();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public onRefresh(): void {
		this.refresh();
	}

	public onWorkflowWindowClosed(event: any): void {
		this.hideWorkflowWindow();
	}

	public onWorkflowSaved(event: any): void {
		this.refresh();
	}

	private showWorkflowWindow() : void {
		this.workflowWindowVisible = true;
	}

	private hideWorkflowWindow() : void {
		this.workflowWindowVisible = false;
	}

	public isWorkflowSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.selectedWorkflow);
	}

	public onWorkflowGraphWindowClosed(event:any): void {
		this.workflowGraphWindowVisible = false;
	}

}
