package ro.cloudSoft.cloudDoc.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;


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
	
	public UserWithAccountAuthentication buildForUniqueAccount(String username) {
		
		User user = userService.getActiveUserByUsername(username);
		if (user == null) {
			throw new IllegalArgumentException("Nu s-a gasit utilizator cu username-ul [" + username + "].");
		}
		
		checkUserAccountActiveness(user);
		
		return buildForUser(user);
	}
	
	public UserWithAccountAuthentication buildForId(Long userId) {
		
		User user = userService.getUserById(userId);
		if (user == null) {
			throw new IllegalArgumentException("Nu s-a gasit utilizator cu ID-ul [" + userId + "].");
		}

		checkUserAccountActiveness(user);
		
		return buildForUser(user);
	}
	
	private void checkUserAccountActiveness(User userAccount) {
		if (!userService.isUserAccountActive(userAccount)) {
			throw new IllegalArgumentException("Contul de utilizator cu ID-ul [" + userAccount.getId() + "] si username-ul [" + userAccount.getUsername() + "] NU este activ.");
		}
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}