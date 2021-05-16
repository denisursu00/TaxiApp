package ro.taxiApp.docs.web.security;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.services.organization.UserService;
import ro.taxiApp.docs.utils.PasswordEncoder;

/**
 * 
 */
public class SpringSecurityDatabaseAuthenticationVerifier implements AuthenticationVerifier, InitializingBean {
	
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(userService);
	}

	@Override
	public boolean userExists(String username, String password) {
		try {
			User user = userService.getUserByUsername(username);	
			String passwordHash = passwordEncoder.generatePasswordHash(password);
			return (user != null && user.getPassword().equals(passwordHash));
		} catch (Exception ex) {
			return false;
		}
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
}