import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { ProjectService, ProjectModel, BaseWindow } from "@app/shared";
import { PageConstants } from "@app/shared";
import { ObjectUtils, ArrayUtils, TranslateUtils, StringUtils, MessageDisplayer } from "@app/shared";
import { AppError } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-project-selection-window",
	templateUrl: "./project-selection-window.component.html"
})
export class ProjectSelectionWindowComponent extends BaseWindow implements OnInit {
	
	@Input()
	public selectionMode: "single" | "multiple";

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public valuesSelected: EventEmitter<number | number[]>;

	public windowVisible: boolean = true;
	public dataLoadingVisible: boolean = false;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private projectService: ProjectService;

	public height: number | string;
	public width: number | string;
	public pageSize: number;

	public okActionEnabled: boolean;

	public projects: ProjectModel[];
	public selectedProjects: any; // ProjectModel or ProjectModel[];

	public dspViewWindowVisible: boolean;
	public dspViewWindowProjectId: number;

	public constructor(projectService: ProjectService,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils,
			formBuilder: FormBuilder) {
		super();
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter();
		this.valuesSelected = new EventEmitter();
		this.init();
	}

	private init(): void {
		this.selectionMode = "single";
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.okActionEnabled = false;
		this.adjustSize();
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 40;
		this.height = "auto";
	}

	public ngOnInit(): void {
		this.loadProjects();
	}

	private loadProjects(): void {
		this.dataLoadingVisible = true;
		this.projectService.getAllOpenedProjectsWithDsp({
			onSuccess: (rProjects: ProjectModel[]): void => {
				this.projects = rProjects;
				this.dataLoadingVisible = false;
			},
			onFailure: (error: AppError): void => {
				this.dataLoadingVisible = false;
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private isSingleSelection(): boolean {
		return this.selectionMode === "single";
	}

	private isMultipleSelection(): boolean {
		return this.selectionMode === "multiple";
	}

	private updatePerspective(): void {
		this.okActionEnabled = false;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedProjects)) {
			if (ObjectUtils.isArray(this.selectedProjects)) {
				this.okActionEnabled = ArrayUtils.isNotEmpty(this.selectedProjects);
			} else {
				this.okActionEnabled = true;
			}
		}		
	}

	public onOKAction(): void {

		if (ObjectUtils.isNullOrUndefined(this.selectedProjects)) {		
			return;
		}

		let selectedProjectIds: number[] = [];
		if (ObjectUtils.isNotNullOrUndefined(this.selectedProjects)) {		
			if (ObjectUtils.isArray(this.selectedProjects)) {
				if (ArrayUtils.isNotEmpty(this.selectedProjects)) {
					this.selectedProjects.forEach((selectedProject: ProjectModel) => {
						selectedProjectIds.push(selectedProject.id);
					});
				}
			} else {
				let selectedProject: ProjectModel = <ProjectModel> this.selectedProjects;
				selectedProjectIds.push(selectedProject.id);
			}
		}

		if (this.isSingleSelection()) {
			if (ArrayUtils.isNotEmpty(selectedProjectIds)) {
				this.valuesSelected.emit(selectedProjectIds[0]);
			}
		} else if (this.isMultipleSelection()) {
			this.valuesSelected.emit(selectedProjectIds);
		} else {
			throw new Error("wrong selection");
		}
	}

	public onCloseAction(): void {
		this.close();
	}

	public onHide(event: any): void {
		this.close();
	}

	private close(): void {
		this.windowClosed.emit();
	}

	public onProjectSelected(event: any) {
		this.updatePerspective();
	}

	public onProjectUnselected(event: any) {
		this.updatePerspective();
	}

	public onViewProjectAction(project: ProjectModel): void {
		if (ObjectUtils.isNullOrUndefined(project)) {
			return;
		}
		this.dspViewWindowProjectId = project.id;
		this.dspViewWindowVisible= true;
	}

	public onDspViewWindowClosed(event: any): void {
		this.dspViewWindowVisible= false;
	} 
}