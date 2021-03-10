package ro.cloudSoft.cloudDoc.dao.organizaiton;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.organization.Role;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

public interface RoleDao {

	List<String> getAllRoleNames();
	
	List<Role> getAllRoles();
	
	List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews();
}
