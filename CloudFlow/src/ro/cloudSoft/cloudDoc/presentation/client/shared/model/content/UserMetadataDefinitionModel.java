package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserMetadataDefinitionModel extends MetadataDefinitionModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ONLY_USERS_FROM_GROUP = "onlyUsersFromGroup";
	public static final String PROPERTY_ID_OF_GROUP_OF_PERMITTED_USERS = "idOfGroupOfPermittedUsers";
	
	public static final String PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER = "autoCompleteWithCurrentUser";
	public static final String PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER_STATE_CODE = "autoCompleteWithCurrentUserStateCode";
	
	public boolean isOnlyUsersFromGroup() {
		Boolean onlyUsersFromGroup = get(PROPERTY_ONLY_USERS_FROM_GROUP);
		return GwtBooleanUtils.isTrue(onlyUsersFromGroup);
	}

	public String getIdOfGroupOfPermittedUsers() {
		return get(PROPERTY_ID_OF_GROUP_OF_PERMITTED_USERS);
	}
	
	public boolean isAutoCompleteWithCurrentUser() {
		Boolean autoCompleteWithCurrentUser = get(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER);
		return GwtBooleanUtils.isTrue(autoCompleteWithCurrentUser);
	}
	
	public String getAutoCompleteWithCurrentUserStateCode() {
		return get(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER_STATE_CODE);
	}
	
	public void setOnlyUsersFromGroup(boolean onlyUsersFromGroup) {
		set(PROPERTY_ONLY_USERS_FROM_GROUP, onlyUsersFromGroup);
	}
	public void setIdOfGroupOfPermittedUsers(String idOfGroupOfPermittedUsers) {
		set(PROPERTY_ID_OF_GROUP_OF_PERMITTED_USERS, idOfGroupOfPermittedUsers);
	}
	public void setAutoCompleteWithCurrentUser(boolean autoCompleteWithCurrentUser) {
		set(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER, autoCompleteWithCurrentUser);
	}
	public void setAutoCompleteWithCurrentUserStateCode(String autoCompleteWithCurrentUserStateCode) {
		set(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_USER_STATE_CODE, autoCompleteWithCurrentUserStateCode);
	}
}