import { Component, OnChanges, Input, Output, EventEmitter } from "@angular/core";
import { ProjectService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { UserModel, AppError } from "../../model";
import { OrganizationEntityModel } from "../../model/organization-entity.model";
import { ObjectUtils } from "../../utils";

@Component({
	selector: "app-task-assignments-selector",
	templateUrl: "./task-assignments-selector.component.html"
})
export class TaskAssignmentsSelectorComponent implements OnChanges {

	@Input()
	public projectId: number;

	@Output()
	public selectionChanged: EventEmitter<OrganizationEntityModel[]>;

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;

	public users: UserModel[];
	public selectedUsers: UserModel[];

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<OrganizationEntityModel[]>();
	}

	private reset(): void {
		this.selectedUsers = [];
		this.loadUsers();
	}

	private loadUsers(): void {
		if (ObjectUtils.isNullOrUndefined(this.projectId)) {
			return;
		}
		this.projectService.getProjectParticipants(this.projectId, {
			onSuccess: (users: UserModel[]): void => {
				this.users = users;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public ngOnChanges(): void {
		this.reset();
	}

	public onUsersSelected(users: UserModel[]): void {
		this.selectionChanged.emit(this.getOrganizationEntitiesFromUserModels(users));
	}

	private getOrganizationEntitiesFromUserModels(users: UserModel[]): OrganizationEntityModel[] {
		let organizationEntities: OrganizationEntityModel[] = [];
		users.forEach((user: UserModel) => {
			organizationEntities.push(this.getOrganizationEntityFromUserModel(user));
		});
		return organizationEntities;
	}

	private getOrganizationEntityFromUserModel(user: UserModel): OrganizationEntityModel {
		let organizationEntity: OrganizationEntityModel = new OrganizationEntityModel();
		organizationEntity.id = Number(user.userId);
		organizationEntity.type = OrganizationEntityModel.TYPE_USER;
		return organizationEntity;
	}
}
