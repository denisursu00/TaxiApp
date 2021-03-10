import { Component, Input } from "@angular/core";
import { ProjectService, AppError, MessageDisplayer, DateUtils, ObjectUtils, DateConstants, TaskStatus, TaskPriority, AclService, SecurityManagerModel, TaskViewModel, BooleanUtils, UiUtils, ConfirmationWindowFacade } from "@app/shared";
import { Column, SelectItem } from "primeng/primeng";
import { DatePipe } from "@angular/common";

@Component({
	selector: "app-my-project-tasks",
	templateUrl: "./task.component.html"
})
export class MyProjectTasksComponent {

	private static readonly COLUMN_TASK_ID: string = "id";
	private static readonly COLUMN_TASK_NAME: string = "name";
	private static readonly COLUMN_TASK_DESCRIPTION: string = "description";
	private static readonly COLUMN_TASK_START_DATE: string = "startDate";
	private static readonly COLUMN_TASK_END_DATE: string = "endDate";
	private static readonly COLUMN_TASK_PRIORITY: string = "priority";
	private static readonly COLUMN_TASK_PROJECT_STATUS: string = "status";
	private static readonly COLUMN_TASK_PROJECT_ID: string = "projectId";
	private static readonly COLUMN_TASK_PROJECT_NAME: string = "projectName";
	private static readonly COLUMN_TASK_SUBPROJECT_NAME: string = "subprojectName";

	private static readonly COLUMN_TASK_DATA = "data";

	private static readonly TASK_STATUS_IN_PROGRESS_OVER_DEADLINE_STYLE = {"background-color": "red", "color": "white"};

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	private aclService: AclService;
	private datePipe: DatePipe;

