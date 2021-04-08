package ro.taxiApp.docs.services.organization;

import java.util.List;

import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RoleModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

public interface RoleService {

	List<String> getAllRoleNames();
	
	List<RoleModel> getAllRoles();
	
	RoleModel getRoleByName(String roleName);

	List<RoleModel> getAvailableRolesForUser(Long userId);
	
	List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews();
}
