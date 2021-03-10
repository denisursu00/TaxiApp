package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

public class SecurityManagerModel implements IsSerializable {
	
	private String userIdAsString;
	private String userUsername;
	private String userTitle;
	
	private List<Long> organizationUnitIds = new LinkedList<Long>();
	private List<Long> groupIds = new LinkedList<Long>();
	
	private List<String> groupNames = new LinkedList<String>();
	
	private boolean userAdmin;
	
	public void setUserAdmin(boolean userAdmin) {
		this.userAdmin = userAdmin;
	}
	
	public boolean isUserAdmin() {
		return userAdmin;
	}
	
	/*public boolean isUserAdmin() {
		String groupNameAdmins = GwtRegistryUtils.getBusinessConstants().getGroupNameAdmins();
		return groupNames.contains(groupNameAdmins);
	}*/

	public String getDisplayName() {
		if (GwtStringUtils.isNotBlank(userTitle)) {
			return userUsername + " - " + userTitle;
		}
		return userUsername;
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
	public String getUserTitle() {
		return userTitle;
	}
	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
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
}