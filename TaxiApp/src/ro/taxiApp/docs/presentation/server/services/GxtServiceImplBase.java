package ro.taxiApp.docs.presentation.server.services;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.PermissionModel;
import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;
import ro.taxiApp.docs.presentation.client.shared.services.GxtServiceBase;
import ro.taxiApp.docs.security.SecurityManagerHolder;

/**
 * Clasa de baza pentru implementarea serviciilor GWT RPC
 * 
 * 
 */
public abstract class GxtServiceImplBase implements GxtServiceBase {

	public SecurityManager getSecurity() {
        return SecurityManagerHolder.getSecurityManager();
    }
    
    protected HttpSession getSession() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        if (session == null) {
        	String message = "S-a incercat luarea sesiunii prin intermediul " +
    			"GwtServiceBase dintr-un context fara legatura cu partea de server GWT.";
        	throw new IllegalStateException(message);
        }
        return session;
    }

    /** {@inheritDoc} */
	@Override
	public void dummy(PermissionModel permissionModel, SecurityManagerModel securityManagerModel, UserModel userModel)
		throws PresentationException {}
}