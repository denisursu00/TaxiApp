import { Component, Input } from "@angular/core";
import { TaskTabContent } from "../task-tab-content";
import { TaskModel, ArrayUtils } from "@app/shared";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";

@Component({
	selector: "app-task-assignment-tab-content",
	templateUrl: "./task-assignment-tab-content.component.html"
})
export class TaskAssignmentTabContentComponent extends TaskTabContent {

	@Input()
	public projectId: number;

	public users: OrganizationEntityModel[];

	public constructor() {
		super();
		this.init();
	}

	private init(): void {
		this.users = [];
	}

	public reset(): void {
	}
	
	public populateForSave(task: TaskModel): void {
		task.taskAssignments = this.users;
	}

	public isValid(): boolean {
		return ArrayUtils.isNotEmpty(this.users);
	}

	public onUsersSelectionChanged(entities: OrganizationEntityModel[]): void {
		this.users = entities;
	}
}
