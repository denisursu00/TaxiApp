import { Component } from "@angular/core";
import { ProjectTabContent } from "../project-tab-content";
import { ProjectModel, ObjectUtils, StringValidators, ArrayUtils, ProjectService, RegistruIntrariIesiriService, AppError, MessageDisplayer } from "@app/shared";
import { ProjectSubactivityModel } from "@app/shared/model/project/project-subactivity.model";
import { FormControl, Validators } from "@angular/forms";


@Component({
	selector: "app-project-subactivities-tab-content",
	templateUrl: "./project-subactivities-tab-content.component.html"
})
export class ProjectSubactivitiesTabContentComponent extends ProjectTabContent{

	private projectService: ProjectService;
	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private messageDisplayer: MessageDisplayer;

	public subactivities: ProjectSubactivityModel[];
	public selectedSubactivity: ProjectSubactivityModel;
	
	public addButtonDisabled: boolean;
	public deleteButtonDisabled: boolean;
	public subactivityWindowVisible: boolean;
	public subactivityWindowMode: "edit" | "add";

	public subactivityName: FormControl;

	public constructor(projectService: ProjectService, registruIntrariIesiriService: RegistruIntrariIesiriService, messageDisplayer: MessageDisplayer){
		super();
		this.projectService = projectService;
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	public init(): void {
		this.addButtonDisabled = true;
		this.deleteButtonDisabled = true;
		this.subactivityWindowVisible = false;
		this.subactivities = [];
		this.subactivityName = new FormControl("", {validators: [Validators.required, Validators.minLength(1)], updateOn: "change"});
	}

	public prepareForAdd(): void {
		this.updatePerspective();
	}

	public prepareForViewOrEdit(): void {
		this.subactivities = this.project.subactivities;
		this.updatePerspective();
	}

	public doWhenOnInit(): void {
	}
	
	public isSubactivityUsedInAnyTask(subactivityId: number): Promise<boolean> {
		return new Promise<boolean>((resolve, reject) => {
			this.projectService.isSubactivityUsedInAnyTask(subactivityId, {
				onSuccess: (isUsed: boolean): void => {
					resolve(isUsed);
				}, onFailure: (error: AppError): void => {
					reject();
					this.messageDisplayer.displayAppError(error);
				}
			});
			
		});
	}
	public isSubactivityUsedInAnyRegisterEntry(subactivityId: number): Promise<boolean> {
		return new Promise<boolean>((resolve, reject) => {
			this.registruIntrariIesiriService.isSubactivityUsedInAnyRegisterEntry(subactivityId, {
				onSuccess: (isUsed: boolean): void => {
					resolve(isUsed);
				}, onFailure: (error: AppError): void => {
					reject();
					this.messageDisplayer.displayAppError(error);
				}
			});
		});
	}

	public reset(): void {
		throw new Error("Method not implemented.");
	}
	public populateForSave(project: ProjectModel): void {
		project.subactivities = this.subactivities;
	}
	public isValid(): boolean {
		throw new Error("Method not implemented.");
	}
	
	private updatePerspective(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedSubactivity)) {
			this.deleteButtonDisabled = false;
			this.addButtonDisabled = true;
		} else {
			this.deleteButtonDisabled = true;
			this.addButtonDisabled = false;
		}
	}

	public onAddSubactivity() {
		this.subactivityWindowMode = "add";
		this.subactivityWindowVisible = true;
	}

	public onDeleteSubactivity(){
		if (this.selectedSubactivity.id) {
			this.isSubactivityUsedInAnyTask(this.selectedSubactivity.id)
				.then((isUsedInTask) => isUsedInTask ? this.messageDisplayer.displayError("PROJECT_SUBACTIVITY_IS_REFERENCED_AND_CANT_BE_DELETED")
					: this.isSubactivityUsedInAnyRegisterEntry(this.selectedSubactivity.id)
						.then((isUsedInRegister) => {
							if (isUsedInRegister) {
								this.messageDisplayer.displayError("PROJECT_SUBACTIVITY_IS_REFERENCED_AND_CANT_BE_DELETED");
							} else {	
								ArrayUtils.removeElement(this.subactivities, this.selectedSubactivity); 
								this.selectedSubactivity = null;
							}
						})
					);
		}else{
			ArrayUtils.removeElement(this.subactivities, this.selectedSubactivity);
			this.selectedSubactivity = null;
		}
		this.updatePerspective();
	}

	public onEditSubactivity(){
		this.subactivityWindowMode = "edit";
		this.subactivityName.setValue(this.selectedSubactivity.name);
		this.subactivityWindowVisible = true;
	}

	public onSubactivitySelected(){
		this.updatePerspective();

	}

	public onSubactivityUnselected(){
		this.updatePerspective();
	}

	public onSubactivityWindowOk(event: any): void{
		this.subactivityName.markAsTouched();
		if (this.subactivityName.valid){
			if (this.subactivities.find(subactivity => subactivity.name === this.subactivityName.value)){
				this.messageDisplayer.displayError("PROJECT_SUBACTIVITY_ALREADY_EXISTS");
			}else{
				if (this.subactivityWindowMode === "add"){
						let newSubactivity: ProjectSubactivityModel = new ProjectSubactivityModel();
						newSubactivity.name = this.subactivityName.value;
						this.subactivities.push(newSubactivity);
				}else{
					this.selectedSubactivity.name = this.subactivityName.value;
				}
				this.subactivityWindowVisible = false;
				this.updatePerspective();
			}
		}
	}

	public onShowSubactivityWindow(){}

	public onHideSubactivityWindow(){
		this.subactivityWindowVisible = false;
		this.subactivityName.reset();
	}
	

}