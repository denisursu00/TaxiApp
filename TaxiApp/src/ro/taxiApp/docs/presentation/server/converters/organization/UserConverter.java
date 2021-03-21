package ro.taxiApp.docs.presentation.server.converters.organization;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RoleModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;

public class UserConverter {

	public static UserModel getModelFromUser(User user) {
		
        UserModel userModel = new UserModel();
        
        userModel.setId(user.getId());
        
        userModel.setUsername(user.getUsername());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPassword(user.getPassword());
        userModel.setEmail(user.getEmail());
        userModel.setMobile(user.getMobile());
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
        
        if (userModel.getId() != null) {
        	user.setId(new Long(userModel.getId()));
        }
        user.setUsername(userModel.getUsername());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setMobile(userModel.getMobile());
        String password = userModel.getPassword();
        if (StringUtils.isNotEmpty(password)) {
        	user.setPassword(password);
        }
      
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