package ro.taxiApp.docs.web.rest.resources;

import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.security.SecurityManagerHolder;

public class BaseResource {

	public SecurityManager getSecurity() {
		return SecurityManagerHolder.getSecurityManager();
    }
    
}
