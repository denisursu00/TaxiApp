import { Component, Input, ViewChild, OnInit, Output, EventEmitter, OnChanges } from "@angular/core";
import { TaskViewModel, TaskPriority, ProjectService, MessageDisplayer, AppError, PageRequest, Page, GetPagedProjectTaskViewModelsRequestModel,
		TaskFilterModel, DateConstants, DateUtils, ObjectUtils, TaskFilterValueType, SortedTaskAttributeModel, PagingList,
		TaskFilterMatchMode, TaskStatus, StringUtils, UserModel, OrganizationService, TaskFilterApplicability, SortedTaskAttributeOrderDirection, ProjectModel, PageConstants, TaskParticipantsTo, UiUtils } from "@app/shared";
import { Column, SelectItem } from "primeng/primeng";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-project-task-selector",
	templateUrl: "./project-task-selector.component.html"
})
export class ProjectTaskSelectorComponent implements OnInit, OnChanges {

	public static readonly TABLE_PAGE_SIZE: number = 5;

	private static readonly COLUMN_TASK_NAME: string = "name";
	private static readonly COLUMN_TASK_DESCRIPTION: string = "description";
	private static readonly COLUMN_TASK_START_DATE: string = "startDate";
	private static readonly COLUMN_TASK_END_DATE: string = "endDate";
	private static readonly COLUMN_TASK_PRIORITY: string = "priority";
	private static readonly COLUMN_TASK_PROJECT_STATUS: string = "status";
	private static readonly COLUMN_TASK_ASSIGNEES: string = "assignedUsers";
	private static readonly COLUMN_TASK_PARTICIPATIONS_TO: string = "participationsTo";
	private static readonly COLUMN_TASK_EXPLICATIONS: string = "explications";
	private static readonly COLUMN_TASK_SUBPROJECT: string = "subactivity";

	private static readonly TASK_ASSIGNMENTS_PROPERTY_NAME = "id";
	private static readonly TASK_SUBACTIVITY_NAME_PROPERTY_NAME = "name";

	private static readonly TASK_STATUS_IN_PROGRESS_OVER_DEADLINE_STYLE = {"background-color": "red", "color": "white"};
	private static readonly TASK_STATUS_FINALIZAT_STYLE = {"background-color": "green", "color": "white"};
	private static readonly TASK_STATUS_FINALIZAT_AND_END_DATE_SMALLER_THAN_FINALIZED_DATE_STYLE = {"background-color": "orange", "color": "white"};

	@Input()
	public projectId: number;

	@Input()
	public defaultStatusFilter: TaskStatus;

	@Input()
	public statusFilterDisabled: boolean = false;

	@Output()
	public selectionChanged: EventEmitter<number>;
	
	@ViewChild(Table)
	public tasksTable: Table;

	private projectService: ProjectService;
	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	public tasks: TaskViewModel[];
	public selectedTask: TaskViewModel;
	
	public project: ProjectModel;

	public taskPropertiesSelectItems: SelectItem[];
	public taskStatusSelectItems: SelectItem[];
	public userSelectItems: SelectItem[];
	public dateFilterMatchModeSelectItems: SelectItem[];
	public participantsToSelectItems: SelectItem[];
	
	public columns: Column[];

	public dateFormat: String;
	public yearRange: String;
	public filterDelay: number;

	public page: Page<TaskViewModel>;

	public caption: string;
	public loading: boolean = true;

	public rowsPerPageOptions: number[] = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;

	public scrollHeight: string;

	public constructor(projectService: ProjectService, organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<number>();
		this.init();
	}

	private init(): void {

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.filterDelay = 500;
		
		this.page = new Page<TaskViewModel>();
		this.page.pageSize = ProjectTaskSelectorComponent.TABLE_PAGE_SIZE;
		
		this.scrollHeight = (window.innerHeight - 300) + "px";

		this.prepareTableColumns();
		this.prepareTaskPropertiesSelectItems();
		this.prepareTaskStatusSelectItems();
		this.prepareUserSelectItems();
		this.prepareDateFilterMatchModeSelectItems();
		this.prepareParticipantsToSelectItems();
	}

	public ngOnInit(): void {
		this.setProjectNameByProjectId(this.projectId);		
	}

