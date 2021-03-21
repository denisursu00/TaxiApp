package ro.taxiApp.docs.security;

import ro.taxiApp.docs.domain.security.SecurityManager;

public interface SecurityManagerHolderStrategy {

	SecurityManager getSecurityManager();
	
	void setSecurityManager(SecurityManager securityManager);
	
	void clearSecurityManager();
}
