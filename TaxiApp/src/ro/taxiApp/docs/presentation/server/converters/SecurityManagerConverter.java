package ro.taxiApp.docs.presentation.server.converters;

import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;

public class SecurityManagerConverter {

	public static SecurityManagerModel getModelFromSecurityManager(SecurityManager securityManager) {
		
		SecurityManagerModel securityManagerModel = new SecurityManagerModel();
		
		securityManagerModel.setUserIdAsString(securityManager.getUserIdAsString());
		securityManagerModel.setUserUsername(securityManager.getUserUsername());
		
		return securityManagerModel;
	}
}