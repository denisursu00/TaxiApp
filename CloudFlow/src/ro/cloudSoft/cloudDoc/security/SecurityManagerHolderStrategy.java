package ro.cloudSoft.cloudDoc.security;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface SecurityManagerHolderStrategy {

	SecurityManager getSecurityManager();
	
	void setSecurityManager(SecurityManager securityManager);
	
	void clearSecurityManager();
}
