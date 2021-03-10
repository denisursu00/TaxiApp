package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public class GwtPermissionBusinessUtils {
	
	/**
	 * Verifica daca utilizatorul curent are drept de editare asupra unei entitati.
	 * @param permissions permisiunile entitatii
	 * @param userSecurity securitatea utilizatorului curent
	 */
	public static boolean canEdit(List<PermissionModel> permissions, SecurityManagerModel userSecurity) {
		
		if (userSecurity.isUserAdmin()) {
			return true;
		}
		
		Set<String> allUserPermissions = getAllUserPermissions(permissions, userSecurity);
		return (
			allUserPermissions.contains(PermissionModel.PERMISSION_EDITOR) ||
			allUserPermissions.contains(PermissionModel.PERMISSION_COLABORATOR) ||
			allUserPermissions.contains(PermissionModel.PERMISSION_COORDINATOR)
		);
	}
	
	/**
	 * Verifica daca utilizatorul curent are drept de schimbare permisiuni.
	 * @param permissions permisiunile entitatii
	 * @param userSecurity securitatea utilizatorului curent
	 */
	public static boolean canChangePermissions(List<PermissionModel> permissions, SecurityManagerModel userSecurity) {
		
		if (userSecurity.isUserAdmin()) {
			return true;
		}
		
		return getAllUserPermissions(permissions, userSecurity).contains(PermissionModel.PERMISSION_COORDINATOR);
	}
	
	private static Set<String> getAllUserPermissions(List<PermissionModel> permissions, SecurityManagerModel userSecurity) {
		
		Set<String> allUserPermissions = new HashSet<String>();
		
		for (PermissionModel permission : permissions) {
			if (permission.getEntityType().equals(PermissionModel.TYPE_USER)) {
				if (permission.getEntityId().equals(userSecurity.getUserIdAsString())) {
					allUserPermissions.add(permission.getPermission());
				}
			} else if (permission.getEntityType().equals(PermissionModel.TYPE_ORGANIZATION_UNIT)) {
				if (userSecurity.getOrganizationUnitIds().contains(Long.valueOf(permission.getEntityId()))) {
					allUserPermissions.add(permission.getPermission());
				}
			} else if (permission.getEntityType().equals(PermissionModel.TYPE_GROUP)) {
				if (userSecurity.getGroupIds().contains(Long.valueOf(permission.getEntityId()))) {
					allUserPermissions.add(permission.getPermission());
				}
			}
		}
		
		return allUserPermissions;
	}
}