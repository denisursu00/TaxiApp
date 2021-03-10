package ro.cloudSoft.cloudDoc.domain.directory.organization;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import ro.cloudSoft.cloudDoc.domain.directory.AbstractDirectoryEntity;

/**
 * 
 */
public class DirectoryUser extends AbstractDirectoryEntity {
	
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String title;
	private String employeeNumber;
	
	public String getUsername() {
		return username;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public String getTitle() {
		return title;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
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
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append(dn)
			.toString();
	}
}