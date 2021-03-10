package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Role;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RoleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

public class UserConverter {

	public static UserModel getModelFromUser(User user) {
		
        UserModel userModel = new UserModel();
        
        userModel.setUserId(user.getId().toString());
        
        userModel.setUserName(user.getUsername());
        userModel.setName(user.getName());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPassword(user.getPassword());
        userModel.setEmail(user.getEmail());
        userModel.setMobile(user.getMobile());
        userModel.setEmployeeNumber(user.getEmployeeNumber());
        List<RoleModel> roleModels = new ArrayList<RoleModel>();
        for (Role role : user.getRoles()) {
			roleModels.add(RoleConverter.getModelFromEntity(role));
		}
        userModel.setRoles(roleModels);
        
        return userModel;
    }
	
	public static User getUserFromModel(UserModel userModel, User userEntity, SecurityManager userSecurity) throws AppException {
		User user;
		
		if (userEntity == null) {
			user = new User();
		} else {
			user = userEntity;
		}
        
        if (StringUtils.isNotBlank(userModel.getUserId())) {
        	user.setId(new Long(userModel.getUserId()));
        }
        user.setUsername(userModel.getUserName());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setMobile(userModel.getMobile());
        String password = userModel.getPassword();
        if (StringUtils.isNotEmpty(password)) {
        	user.setPassword(password);
        }
        user.setEmployeeNumber(userModel.getEmployeeNumber());
      
        Set<Role> userEntityRoles = new HashSet<>();
        if ((CollectionUtils.isNotEmpty(user.getRoles()))) {
        	userEntityRoles = user.getRoles();
        }
        
        Iterator<Role> iteratorRole = userEntityRoles.iterator();
        while (iteratorRole.hasNext()) {
        	Long userId = iteratorRole.next().getId();
			boolean existsInRolesModel = false;
			for (RoleModel roleModel : userModel.getRoles()) {
				if (roleModel.getId().equals(userId)) {
					existsInRolesModel = true;
				}
			}
			if (!existsInRolesModel) {
				iteratorRole.remove();
			}
		}
       
        //add the new roles
        for (RoleModel roleModel : userModel.getRoles()) {
        	boolean existsInRolesEntiy = false;
        	for (Role role: userEntityRoles) {
				if (roleModel.getId().equals(role.getId())) {
					existsInRolesEntiy = true;
				}
			}
			if (!existsInRolesEntiy) {
				userEntityRoles.add(RoleConverter.getEntityFromModel(roleModel));
			}
		}

        user.setRoles(userEntityRoles);
        
        return user;
    }
}