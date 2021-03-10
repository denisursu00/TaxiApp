import { Component } from "@angular/core";
import { GroupModel, GroupService, MessageDisplayer, AppError, OrganizationService, ConfirmationUtils, ObjectUtils } from "@app/shared";

@Component({
	selector: "app-groups",
	templateUrl: "./groups.component.html",
	styleUrls: ["./groups.component.css"]
})
export class GroupsComponent {

	public groups: GroupModel[];
	public selectedGroup: GroupModel;
	
	public loading: boolean;
	
	private groupService: GroupService;
	private messageDisplayer: MessageDisplayer;
	private organizationService: OrganizationService;
	private confirmationUtils: ConfirmationUtils;

	public groupWindowVisible: boolean = false;
	public groupId: string;
	public mode: string;

	public scrollHeight: string;

	public constructor(groupService: GroupService, messageDisplayer: MessageDisplayer, organizationService: OrganizationService, 
			confirmationUtils: ConfirmationUtils) {
		this.groupService = groupService;
		this.messageDisplayer = messageDisplayer;
		this.organizationService = organizationService;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.reset();
		this.loadGroups();
	}

	private reset(): void {
		this.groups = [];
		this.selectedGroup = null;
	}

	private loadGroups(): void {
		this.lock();
		this.groupService.getGroups({
			onSuccess: (groups: GroupModel[]) => {
				this.groups = groups;
				this.unlock();
			},
			onFailure: (appError: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onAdd(event: any): void {
		this.mode = "add";
		this.groupWindowVisible = true;
	}

	public onEdit(event: any): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedGroup)) {
			this.editGroup();
		}
	}

	private editGroup(): void {
		this.mode = "edit";
		this.groupId = this.selectedGroup.id;
		this.groupWindowVisible = true;
	}

	public onDelete(event: any): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedGroup)) {
			this.confirmationUtils.confirm("CONFIRM_DELETE_GROUP", {
				approve: (): void => {
					this.deleteGroup();
				},
				reject: (): void => {
					this.refresh();
				}
			});
		}
	}

	private deleteGroup(): void {
		this.organizationService.deleteGroup(this.selectedGroup, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("GROUP_DELETED");
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	private refresh(): void {
		this.reset();
		this.loadGroups();
	}

	public onGroupWindowClosed(event: any): void {
		this.onWindowClosed();
		this.groupWindowVisible = false;
	}

	private onWindowClosed(): void {
		this.refresh();
	}
}