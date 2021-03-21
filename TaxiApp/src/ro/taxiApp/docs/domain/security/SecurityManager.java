package ro.taxiApp.docs.domain.security;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class SecurityManager implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private String userIdAsString;
	private String userUsername;
	
	
	
	public String getDisplayName() {
		return (getUserUsername());
	}
	
	@Override
	public String toString() {
		return getDisplayName();
	}
	
	public Long getUserId() {
		if (StringUtils.isNotBlank(userIdAsString)) {
			return Long.valueOf(userIdAsString);
		} else {
			return null;
		}
	}
	
	public String getUserIdAsString() {
		return userIdAsString;
	}
	public void setUserIdAsString(String userIdAsString) {
		this.userIdAsString = userIdAsString;
	}
	public String getUserUsername() {
		return userUsername;
	}
	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}
}