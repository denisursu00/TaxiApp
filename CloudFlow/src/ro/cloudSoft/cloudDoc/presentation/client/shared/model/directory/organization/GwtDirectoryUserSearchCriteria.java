package ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtDirectoryUserSearchCriteria implements IsSerializable {
	
	private String username;
	private String firstName;
	private String lastName;
	
	public String getUsername() {
		return username;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}