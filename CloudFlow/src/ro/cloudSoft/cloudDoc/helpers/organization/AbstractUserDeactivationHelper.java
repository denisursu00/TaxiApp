package ro.cloudSoft.cloudDoc.helpers.organization;

import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractUserDeactivationHelper {
	
	
    private final String titleSuffixForDeactivatedUsers;
    private final String nameForOrganizationUnitWithDeactivatedUsers;
    
    private final OrganizationUnitService organizationUnitService;
    private final GroupService groupService;
    private final UserService userService;
    
    
    private OrganizationUnit organizationUnitWithDeactivatedUsers;
    private Set<Group> groupsToUpdate;
    private List<User> userAccountsToDeactivate;
    private List<UserDeactivation> userDeactivations;
	
    
	protected AbstractUserDeactivationHelper(String titleSuffixForDeactivatedUsers, String nameForOrganizationUnitWithDeactivatedUsers,
			OrganizationUnitService organizationUnitService, GroupService groupService, UserService userService) {
		
		this.titleSuffixForDeactivatedUsers = titleSuffixForDeactivatedUsers;
		this.nameForOrganizationUnitWithDeactivatedUsers = nameForOrganizationUnitWithDeactivatedUsers;
		
		this.organizationUnitService = organizationUnitService;
		this.groupService = groupService;
		this.userService = userService;
	}

	public void deactivate() throws AppException {
		
		init();
		prepareOrganizationUnitWithDeactivatedUsers();
		
		userAccountsToDeactivate = getUserAccountsToDeactivate();
		if (userAccountsToDeactivate.isEmpty()) {
			throw new IllegalStateException("Nu s-au gasit conturi de dezactivat.");
		}
		
		for (User userAccountToDeactivate : userAccountsToDeactivate) {
			deactivateUserAccount(userAccountToDeactivate);
		}
		
		persistModifications();
		
	}
	
	private void init() {
		organizationUnitWithDeactivatedUsers = null;
		groupsToUpdate = Sets.newHashSet();
		userAccountsToDeactivate = Lists.newArrayList();
		userDeactivations = Lists.newArrayList();
	}
	
	private void prepareOrganizationUnitWithDeactivatedUsers() {
		organizationUnitWithDeactivatedUsers = organizationUnitService.getOrganizationUnitByName(nameForOrganizationUnitWithDeactivatedUsers);
		if (organizationUnitWithDeactivatedUsers == null) {
			throw new IllegalStateException("Nu s-a gasit unitatea organizatorica pentru utilizatorii dezactivati (cu numele [" + nameForOrganizationUnitWithDeactivatedUsers + "]).");
		}
	}

	protected abstract List<User> getUserAccountsToDeactivate() throws AppException;
	
	private void deactivateUserAccount(User userAccountToDeactivate) {
		
		addDeactivationEntryForUserAccount(userAccountToDeactivate);
		
		for (Group groupOfUser : userAccountToDeactivate.getGroups()) {
			groupOfUser.getUsers().remove(userAccountToDeactivate);
			groupsToUpdate.add(groupOfUser);
		}
		
		setParentForDeactivatedUserAccount(userAccountToDeactivate);
		setUsernameForDeactivatedUserAccount(userAccountToDeactivate);
	}
	
	private void addDeactivationEntryForUserAccount(User userAccount) {
		
		UserDeactivation userDeactivation = new UserDeactivation();
		
		userDeactivation.setUser(userAccount);
		userDeactivation.setUserOldTitle(userAccount.getTitle());
		
		userDeactivation.setParentOrganization(userAccount.getOrganization());
		userDeactivation.setParentOrganizationUnit(userAccount.getOu());
		
		Set<Group> groupsOfUserCopy = Sets.newHashSet(userAccount.getGroups());
		userDeactivation.setGroups(groupsOfUserCopy);
		
		userDeactivations.add(userDeactivation);
	}
	
	private void setParentForDeactivatedUserAccount(User deactivatedUserAccount) {
		deactivatedUserAccount.setOu(organizationUnitWithDeactivatedUsers);
		deactivatedUserAccount.setOrganization(null);
	}
	
	private void setUsernameForDeactivatedUserAccount(User deactivatedUserAccount) {
		String oldTitle = deactivatedUserAccount.getTitle();
		String newTitle = (oldTitle + " " + titleSuffixForDeactivatedUsers);
		deactivatedUserAccount.setTitle(newTitle);
	}
	
	private void persistModifications() {
		userService.updateUsers(userAccountsToDeactivate);
		groupService.updateGroups(groupsToUpdate);
		userService.saveUserDeactivations(userDeactivations);
	}
}