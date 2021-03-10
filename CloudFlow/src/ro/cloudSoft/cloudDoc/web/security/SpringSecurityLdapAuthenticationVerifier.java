package ro.cloudSoft.cloudDoc.web.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class SpringSecurityLdapAuthenticationVerifier implements AuthenticationVerifier, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(SpringSecurityLdapAuthenticationVerifier.class);
	
	private LdapAuthenticator ldapAuthenticator;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(ldapAuthenticator);
	}

	@Override
	public boolean userExists(String username, String password) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
		try {
			DirContextOperations authenticationResult = ldapAuthenticator.authenticate(authentication);
			return (authenticationResult != null);
		} catch (RuntimeException re) {
			LOGGER.warn("Exceptie in timpul autentificarii", re, "userExists");
			return false;
		}
	}

	public void setLdapAuthenticator(LdapAuthenticator ldapAuthenticator) {
		this.ldapAuthenticator = ldapAuthenticator;
	}
}