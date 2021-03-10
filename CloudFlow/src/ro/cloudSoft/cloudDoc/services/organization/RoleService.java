package ro.cloudSoft.cloudDoc.services.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RoleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

public interface RoleService {

	List<String> getAllRoleNames();
	
	List<RoleModel> getAllRoles();

	List<RoleModel> getAvailableRolesForUser(Long userId);
	
	List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews();
}
