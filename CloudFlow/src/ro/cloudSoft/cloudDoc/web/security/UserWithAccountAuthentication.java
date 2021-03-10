package ro.cloudSoft.cloudDoc.web.security;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


public class UserWithAccountAuthentication implements Authentication {
	
	private static final long serialVersionUID = 1L;
	
	private final Long userId;
	private final String userUsername;
	private final Collection<GrantedAuthority> authorities;
	
	public UserWithAccountAuthentication(Long userId, String userUsername) {
		this.userId = userId;
		this.userUsername = userUsername;
		this.authorities = null;
	}
	
	public UserWithAccountAuthentication(Long userId, String userUsername, Collection<GrantedAuthority> authorities) {
		this.userId = userId;
		this.userUsername = userUsername;
		this.authorities = authorities;
	}
	
	public Long getUserId() {
		return userId;
	}
	public String getUserUsername() {
		return userUsername;
	}

	@Override
	public String getName() {
		return userUsername;
	}
	
	@Override
	public Object getPrincipal() {
		return userId;
	}
	
	@Override
	public Object getDetails() {
		return userUsername;
	}
	
	@Override
	public Object getCredentials() {
		return "N/A";
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public GrantedAuthority[] getGrantedAuthorities() {
		if (CollectionUtils.isNotEmpty(authorities)) {
			return authorities.toArray(new GrantedAuthority[authorities.size()]);
		}
		return new GrantedAuthority[0];
	}
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}
	
	@Override
	public void setAuthenticated(boolean authenticated) {}
}