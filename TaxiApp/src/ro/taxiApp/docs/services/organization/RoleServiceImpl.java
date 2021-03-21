package ro.taxiApp.docs.services.organization;

import java.util.ArrayList;
import java.util.List;

import ro.taxiApp.docs.dao.organizaiton.RoleDao;
import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RoleModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RolePermissionMappingViewModel;
import ro.taxiApp.docs.presentation.server.converters.organization.RoleConverter;

public class RoleServiceImpl implements RoleService {

	private RoleDao roleDao;
	private UserService userService;
	
	@Override
	public List<String> getAllRoleNames() {
		return roleDao.getAllRoleNames();
	}

	@Override
	public List<RoleModel> getAllRoles() {
		List<Role> allRoles = roleDao.getAllRoles();
		List<RoleModel> allRoleModels = new ArrayList<>();
		for (Role roleEntity : allRoles) {
			allRoleModels.add(RoleConverter.getModelFromEntity(roleEntity));
		}
		return allRoleModels;
	}
	
	@Override
	public List<RoleModel> getAvailableRolesForUser(Long userId) {
		List<Role> allRoles = roleDao.getAllRoles();
		//delete existing roles for user
		User user = userService.getUserById(userId);
		
		for (Role role : user.getRoles()) {
			allRoles.remove(role);
		}
		
		List<RoleModel> allRoleModels = new ArrayList<>();
		for (Role roleEntity : allRoles) {
			allRoleModels.add(RoleConverter.getModelFromEntity(roleEntity));
		}
		return allRoleModels;
	}
	
	@Override
	public List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews() {
		return roleDao.getAllRolePermissionMappingViews();
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
