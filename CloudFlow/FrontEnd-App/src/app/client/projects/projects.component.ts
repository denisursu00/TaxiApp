import { Component, OnInit, Input } from "@angular/core";
import { ProjectModel, MessageDisplayer, ProjectService, AclService, SecurityManagerModel, AppError, ObjectUtils, UiUtils } from "@app/shared";
import { forEach } from "@angular/router/src/utils/collection";
import { Column } from "primeng/primeng";

@Component({
	selector: "app-projects",
	templateUrl: "./projects.component.html",
})
export class ProjectsComponent {

	private static readonly COLUMN_PROJECT_ID: string = "id";
	private static readonly COLUMN_PROJECT_NAME: string = "name";
	private static readonly COLUMN_PROJECT_DESCRIPTION: string = "description";
	private static readonly COLUMN_PROJECT_STATUS: string = "projectStatusLabel";
	private static readonly COLUMN_DATA: string = "data";
	private static readonly COLUMN_STYLE: string = "style";

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	private aclService: AclService;

	public projects: any[];
	public selectedProject: any;

	public columns: Column[];

	public projectWindowVisible: boolean;
	public projectWindowMode: "add" | "viewOrEdit";
	public projectId: ProjectModel;

	public scrollHeight: string;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer,
		aclService: AclService) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.aclService = aclService;
		this.init();
	}

	private init(): void {
		this.scrollHeight = (window.innerHeight - 300) + "px";
		
		this.projectWindowVisible = false;
		this.prepareTableColumns();
		this.getUserProjects();
	}

	private getUserProjects(): void {

		this.projectService.getAllProjects({
			onSuccess: (projects: ProjectModel[]): void => {
				this.prepareProjects(projects);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareProjects(projects: ProjectModel[]): void {
		this.projects = [];
		projects.forEach((projectModel: ProjectModel) => {
			let project: any = {};
			project[ProjectsComponent.COLUMN_PROJECT_ID] = projectModel.id;
			project[ProjectsComponent.COLUMN_PROJECT_NAME] = projectModel.name;
			project[ProjectsComponent.COLUMN_PROJECT_DESCRIPTION] = projectModel.description;
			project[ProjectsComponent.COLUMN_PROJECT_STATUS] = projectModel.projectStatusLabel;
			project[ProjectsComponent.COLUMN_DATA] = projectModel;
			project[ProjectsComponent.COLUMN_STYLE] = {};
			UiUtils.appendTableCellCollapseStyle(project[ProjectsComponent.COLUMN_STYLE]);
			this.projects.push(project);
		});
	}

	private prepareTableColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn(ProjectsComponent.COLUMN_PROJECT_NAME, "LABELS.PROJECT_NAME"));
		this.columns.push(this.buildColumn(ProjectsComponent.COLUMN_PROJECT_DESCRIPTION, "LABELS.PROJECT_DESCRIPTION"));
		this.columns.push(this.buildColumn(ProjectsComponent.COLUMN_PROJECT_STATUS, "LABELS.STATUS"));
	}

	private buildColumn(field: string, header: string): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		return column;
	}

	public isProjectStatusColumn(column: Column): boolean {
		return column.field === ProjectsComponent.COLUMN_PROJECT_STATUS;
	}

	public onAddProject(): void {
		this.projectWindowMode = "add";
		this.projectWindowVisible = true;
	}

	public onEditProject(): void {
		this.projectWindowMode = "viewOrEdit";
		this.projectId = this.selectedProject[ProjectsComponent.COLUMN_DATA].id;
		this.projectWindowVisible = true;
	}

	public onProjectWindowClosed(): void {
		this.selectedProject = null;
		this.getUserProjects();
		this.projectWindowVisible = false;
	}

	public isProjectSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.selectedProject);
	}

	public onRowSelect(event: any) {
		let selectedRowId = event.data.id;
		this.projects.forEach(element => {
			if (element.id === selectedRowId) {
				element[ProjectsComponent.COLUMN_STYLE] = {};
			} else {
				UiUtils.appendTableCellCollapseStyle(element[ProjectsComponent.COLUMN_STYLE]);
			}
		});
	}

	public onRowUnselect(event: any) {
		UiUtils.appendTableCellCollapseStyle(event.data[ProjectsComponent.COLUMN_STYLE]);
	}
}
