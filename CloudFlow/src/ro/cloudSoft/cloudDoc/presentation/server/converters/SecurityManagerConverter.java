package ro.cloudSoft.cloudDoc.presentation.server.converters;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public class SecurityManagerConverter {

	public static SecurityManagerModel getModelFromSecurityManager(SecurityManager securityManager) {
		
		SecurityManagerModel securityManagerModel = new SecurityManagerModel();
		
		securityManagerModel.setUserIdAsString(securityManager.getUserIdAsString());
		securityManagerModel.setUserUsername(securityManager.getUserUsername());
		
		return securityManagerModel;
	}
}