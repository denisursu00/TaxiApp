import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { TaskViewModel, ProjectService, AppError, MessageDisplayer, TaskPriority, TaskStatus, DateConstants, OrganizationService, UserModel, TaskAttachmentModel, BaseWindow } from "@app/shared";
import { DatePipe } from "@angular/common";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";
import { environment } from "@app/../environments/environment";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-view-task-window",
	templateUrl: "./view-task-window.component.html"
})
export class ViewTaskWindowComponent extends BaseWindow implements OnInit {
	
	private static readonly TASK_ID: string = "id";
	private static readonly TASK_NAME: string = "name";
	private static readonly TASK_DESCRIPTION: string = "description";
	private static readonly TASK_START_DATE: string = "startDate";
	private static readonly TASK_END_DATE: string = "endDate";
	private static readonly TASK_PRIORITY: string = "priority";
	private static readonly TASK_PROJECT_NAME: string = "projectName";
	private static readonly TASK_COMMENTS: string = "comments";
	private static readonly TASK_PROJECT_STATUS: string = "status";
	private static readonly TASK_ASSIGNMENT: string = "assignments";
	private static readonly TASK_ATTACHMENTS: string = "attachments";
	private static readonly TASK_INITIATOR: string = "initiator";
	private static readonly TASK_PERMANENT: string = "permanent";
	private static readonly TASK_PARTICIPATIONS_TO: string = "participationsTo";
	private static readonly TASK_EXPLICATIONS: string = "explications";
	private static readonly TASK_SUBACTIVITY: string = "subactivity";
	
	@Output()
	public windowClosed: EventEmitter<void>;
	
	private projectService: ProjectService;
	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private datePipe: DatePipe;

	public windowVisible: boolean = false;

	public task: any;

	@Input()
	public taskId: number;

	public constructor(projectService: ProjectService, organizationService: OrganizationService, 
			messageDisplayer: MessageDisplayer, datePipe: DatePipe) {
		super();
		this.windowClosed = new EventEmitter();
		this.projectService = projectService;
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.datePipe = datePipe;
		this.init();
	}

	public ngOnInit(): void {
		this.getTask();
	}

	private init(): void {
		this.task = {};
	}

	private getTask(): void {
		this.lock();
		this.projectService.getTaskViewModel(this.taskId, {
			onSuccess: (task: TaskViewModel): void => {
				this.prepareTaskForView(task);
				this.unlock();
				this.windowVisible = true;
			},
			onFailure: (appError: AppError): void => {				
				this.unlock();
				this.windowVisible = false;
				this.windowClosed.emit();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private prepareTaskForView(taskModel: TaskViewModel): void {
		this.task[ViewTaskWindowComponent.TASK_ID] = taskModel.id;
		this.task[ViewTaskWindowComponent.TASK_NAME] = taskModel.name;
		this.task[ViewTaskWindowComponent.TASK_DESCRIPTION] = taskModel.description;
		this.task[ViewTaskWindowComponent.TASK_PROJECT_NAME] = taskModel.project.name;
		this.task[ViewTaskWindowComponent.TASK_COMMENTS] = taskModel.comments;
		this.task[ViewTaskWindowComponent.TASK_SUBACTIVITY] = taskModel.subactivity != null ? taskModel.subactivity.name : null;

		let startDate: string = this.datePipe.transform(
			taskModel.startDate,
			DateConstants.DATE_FORMAT
		);
		this.task[ViewTaskWindowComponent.TASK_START_DATE] = startDate;

		let endDate: string = this.datePipe.transform(
			taskModel.endDate,
			DateConstants.DATE_FORMAT
		);
		this.task[ViewTaskWindowComponent.TASK_END_DATE] = endDate;
		
		
		if (taskModel.priority === TaskPriority.HIGH) {
			this.task[ViewTaskWindowComponent.TASK_PRIORITY] = "High";
		} else if (taskModel.priority === TaskPriority.NORMAL) {
			this.task[ViewTaskWindowComponent.TASK_PRIORITY] = "Normal";
		} else if (taskModel.priority === TaskPriority.LOW) {
			this.task[ViewTaskWindowComponent.TASK_PRIORITY] = "Low";
		}

		if (taskModel.status === TaskStatus.IN_PROGRESS) {
			this.task[ViewTaskWindowComponent.TASK_PROJECT_STATUS] = "In progress";
		} else if (taskModel.status === TaskStatus.FINALIZED) {
			this.task[ViewTaskWindowComponent.TASK_PROJECT_STATUS] = "Finalized";
		}

		this.task[ViewTaskWindowComponent.TASK_ASSIGNMENT] = taskModel.taskAssignments;
		this.task[ViewTaskWindowComponent.TASK_ATTACHMENTS] = taskModel.taskAttachments;
		this.task[ViewTaskWindowComponent.TASK_INITIATOR] = taskModel.initiator.displayName;
		this.task[ViewTaskWindowComponent.TASK_PERMANENT] = taskModel.permanent;
		
		this.task[ViewTaskWindowComponent.TASK_PARTICIPATIONS_TO] = taskModel.participationsTo;
		this.task[ViewTaskWindowComponent.TASK_EXPLICATIONS] = taskModel.explications;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}
	/*
	//
	// TODO - Descarcarea de atasamente trebuie facuta cum e la dsp view window.
	//
	public buildDownloadUrl(attachmentModel: TaskAttachmentModel): string {
		let downloadUrl: string = "downloadTaskAttachment" + 
			"?attachmentId=" + attachmentModel.id + 
			"&attachmentName=" + attachmentModel.name;
		return downloadUrl;
	}*/
}