	public confirmationWindow: ConfirmationWindowFacade;
	public columns: Column[];
	public tasks: any[];
	public selectedTask: any;
	public completeTaskWindowVisible: boolean;
	public taskWindowVisible: boolean;
	public viewTaskWindowVisible: boolean;
	public taskWindowPerspective: string;
	public dateFormat: String;
	public yearRange: String;
	public rowGroupMetadata: any;
	public taskProritiesSelectItems: SelectItem[];
	public scrollHeight: string;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer, aclService: AclService,
			datePipe: DatePipe) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.aclService = aclService;
		this.datePipe = datePipe;
		this.init();
	}

	public init(): void {
		this.completeTaskWindowVisible = false;
		this.taskWindowVisible = false;
		this.viewTaskWindowVisible = false;
		this.confirmationWindow = new ConfirmationWindowFacade();
		
		this.scrollHeight = (window.innerHeight - 300) + "px";

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();

		this.prepareTableColumns();

		this.createtaskProritiesSelectItems();
		this.loadTasks();
	}

	private createtaskProritiesSelectItems(): void {
		this.taskProritiesSelectItems = [
			{label: "", value: null},
			{label: "Low", value: TaskPriority.LOW},
			{label: "Normal", value: TaskPriority.NORMAL},
			{label: "High", value: TaskPriority.HIGH}
		];
	}

	private updateRowGroupMetaData(tasks: any): void {
		this.rowGroupMetadata = {};
		if (ObjectUtils.isNotNullOrUndefined(tasks)) {
			for (let taskIndex = 0; taskIndex < tasks.length; taskIndex++) {
				let task = tasks[taskIndex];
				let projectId = task.data.project.id;
				if (taskIndex === 0) {
					this.rowGroupMetadata[projectId] = { index: 0, size: 1 };
				}
				else {
					let previousRowData = tasks[taskIndex - 1];
					let previousRowGroup = previousRowData.projectId;
					if (projectId === previousRowGroup) {
						this.rowGroupMetadata[projectId].size++;
					} else {
						this.rowGroupMetadata[projectId] = { index: taskIndex, size: 1 };
					}
				}
			}
		}
	}

	private sortTasksBySubprojectGroupedByProject(): void {
		let rowGroupList: {index: number, size:number}[] = Object.values(this.rowGroupMetadata);
		for (let rowGroup of rowGroupList){
			let taskList = this.tasks.slice(rowGroup.index, rowGroup.size);
			taskList.sort((task1, task2,) => {
				let a: string = task1.subprojectName;
				let b: string = task2.subrpojectName;
				if (a == null && b == null){
					return 0;
				}else if (a == null && b != null){
					return 1;
				}else if (b == null && a != null){
					return -1;
				}else{
					return a.toLocaleLowerCase().localeCompare(b.toLocaleLowerCase());
				}
			});
			this.tasks.splice(rowGroup.index, rowGroup.size - rowGroup.index, ...taskList);
		}
	}

	private loadTasks(): void {
		this.aclService.getSecurityManager({
			onSuccess: (secutityManager: SecurityManagerModel): void => {
				this.projectService.getUserInProgressTasksModels(Number(secutityManager.userIdAsString), {
					onSuccess: (tasks: TaskViewModel[]): void => {
						this.prepareTasks(tasks);
						this.updateRowGroupMetaData(this.tasks);
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onSort(): void {
		this.updateRowGroupMetaData(this.tasks);
		this.sortTasksBySubprojectGroupedByProject();
	}

	public onFilter(event: any): void {
		this.updateRowGroupMetaData(event.filteredValue);
	}

	private prepareTasks(tasks: TaskViewModel[]) {
		this.tasks = [];
		tasks.forEach((task: TaskViewModel) => {
			this.tasks.push(this.prepareTask(task));
		});
	}

	private prepareTask(taskModel: TaskViewModel): any {
		let task: any = {};
		task[MyProjectTasksComponent.COLUMN_TASK_ID] = taskModel.id;
		task[MyProjectTasksComponent.COLUMN_TASK_PROJECT_NAME] = taskModel.project.name;
		task[MyProjectTasksComponent.COLUMN_TASK_PROJECT_ID] = taskModel.project.id;
		task[MyProjectTasksComponent.COLUMN_TASK_NAME] = taskModel.name;
		task[MyProjectTasksComponent.COLUMN_TASK_DESCRIPTION] = taskModel.description;
		task[MyProjectTasksComponent.COLUMN_TASK_START_DATE] = taskModel.startDate;
		task[MyProjectTasksComponent.COLUMN_TASK_END_DATE] = taskModel.endDate;
		task[MyProjectTasksComponent.COLUMN_TASK_SUBPROJECT_NAME] = taskModel.subactivity != null ? taskModel.subactivity.name : null;
		task.style = {};
		
		
		if (taskModel.priority === TaskPriority.HIGH) {
			task[MyProjectTasksComponent.COLUMN_TASK_PRIORITY] = "High";
		} else if (taskModel.priority === TaskPriority.NORMAL) {
			task[MyProjectTasksComponent.COLUMN_TASK_PRIORITY] = "Normal";
		} else if (taskModel.priority === TaskPriority.LOW) {
			task[MyProjectTasksComponent.COLUMN_TASK_PRIORITY] = "Low";
		}

		if (taskModel.status === TaskStatus.IN_PROGRESS) {
			task[MyProjectTasksComponent.COLUMN_TASK_PROJECT_STATUS] = "In progress";
		} else if (taskModel.status === TaskStatus.FINALIZED) {
			task[MyProjectTasksComponent.COLUMN_TASK_PROJECT_STATUS] = "Finalized";
		}

		task[MyProjectTasksComponent.COLUMN_TASK_DATA] = taskModel;

		if (BooleanUtils.isFalse(taskModel.permanent)) {
			let currentDate: Date = new Date();
			if (ObjectUtils.isNotNullOrUndefined(task.endDate)) {
				let curentDateWithoutTime = DateUtils.removeTimeFromDate(currentDate);
				let taskEndDateWithoutTime = DateUtils.removeTimeFromDate(task.endDate);
				if ((taskModel.status === TaskStatus.IN_PROGRESS) 
						&& ObjectUtils.isNotNullOrUndefined(taskModel.endDate)
						&& (taskEndDateWithoutTime < curentDateWithoutTime)	) {
					task.style =  Object.assign({},MyProjectTasksComponent.TASK_STATUS_IN_PROGRESS_OVER_DEADLINE_STYLE);
				}
			}
		}	

		UiUtils.appendTableCellCollapseStyle(task.style);
		return task;
	}

	private prepareTableColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_PROJECT_NAME, "LABELS.PROJECT_NAME", "TEXT", "contains"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_SUBPROJECT_NAME, "LABELS.PROJECT_SUBACTIVITY", "TEXT", "contains"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_NAME, "LABELS.PROJECT_TASK_NAME", "TEXT", "contains"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_DESCRIPTION, "LABELS.PROJECT_TASK_DESCRIPTION", "TEXT", "contains"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_PRIORITY, "LABELS.PROJECT_TASK_PRIORITY", "PRIORITY", "contains"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_START_DATE, "LABELS.PROJECT_TASK_START_DATE", "DATE", "equals"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_END_DATE, "LABELS.PROJECT_TASK_END_DATE", "DATE", "equals"));
		this.columns.push(this.buildColumn(MyProjectTasksComponent.COLUMN_TASK_PROJECT_STATUS, "LABELS.PROJECT_TASK_STATUS", "NONE", "contains"));
	}

	private buildColumn(field: string, header: string, filterType: string, filterMatchMode: string): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		column.filterType = filterType;
		column.filterMatchMode = filterMatchMode;
		return column;
	}
	
	public isColumnStartDate(column: Column): boolean {
		return column.field === MyProjectTasksComponent.COLUMN_TASK_START_DATE;
	}
	
	public isColumnEndDate(column: Column): boolean {
		return column.field === MyProjectTasksComponent.COLUMN_TASK_END_DATE;
	}

	public onShowCompleteTaskWindow(): void {
		this.completeTaskWindowVisible = true;
	}

	public onCompleteTaskWindowClosed(event: any): void {
		this.completeTaskWindowVisible = false;
		this.loadTasks();
		this.selectedTask = null;
	}

	public onTaskWindowClosed(event: any): void {
		this.taskWindowVisible = false;
		this.loadTasks();
		this.selectedTask = null;
	}

	public isTaskSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.selectedTask);
	}

	public completeTaskButtonDisabled(): boolean {
		if (!this.isTaskSelected()) {
			return true;
		}
		return this.selectedTask.data.status === TaskStatus.FINALIZED;
	}

	public isCancelTaskButtonDisabled(): boolean{
		return false;
	}

	public onViewTaskWindowClosed(event: any): void {
		this.viewTaskWindowVisible = false;
	}

	public showViewTaskWindow(): void {
		this.viewTaskWindowVisible = true;
	}

	public getSelectedTaskId(): number {
		return this.selectedTask[MyProjectTasksComponent.COLUMN_TASK_DATA].id;
	}

	public onAddTask(): void {
		this.selectedTask = null;
		this.taskWindowPerspective = "add";
		this.taskWindowVisible = true;
	}

	public onViewTask(): void {
		this.taskWindowPerspective = "view";
		this.taskWindowVisible = true;
	}

	public onEditTask(): void {
		this.taskWindowPerspective = "edit";
		this.taskWindowVisible = true;
	}

	public onCancelTask(): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.projectService.cancelTask(this.selectedTask.id, {
					onSuccess: (): void => {
						this.confirmationWindow.hide();
						this.messageDisplayer.displayMessage("success", "PROJECT_TASK_CANCELLED");
						this.loadTasks();
					},
					onFailure: (error: AppError): void => {
						this.confirmationWindow.hide();
						this.messageDisplayer.displayError(error.errorCode);
					}
				});
			},
			reject: ():void => {
				this.confirmationWindow.hide();
			}
		},"PROJECT_TASK_CANCEL_CONFIRMATION");
	}

	public onRowSelect(event: any) {
		let selectedRowId = event.data.id;
		this.tasks.forEach(element => {
			if (element.id === selectedRowId) {
				UiUtils.appendTableCellExpandStyle(element.style);
				if (element.style["background-color"] === "red"){
					element.style["background-color"] = "darkmagenta"; 
				}
			} else {
				UiUtils.appendTableCellCollapseStyle(element.style);			
				if (element.style["background-color"] === "darkmagenta"){
					element.style["background-color"] = "red"; 
				}
			}
		});
	}

	public onRowUnselect(event: any) {
		UiUtils.appendTableCellCollapseStyle(event.data.style);
		if (event.data.style["background-color"] === "darkmagenta"){
			event.data.style["background-color"] = "red"; 
		}
	}
}