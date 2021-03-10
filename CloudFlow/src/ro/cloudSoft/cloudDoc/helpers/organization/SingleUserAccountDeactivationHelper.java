package ro.cloudSoft.cloudDoc.helpers.organization;

import java.util.Collections;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

public class SingleUserAccountDeactivationHelper extends AbstractUserDeactivationHelper {

	private final Long userId;
	
	private final UserService userService;
	
	public SingleUserAccountDeactivationHelper(String titleSuffixForDeactivatedUsers, String nameForOrganizationUnitWithDeactivatedUsers,
			Long userId, OrganizationUnitService organizationUnitService, GroupService groupService, UserService userService) {
		
		super(titleSuffixForDeactivatedUsers, nameForOrganizationUnitWithDeactivatedUsers, organizationUnitService, groupService, userService);
		
		this.userId = userId;
		
		this.userService = userService;
	}
	
	@Override
	protected List<User> getUserAccountsToDeactivate() throws AppException {

		User userAccount = userService.getUserById(userId);
		if (userAccount == null) {
			throw new IllegalStateException("Nu s-a gasit utilizator cu ID-ul [" + userAccount + "].");
		}
		
		if (!userService.isUserAccountActive(userAccount)) {
			throw new AppException(AppExceptionCodes.ONLY_ACTIVE_USER_ACCOUNTS_CAN_BE_DEACTIVATED);
		}
		
		return Collections.singletonList(userAccount);
	}
}