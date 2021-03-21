package ro.taxiApp.docs.web.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ro.taxiApp.docs.web.security.AuthenticationHelper.AuthenticationRequestInfo;


public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	
	private AuthenticationHelper authenticationHelper;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {		
		String username = authentication.getName();
        String password = null;
        if (authentication.getCredentials() != null) {
        	password = authentication.getCredentials().toString();
        }
		return authenticationHelper.attemptAuthentication(new AuthenticationRequestInfo(username, password, null));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
	
	public void setAuthenticationHelper(AuthenticationHelper authenticationHelper) {
		this.authenticationHelper = authenticationHelper;
	}	
}
