package ro.cloudSoft.cloudDoc.services.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.Permission;
import ro.cloudSoft.cloudDoc.domain.organization.Role;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoggedInUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoginRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoginResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.PasswordChangeModel;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.PasswordEncoder;
import ro.cloudSoft.cloudDoc.web.security.JwtTokenProvider;
import ro.cloudSoft.cloudDoc.web.security.UserWithAccountAuthentication;

public class AuthServiceImpl implements AuthService {
	
	private AuthenticationManager authenticationManager;	
	
	private JwtTokenProvider jwtTokenProvider;	
	
	private UserService userService;
	
	private SecurityManagerFactory securityManagerFactory;
	
	private PasswordEncoder passwordEncoder;
		
	@Override
	public LoginResponseModel login(LoginRequestModel loginRequest) throws AppException {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
			
			UserWithAccountAuthentication authentication = (UserWithAccountAuthentication) this.authenticationManager.authenticate(authenticationToken);
			SecurityContext context = SecurityContextHolder.createEmptyContext();
	        context.setAuthentication(authentication);
	        SecurityContextHolder.setContext(context);
	        
	        SecurityManager securityManager = securityManagerFactory.getSecurityManager(authentication.getUserId());
			SecurityManagerHolder.setSecurityManager(securityManager);
			
	        String authToken = jwtTokenProvider.createToken(authentication, loginRequest.isRememberMe());
	        LoginResponseModel loginResponse = new LoginResponseModel();
	        loginResponse.setAuthToken(authToken);
	    	loginResponse.setLoggedInUser(prepareLoggedInUser(authentication));
	    	
	    	return loginResponse;
	    	
		} catch (BadCredentialsException ae) {
			throw new AppException(AppExceptionCodes.BAD_CREDENTIALS);
		} catch (Exception e) {
			throw new AppException(AppExceptionCodes.AUTHENTICATION_EXCEPTION);
		}
	}
	
	@Override
	public LoggedInUserModel getLoggedInUser() {
		UserWithAccountAuthentication authentication = (UserWithAccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return prepareLoggedInUser(authentication);
	}
	
	private LoggedInUserModel prepareLoggedInUser(UserWithAccountAuthentication authentication) {
		
		User user = userService.getUserById(authentication.getUserId());
		
		LoggedInUserModel loggedInUser = new LoggedInUserModel();
		loggedInUser.setId(user.getId());
		loggedInUser.setUsername(user.getUsername());
		loggedInUser.setFirstName(user.getFirstName());
		loggedInUser.setLastName(user.getLastName());
		loggedInUser.setTitle(user.getTitle());	
		Set<String> permissionNames = new HashSet<>();
		Set<Role> roles = user.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				List<Permission> permissions = role.getPermissions();
				if (CollectionUtils.isNotEmpty(permissions)) {
					for (Permission permission : permissions) {
						permissionNames.add(permission.getName());
					}
				}
			}
		}
		loggedInUser.setPermissions(permissionNames);
		
		return loggedInUser;
	}
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}

	@Override
	public void changePassword(PasswordChangeModel passwordChangeModel, SecurityManager securityManager) throws AppException {
		User user = userService.getUserById(securityManager.getUserId());
		if (user == null) {
			throw new AppException(AppExceptionCodes.AUTHENTICATION_EXCEPTION);
		}
		if (!user.getPassword().equals(passwordEncoder.generatePasswordHash(passwordChangeModel.getCurrentPassword()))) {
			throw new AppException(AppExceptionCodes.INVALID_PASSWORD);
		}
		
		user.setPassword(passwordEncoder.generatePasswordHash(passwordChangeModel.getNewPassword()));
		userService.setUser(user, securityManager);
		
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	
}
