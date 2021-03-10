package ro.cloudSoft.cloudDoc.domain.directory.organization;

/**
 *  
 */
public class DirectoryUserSearchCriteria {
	
	private String firstName;
	private String lastName;
	private String username;
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}