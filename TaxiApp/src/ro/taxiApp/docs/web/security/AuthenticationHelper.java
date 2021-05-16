package ro.taxiApp.docs.web.security;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.services.organization.UserService;

public class AuthenticationHelper implements InitializingBean {
	
	private static final String EXCEPTION_MESSAGE_BAD_CREDENTIALS = "Bad credentials";
	
	private UserService userService;
	private AuthenticationVerifier authenticationVerifier;
	private UserWithAccountAuthenticationBuilder userWithAccountAuthenticationBuilder;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			userService,
			authenticationVerifier,
			userWithAccountAuthenticationBuilder
		);
	}
	
	private BadCredentialsException getBadCredentialsException() {
		return new BadCredentialsException(EXCEPTION_MESSAGE_BAD_CREDENTIALS);
	}
	
	public Authentication attemptAuthentication(AuthenticationRequestInfo requestInfo) throws AuthenticationException {
		
		String username = requestInfo.getUsername();
		String password = requestInfo.getPassword();
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw getBadCredentialsException();
		}
		
		if (!authenticationVerifier.userExists(username, password)) {
			
			throw getBadCredentialsException();
		}
		
		User user = userService.getUserByUsername(username);
		
		if (user != null) {
			return userWithAccountAuthenticationBuilder.buildForId(user.getId());
		} else {
			throw getBadCredentialsException();
		}
		
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setAuthenticationVerifier(AuthenticationVerifier authenticationVerifier) {
		this.authenticationVerifier = authenticationVerifier;
	}
	
	public void setUserWithAccountAuthenticationBuilder(UserWithAccountAuthenticationBuilder userWithAccountAuthenticationBuilder) {
		this.userWithAccountAuthenticationBuilder = userWithAccountAuthenticationBuilder;
	}
	
	public static class AuthenticationRequestInfo {
		
		private String username;
		private String password;
		private String userId;
		
		public AuthenticationRequestInfo() {
		}
		
		public AuthenticationRequestInfo(String username, String password, String userId) {
			this.username = username;
			this.password = password;
			this.userId = userId;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
	}
}
