package ro.cloudSoft.cloudDoc.web.security;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class MultipleAccountsAuthenticationHelper implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(MultipleAccountsAuthenticationHelper.class);
	
	private static final String EXCEPTION_MESSAGE_BAD_CREDENTIALS = "Bad credentials";
	private static final String EXCEPTION_MESSAGE_DEACTIVATED_ACCOUNT = "Deactivated account";
	
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
	
	private BadCredentialsException getDeactivatedAccountException() {
		return new BadCredentialsException(EXCEPTION_MESSAGE_DEACTIVATED_ACCOUNT);
	}
	
	public Authentication attemptAuthentication(AuthenticationRequestInfo requestInfo) throws AuthenticationException {
		
		String username = requestInfo.getUsername();
		String password = requestInfo.getPassword();
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw getBadCredentialsException();
		}
		
		if (!authenticationVerifier.userExists(username, password)) {
			
			String logMessage = "S-a incercat autentificarea cu username-ul " +
				"[" + username + "], insa username-ul si/sau parola sunt gresite.";
			LOGGER.error(logMessage, "autentificare");
			
			throw getBadCredentialsException();
		}
		
		Set<Long> idsOfActiveUserAccountsForUsername = userService.getIdsOfActiveUserAccountsForUsername(username);
		if (idsOfActiveUserAccountsForUsername.size() == 0) {
			List<User> userAccountsWithUsername = userService.getUsersWithUsername(username);
			if (userAccountsWithUsername.isEmpty()) {
				
				String logMessage = "NU exista in aplicatie utilizator cu username-ul [" + username + "].";
				LOGGER.error(logMessage, "autentificare");
				
				throw getBadCredentialsException();
			} else {
				
				String logMessage = "S-a incercat autentificarea cu username-ul " +
					"[" + username + "], insa contul este dezactivat.";
				LOGGER.error(logMessage, "autentificare");
				
				throw getDeactivatedAccountException();
			}
		} else if (idsOfActiveUserAccountsForUsername.size() == 1) {
			return userWithAccountAuthenticationBuilder.buildForUniqueAccount(username);
		} else {
			
			// TODO - Trebuie vazut si de cazul acesta cu mai multe conturi - dar mai incolo - intrucat din frontend nu se face nimic inca.
			
			// Are mai multe conturi active.
			
			String idAsString = requestInfo.getUserId();
			if (StringUtils.isBlank(idAsString)) {
				throw new MultipleAccountsAuthenticationException(username, password);
			}
			
			Long id = null;
			try {
				id = Long.valueOf(idAsString);
			} catch (NumberFormatException nfe) {
				LOGGER.error("ID de user invalid: [" + idAsString + "]", "autentificare");
				throw getBadCredentialsException();
			}
			
			if (!userService.userExists(id)) {
				LOGGER.error("ID de user care nu exista: [" + idAsString + "]", "autentificare");
				throw getBadCredentialsException();
			}
			
			return userWithAccountAuthenticationBuilder.buildForId(id);
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
