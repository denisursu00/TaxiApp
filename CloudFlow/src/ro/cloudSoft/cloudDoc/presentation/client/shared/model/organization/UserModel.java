/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithDisplayName;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.organization.UserDisplayNameAdditionalPropertyProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.BaseTreeModelWithAdditionalProperties;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserModel extends BaseTreeModelWithAdditionalProperties implements ModelWithId, ModelWithDisplayName, IsSerializable {

	private static final long serialVersionUID = 1L;
	
	private static final String PLACEHOLDER_CUSTOM_TITLE_TEMPLATE_TITLE = "{title}";
	
	public static final String USER_PROPERTY_USERID = "userId";
	public static final String USER_PROPERTY_ORGANIZATION_ID = "organizationId";
	public static final String USER_PROPERTY_ORGANIZATION_UNIT_ID = "organizationUnitId";
    public static final String USER_PROPERTY_USERNAME = "userName";
    public static final String USER_PROPERTY_NAME = "name";
    public static final String USER_PROPERTY_FIRSTNAME = "firstName";
    public static final String USER_PROPERTY_LASTNAME = "lastName";
    public static final String USER_PROPERTY_PASSWORD = "password";
    public static final String USER_PROPERTY_TITLE = "title";
    public static final String USER_PROPERTY_CUSTOM_TITLE_TEMPLATE = "customTitleTemplate";
    public static final String USER_PROPERTY_PHONE = "phone";
    public static final String USER_PROPERTY_EMAIL = "email";
    public static final String USER_PROPERTY_SUBSTITUTE_ID = "substituteId";
    public static final String USER_PROPERTY_IS_MANAGER = "isManager";
    public static final String USER_PROPERTY_EMPLOYEE_NUMBER = "employeeNumber";
    
    public static final String USER_PROPERTY_DISPLAY_NAME = "displayName";
    
    private String fax;
    private String mobile;
    private String type;
    private List<RoleModel> roles;
    
    public UserModel() {}

    public UserModel(String userId, String userName) {
        this();
        setUserId(userId);
        setUserName(userName);
    }
	
	@Override
	public AdditionalPropertyProviderForModel[] getAdditionalPropertyProviders() {
		return new AdditionalPropertyProviderForModel[] {
			new UserDisplayNameAdditionalPropertyProvider(USER_PROPERTY_DISPLAY_NAME, this)
		};
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
    
    public String getOrganizationId() {
    	return get(USER_PROPERTY_ORGANIZATION_ID);
    }
    
    public void setOrganizationId(String organizationId) {
    	set(USER_PROPERTY_ORGANIZATION_ID, organizationId);
    }
    
    public String getOrganizationUnitId() {
    	return get(USER_PROPERTY_ORGANIZATION_UNIT_ID);
    }
    
    public void setOrganizationUnitId(String organizationUnitId) {
    	set(USER_PROPERTY_ORGANIZATION_UNIT_ID, organizationUnitId);
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
    
    public String getTitle(){
        return (String)get(USER_PROPERTY_TITLE);
    }

    public void setTitle(String title){
        set(USER_PROPERTY_TITLE, title);
    }
    
	public String getCustomTitleTemplate() {
		return get(USER_PROPERTY_CUSTOM_TITLE_TEMPLATE);
	}
	
	public void setCustomTitleTemplate(String customTitleTemplate) {
		set(USER_PROPERTY_CUSTOM_TITLE_TEMPLATE, customTitleTemplate);
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
    
    public String getSubstituteId(){
        return (String)get(USER_PROPERTY_SUBSTITUTE_ID);
    }

    public void setSubstituteId(String substituteId){
        set(USER_PROPERTY_SUBSTITUTE_ID, substituteId);
    }
    
    public Boolean isIsManager(){
        return (Boolean)get(USER_PROPERTY_IS_MANAGER);
    }

    public void setIsManager(Boolean isManager){
        set(USER_PROPERTY_IS_MANAGER, isManager);
    }
    
    public String getEmployeeNumber(){
        return (String)get(USER_PROPERTY_EMPLOYEE_NUMBER);
    }

    public void setEmployeeNumber(String employeeNumber){
        set(USER_PROPERTY_EMPLOYEE_NUMBER, employeeNumber);
    }
    
    @Override
    public String getDisplayName() {
    	if (GwtStringUtils.isNotBlank(getTitle())) {
    		if (GwtStringUtils.isNotBlank(getCustomTitleTemplate())) {
    			return (getNameWithTitle(getName(), getTitleByTemplate()));
    		} else {
    			return (getNameWithTitle(getName(), getTitle()));
    		}
    	} else {
    		return getName();
    	}
    }
    
    private String getTitleByTemplate() {
    	return getCustomTitleTemplate().replace(PLACEHOLDER_CUSTOM_TITLE_TEMPLATE_TITLE, getTitle());
    }
    
    private String getNameWithTitle(String name, String title) {
    	return (name + " - " + title);
    }
    
    public String getFax() {
		return fax;
	}
    
    public void setFax(String fax) {
		this.fax = fax;
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
    }
}