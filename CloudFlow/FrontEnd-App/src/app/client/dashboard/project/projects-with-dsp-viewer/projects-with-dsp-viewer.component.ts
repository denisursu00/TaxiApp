import { Component, OnInit } from "@angular/core";
import { ProjectWithDspViewModel, ProjectService, AppError, MessageDisplayer, NomenclatorService, NomenclatorConstants, ObjectUtils, NomenclatorValueModel, ProjectDegreeOfAchievement } from "@app/shared";
import { SelectItem } from "primeng/api";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-projects-with-dsp-viewer",
	templateUrl: "./projects-with-dsp-viewer.component.html"
})
export class ProjectsWithDspViewerComponent implements OnInit {
	
	private projectService: ProjectService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public loading: boolean = false;

	public projects: ProjectWithDspViewModel[];
	public showAllProjects: boolean;

	public finalizedTasksWindowVisible: boolean = false;
	public inProgressTasksWindowVisible: boolean = false;

	public projectId: number;

	public dspViewWindowProjectId: number;
	public dspViewWindowVisible: boolean = false;

	public importanceDegreeColorSelectItems: SelectItem[];
	public degreeOfAchievementSelectItems: SelectItem[];
	
	public constructor(projectService: ProjectService, nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	private init(): void {
		this.showAllProjects = false;
		this.loadProjects(this.showAllProjects);
		this.prepareDegreeOfAchievementSelectItems();
	}

	private prepareImportanceDegree(projects: ProjectWithDspViewModel[]): void {
		this.importanceDegreeColorSelectItems = [
			{label: "", value: ""}
		];
		this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE, {
            onSuccess: (importantaProiecte: NomenclatorValueModel[]): void => {
                importantaProiecte.forEach(importanta => {
                    let culoare: string = importanta[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_CULOARE];
                    let valoare: string = importanta[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_VALOARE];
			  
					if (!this.importanceDegreeColorSelectItems.find(item => item['value'] == culoare)) {
						this.importanceDegreeColorSelectItems.push({ label: "", value: culoare });
					}
					
					projects.forEach(proiect => {
						if (proiect.importanceDegreeColor == culoare) {
							proiect.importanceDegreeColorForSort = Number(valoare);
						}
					});
                });
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
	}

	private prepareDegreeOfAchievementSelectItems(): void {
		this.degreeOfAchievementSelectItems = [
			{label: "", value: ""},
			{label: "nesatisfacator", value: ProjectDegreeOfAchievement.NESATISFACATOR},
			{label: "satisfacator", value: ProjectDegreeOfAchievement.SATISFACATOR},
			{label: "bine", value: ProjectDegreeOfAchievement.BINE},
			{label: "foarte bine", value: ProjectDegreeOfAchievement.FOARTE_BINE}
		];

		ListItemUtils.sortByLabel(this.degreeOfAchievementSelectItems);
	}

	private loadProjects(showAllProjects: boolean): void {
		this.showLoading();
		this.projectService.getProjectWithDspViewModels(showAllProjects, {
			onSuccess: (projects: ProjectWithDspViewModel[]): void => {
				this.projects = projects;
				this.hideLoading();
				this.prepareImportanceDegree(projects);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.hideLoading();
			}
		});
	}

	public ngOnInit(): void {
	}

	public onShowAllProjectChanged(event: any): void {
		this.loadProjects(this.showAllProjects);
	}

	private showLoading(): void {
		this.loading = true;
	}

	private hideLoading(): void {
		this.loading = false;
	}

	public onShowFinalizedTasksWindow(projectId: number): void {
		this.projectId = projectId;
		this.finalizedTasksWindowVisible = true;
	}

	public onShowInProgressTasksWindow(projectId: number): void {
		this.projectId = projectId;
		this.inProgressTasksWindowVisible = true;
	}

	public onShowDspWindow(projectId: number): void {
		this.dspViewWindowProjectId = projectId;
		this.dspViewWindowVisible = true;
	}

	public onFinalizedTasksWindowClosed(): void {
		this.projectId = null;
		this.finalizedTasksWindowVisible = false;
	}

	public onInProgressTasksWindowClosed(): void {
		this.projectId = null;
		this.inProgressTasksWindowVisible = false;
	}

	public onDspViewWindowClosed(): void {
		this.dspViewWindowProjectId = null;
		this.dspViewWindowVisible = false;
	}
}
