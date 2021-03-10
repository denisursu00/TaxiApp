import { Component, ViewChild } from "@angular/core";
import { MenuItem } from "primeng/primeng";
import { TranslateUtils, OrganizationTreeNodeModel, OrganizationTreeNodeType, OrganizationSelectorComponent, DirectoryUserModel} from "@app/shared";
import { StringUtils, DirectoryUsersModel, OrganizationService, AppError, MessageDisplayer, ConfirmationUtils, OrganizationUnitModel, UserModel, ObjectUtils, OrganizationTreeModel, ArrayUtils } from "@app/shared";
import { ViewEncapsulation } from "@angular/core";
import { DeactivationUserModel } from "@app/shared/model/organization/deactivation-user.model";
import { OrganizationalStructureBusinessUtils } from "@app/shared/utils/organizational-structure-business-utils";

@Component({
	selector: "app-organizational-structure",
	templateUrl: "./organizational-structure.component.html",
	styleUrls: ["./organizational-structure.component.css"],
	encapsulation: ViewEncapsulation.None
})
export class OrganizationalStructureComponent {

	@ViewChild(OrganizationSelectorComponent)
	public organizationSelectorComponent: OrganizationSelectorComponent;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;

	private childrenOfEntityToMove: OrganizationTreeNodeModel[];
	private idOfOrganizationUnitToMoveTo: string;
	private idOfOrganizationUnitToMove: string;
	private idOfOrganizationToMoveTo: string;
	private idOfUserToMove: string;
	public moveItemWindowVisible: boolean = false;
	public selectedNodeModelToMoveTo: OrganizationTreeNodeModel;

	public selectedNodeModel: OrganizationTreeNodeModel;

	public addMenuItems: MenuItem[];
	public deleteMenuItems: MenuItem[];
	public toolsMenuItems: MenuItem[];

	public userWindowVisible: boolean = false;
	public organizationUnitWindowVisible: boolean = false;
	public directoryUserSearchWindowVisible: boolean = false;
	public usersFoundInDirectoryWindowVisible: boolean = false;
	public isMoveButtonDisabled: boolean = true;
	public isEditButtonDisabled: boolean = true;

	public deactivateAllUserAccounts: boolean = false;
	public deactivateUserWindowVisible: boolean = false;

	public userWindowMode: string;
	public organizationUnitWindowMode: string;
	public userId: string;
	public organizationId: string;
	public organizationUnitId: string;

	public selectedNodeModelNameParam: {};
	
