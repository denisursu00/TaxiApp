import { Component, Input, OnInit, Output, EventEmitter} from "@angular/core";
import { Response } from "@angular/http";
import { ProjectService, TranslateUtils, AppError, MessageDisplayer, DspViewModel, PageConstants, DateConstants, ExportType, TasksViewModel, BaseWindow } from "@app/shared";
import { DspActivitateViewModel, TaskPriority, TaskStatus, DspActivitateAttachmentViewModel, DownloadUtils } from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";
import { HttpResponse } from "@angular/common/http";

@Component({
	selector: "app-tasks-view-window",
	templateUrl: "./tasks-view-window.component.html",
	styleUrls: ["./tasks-view-window.component.css"]
})
export class TAsksViewWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public projectId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private projectService: ProjectService;

	public loadedData: boolean;
	public windowVisible: boolean;

	public title: string;

	public pageSize: number;
	public dateFormat: string;

	public prioritateActivitateFilterSelectItems: SelectItem[];
	public statusActivitateFilterSelectItems: SelectItem[];

	public tasksView: TasksViewModel;

	public constructor(translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer, 
			projectService: ProjectService) {
		super();
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.projectService = projectService;

		this.title = "Vizualizare taskuri";
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.lock();
		this.loadedData = false;
		this.windowVisible = false;
		this.windowClosed = new EventEmitter();
		this.prepareFilters();
	}

	public ngOnInit(): void {	
		this.projectService.getTasksViewModelByProjectId(this.projectId, {
			onSuccess: (tasks: TasksViewModel): void => {
				this.tasksView = tasks;
				this.updateTitle();	
				this.unlock();
				this.windowVisible = true;
				this.loadedData = true;
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareFilters(): void {

		this.prioritateActivitateFilterSelectItems = [];
		Object.keys(TaskPriority).forEach((enumOption: string) => {
			this.prioritateActivitateFilterSelectItems.push({
				value: enumOption,
				label: this.translateUtils.translateLabel("PROJECT_TASK_PRIORITY_" + enumOption)
			});
		});

		this.statusActivitateFilterSelectItems = [];
		Object.keys(TaskStatus).forEach((enumOption: string) => {
			this.statusActivitateFilterSelectItems.push({
				value: enumOption,
				label: this.translateUtils.translateLabel("PROJECT_TASK_STATUS_" + enumOption)
			});
		});
	}

	private updateTitle(): void {
		this.title = this.tasksView.abreviereProiect;
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onViewAttachments(event: any): void {
	}

	public onDownloadTaskAttachment(activitateAttachment: DspActivitateAttachmentViewModel): void {
		this.lock();
		this.projectService.downloadTaskAttachment(activitateAttachment.id).subscribe(
			(response: HttpResponse<Blob>) => {
				this.unlock();
				DownloadUtils.saveFileFromResponse(response, activitateAttachment.name);
			}, 
			(error: any) => {
				this.unlock();
				this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
			}
		);
	}

	public onCloseAction(): void {
		this.closeWindow();
	}
}