package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.Role;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.User.UserTypeEnum;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RoleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;

public class UserConverter {

	public static UserModel getModelFromUser(User user) {
		
        UserModel userModel = new UserModel();
        
        userModel.setUserId(user.getId().toString());
        
        if (user.getOu() != null) {
        	
        	String organizationUnitIdAsString = user.getOu().getId().toString();
        	userModel.setOrganizationUnitId(organizationUnitIdAsString);
        	
        	if (user.getOu().getManager() != null) {
        		if (user.getOu().getManager().getId().equals(user.getId())) {
        			userModel.setIsManager(true);
        		}
        	}
        } else if (user.getOrganization() != null) {
        	
        	String organizationIdAsString = user.getOrganization().getId().toString();
        	userModel.setOrganizationId(organizationIdAsString);
        	
        	if (user.getOrganization().getOrganizationManager() != null) {
        		if (user.getOrganization().getOrganizationManager().getId().equals(user.getId())) {
        			userModel.setIsManager(true);
        		}
        	}
        } else {
        	throw new IllegalArgumentException("Un utilizator trebuie sa aiba un parinte.");
        }
        
        userModel.setUserName(user.getUsername());
        userModel.setName(user.getName());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPassword(user.getPassword());
        userModel.setTitle(user.getTitle());
        userModel.setCustomTitleTemplate(user.getCustomTitleTemplate());
        userModel.setEmail(user.getEmail());
        userModel.setPhone(user.getPhone());
        userModel.setFax(user.getFax());
        userModel.setMobile(user.getMobile());
        userModel.setEmployeeNumber(user.getEmployeeNumber());
        userModel.setType(user.getType().name());
        List<RoleModel> roleModels = new ArrayList<RoleModel>();
        for (Role role : user.getRoles()) {
			roleModels.add(RoleConverter.getModelFromEntity(role));
		}
        userModel.setRoles(roleModels);
        
        return userModel;
    }
	
	public static User getUserFromModel(UserModel userModel, User userEntity, OrganizationService organizationService,
			OrganizationUnitService organizationUnitService, SecurityManager userSecurity) throws AppException {
		User user;
		
		if (userEntity == null) {
			user = new User();
			user.setType(UserTypeEnum.PERSON);
		} else {
			user = userEntity;
			user.setType(userEntity.getType());
		}
        
        // nu trebuie setat id cand merge pe adaugare user de aceea pun conditie
        if (StringUtils.isNotBlank(userModel.getUserId())) {
        	user.setId(new Long(userModel.getUserId()));
        }
        if (userModel.getOrganizationUnitId() != null) {
        	
        	Long organizationUnitId = Long.valueOf(userModel.getOrganizationUnitId());
        	OrganizationUnit organizationUnit = organizationUnitService.getOrganizationUnitById(organizationUnitId);
        	
        	if (organizationUnit != null) {
        		user.setOu(organizationUnit);
        	} else {
        		throw new IllegalStateException("Nu s-a gasit unitatea organizatorica cu ID-ul [" + organizationUnitId + "].");
        	}
        } else if (userModel.getOrganizationId() != null) {
        	
        	Long organizationId = Long.valueOf(userModel.getOrganizationId());
        	Organization organization = organizationService.getOrganizationById(organizationId);
        	
        	if (organization != null) {
        		user.setOrganization(organization);
        	} else {
        		throw new IllegalStateException("Nu s-a gasit organizatia cu ID-ul [" + organizationId + "].");
        	}
        } else {
        	throw new IllegalArgumentException("Un utilizator trebuie sa aiba un parinte.");
        }
        
        user.setUsername(userModel.getUserName());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setFax(userModel.getFax());
        user.setMobile(userModel.getMobile());
        String password = userModel.getPassword();
        if (StringUtils.isNotEmpty(password)) {
        	user.setPassword(password);
        }
        user.setTitle(userModel.getTitle());
        user.setCustomTitleTemplate(userModel.getCustomTitleTemplate());
        user.setEmployeeNumber(userModel.getEmployeeNumber());
      
        // delete roles which doesn't exist in model
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