	public usersFoundInDirectory: DirectoryUserModel[];
	public directoryUser: DirectoryUserModel;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;
		this.reset();
		this.init();
	}

	private reset(): void {
		this.selectedNodeModel = null;
		this.userId = null;
		this.organizationId = null;
		this.organizationUnitId = null;
	}

	private init(): void {
		
		this.addMenuItems = [
			this.createMenuItem("ORG_UNIT", "fa fa-users", this.onAddOrganizationUnit.bind(onclick, this), true, "ORGANIZATION_UNIT"),
			this.createMenuItem("USER", "fa fa-user", this.onAddUser.bind(onclick, this), true, "USER"),
			this.createMenuItem("USER_FROM_DIRECTORY", "fa fa-database", this.onAddUserFromLDAP.bind(onclick, this), true, "USER_FROM_DIRECTORY")
		];
		this.deleteMenuItems = [
			this.createMenuItem("ORG_UNIT", "fa fa-users", this.onDeleteOrganizationUnit.bind(onclick, this), true, "ORGANIZATION_UNIT"),
			this.createMenuItem("USER", "fa fa-user", this.onDeleteUser.bind(onclick, this), true, "USER")
		];
		this.toolsMenuItems = [
			this.createMenuItem("DEACTIVATE_USER", "fa fa-user", this.onDeactivateUser.bind(onclick, this), true, "DEACTIVATE_USER"),
			this.createMenuItem("REACTIVATE_USER", "fa fa-user", this.onReactivateUser.bind(onclick, this), true, "REACTIVATE_USER")
		];
	}

	private createMenuItem(label: string, icon: string, command: () => {}, disabled: boolean, id: string): MenuItem {
		return {
			label: this.translateUtils.translateLabel(label), 
			icon: icon, 
			command: command,
			disabled: disabled,
			id: id
		};
	}

	private onAddOrganizationUnit(thisComponent: OrganizationalStructureComponent): void {
		thisComponent.organizationUnitWindowMode = "add";
		thisComponent.organizationUnitWindowVisible = true;
	}

	private onAddUser(thisComponent: OrganizationalStructureComponent): void {
		thisComponent.userWindowMode = "add";
		thisComponent.userWindowVisible = true;
	}

	public onEdit(event: any): void {
		if (this.selectedNodeModel.isUser()) {
			this.onEditUser(this.selectedNodeModel);
		} else if (this.selectedNodeModel.isOrganizationUnit()) {
			this.onEditOrganizationUnit(this.selectedNodeModel);
		}
	}

	private onEditUser(nodeModel: OrganizationTreeNodeModel): void {
		this.userWindowMode = "edit";
		this.userId = nodeModel.id;
		this.userWindowVisible = true;
	}

	private onEditOrganizationUnit(nodeModel: OrganizationTreeNodeModel): void {
		this.organizationUnitWindowMode = "edit";
		this.organizationUnitId = nodeModel.id;
		this.organizationUnitWindowVisible = true;
	}

	private onAddUserFromLDAP(thisComponent: OrganizationalStructureComponent): void {
		if (thisComponent.selectedNodeModel.isOrganization()) {
			thisComponent.organizationId = thisComponent.selectedNodeModel.id;
		} else if (thisComponent.selectedNodeModel.isOrganizationUnit()) {
			thisComponent.organizationUnitId = thisComponent.selectedNodeModel.id;
		}
		thisComponent.directoryUserSearchWindowVisible = true;
	}

	private onDeleteOrganizationUnit(thisComponent: OrganizationalStructureComponent): void {
		let organizationUnitModel: OrganizationUnitModel = thisComponent.populateOrganizationUnitModel();
		thisComponent.confirmationUtils.confirm("CONFIRM_DELETE_ORG_UNIT", {
			approve: (): void => {
				thisComponent.deleteOrganizationUnit(organizationUnitModel);
			}, 
			reject: (): void => {
				thisComponent.refresh();
			}
		});
	}

	private populateOrganizationUnitModel(): OrganizationUnitModel {
		let organizationUnitModel: OrganizationUnitModel = new OrganizationUnitModel();
		organizationUnitModel.id = this.selectedNodeModel.id;
		organizationUnitModel.name = this.selectedNodeModel.name;
		organizationUnitModel.description = this.selectedNodeModel.description;
		organizationUnitModel.managerId = this.selectedNodeModel.managerId;
		return organizationUnitModel;
	}

	private deleteOrganizationUnit(organizationUnitModel: OrganizationUnitModel): void {
		this.organizationService.deleteOrgUnit(organizationUnitModel, {
			onSuccess: () => {
				this.refresh();
				this.messageDisplayer.displaySuccess("ORG_UNIT_DELETED");
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	private onDeleteUser(thisComponent: OrganizationalStructureComponent): void {
		let userModel: UserModel = thisComponent.populateUserModel();
		thisComponent.confirmationUtils.confirm("CONFIRM_DELETE_USER", {
			approve: (): void => {
				thisComponent.deleteUser(userModel);
			}, 
			reject: (): void => {
				thisComponent.refresh();
			}
		});
	}

	private populateUserModel(): UserModel {
		let userModel: UserModel = new UserModel();
		userModel.userId = this.selectedNodeModel.id;
		userModel.name = this.selectedNodeModel.name;
		userModel.title = this.selectedNodeModel.title;
		return userModel;
	}

	private deleteUser(userModel: UserModel): void {
		this.organizationService.deleteUser(userModel, {
			onSuccess: () => {
				this.refresh();
				this.messageDisplayer.displaySuccess("USER_DELETED");
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	public onMove(event: any): void {
		this.selectedNodeModelNameParam = {value: this.selectedNodeModel.name};
		this.moveItemWindowVisible = true;
	}

	public onOrganizationSelectionForMoveItemChanged(nodeModels: OrganizationTreeNodeModel[]): void {
		this.selectedNodeModelToMoveTo = nodeModels[0];
		if (this.selectedNodeModelToMoveTo.isOrganization()) {
			this.idOfOrganizationToMoveTo = this.selectedNodeModelToMoveTo.id;
			this.idOfOrganizationUnitToMoveTo = null;
		} else if (this.selectedNodeModelToMoveTo.isOrganizationUnit()) {
			this.idOfOrganizationUnitToMoveTo = this.selectedNodeModelToMoveTo.id;
			this.idOfOrganizationToMoveTo = null;
		}
	}

	private populateChildrenOfEntityToMove(nodeModel: OrganizationTreeNodeModel, childrenOfEntityToMove: OrganizationTreeNodeModel[]): void {
		if (ObjectUtils.isNotNullOrUndefined(nodeModel)) {
			if (ArrayUtils.isNotEmpty(nodeModel.children)) {
				for (let childOfEntityToMove of nodeModel.children) {
					this.childrenOfEntityToMove.push(childOfEntityToMove);
					this.populateChildrenOfEntityToMove(childOfEntityToMove, this.childrenOfEntityToMove);
				}
			}
		}
	}

	public onOkForMoveItemAction(event: any): void {	

		this.childrenOfEntityToMove = new Array<OrganizationTreeNodeModel>();
		this.populateChildrenOfEntityToMove(this.selectedNodeModel, this.childrenOfEntityToMove);
		if (OrganizationalStructureBusinessUtils.canMoveTo(this.selectedNodeModel, this.selectedNodeModelToMoveTo, this.childrenOfEntityToMove, this.selectedNodeModel.parentId)) {
			if (this.selectedNodeModel.isUser()) {
				this.idOfUserToMove = this.selectedNodeModel.id;
				if (StringUtils.isNotBlank(this.idOfOrganizationToMoveTo)) {
					this.moveUserToOrganization(this.idOfUserToMove, this.idOfOrganizationToMoveTo);
				} else if (StringUtils.isNotBlank(this.idOfOrganizationUnitToMoveTo)) {
					this.moveUserToOrganizationUnit(this.idOfUserToMove, this.idOfOrganizationUnitToMoveTo);
				}
			} else if (this.selectedNodeModel.isOrganizationUnit()) {
				this.idOfOrganizationUnitToMove = this.selectedNodeModel.id;
				if (StringUtils.isNotBlank(this.idOfOrganizationToMoveTo)) {
					this.moveOrganizationUnitToOrganization(this.idOfOrganizationUnitToMove, this.idOfOrganizationToMoveTo);
				} else if (StringUtils.isNotBlank(this.idOfOrganizationUnitToMoveTo)) {
					this.moveOrganizationUnitToOrganizationUnit(this.idOfOrganizationUnitToMove, this.idOfOrganizationUnitToMoveTo);
				}
			}
		}
	}

	private moveUserToOrganization(idOfUserToMove: string, idOfOrganizationToMoveTo: string): void {
		this.organizationService.moveUserToOrganization(idOfUserToMove, idOfOrganizationToMoveTo, {
			onSuccess: () => {
				this.moveItemWindowVisible = false;
				this.messageDisplayer.displaySuccess("USER_MOVED");
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	private moveUserToOrganizationUnit(idOfUserToMove: string, idOfOrganizationUnitToMoveTo: string): void {
		this.organizationService.moveUserToOrganizationUnit(idOfUserToMove, idOfOrganizationUnitToMoveTo, {
			onSuccess: () => {
				this.moveItemWindowVisible = false;
				this.messageDisplayer.displaySuccess("USER_MOVED");
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	private moveOrganizationUnitToOrganization(idOfOrganizationUnitToMove: string, idOfOrganizationToMoveTo: string): void {
		this.organizationService.moveOrganizationUnitToOrganization(idOfOrganizationUnitToMove, idOfOrganizationToMoveTo, {
			onSuccess: () => {
				this.moveItemWindowVisible = false;
				this.messageDisplayer.displaySuccess("ORG_UNIT_MOVED");
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	private moveOrganizationUnitToOrganizationUnit(idOfOrganizationUnitToMove: string, idOfOrganizationUnitToMoveTo: string): void {
		this.organizationService.moveOrganizationUnitToOrganizationUnit(idOfOrganizationUnitToMove, idOfOrganizationUnitToMoveTo, {
			onSuccess: () => {
				this.moveItemWindowVisible = false;
				this.messageDisplayer.displaySuccess("ORG_UNIT_MOVED");
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
	}

	public onCancelForMoveItemAction(event: any): void {
		this.moveItemWindowVisible = false;
		this.refresh();
	}

	public onImport(event: any): void {
		this.confirmationUtils.confirm("CONFIRM_IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY", {
			approve: (): void => {
				this.organizationService.importOrganizationalStructureFromDirectory({
					onSuccess: () => {
						this.refresh();
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
						this.refresh();
					}
				});
			}, 
			reject: (): void => {
				this.refresh();
			}
		});
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	private onDeactivateUser(thisComponent: OrganizationalStructureComponent): void {
		thisComponent.deactivateUserWindowVisible = true;
	}

	public onAcceptDeactivateUserAction(event: any): void {
		
		let deactivationUserModel: DeactivationUserModel = new DeactivationUserModel();
		deactivationUserModel.userId = this.selectedNodeModel.id;
		deactivationUserModel.deactivationMode = this.getDeactivationMode();

		this.organizationService.deactivateUser(deactivationUserModel, {
			onSuccess: () => {
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.refresh();
			}
		});
		this.deactivateUserWindowVisible = false;
	}

	public onRejectDeactivateUserAction(event: any): void {
		this.deactivateUserWindowVisible = false;
		this.refresh();
	}

	private getDeactivationMode(): string {
		return this.deactivateAllUserAccounts ? "ALL_ACCOUNTS" : "SINGLE_ACCOUNT";
	}

	private onReactivateUser(thisComponent: OrganizationalStructureComponent): void {
		thisComponent.confirmationUtils.confirm("CONFIRM_REACTIVATE_USER", {
			approve: (): void => {
				thisComponent.organizationService.reactivateUser(thisComponent.selectedNodeModel.id, {
					onSuccess: () => {
						thisComponent.refresh();
					},
					onFailure: (appError: AppError) => {
						thisComponent.messageDisplayer.displayAppError(appError);
						thisComponent.refresh();
					}
				});
			}, 
			reject: (): void => {
				thisComponent.refresh();
			}
		});
	}

	public onOrganizationSelectionChanged(nodeModels: OrganizationTreeNodeModel[]): void {
		this.reset();
		let nodeModel: OrganizationTreeNodeModel = nodeModels[0];
		this.selectedNodeModel = nodeModel;
		if (nodeModel.isOrganization()) {
			this.setDeleteMenuItemsDisabled(nodeModel.type);
			this.setAddMenuItemsDisabled(false);
			this.setToolsMenuItemsDisabled(true);
			this.isMoveButtonDisabled = true;
			this.isEditButtonDisabled = true;
			this.organizationId = nodeModel.id;
		} else if (nodeModel.isOrganizationUnit()) {
			this.setDeleteMenuItemsDisabled(nodeModel.type);
			this.setAddMenuItemsDisabled(false);
			this.setToolsMenuItemsDisabled(true);
			this.isMoveButtonDisabled = false;
			this.isEditButtonDisabled = false;
			this.organizationUnitId = nodeModel.id;
		} else {
			this.setDeleteMenuItemsDisabled(nodeModel.type);
			this.setAddMenuItemsDisabled(true);
			this.setToolsMenuItemsDisabled(false);
			this.isMoveButtonDisabled = false;
			this.isEditButtonDisabled = false;
		}
	}

	private setAddMenuItemsDisabled(disabled: boolean): void {
		this.addMenuItems.forEach(addMenuItem => {
			addMenuItem.disabled = disabled;
		});
	}

	private setDeleteMenuItemsDisabled(type: string): void {
		this.deleteMenuItems.forEach(deleteMenuItem => {
			if (deleteMenuItem.id === OrganizationTreeNodeType[OrganizationTreeNodeType.ORGANIZATION]) {
				deleteMenuItem.disabled = true;
			} else if (deleteMenuItem.id === type) {
				deleteMenuItem.disabled = false;
			} else {
				deleteMenuItem.disabled = true;
			}
		});
	}

	private setToolsMenuItemsDisabled(disabled: boolean): void {
		this.toolsMenuItems.forEach(toolsMenuItem => {
			toolsMenuItem.disabled = disabled;
		});
	}

	public onUserWindowClosed(event: any): void {
		this.onWindowClosed();
		this.userWindowVisible = false;
	}

	public onOrganizationUnitWindowClosed(event: any): void {
		this.onWindowClosed();
		this.organizationUnitWindowVisible = false;
	}

	public onDirectoryUserSearchWindowClosed(event: any): void {
		this.onWindowClosed();
		this.directoryUserSearchWindowVisible = false;
	}

	public onUsersFoundInDirectoryWindowClosed(event: any): void {
		this.onWindowClosed();
		this.usersFoundInDirectoryWindowVisible = false;
	}

	private onWindowClosed(): void {
		this.refresh();
	}

	private refresh(): void {
		this.reset();
		this.setDeleteMenuItemsDisabled(null);
		this.setAddMenuItemsDisabled(true);
		this.setToolsMenuItemsDisabled(true);
		this.isEditButtonDisabled = true;
		this.isMoveButtonDisabled = true;
		this.organizationSelectorComponent.refresh();
	}

	public onDirectoryUserSearchDone(directoryUsersModel: DirectoryUsersModel): void {
		this.organizationId = directoryUsersModel.organizationId;
		this.organizationUnitId = directoryUsersModel.organizationUnitId;
		this.usersFoundInDirectory = directoryUsersModel.directoryUsers;
		this.usersFoundInDirectoryWindowVisible = true;
	}

	public onDirectoryUserSelected(directoryUser: DirectoryUserModel): void {
		this.userWindowMode = "import";
		this.directoryUser = directoryUser;
		if (StringUtils.isNotBlank(directoryUser.organizationId)) {
			this.organizationId = directoryUser.organizationId;
		} else if (StringUtils.isNotBlank(directoryUser.organizationUnitId)) {
			this.organizationUnitId = directoryUser.organizationUnitId;
		}
		this.userWindowVisible = true;
	}

	public onHideDeactivateUserWindow(event: any): void {
		this.deactivateUserWindowVisible = false;
	}

	public onHideMoveItemWindow(event: any): void {
		this.moveItemWindowVisible = false;
	}
}