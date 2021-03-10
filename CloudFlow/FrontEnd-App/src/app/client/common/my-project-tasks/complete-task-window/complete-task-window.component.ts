import { Component, OnInit, EventEmitter, Output, Input } from "@angular/core";
import { MessageDisplayer, AppError, ObjectUtils, ProjectService, TaskModel, CompleteTaskRequestModel, StringUtils, ConfirmationWindowFacade } from "@app/shared";

@Component({
	selector: "app-complete-task",
	templateUrl: "./complete-task-window.component.html"
})
export class CompleteTaskWindowComponent implements OnInit {

	@Input()
	public taskId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;

	public confirmationWindow: ConfirmationWindowFacade;
	public comments: string;

	private task: TaskModel;
	

	public width: number | string;
	public height: number | string;
	public visible: boolean = false;
	public formSubmitted: boolean = false;

	public loading: boolean;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.windowClosed = new EventEmitter<void>();
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.init();
	}

	public ngOnInit(): void {
		this.lock();
		this.projectService.getTask(this.taskId, {
			onSuccess: (task: TaskModel): void => {
				this.task = task;
				this.comments = task.comments;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private init(): void {
		this.visible = true;
		this.loading = false;
		this.windowClosed = new EventEmitter<void>();
		this.adjustSize();
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 400;
		this.height = "auto";
	}
	
	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	public commentsExists(): boolean {
		return StringUtils.isNotBlank(this.comments);
	}

	public onCompleteTask(): void {
		this.formSubmitted = true;
		if (this.commentsExists()) {
			this.completeTask();
		}
	}

	private completeTask(): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.projectService.completeTask(this.getCompleteTaskRequestModel(), {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("PROJECT_TASK_COMPLETED");
						this.windowClosed.emit();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		},"CONFIRM_FINALIZE_TASK");
		
	}

	private getCompleteTaskRequestModel(): CompleteTaskRequestModel {
		let requestModel: CompleteTaskRequestModel = new CompleteTaskRequestModel();
		requestModel.taskId = this.task.id;
		requestModel.comments = this.comments;
		return requestModel;
	}

	public getHeaderTaskName(): string {
		if (ObjectUtils.isNotNullOrUndefined(this.task)){
			return this.task.name;
		}
		return null;
	}
}