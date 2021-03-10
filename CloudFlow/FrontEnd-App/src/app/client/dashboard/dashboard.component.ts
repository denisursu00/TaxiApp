import { Component } from "@angular/core";
import { ProjectService, MessageDisplayer, ProjectModel, AppError, UiUtils } from "@app/shared";

@Component({
	selector: "app-dashboard",
	templateUrl: "./dashboard.component.html"
})
export class DashboardComponent {

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;

	public projects: ProjectModel[];

	public calendarAspectRatio: number;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;

		this.init();
	}

	private init(): void {
		this.loadProjects();
		this.calendarAspectRatio = UiUtils.getAspectRation() * 1.8;
	}

	private loadProjects(): void {
		this.projectService.getUserProjects({
			onSuccess: (projects: ProjectModel[]): void => {
				this.projects = projects;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
}
