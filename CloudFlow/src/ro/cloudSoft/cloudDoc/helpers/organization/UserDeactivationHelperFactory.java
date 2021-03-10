package ro.cloudSoft.cloudDoc.helpers.organization;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivationMode;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class UserDeactivationHelperFactory implements InitializingBean {
	
    private String titleSuffixForDeactivatedUsers;
    private String nameForOrganizationUnitWithDeactivatedUsers;
    
    private OrganizationUnitService organizationUnitService;
    private GroupService groupService;
    private UserService userService;
    
    @Override
    public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			titleSuffixForDeactivatedUsers,
			nameForOrganizationUnitWithDeactivatedUsers,
			
			organizationUnitService,
			groupService,
			userService
		);
    }

	public AbstractUserDeactivationHelper getHelperFor(Long userId, UserDeactivationMode deactivationMode) {
		if (deactivationMode.equals(UserDeactivationMode.SINGLE_ACCOUNT)) {
			return new SingleUserAccountDeactivationHelper(titleSuffixForDeactivatedUsers,
				nameForOrganizationUnitWithDeactivatedUsers, userId, organizationUnitService,
				groupService, userService);
		} else if (deactivationMode.equals(UserDeactivationMode.ALL_ACCOUNTS)) {
			return new AllUserAccountsDeactivationHelper(titleSuffixForDeactivatedUsers,
				nameForOrganizationUnitWithDeactivatedUsers, userId, organizationUnitService,
				groupService, userService);
		} else {
			throw new IllegalArgumentException("Tip necunoscut de dezactivare: [" + deactivationMode + "]");
		}
	}
	
    public void setTitleSuffixForDeactivatedUsers(String titleSuffixForDeactivatedUsers) {
		this.titleSuffixForDeactivatedUsers = titleSuffixForDeactivatedUsers;
	}
    public void setNameForOrganizationUnitWithDeactivatedUsers(String nameForOrganizationUnitWithDeactivatedUsers) {
		this.nameForOrganizationUnitWithDeactivatedUsers = nameForOrganizationUnitWithDeactivatedUsers;
	}
    public void setOrganizationUnitService(OrganizationUnitService organizationUnitService) {
		this.organizationUnitService = organizationUnitService;
	}
    public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
    public void setUserService(UserService userService) {
		this.userService = userService;
	}
}