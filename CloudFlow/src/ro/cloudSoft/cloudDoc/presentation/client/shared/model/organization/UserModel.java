/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String USER_PROPERTY_USERID = "userId";
    public static final String USER_PROPERTY_USERNAME = "userName";
    public static final String USER_PROPERTY_NAME = "name";
    public static final String USER_PROPERTY_FIRSTNAME = "firstName";
    public static final String USER_PROPERTY_LASTNAME = "lastName";
    public static final String USER_PROPERTY_PASSWORD = "password";
    public static final String USER_PROPERTY_PHONE = "phone";
    public static final String USER_PROPERTY_EMAIL = "email";
    
    public static final String USER_PROPERTY_DISPLAY_NAME = "displayName";
    
    private String mobile;
    private String type;
    private List<RoleModel> roles;  
    
    public UserModel() {}

    /*public UserModel(String userId, String userName) {
        this();
        setUserId(userId);
        setUserName(userName);
    }
    
    public String getUserId(){
        return (String)get(USER_PROPERTY_USERID);
    }
    
    @Override
    public Object getId() {
    	return getUserId();
    }
    
    public Long getUserIdAsLong() {
    	String userIdAsString = getUserId();
    	if (userIdAsString != null) {
    		return Long.valueOf(userIdAsString);
    	}
    	return null;
    }

    public void setUserId(String userId){
        set(USER_PROPERTY_USERID, userId);
    }

    public String getUserName(){
        return (String)get(USER_PROPERTY_USERNAME);
    }

    public void setUserName(String userName){
        set(USER_PROPERTY_USERNAME, userName);
    }

    public String getName(){
        return (String)get(USER_PROPERTY_NAME);
    }

    public void setName(String name){
        set(USER_PROPERTY_NAME, name);
    }
    
    public String getLastName(){
        return (String)get(USER_PROPERTY_LASTNAME);
    }

    public void setLastName(String lastName){
        set(USER_PROPERTY_LASTNAME, lastName);
    }
    
    public String getFirstName(){
        return (String)get(USER_PROPERTY_FIRSTNAME);
    }

    public void setFirstName(String firstName){
        set(USER_PROPERTY_FIRSTNAME, firstName);
    }
    
    public String getPassword(){
        return (String)get(USER_PROPERTY_PASSWORD);
    }

    public void setPassword(String password){
        set(USER_PROPERTY_PASSWORD, password);
    }
    
    public String getEmail(){
        return (String)get(USER_PROPERTY_EMAIL);
    }

    public void setEmail(String email){
        set(USER_PROPERTY_EMAIL, email);
    }
    
    public String getPhone(){
        return (String)get(USER_PROPERTY_PHONE);
    }

    public void setPhone(String phone){
        set(USER_PROPERTY_PHONE, phone);
    }

    @Override
    public String getDisplayName() {
    	return getName();
    }
    
    public String getMobile() {
		return mobile;
	}
    
    public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleModel> roles) {
		this.roles = roles;
	}

	@Override
    public String toString() {
    	return ("Utilizator (" + getDisplayName() + ")");
    }*/
}