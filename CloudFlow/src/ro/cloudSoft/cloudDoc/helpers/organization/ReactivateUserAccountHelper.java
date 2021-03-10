package ro.cloudSoft.cloudDoc.helpers.organization;

import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.Sets;

public class ReactivateUserAccountHelper {

	private final Long userId;
	
	private final UserService userService;
	private final GroupService groupService;
	
	private final SecurityManager userSecurity;
	
	private User user;
	private UserDeactivation userDeactivation;
	private Set<Group> groupsToSave;
	
	public ReactivateUserAccountHelper(Long userId, UserService userService,
			GroupService groupService, SecurityManager userSecurity) {
		
		this.userId = userId;
		
		this.userService = userService;
		this.groupService = groupService;
		
		this.userSecurity = userSecurity;
	}
	
	public void reactivate() throws AppException {
		
		init();
		
		retrieveUser();
		checkIfNotAlreadyActive();
		retrieveUserDeactivation();
		
		setOldPropertiesBackToUser();
		setOldParentsBackToUser();
		addUserBackToGroups();
		
		persistModifications();
	}
	
	private void init() {
		user = null;
		userDeactivation = null;
		groupsToSave = Sets.newLinkedHashSet();
	}
	
	private void retrieveUser() {
		user = userService.getUserById(userId);
		if (user == null) {
			throw new IllegalArgumentException("Nu s-a gasit utilizatorul cu ID-ul [" + userId + "].");
		}
	}
	
	private void checkIfNotAlreadyActive() throws AppException {
		if (userService.isUserAccountActive(user)) {
			throw new AppException(AppExceptionCodes.USER_IS_ALREADY_ACTIVE);
		}
	}
	
	private void retrieveUserDeactivation() throws AppException {
		userDeactivation = userService.getDeactivationForUserWithId(userId);
		if (userDeactivation == null) {
			throw new AppException(AppExceptionCodes.NO_DATA_FOR_USER_REACTIVATION);
		}
	}
	
	private void setOldPropertiesBackToUser() {
		user.setTitle(userDeactivation.getUserOldTitle());
	}
	
	private void setOldParentsBackToUser() {
		user.setOrganization(userDeactivation.getParentOrganization());
		user.setOu(userDeactivation.getParentOrganizationUnit());
	}
	
	private void addUserBackToGroups() {
		groupsToSave = userDeactivation.getGroups();
		for (Group group : groupsToSave) {
			group.getUsers().add(user);
		}
	}
	
	private void persistModifications() throws AppException {
		userService.setUser(user, userSecurity);
		userService.deleteUserDeactivation(userDeactivation);
		groupService.updateGroups(groupsToSave);
	}
}