	public ngOnChanges(): void {
		this.applyDefaultFilterForStatus();
	}

	private applyDefaultFilterForStatus(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.defaultStatusFilter)) {
			let statusColumn: Column = this.getColumnByField(ProjectTaskSelectorComponent.COLUMN_TASK_PROJECT_STATUS);
			this.tasksTable.filter(this.defaultStatusFilter, statusColumn.filterField, statusColumn.filterMatchMode);
		}
	}

	private getColumnByField(field: string): Column {
		let column: Column;
		this.columns.forEach((col: Column) => {
			if (col.field === field) {
				column = col;
			}
		});
		return column;
	}

	private prepareTaskPropertiesSelectItems(): void {
		this.taskPropertiesSelectItems = [
			{label: "Low", value: TaskPriority.LOW},
			{label: "Normal", value: TaskPriority.NORMAL},
			{label: "High", value: TaskPriority.HIGH}
		];
	}

	private prepareTaskStatusSelectItems(): void {
		this.taskStatusSelectItems = [
			{label: "In progress", value: TaskStatus.IN_PROGRESS},
			{label: "Finalized", value: TaskStatus.FINALIZED},
			{label: "Cancelled", value: TaskStatus.CANCELLED}
		];

		ListItemUtils.sortByLabel(this.taskStatusSelectItems);		
	}

	private prepareDateFilterMatchModeSelectItems(): void {
		this.dateFilterMatchModeSelectItems = [
			{label: "=", value: TaskFilterMatchMode.EQUAL},
			{label: ">", value: TaskFilterMatchMode.GREATER},
			{label: "<", value: TaskFilterMatchMode.LOWER}
		];
	}

	private prepareParticipantsToSelectItems(): void {
		this.participantsToSelectItems = [];
		Object.keys(TaskParticipantsTo).forEach((enumItem: string) => {
			let item: SelectItem = { value: TaskParticipantsTo[enumItem], label: TaskParticipantsTo[enumItem]};
			this.participantsToSelectItems.push(item);
		});

		ListItemUtils.sortByLabel(this.participantsToSelectItems);
	}

	private prepareUserSelectItems(): void {
		this.userSelectItems = [
		];
		
		this.organizationService.getUsers({
			onSuccess: (users: UserModel[]): void => {
				users.forEach((user: UserModel) => {
					this.userSelectItems.push({label: user.displayName, value: user.userId});
				});
		
				ListItemUtils.sortByLabel(this.userSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setProjectNameByProjectId(projectId: number): void {
		this.projectService.getProjectById(projectId, {
			onSuccess: (project: ProjectModel): void => {
				this.project = project;
				this.prepareTableCaption();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public prepareTableCaption(): void {
		if (ObjectUtils.isNullOrUndefined(this.project)) {
			return ;
		}
		this.caption = this.project.name;
	}

	private prepareTableColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_SUBPROJECT, "LABELS.PROJECT_SUBACTIVITY", "TEXT", TaskFilterMatchMode.LIKE, false));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_NAME, "LABELS.PROJECT_TASK_NAME", "TEXT", TaskFilterMatchMode.LIKE, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_DESCRIPTION, "LABELS.PROJECT_TASK_DESCRIPTION", "TEXT", TaskFilterMatchMode.LIKE, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_ASSIGNEES, "LABELS.PROJECT_TASK_ASSIGNEES", "ASSIGNEES", TaskFilterMatchMode.EQUAL, false));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_PRIORITY, "LABELS.PROJECT_TASK_PRIORITY", "PRIORITY", TaskFilterMatchMode.LIKE, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_START_DATE, "LABELS.PROJECT_TASK_START_DATE", "DATE", TaskFilterMatchMode.EQUAL, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_END_DATE, "LABELS.PROJECT_TASK_END_DATE", "DATE", TaskFilterMatchMode.EQUAL, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_PROJECT_STATUS, "LABELS.PROJECT_TASK_STATUS", "STATUS", TaskFilterMatchMode.LIKE, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_PARTICIPATIONS_TO, "LABELS.PROJECT_TASK_PARTICIPATIONS_TO", "PARTICIPATIONS_TO", TaskFilterMatchMode.EQUAL, true));
		this.columns.push(this.buildColumn(ProjectTaskSelectorComponent.COLUMN_TASK_EXPLICATIONS, "LABELS.PROJECT_TASK_EXPLICATIONS", "TEXT", TaskFilterMatchMode.LIKE, true));
	}

	private buildColumn(field: string, header: string, filterType: string, filterMatchMode: string, sortable: boolean): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		column.filterType = filterType;
		column.filterField = field;
		column.filterMatchMode = filterMatchMode;
		column.sortable = sortable;
		return column;
	}

	public prepareColumnValue(column: Column, task: TaskViewModel) {
		if (column.field === ProjectTaskSelectorComponent.COLUMN_TASK_PRIORITY) {
			return this.getPriorityValueForDisplay(task[column.field]);
		} else if (column.field === ProjectTaskSelectorComponent.COLUMN_TASK_PROJECT_STATUS) {
			return this.getStatusForDisplay(task[column.field]);
		} else if (column.field === ProjectTaskSelectorComponent.COLUMN_TASK_ASSIGNEES) {
			return this.getAssignedUsersForDisplay(task[column.field]);
		} else if (column.field === ProjectTaskSelectorComponent.COLUMN_TASK_SUBPROJECT) {
			return task[column.field] != null ? task[column.field].name : null;
		}
		return task[column.field];
	}

	public onChangeDateFilterMatchMode(event: any, column: Column): void {
		let filterMatchMode: TaskFilterMatchMode = event.value;
		column.filterMatchMode = filterMatchMode;

		this.tasksTable.filter(this.getFilterValueByFilterFieldName(column.filterField), column.filterField, filterMatchMode);
	}

	private getFilterValueByFilterFieldName(filterFieldName: string): any {
		let filters: any = this.tasksTable.filters;
		
		let filterValue: any;

		Object.entries(filters).forEach((filter: any) => {		
			let filterKey: string = filter[0];
			if (filterKey === filterFieldName) {
				filterValue =  filter[1].value;
			}
		});
		return filterValue;
	}

	public isColumnSortable(column: Column): boolean {
		return column.sortable;
	}

	public getPriorityValueForDisplay(value: string): string {
		if (value === TaskPriority.LOW) {
			return StringUtils.capitalizeFirstLetter(TaskPriority.LOW.toLowerCase());
		} else if (value === TaskPriority.NORMAL) {
			return StringUtils.capitalizeFirstLetter(TaskPriority.NORMAL.toLowerCase());
		} else if (value === TaskPriority.HIGH) {
			return StringUtils.capitalizeFirstLetter(TaskPriority.HIGH.toLowerCase());
		}
		throw new Error("Unknown priority.");
	}

	public getStatusForDisplay(value: string): String {
		if (value === TaskStatus.IN_PROGRESS) {
			return "In progress";
		} else if (value === TaskStatus.FINALIZED) {
			return "Finalized";
		} else if (value === TaskStatus.CANCELLED) {
			return "Cancelled";
		}
		throw new Error("Unknown status.");
	}

	public getAssignedUsersForDisplay(value: UserModel[]): string {
		let concatenatedUsersName: string = null;
		value.forEach((taskAssignment: UserModel) => {
			if (StringUtils.isBlank(concatenatedUsersName)) {
				concatenatedUsersName = taskAssignment.displayName;
			} else {
				concatenatedUsersName += ", " + taskAssignment.displayName;
			}
		});
		return concatenatedUsersName;
	}

	public onLazyLoadTasks(event: any): void {
		this.loadProjectTasks(event);
	}

	private loadProjectTasks(lazyLoadEvent: any): void {
		this.showLoading();
		this.projectService.getPagedProjectTaskViewModels(this.getPageRequest(lazyLoadEvent), {
			onSuccess: (pagingList: PagingList<TaskViewModel>): void => {
				this.updateTaskRowStyle(pagingList);
				this.page.items = [... pagingList.elements];
				this.page.totalItems = pagingList.totalCount;
				this.hideLoading();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.hideLoading();
			}
		});
	}
	private updateTaskRowStyle(pagingList: PagingList<TaskViewModel>) {
		let currentDate: Date = new Date();
		let curentDateWithoutTime = DateUtils.removeTimeFromDate(currentDate);
		pagingList.elements.forEach(task => {
			task.endDate = ObjectUtils.isNotNullOrUndefined(task.endDate) ? new Date(task.endDate): null;
			//TODO: linia de mai sus este un artificiu, din cauza ca elementele din pagingList vin ca json si nu ca TaskViewModel obiect
			if (ObjectUtils.isNotNullOrUndefined(task.endDate)) {
				let taskEndDateWithoutTime = DateUtils.removeTimeFromDate(task.endDate);
				if (task.status === TaskStatus.FINALIZED) {
					task.style = Object.assign({},ProjectTaskSelectorComponent.TASK_STATUS_FINALIZAT_STYLE);
					if (ObjectUtils.isNotNullOrUndefined(task.endDate) && ObjectUtils.isNotNullOrUndefined(task.finalizedDate)) {
						let finalizedDate: any = task.finalizedDate;
						if (ObjectUtils.isString(task.finalizedDate)) {
							finalizedDate = new Date(task.finalizedDate);
						}
						let taskFinalizedDateWithoutTime: Date = DateUtils.removeTimeFromDate(finalizedDate);
						if (taskEndDateWithoutTime < taskFinalizedDateWithoutTime) {
							task.style = Object.assign({}, ProjectTaskSelectorComponent.TASK_STATUS_FINALIZAT_AND_END_DATE_SMALLER_THAN_FINALIZED_DATE_STYLE);
						}
					}
				} else if (	(task.status === TaskStatus.IN_PROGRESS) 
							&& ObjectUtils.isNotNullOrUndefined(task.endDate)
							&& (taskEndDateWithoutTime < curentDateWithoutTime)	) {
					task.style = Object.assign({}, ProjectTaskSelectorComponent.TASK_STATUS_IN_PROGRESS_OVER_DEADLINE_STYLE);
				}
				if (ObjectUtils.isNullOrUndefined(task.style)) {
					task.style = {};
				}
				UiUtils.appendTableCellCollapseStyle(task.style);
			}  else if (task.status === TaskStatus.FINALIZED && task.permanent) {
				task.style = Object.assign({},ProjectTaskSelectorComponent.TASK_STATUS_FINALIZAT_STYLE);
			}
		});
	}

	private getPageRequest(lazyLoadEvent: any): PageRequest<GetPagedProjectTaskViewModelsRequestModel> {
		let pageRequest: PageRequest<GetPagedProjectTaskViewModelsRequestModel> = new PageRequest<GetPagedProjectTaskViewModelsRequestModel>();
		pageRequest.limit = lazyLoadEvent.rows;
		pageRequest.offset = lazyLoadEvent.first;

		let payload: GetPagedProjectTaskViewModelsRequestModel = new GetPagedProjectTaskViewModelsRequestModel();
		payload.projectId = this.projectId;
		payload.filters = this.getTaskFilters(lazyLoadEvent.filters);
		payload.sortedAttribute = this.buildSortedTaskAttribute(lazyLoadEvent.sortField, lazyLoadEvent.sortOrder);
		pageRequest.payload = payload;

		return pageRequest;
	}

	private getTaskFilters(filters: any): TaskFilterModel[] {

		let taskFilters: TaskFilterModel[] = [];
		
		if (ObjectUtils.isNullOrUndefined(filters)) {
			return taskFilters;
		}
		Object.entries(filters).forEach((filter: any) => {			
			let filterKey: string = filter[0];
			let filterValue: any;
			if (filterKey === TaskFilterApplicability.SUBACTIVITY){
				filterValue = filter[1].value.name;
			}else {
				filterValue = filter[1].value;
			}
			let filterMatchMode: any = filter[1].matchMode;
			let taskFilter: TaskFilterModel = new TaskFilterModel();

			if (filterKey === ProjectTaskSelectorComponent.COLUMN_TASK_ASSIGNEES) {
				taskFilter.aplicability = TaskFilterApplicability.ASSIGNMENTS;
				taskFilter.propertyName = ProjectTaskSelectorComponent.TASK_ASSIGNMENTS_PROPERTY_NAME;
			}else if (filterKey === ProjectTaskSelectorComponent.COLUMN_TASK_SUBPROJECT){
				taskFilter.aplicability = TaskFilterApplicability.SUBACTIVITY;
				taskFilter.propertyName = ProjectTaskSelectorComponent.TASK_SUBACTIVITY_NAME_PROPERTY_NAME;
			} else {
				taskFilter.aplicability = TaskFilterApplicability.TASK;
				taskFilter.propertyName = filterKey;
			}
			
			taskFilter.valueType = this.getTaskFilterValueTypeByColumnName(filterKey);
			
			if (taskFilter.valueType === TaskFilterValueType.DATE) {
				let valueAsDate = new Date(filterValue);
				let formattedDate: string = DateUtils.formatForStorage(valueAsDate);
				taskFilter.values = [formattedDate];
			} else {
				taskFilter.values = [filterValue];
			}

			taskFilter.matchMode = filterMatchMode;
			taskFilters.push(taskFilter);
		});
		
		return taskFilters;
	}

	private getTaskFilterValueTypeByColumnName(columnName: string): TaskFilterValueType {
		switch (columnName) {
			case ProjectTaskSelectorComponent.COLUMN_TASK_NAME:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_DESCRIPTION:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_ASSIGNEES:
				return TaskFilterValueType.INTEGER;
			case ProjectTaskSelectorComponent.COLUMN_TASK_PRIORITY:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_START_DATE:
				return TaskFilterValueType.DATE;
			case ProjectTaskSelectorComponent.COLUMN_TASK_END_DATE:
				return TaskFilterValueType.DATE;
			case ProjectTaskSelectorComponent.COLUMN_TASK_PROJECT_STATUS:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_PARTICIPATIONS_TO:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_EXPLICATIONS:
				return TaskFilterValueType.STRING;
			case ProjectTaskSelectorComponent.COLUMN_TASK_SUBPROJECT:
				return TaskFilterValueType.STRING;
			default:
				throw Error("Column with name [" + columnName + "] does not exists.");
		}
	}

	private buildSortedTaskAttribute(sortField: string, sortOrder: number): SortedTaskAttributeModel {
		if (ObjectUtils.isNullOrUndefined(sortField)) {
			return null;
		}

		let sortedAttribute: SortedTaskAttributeModel = new SortedTaskAttributeModel();
		
		sortedAttribute.propertyName = sortField;

		if (sortOrder === 1) {
			sortedAttribute.order = SortedTaskAttributeOrderDirection.ASC;
		} else if (sortOrder === -1) {
			sortedAttribute.order = SortedTaskAttributeOrderDirection.DESC;
		}
		return sortedAttribute;
	}

	public onTaskSelected(event: any): void {
		this.selectionChanged.emit(this.selectedTask.id);
		let selectedRowId = event.data.id;
		this.page.items.forEach(element => {
			if (element.id === selectedRowId) {
				UiUtils.appendTableCellExpandStyle(element.style);
				if (element.style["background-color"] === "red"){
					element.style["background-color"] = "darkmagenta";
				}
				
				if (element.style["background-color"] === "orange"){
					element.style["background-color"] = "darkorange";
				}
				if (element.style["background-color"] === "green"){
					element.style["background-color"] = "darkgreen";
				}
			} else {
				UiUtils.appendTableCellCollapseStyle(element.style);
				if (element.style["background-color"] === "darkmagenta"){
					element.style["background-color"] = "red";
				}
				if (element.style["background-color"] === "darkorange"){
					element.style["background-color"] = "orange";
				}
				if (element.style["background-color"] === "darkgreen"){
					element.style["background-color"] = "green";
				}
			}
		});
	}

	public onTaskUnselected(event: any): void {
		this.selectionChanged.emit(null);
		UiUtils.appendTableCellCollapseStyle(event.data.style);
		if (event.data.style["background-color"] === "darkmagenta"){
			event.data.style["background-color"] = "red";
		}
		if (event.data.style["background-color"] === "darkorange"){
			event.data.style["background-color"] = "orange";
		}
		if (event.data.style["background-color"] === "darkgreen"){
			event.data.style["background-color"] = "green";
		}
	}

	private showLoading(): void {
		this.loading = true;
	}

	private hideLoading(): void {
		this.loading = false;
	}

}
