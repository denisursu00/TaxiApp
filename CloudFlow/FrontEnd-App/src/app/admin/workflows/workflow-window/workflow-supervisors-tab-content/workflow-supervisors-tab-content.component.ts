import { Component, } from "@angular/core";
import { WorkflowTabContent } from "../workflow-tab-content";
import { WorkflowModel } from "@app/shared";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";

@Component({
	selector: "app-workflow-supervisors-tab-content",
	templateUrl: "./workflow-supervisors-tab-content.component.html",
	styleUrls: ["./workflow-supervisors-tab-content.component.css"]
})
export class WorkflowSupervisorsTabContentComponent extends WorkflowTabContent {
	
	public supervisors: OrganizationEntityModel[];

	public constructor() {
		super();
		this.init();
	}

	private init(): void {
		this.reset();
	}

	protected doWhenNgOnInit(): void {
	}

	public prepareForAdd(): void {
	}

	public prepareForEdit(): void {
		this.supervisors = this.inputData.supervisors;
	}

	public populateForSave(workflowModel: WorkflowModel): void {
		workflowModel.supervisors = this.supervisors;
	}

	public isValid(): boolean {
		return true;
	}
	
	public reset(): void {
		this.supervisors = [];
	}

	public onOrganizationalStructureEntitiesSelectionChanged(entities: OrganizationEntityModel[]): void {
		this.supervisors = entities;
	}
}
