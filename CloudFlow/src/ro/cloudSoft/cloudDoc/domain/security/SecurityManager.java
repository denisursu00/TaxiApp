package ro.cloudSoft.cloudDoc.domain.security;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class SecurityManager implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private String userIdAsString;
	private String userUsername;	
	private String userTitle;
	
	/**
	 * toate unitatile organizatorice din care utilizatorul face parte
	 * (cea "directa" si "stramosii")
	 */
	private List<Long> organizationUnitIds = Lists.newLinkedList();
	/**
	 * toate grupurile din care utilizatorul face parte
	 */
	private List<Long> groupIds = Lists.newLinkedList();
	
	private List<String> groupNames = Lists.newLinkedList();
	
	public String getDisplayName() {
		return (getUserUsername() + " - " + getUserTitle());
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
	public List<Long> getOrganizationUnitIds() {
		return organizationUnitIds;
	}
	public void setOrganizationUnitIds(List<Long> organizationUnitIds) {
		this.organizationUnitIds = organizationUnitIds;
	}
	public List<Long> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}
	public List<String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}
	public String getUserTitle() {
		return userTitle;
	}
	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}
}