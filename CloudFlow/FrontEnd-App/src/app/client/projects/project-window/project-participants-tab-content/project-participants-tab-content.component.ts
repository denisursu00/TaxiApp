import { Component, OnInit, Input } from "@angular/core";
import { ProjectTabContent } from "../project-tab-content";
import { ProjectModel, ArrayUtils } from "@app/shared";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";

@Component({
	selector: "app-project-participants-tab-content",
	templateUrl: "./project-participants-tab-content.component.html"
})
export class ProjectParticipantsTabContentComponent extends ProjectTabContent {


	public participants: OrganizationEntityModel[];

	public visibleForAllUsers: boolean = false;
	
	public constructor() {
		super();
	}

	public participantsSelected(entities: OrganizationEntityModel[]): void {
		this.participants = entities;
	}

	public doWhenOnInit(): void {
	}

	public prepareForAdd(): void {
		this.participants = [];
	}

	public prepareForViewOrEdit(): void {
		this.participants = this.project.participants;
	}

	public reset(): void {
		this.participants = null;
	}

	public isValid(): boolean {
		return ArrayUtils.isNotEmpty(this.participants);
	}

	public populateForSave(project: ProjectModel): void {
		project.participants = this.participants;
	}
	
}
