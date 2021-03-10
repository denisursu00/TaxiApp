package ro.cloudSoft.cloudDoc.security;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.web.security.UserWithAccountAuthentication;

public class SecurityManagerFactory {
	
	private static final String SESSION_ATTRIBUTE_NAME_SECURITY_MANAGER = "SECURITY_MANAGER";
	
	private UserService userService;
	private GroupService groupService;
	
	public SecurityManager getSecurityManager(HttpSession session) {
		return getHttpSessionSecurityManager(session);
	}
	
	public SecurityManager getSecurityManager(String username) {
		return getSecurityManagerByUser(username);
	}
	
	public SecurityManager getSecurityManager(Long userId) {
		return getSecurityManagerByUserId(userId);
	}
	
	private SecurityManager getHttpSessionSecurityManager(HttpSession session) {
		
		SecurityManager userSecurity = (SecurityManager) session.getAttribute(SESSION_ATTRIBUTE_NAME_SECURITY_MANAGER);
        if (userSecurity != null) {
        	return userSecurity;
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
        	return null;
        }
        if (!(authentication instanceof UserWithAccountAuthentication)) {
        	String exceptionMessage = "Datele utilizatorului autentificat cu Spring nu sunt de tipul asteptat ([" + UserWithAccountAuthentication.class + "]), " +
    			"ci de tipul [" + authentication.getClass() + "]. Obiectul gasit este: [" + authentication + "].";
        	throw new IllegalStateException(exceptionMessage);
        }
        
        UserWithAccountAuthentication userWithAccountAuthentication = (UserWithAccountAuthentication) authentication;
        Long userId = userWithAccountAuthentication.getUserId();
        
        User user = userService.getUserById(userId);
        if (user == null) {
        	String exceptionMessage = "Utilizatorul autentificat are ID-ul [" + userId + "], insa NU s-a gasit in aplicatie utilizator cu acel ID.";
        	throw new IllegalStateException(exceptionMessage);
        }
        
        userSecurity = new SecurityManager();
        userSecurity.setUserIdAsString(user.getId().toString());
        userSecurity.setUserUsername(user.getUsername());
        userSecurity.setUserTitle(user.getTitle());

        // Adauga ID-urile tuturor unitatilor organizatorice din care utilizatorul face parte.
        userSecurity.setOrganizationUnitIds(userService.getOrganizationUnitIds(user.getId()));            
        // Adauga ID-urile tuturor grupurilor din care utilizatorul face parte.
        userSecurity.setGroupIds(userService.getGroupIds(user.getId()));
        
        userSecurity.setGroupNames(groupService.getGroupNamesOfUserWithId(userId));

        session.setAttribute(SESSION_ATTRIBUTE_NAME_SECURITY_MANAGER, userSecurity);
        
        return userSecurity;
	}
	
	private SecurityManager getSecurityManagerByUser(String username) {
		
        User user = userService.getUserByUsername(username);
        if (user == null) {
        	String exceptionMessage = "Utilizatorul cu username [" + username + "] NU s-a gasit in aplicatie.";
        	throw new IllegalStateException(exceptionMessage);
        }
        
        SecurityManager userSecurity = new SecurityManager();
        userSecurity.setUserIdAsString(user.getId().toString());
        userSecurity.setUserUsername(user.getUsername());
        userSecurity.setUserTitle(user.getTitle());

        userSecurity.setOrganizationUnitIds(userService.getOrganizationUnitIds(user.getId()));            
        userSecurity.setGroupIds(userService.getGroupIds(user.getId()));
        
        userSecurity.setGroupNames(groupService.getGroupNamesOfUserWithId(user.getId()));
        
        return userSecurity;
	}
	
	private SecurityManager getSecurityManagerByUserId(Long userId) {
		
		User user = userService.getUserById(userId);
        if (user == null) {
        	String exceptionMessage = "Utilizatorul cu user ID [" + userId + "] NU s-a gasit in aplicatie.";
        	throw new IllegalStateException(exceptionMessage);
        }
        
        SecurityManager userSecurity = new SecurityManager();
        userSecurity.setUserIdAsString(user.getId().toString());
        userSecurity.setUserUsername(user.getUsername());
        userSecurity.setUserTitle(user.getTitle());

        userSecurity.setOrganizationUnitIds(userService.getOrganizationUnitIds(user.getId()));            
        userSecurity.setGroupIds(userService.getGroupIds(user.getId()));
        
        userSecurity.setGroupNames(groupService.getGroupNamesOfUserWithId(user.getId()));
        
        return userSecurity;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
}
