package ro.taxiApp.docs.services.organization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.core.AppExceptionCodes;
import ro.taxiApp.docs.dao.organizaiton.RoleDao;
import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.plugins.organization.UserPersistencePlugin;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;
import ro.taxiApp.docs.presentation.server.converters.organization.UserConverter;

public class UserServiceImpl implements UserService, InitializingBean {
	
	private UserPersistencePlugin userPersistencePlugin;
	private RoleDao roleDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			userPersistencePlugin,
			roleDao
		);
	}
	
	@Override
	public User getUserById(Long id) {
		return userPersistencePlugin.getUserById(id);
	}
	
	@Override
	public Long saveUserWithRole(UserModel user, String roleName, SecurityManager userSecurity) throws AppException {
		User userEntity = null;
		userEntity = UserConverter.getUserFromModel(user, userEntity, userSecurity);
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(roleDao.getRoleByName(roleName));
		return userPersistencePlugin.saveUserAndReturnId(userEntity);
	}
	
	@Override
	public UserModel getUserByIdAsModel(Long id) {
		return UserConverter.getModelFromUser(userPersistencePlugin.getUserById(id));
	}
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void setUser(User user, SecurityManager userSecurity) throws AppException {
    	
    	if (userPersistencePlugin.userWithUsernameExists(user.getUsername(), user.getId())) {
    		throw new AppException(AppExceptionCodes.USER_EXISTS);
    	}
    	
    	userPersistencePlugin.saveUser(user);
    }

    @Override
    public List<User> getAllUsers( SecurityManager userSecurity) {
        return userPersistencePlugin.getAllUsers();
    }

    @Override
    @Transactional
    public void delete(User theUser, SecurityManager userSecurity) {
    	userPersistencePlugin.delete(theUser);
    }
	
	@Override
	public boolean userExists(Long id) {
		return userPersistencePlugin.userExists(id);
	}
	
	@Override
	public User getUserByUsername(String username) {
		return userPersistencePlugin.getUserByUsername(username);
	}
	
	@Override
	public List<String> getUserRoleNames(Long userId) {
		return this.userPersistencePlugin.getUserRoleNames(userId);
	}
	
	
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

}