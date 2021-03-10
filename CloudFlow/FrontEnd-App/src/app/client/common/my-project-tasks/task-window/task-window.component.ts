import { Component, Output, Input, EventEmitter, ViewChild, AfterViewInit } from "@angular/core";
import { TaskGeneralTabContentComponent } from "./task-general-tab-content/task-general-tab-content.component";
import { TaskAssignmentTabContentComponent } from "./task-assignment-tab-content/task-assignment-tab-content.component";
import { TaskModel, ProjectService, AppError, MessageDisplayer, TaskStatus, MessagesWindowComponent, Message, BaseWindow, TranslateUtils, ConfirmationWindowFacade } from "@app/shared";
import { Dialog } from "primeng/primeng";
import { Validators } from "@angular/forms";

@Component({
	selector: "app-task-window",
	templateUrl: "./task-window.component.html"
})
export class TaskWindowComponent extends BaseWindow implements AfterViewInit{
	@Input()
	public perspective: "view" | "add" | "edit" = "view";

	@Input()
	public taskId: number = null;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

	@ViewChild(TaskGeneralTabContentComponent)
	private generalTab: TaskGeneralTabContentComponent;

	@ViewChild(TaskAssignmentTabContentComponent)
	private assignmentTab: TaskAssignmentTabContentComponent;

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = true;
	public tabContentVisible: boolean = false;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public selectedProjectId: number;
	private taskName: string;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		super();
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.unlock();
	}

	ngAfterViewInit(): void {
		if (this.perspective === "view" || this.perspective === "edit"){
			this.lock();
			this.getTask(this.taskId)
				.then((result: TaskModel) => {
					this.taskName = result.name;
					this.generalTab.task = result;
					this.assignmentTab.users = result.taskAssignments;
					this.populateGeneralTab(result);
					this.generalTab.onProjectChanged(result.projectId);
					this.generalTab.onParticipantsToChanged(null);
					this.unlock();
				})
				.catch((error) => {
					if (error instanceof AppError){
						this.messageDisplayer.displayAppError(error);
					}else{
						console.error(error);
					}
					this.unlock();
				});
		}
	}

	private getTask(taskId: number): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			this.projectService.getTask(taskId, {
				onSuccess: (task: TaskModel): void => {
					resolve(task);
				},onFailure: (error: AppError): void => {
					reject(error);
				}
			});
		});
	}
	

	public onSaveAction(event: any): void {
		if (!this.assignmentTab.isValid()) {
			this.showErrorMessages();
		}
		if (this.generalTab.isValid() && this.assignmentTab.isValid()) {
			let task: TaskModel = new TaskModel();
			this.generalTab.populateForSave(task);
			this.assignmentTab.populateForSave(task);
			task.status = TaskStatus.IN_PROGRESS;
			task.id = this.taskId;
			this.saveTask(task);
		}
	}

	private saveTask(task: TaskModel): void {
		this.lock();
		this.projectService.saveTask(task, {
			onSuccess: (): void => {
				this.unlock();
				this.messageDisplayer.displaySuccess("PROJECT_TASK_SAVED");
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateGeneralTab(task: TaskModel): void {
		this.generalTab.form.setValue({
			projectId: task.projectId,
			name: task.name,
			subactivity: task.subactivityId,
			description: task.description,
			startDate: task.startDate,
			endDate: task.endDate,
			priority: task.priority,
			permanent: task.permanent,
			participantsTo: task.participationsTo,
			explications: task.explications,
			comments: task.comments,
			evenimentStartDate: task.evenimentStartDate,
			evenimentEndDate: task.evenimentEndDate
		});
		this.generalTab.uploadedAttachments = task.taskAttachments;
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private showErrorMessages(): void {
		this.messagesWindowMessages = [{
			type: "error",
			code: "PROJECT_TASK_NOT_ASSIGNED",
			translate: true
		}];
		this.messagesWindowVisible = true;
	}

	public onMessagesWindowClosed(event: any): void {
		this.messagesWindowMessages = [];
		this.messagesWindowVisible = false;
	}

	public onProjectChanged(projectId: number): void {
		this.selectedProjectId = projectId;
	}
	
	public getHeaderTaskName(): string {
		if (["view","edit"].includes(this.perspective)){
			let viewOrEditLabel: string = this.perspective === "view" ? "VIEW" : "EDIT";
			return this.translateUtils.translateLabel(viewOrEditLabel)+" "+this.translateUtils.translateLabel("PROJECT_TASK")+" - "+this.taskName;
		}
		return this.translateUtils.translateLabel("ADD");
	}
}
