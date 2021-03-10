package ro.cloudSoft.cloudDoc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.constants.AppConstants;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public class SecurityUtils {
	
	public static String getPermission(ACL acl, SecurityManager userSecurity) {
		Set<String> allUserPermissions = new HashSet<String>();
		
		// Obtine o "harta" cu permisiunile, pentru o cautare mai rapida.
		Map<Integer, Map<String, String>> permissionMap = getPermissionMap(acl);
		
		String userPermission = null;
		if ((userPermission = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER).get(userSecurity.getUserIdAsString())) != null) {
			allUserPermissions.add(userPermission);
		}
		
		// Ia toate permisiunile unitatilor organizatorice din care utilizatorul face parte.
		String organizationUnitPermission = null;
		for (Long organizationUnitId : userSecurity.getOrganizationUnitIds()) {
			if ((organizationUnitPermission = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT).get(organizationUnitId.toString())) != null) {
				allUserPermissions.add(organizationUnitPermission);
			}
		}
		
		// Ia toate permisiunile grupurilor din care utilizatorul face parte.
		String groupPermission = null;
		for (Long groupId : userSecurity.getGroupIds()) {
			if ((groupPermission = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_GROUP).get(groupId.toString())) != null) {
				allUserPermissions.add(groupPermission);
			}
		}
		
		return (!allUserPermissions.isEmpty()) ? Collections.max(allUserPermissions) : null;
	}
	
	public static Map<Integer, Map<String, String>> getPermissionMap(ACL acl) {
		Map<Integer, Map<String, String>> permissionMap = new HashMap<Integer, Map<String,String>>();
		
		permissionMap.put(AppConstants.ACL_ENTITY_TYPE_USER, new HashMap<String, String>());
		permissionMap.put(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT, new HashMap<String, String>());
		permissionMap.put(AppConstants.ACL_ENTITY_TYPE_GROUP, new HashMap<String, String>());
		
		for (Permission permission : acl.getPermissions()) {
			Map<String, String> entityTypePermissions = permissionMap.get(permission.getEntityType());
			entityTypePermissions.put(permission.getEntityId(), permission.getPermission());
		}
		return permissionMap;
	}
	
	public static ACL getAclFromPermissionMap(Map<Integer, Map<String, String>> permissionMap) {
		List<Permission> permissions = new ArrayList<Permission>();
		
		// Adauga cate o permisiune pentru fiecare utilizator.
		for (Entry<String, String> entry : permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER).entrySet()) {
			Permission permission = new Permission();
			permission.setEntityType(AppConstants.ACL_ENTITY_TYPE_USER);
			permission.setEntityId(entry.getKey());
			permission.setPermission(entry.getValue());
			permissions.add(permission);
		}
		// Adauga cate o permisiune pentru fiecare unitate organizatorica.
		for (Entry<String, String> entry : permissionMap.get(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT).entrySet()) {
			Permission permission = new Permission();
			permission.setEntityType(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT);
			permission.setEntityId(entry.getKey());
			permission.setPermission(entry.getValue());
			permissions.add(permission);
		}
		// Adauga cate o permisiune pentru fiecare grup.
		for (Entry<String, String> entry : permissionMap.get(AppConstants.ACL_ENTITY_TYPE_GROUP).entrySet()) {
			Permission permission = new Permission();
			permission.setEntityType(AppConstants.ACL_ENTITY_TYPE_GROUP);
			permission.setEntityId(entry.getKey());
			permission.setPermission(entry.getValue());
			permissions.add(permission);
		}

		ACL acl = new ACL();
		acl.setPermissions(permissions);
		return acl;
	}
	
	/**
	 * Transforma toate permisiunile in reader.
	 * @param acl ACL-ul ce contine permisiunile
	 */
	public static void setReadOnly(ACL acl) {
		for (Permission permission : acl.getPermissions()) {
			permission.setPermission(AppConstants.ACL_ROLE_READER);
		}
	}
	
	public static boolean isUserAdmin(SecurityManager userSecurity) {
		if (userSecurity != null){
			String groupNameAdmins = BusinessConstants.get().getGroupNameAdmins();
			return userSecurity.getGroupNames().contains(groupNameAdmins);
		}
		return false;
	}
}