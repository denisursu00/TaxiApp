import { Component, OnInit } from "@angular/core";
import { ProjectTabContent } from "../project-tab-content";
import { ProjectEstimationModel } from "@app/shared/model/project/project-estimation.model";
import { ProjectModel, ObjectUtils, FormUtils, DateUtils } from "@app/shared";

@Component({
	selector: "app-project-estimations-tab-content",
	templateUrl: "./project-estimations-tab-content.component.html"
})
export class ProjectEstimationsTabContentComponent extends ProjectTabContent {

	public estimations: ProjectEstimationModel[];

	public projectEstimationWindowVisible: boolean = false;
	public projectEstimationMinDate: Date;

	public scrollHeight: string;

	public constructor() {
		super();
		this.scrollHeight = (window.innerHeight - 300) + "px";
	}

	public doWhenOnInit(): void {
	}

	private setProjectEstimationMinDate(): void {
		this.projectEstimationMinDate = new Date();
		this.estimations.forEach((estimation: ProjectEstimationModel) => {
			if (this.projectEstimationMinDate < estimation.startDate) {
				this.projectEstimationMinDate = DateUtils.addDaysToDate(estimation.startDate, 1);
			}
		});
	}

	public prepareForAdd(): void {
		this.estimations = [];
	}
	
	public prepareForViewOrEdit(): void {
		this.estimations = [...this.project.estimations];
		this.setProjectEstimationMinDate();
	}

	public reset(): void {
	}

	public populateForSave(project: ProjectModel): void {
		project.estimations = this.estimations;
	}

	public isValid(): boolean {
		return true;
	}

	public onAddEstimation(): void {
		this.projectEstimationWindowVisible = true;
	}

	public onProjectEstimationWindowClosed(): void {
		this.projectEstimationWindowVisible = false;
	}

	public onProjectEstimationDataSaved(projectEstimationModel: ProjectEstimationModel): void {
		this.estimations.forEach((estimation: ProjectEstimationModel): void => {
			if (ObjectUtils.isNullOrUndefined(estimation.endDate)) {
				estimation.endDate =projectEstimationModel.startDate;
			}
		});
		this.estimations.push(projectEstimationModel);
		this.setProjectEstimationMinDate();
	}
}
