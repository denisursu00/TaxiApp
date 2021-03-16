package ro.cloudSoft.cloudDoc.web.rest.resources;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;

public class BaseResource {

	public SecurityManager getSecurity() {
		return SecurityManagerHolder.getSecurityManager();
    }
    
}
