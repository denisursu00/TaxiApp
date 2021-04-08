package ro.taxiApp.docs.dao.organizaiton;

import java.util.List;

import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

public interface RoleDao {

	List<String> getAllRoleNames();
	
	List<Role> getAllRoles();
	
	Role getRoleByName(String roleName);
	
	List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews();
}
