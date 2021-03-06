package ro.taxiApp.docs.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.services.organization.UserService;


public class UserWithAccountAuthenticationBuilder implements InitializingBean {

	private UserService userService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			userService
		);
	}
	
	private UserWithAccountAuthentication buildForUser(User user) {
		List<String> roleNames = userService.getUserRoleNames(user.getId());
		Collection<GrantedAuthority> authorities = new ArrayList<>();		
		if (CollectionUtils.isNotEmpty(roleNames)) {
			authorities = AuthorityUtils.createAuthorityList(roleNames.toArray(new String[roleNames.size()]));
		}
		return new UserWithAccountAuthentication(user.getId(), user.getUsername(), authorities);
	}
	
	public UserWithAccountAuthentication buildForId(Long userId) {
		
		User user = userService.getUserById(userId);
		if (user == null) {
			throw new IllegalArgumentException("Nu s-a gasit utilizator cu ID-ul [" + userId + "].");
		}
		
		return buildForUser(user);
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}