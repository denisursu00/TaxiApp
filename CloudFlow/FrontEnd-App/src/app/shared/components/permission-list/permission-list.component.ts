import { Component, OnInit, ViewEncapsulation, Input } from "@angular/core";
import { ArrayUtils, StringUtils } from "../../utils";
import { MessageDisplayer } from "@app/shared/message-displayer";
import { AclService } from "../../service";
import { IconConstants } from "../../constants";
import { 
	GroupModel, 
	OrganizationTreeNodeModel, 
	SecurityManagerModel, 
	AppError, 
	PermissionModel,
	DocumentModel 
} from "../../model";

@Component({
	selector: "app-permission-list",
	templateUrl: "./permission-list.component.html",
	styleUrls: ["./permission-list.component.css"],
	encapsulation: ViewEncapsulation.None
})
export class PermissionListComponent implements OnInit {

	@Input()
	public permissions: PermissionModel[];
	
	@Input()
	public readonly: boolean;

	@Input()
	public activateDefaultPermissions: boolean;

	private permissionCoordinator: string;
	private permissionColaborator: string;
	private permissionEditor: string;
	private permissionReader: string;

	public selectedPermissions: PermissionModel[];
	private aclService: AclService;
	private messageDisplayer: MessageDisplayer;
	private securityManager: SecurityManagerModel;

	public constructor(aclService: AclService, messageDisplayer: MessageDisplayer) {
		this.aclService = aclService;
		this.messageDisplayer = messageDisplayer;
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.initPermissionTypes();
		this.resetSelectedPermissionModels();
		if (this.activateDefaultPermissions) {
			this.resetPermissionModels();
			this.addDefaultPermissions();
		}
	}

	private initPermissionTypes(): void {
		this.permissionCoordinator = PermissionModel.PERMISSION_COORDINATOR;
		this.permissionColaborator = PermissionModel.PERMISSION_COLABORATOR;
		this.permissionEditor = PermissionModel.PERMISSION_EDITOR;
		this.permissionReader = PermissionModel.PERMISSION_READER;
	}

	private addDefaultPermissions(): void {
		this.aclService.getSecurityManager({
			onSuccess: (securityManager: SecurityManagerModel) => {
				this.addDefaultPermission(securityManager);
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.securityManager = null;
			}
		});
	}

	private addDefaultPermission(securityManager: SecurityManagerModel): void {
		if (this.permissionModelsContainElementWithId(securityManager.userIdAsString)) {
			let permissionModel: PermissionModel = new PermissionModel();
			permissionModel.entityId = securityManager.userIdAsString;
			permissionModel.permission = PermissionModel.PERMISSION_COORDINATOR;
			permissionModel.entityType = PermissionModel.TYPE_USER;
			if (StringUtils.isNotBlank(securityManager.userTitle)) {
				permissionModel.entityName = securityManager.userUsername + " - " + securityManager.userTitle;
			} else {
				permissionModel.entityName = securityManager.userUsername;
			}
			this.permissions.push(permissionModel);
		}
	}

	private resetPermissionModels(): void {
		this.permissions = [];
	}

	private resetSelectedPermissionModels(): void {
		this.selectedPermissions = [];
	}

	public onAddPermissionEvent(selectedItems: any[]): void {
		selectedItems.forEach(selectedItem => {
			if (this.permissionModelsContainElementWithId(selectedItem.id)) {
				if (selectedItem instanceof GroupModel) {
					this.addGroupPermission(selectedItem);
				} else if (selectedItem instanceof OrganizationTreeNodeModel) {
					this.addOrganizationPermission(selectedItem);
				}
			}
		});
	}

	private addOrganizationPermission(selectedItem: OrganizationTreeNodeModel): void {
		let permission: PermissionModel = new PermissionModel();
		permission.entityId = selectedItem.id;
		permission.entityType = this.getEntityType(selectedItem);
		permission.entityName = this.getEntityLabel(selectedItem);
		this.permissions.push(permission);
	}

	private addGroupPermission(selectedItem: GroupModel): void {
		let permission: PermissionModel = new PermissionModel();
		permission.entityId = selectedItem.id;
		permission.entityName = selectedItem.name;
		permission.entityType = PermissionModel.TYPE_GROUP;
		this.permissions.push(permission);
	}

	private getUserNameWithTitle(name: string, title: string): string {
		return name + " - " + title;
	}

	private getUserTitleByTemplate(template: string, title: string): string {
		return template.replace("{title}", title);
	}

	public onDeletePermissionEvent(): void {
		this.selectedPermissions.forEach(element => {
			this.permissions.splice(this.permissions.indexOf(element), 1);
			this.permissions = [...this.permissions];
		});
		this.resetSelectedPermissionModels();
	}

	private getEntityType(nodeModel: OrganizationTreeNodeModel): number {
		if (nodeModel.isOrganizationUnit()) {
			return PermissionModel.TYPE_ORGANIZATION_UNIT;
		} else if (nodeModel.isUser()) {
			return PermissionModel.TYPE_USER;
		} else {
			throw new Error("Tipul de nod nu este cunoscut pentru determinarea icon-ului [" + nodeModel.type + "]");
		}
	}

	public getEntityIconName(permissionModel: PermissionModel): string {
		if (permissionModel.isGroup()) {
			return IconConstants.ORGANIZATION_GROUP_ICON;
		} else if (permissionModel.isOrganizationUnit()) {
			return IconConstants.ORGANIZATION_UNIT_ICON;
		} else if (permissionModel.isUser()) {
			return IconConstants.ORGANIZATION_USER_ICON;
		} else {
			throw new Error("Tipul de drept nu este cunoscut pentru determinarea icon-ului [" + permissionModel.entityType + "]");
		}
	}

	public getPermissions(): PermissionModel[] {
		return this.permissions;
	}

	private permissionModelsContainElementWithId(id: string): boolean {
		return ArrayUtils.isEmpty(
			this.permissions.filter(
				permission => permission.entityId === id
			)
		);
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
}