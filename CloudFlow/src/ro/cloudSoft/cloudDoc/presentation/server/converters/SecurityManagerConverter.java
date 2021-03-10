package ro.cloudSoft.cloudDoc.presentation.server.converters;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public class SecurityManagerConverter {

	public static SecurityManagerModel getModelFromSecurityManager(SecurityManager securityManager,  BusinessConstants businessConstants) {
		
		SecurityManagerModel securityManagerModel = new SecurityManagerModel();
		
		securityManagerModel.setUserIdAsString(securityManager.getUserIdAsString());
		securityManagerModel.setUserUsername(securityManager.getUserUsername());
		securityManagerModel.setUserTitle(securityManager.getUserTitle());		
		securityManagerModel.setOrganizationUnitIds(securityManager.getOrganizationUnitIds());
		securityManagerModel.setGroupIds(securityManager.getGroupIds());
		securityManagerModel.setGroupNames(securityManager.getGroupNames());
		
		String groupNameAdmins = businessConstants.getGroupNameAdmins();
		boolean isUserAdmin = securityManager.getGroupNames().contains(groupNameAdmins);
		securityManagerModel.setUserAdmin(isUserAdmin);
		
		return securityManagerModel;
	}
}