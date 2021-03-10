package ro.cloudSoft.cloudDoc.services.organization;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class UserServiceImpl implements UserService, InitializingBean {
	
	private UserPersistencePlugin userPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			userPersistencePlugin
		);
	}
	
	@Override
	public User getUserById(Long id) {
		return userPersistencePlugin.getUserById(id);
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

}