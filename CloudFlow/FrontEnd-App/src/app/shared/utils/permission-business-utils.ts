import { PermissionModel } from "../model/permission.model";
import { SecurityManagerModel } from "../model/security-manager.model";

export class PermissionBusinessUtils {
	
	public static canEdit(permissions: PermissionModel[], userSecurity: SecurityManagerModel): boolean {
		if (userSecurity.userAdmin) {
			return true;
		}

		let allUserPermissions: string[] = PermissionBusinessUtils.getAllUserPermissions(permissions, userSecurity);
		return (
			allUserPermissions.includes(PermissionModel.PERMISSION_EDITOR) ||
			allUserPermissions.includes(PermissionModel.PERMISSION_COLABORATOR) ||
			allUserPermissions.includes(PermissionModel.PERMISSION_COORDINATOR)
		);
	}

	public static canChangePermissions(permissions: PermissionModel[], userSecurity: SecurityManagerModel): boolean {
		if (userSecurity.userAdmin) {
			return true;
		}
		return PermissionBusinessUtils.getAllUserPermissions(permissions, userSecurity).includes(PermissionModel.PERMISSION_COORDINATOR);
	}

	private static getAllUserPermissions(permissions: PermissionModel[], userSecurity: SecurityManagerModel): string[] {
		let allUserPermissions: string[] = [];
		
		permissions.forEach(permission => {
			if (permission.entityType === PermissionModel.TYPE_USER) {
				if (permission.entityId === userSecurity.userIdAsString) {
					allUserPermissions.push(permission.permission);
				}
			} else if (permission.entityType === PermissionModel.TYPE_ORGANIZATION_UNIT) {
				if (userSecurity.organizationUnitIds.includes(Number(permission.entityId))) {
					allUserPermissions.push(permission.permission);
				}
			} else if (permission.entityType === PermissionModel.TYPE_GROUP) {
				if (userSecurity.groupIds.includes(Number(permission.entityId))) {
					allUserPermissions.push(permission.permission);
				}
			}
		});
		return allUserPermissions;
	}
}