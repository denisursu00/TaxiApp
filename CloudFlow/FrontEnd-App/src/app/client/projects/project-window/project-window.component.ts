import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { UiUtils, MessageDisplayer, Message, ProjectModel, ProjectService, AppError, ConfirmationUtils, ProjectStatus, ObjectUtils, ProjectType, ProjectViewModel, ProjectComisieSauGlViewModel, BooleanUtils, TranslateUtils, BaseWindow } from "@app/shared";
import { ProjectGeneralTabContentComponent } from "./project-general-tab-content/project-general-tab-content.component";
import { ProjectParticipantsTabContentComponent } from "./project-participants-tab-content/project-participants-tab-content.component";
import { ProjectEstimationsTabContentComponent } from "./project-estimations-tab-content/project-estimations-tab-content.component";
import { ProjectComisiiSauGlTabContentComponent } from "./project-comisii-sau-gl-tab-content/project-comisii-sau-gl-tab-content.component";
import { ProjectSubactivitiesTabContentComponent } from "./project-subactivities-tab-content/project-subactivities-tab-content.component";

@Component({
	selector: "app-project-window",
	templateUrl: "./project-window.component.html"
})
export class ProjectWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public mode: "add" | "viewOrEdit";

	@Input()
	public projectId: number;
	
	@Output()
	public windowClosed: EventEmitter<void>;

	@ViewChild(ProjectGeneralTabContentComponent)
	private generalTab: ProjectGeneralTabContentComponent;

	@ViewChild(ProjectParticipantsTabContentComponent)
	private participantsTab: ProjectParticipantsTabContentComponent;

	@ViewChild(ProjectEstimationsTabContentComponent)
	private projectEstimationsTab: ProjectEstimationsTabContentComponent;

	@ViewChild(ProjectComisiiSauGlTabContentComponent)
	private comisiiSauGlTab: ProjectComisiiSauGlTabContentComponent;

	@ViewChild(ProjectSubactivitiesTabContentComponent)
	private subactivitiesTab: ProjectSubactivitiesTabContentComponent;

	private projectViewModel: ProjectViewModel;

	public projectService: ProjectService;
	public messageDisplayer: MessageDisplayer;
	public confirmationUtils: ConfirmationUtils;
	private translateUtils: TranslateUtils;
	
	public width: number;
	public height: string;

	public windowVisible: boolean = true;
	public tabContentVisible: boolean = false;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public projectEstimationsTabDisabled: boolean;

	public dspViewWindowVisible: boolean;
	public dspViewWindowProjectId: number;
	public tasksViewWindowVisible: boolean;
	public tasksViewWindowProjectId: number;

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		super();
		this.windowClosed = new EventEmitter<void>();
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.translateUtils = translateUtils;
		this.init();
	}

	public init(): void {
	}

	public ngOnInit(): void {
		this.projectEstimationsTabDisabled = false;
		if (this.mode === "add") {
			this.projectEstimationsTabDisabled = true;
			this.tabContentVisible = true;
			this.unlock();
		} else if (this.mode === "viewOrEdit") {
			this.loadProjectViewModel();
		}
	}

	private loadProjectViewModel(): void {
		this.lock();
		this.projectService.getProjectViewModelById(this.projectId, {
			onSuccess: (projectViewModel: ProjectViewModel): void => {
				this.projectViewModel = projectViewModel;
				this.tabContentVisible = true;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onSaveAction(): void {
		if (!this.areTabsValid()) {
			return;
		}
		let project: ProjectModel = new ProjectModel();
		this.generalTab.populateForSave(project);
		this.participantsTab.populateForSave(project);
		this.projectEstimationsTab.populateForSave(project);
		this.comisiiSauGlTab.populateForSave(project);
		this.subactivitiesTab.populateForSave(project);

		if (ObjectUtils.isNotNullOrUndefined(this.projectViewModel)) {
			project.status = this.projectViewModel.status; 
		} else {
			project.status = ProjectStatus.INITIATED; 
		}

		if (this.mode === "viewOrEdit") {
			project.id = this.projectViewModel.id;
			project.type = this.projectViewModel.type;
			project.documentId = this.projectViewModel.documentId;
			project.documentLocationRealName = this.projectViewModel.documentLocationRealName;
		} else if (this.mode === "add") {
			project.type = ProjectType.SIMPLE;
		}

		this.lock();

		this.projectService.existsName(project.name, {
			onSuccess: (exists: boolean): void => {

				if((this.mode === "add" && BooleanUtils.isTrue(exists)) || (this.mode !== "add" && BooleanUtils.isTrue(exists) && project.name !== this.projectViewModel.name)) {
					this.unlock();
					this.messageDisplayer.displayError("PROJECT_WITH_SAME_NAME_EXISTS");
				} else {

					this.projectService.existsAbbreviation(project.projectAbbreviation, {
						onSuccess: (exists: boolean ): void => {
							if ( (this.mode === "add" && BooleanUtils.isTrue(exists)) || (this.mode !== "add" && BooleanUtils.isTrue(exists) && project.projectAbbreviation !== this.projectViewModel.projectAbbreviation)) {
								this.unlock();
								this.messageDisplayer.displayError("PROJECT_WITH_SAME_ABBREVIATION_EXISTS");
							} else {
								
								this.projectService.saveProject(project, {
									onSuccess: (): void => {
										this.unlock();
										this.messageDisplayer.displaySuccess("PROJECT_SAVED");
										this.windowClosed.emit();
									},
									onFailure: (appError: AppError): void => {
										this.unlock();
										this.messageDisplayer.displayAppError(appError);
									}
								});
							}
							
						},
						onFailure: (appError: AppError): void => {
							this.unlock();
							this.messageDisplayer.displayAppError(appError);
						}
					});
				}

			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onCloseProjectAction(): void {
		this.confirmationUtils.confirm("CONFIRM_CLOSE_PROJECT", {
			approve: (): void => {
				this.lock();
				this.projectService.closeProject(this.projectViewModel.id, {
					onSuccess: (): void => {
						this.unlock();
						this.messageDisplayer.displaySuccess("PROJECT_CLOSED");
					},
					onFailure: (appError: AppError): void => {
						this.unlock();
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public onCancelAction(): void {
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public isProjectClosed(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.projectViewModel)) {
			return false;
		}
		return this.projectViewModel.status === ProjectStatus.CLOSED;
	}

	public areTabsValid(): boolean {
		if (!this.participantsTab.isValid()) {
			this.showErrorMessages();
		}
		return this.generalTab.isValid() && this.participantsTab.isValid() && this.projectEstimationsTab.isValid() && this.comisiiSauGlTab.isValid();
	}

	private showErrorMessages(): void {
		this.messagesWindowMessages = [{
			type: "error",
			code: this.translateUtils.translateMessage("PROJECT_USERS_RESPONSIBLE_NOT_ASSIGNED")
			}
		];
		this.messagesWindowVisible = true;
	}

	public onMessagesWindowClosed(): void {
		this.messagesWindowMessages = [];
		this.messagesWindowVisible = false;
	}

	public get viewDspVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.projectViewModel) 
			&& ("DSP" === this.projectViewModel.type);
	}

	public get viewTasksVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.projectViewModel) 
			&& ("DSP" !== this.projectViewModel.type);
	}

	public onDspViewAction(): void {
		this.dspViewWindowProjectId = this.projectId;
		this.dspViewWindowVisible = true;
	}

	public onDspViewWindowClosed() {
		this.dspViewWindowVisible = false;
	}

	public onTasksViewAction(): void {
		this.tasksViewWindowProjectId = this.projectId;
		this.tasksViewWindowVisible = true;
	}

	public onTasksViewWindowClosed() {
		this.tasksViewWindowVisible = false;
	}	
}
