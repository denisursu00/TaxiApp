import { Component, OnInit, ViewChild, Input } from "@angular/core";
import { DocumentModel, GroupModel, OrganizationTreeNodeModel, PermissionModel, OrganizationTreeModel } from "../../model";
import { PermissionListComponent } from "../../components/permission-list";
import { ArrayUtils, ObjectUtils, StringUtils } from "../../utils";

@Component({
	selector: "app-permission-manager",
	templateUrl: "./permission-manager.component.html"
})
export class PermissionManagerComponent {

	@ViewChild(PermissionListComponent)
	private permissionListComponent: PermissionListComponent;

	@Input()
	public readonly: boolean;
	
	@Input()
	public permissions: PermissionModel[];

	@Input()
	public activateDefaultPermissions: boolean;

	private selectedItems: GroupModel[] | OrganizationTreeNodeModel[];
	private validationMessageCodes: string[];

	public constructor() {
		this.init();
	}

	private init(): void {
		this.resetSelectedItems();
		this.validationMessageCodes = [];
	}

	private resetSelectedItems(): void {
		this.selectedItems = [];
	}

	public onGroupSelectionChanged(selectedGroups: GroupModel[]): void {
		this.selectedItems = selectedGroups;
	}

	public onOrganizationSelectionChanged(selectedOrganizations: OrganizationTreeNodeModel[]): void {
		this.selectedItems = selectedOrganizations;
	}

	public onAddPermission(event: any): void {
		this.permissionListComponent.onAddPermissionEvent(this.selectedItems);
	}

	public onDeletePermission(): void {
		this.permissionListComponent.onDeletePermissionEvent();
	}

	public getPermissions(): PermissionModel[] {
		return this.permissionListComponent.getPermissions();
	}

	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		this.validationMessageCodes = [];
		let isValid: boolean = true;
		let permissions: PermissionModel[] = this.getPermissions();
		if (ArrayUtils.isEmpty(permissions)) {
			isValid = false;
			this.validationMessageCodes.push("NO_SELECTED_ENTITIES");
		} else {
			let arePermissionsValid: boolean = true;
			permissions.forEach((permission: PermissionModel) => {
				if (!permission.permission) {
					arePermissionsValid = false;
				}
			});
			if (!arePermissionsValid) {
				isValid = false;
				this.validationMessageCodes.push("PERMISSIONS_NOT_SET_FOR_ALL_SELECTED_ENTITIES");
			}
		}
		return isValid;
	}

	public getValidationMessageCodes(): string[] {
		return this.validationMessageCodes;
	}

	public onOrganizationSelectorDataLoaded(organizationTreeModel: OrganizationTreeModel): void {
		if (ArrayUtils.isNotEmpty(this.permissions)) {			
			this.permissions.forEach(permission => {
				if (permission.entityType === PermissionModel.TYPE_USER || permission.entityType === PermissionModel.TYPE_ORGANIZATION_UNIT) {
					let foundEntityArray: OrganizationTreeNodeModel[] = [];
					this.findEntity([organizationTreeModel.rootNode], permission.entityId, permission.entityType, foundEntityArray);
					if (ArrayUtils.isNotEmpty(foundEntityArray)) {
						permission.entityName = this.getEntityLabel(foundEntityArray[0]);
					}
					foundEntityArray = [];
				}
			});
		}
	}

	private findEntity(models: OrganizationTreeNodeModel[], entityId: string, entityType: number, foundEntityArray: OrganizationTreeNodeModel[]): OrganizationTreeNodeModel {
		let organizationTreeNodeModel: OrganizationTreeNodeModel = models.filter(orgTreeModel => orgTreeModel.id === entityId)[0];
		if (ObjectUtils.isNullOrUndefined(organizationTreeNodeModel)) {
			models.forEach(model => {
				if (ArrayUtils.isNotEmpty(model.children)) {
					organizationTreeNodeModel = this.findEntity(model.children, entityId, entityType, foundEntityArray);
					if (ObjectUtils.isNotNullOrUndefined(organizationTreeNodeModel)) {
						foundEntityArray.push(organizationTreeNodeModel);
					}
				}
			});
		}
		return organizationTreeNodeModel;
	}

	public onGroupSelectorDataLoaded(groups: GroupModel[]): void {
		if (ArrayUtils.isNotEmpty(this.permissions)) {
			this.permissions.forEach(permission => {
				if (permission.entityType === PermissionModel.TYPE_GROUP) {
					let groupModel: GroupModel = groups.filter(group => group.id === permission.entityId)[0];
					if (ObjectUtils.isNotNullOrUndefined(groupModel)) {
						permission.entityName = groupModel.name;
					}
				}
			});
		}
	}

	private getEntityLabel(nodeModel: OrganizationTreeNodeModel): string {
		if (nodeModel.isOrganizationUnit()) {
			return nodeModel.name;
		} else if (nodeModel.isUser()) {
			if (StringUtils.isNotBlank(nodeModel.title)) {
				if (StringUtils.isNotBlank(nodeModel.customTitleTemplate)) {
					return this.getUserNameWithTitle(nodeModel.name, this.getUserTitleByTemplate(nodeModel.customTitleTemplate, nodeModel.title));
				} else {
					return this.getUserNameWithTitle(nodeModel.name, nodeModel.title);
				}
			} else {
				return nodeModel.name;
			}
		} else {
			throw new Error("Tipul de nod nu este cunoscut pentru determinarea label-ului [" + nodeModel.type + "]");
		}
	}

	private getUserNameWithTitle(name: string, title: string): string {
		return name + " - " + title;
	}

	private getUserTitleByTemplate(template: string, title: string): string {
		return template.replace("{title}", title);
	}
}