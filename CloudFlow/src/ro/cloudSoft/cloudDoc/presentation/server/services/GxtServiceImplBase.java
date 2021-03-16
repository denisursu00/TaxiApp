package ro.cloudSoft.cloudDoc.presentation.server.services;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;

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