package ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DirectoryUserModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_USERNAME = "username";
	public static final String PROPERTY_FIRST_NAME = "firstName";
	public static final String PROPERTY_LAST_NAME = "lastName";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_EMAIL = "email";
	public static final String PROPERTY_TITLE = "title";
	public static final String PROPERTY_EMPLOYEE_NUMBER = "employeeNumber";
	
	public String getUsername() {
        return get(PROPERTY_USERNAME);
    }
    public void setUsername(String username) {
        set(PROPERTY_USERNAME, username);
    }
    public String getFirstName() {
    	return get(PROPERTY_FIRST_NAME);
    }
    public void setFirstName(String firstName) {
    	set(PROPERTY_FIRST_NAME, firstName);
    }
    public String getLastName() {
    	return get(PROPERTY_LAST_NAME);
    }
    public void setLastName(String lastName) {
    	set(PROPERTY_LAST_NAME, lastName);
    }
    public String getPassword() {
    	return get(PROPERTY_PASSWORD);
    }
    public void setPassword(String password) {
    	set(PROPERTY_PASSWORD, password);
    }
    public String getEmail() {
    	return get(PROPERTY_EMAIL);
    }
    public void setEmail(String email) {
    	set(PROPERTY_EMAIL, email);
    }
    public String getTitle() {
    	return get(PROPERTY_TITLE);
    }
    public void setTitle(String title) {
    	set(PROPERTY_TITLE, title);
    }
    public String getEmployeeNumber() {
    	return get(PROPERTY_EMPLOYEE_NUMBER);
    }
    public void setEmployeeNumber(String employeeNumber) {
    	set(PROPERTY_EMPLOYEE_NUMBER, employeeNumber);
    }
}