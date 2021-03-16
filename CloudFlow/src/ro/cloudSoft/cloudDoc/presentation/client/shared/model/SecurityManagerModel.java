package ro.cloudSoft.cloudDoc.presentation.client.shared.model;


import com.google.gwt.user.client.rpc.IsSerializable;

public class SecurityManagerModel implements IsSerializable {
	
	private String userIdAsString;
	private String userUsername;

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