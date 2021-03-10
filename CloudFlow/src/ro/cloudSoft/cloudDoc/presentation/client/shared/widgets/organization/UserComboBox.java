package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.organization;

import ro.cloudSoft.cloudDoc.presentation.client.shared.DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class UserComboBox extends ComboBox<UserModel> {

	public UserComboBox() {
		
		setDisplayField(UserModel.USER_PROPERTY_DISPLAY_NAME);
		setEditable(false);
		setStore(AppStoreCache.getUserListStore());
		setTriggerAction(TriggerAction.ALL);
		
		DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener.attachTo(this);
	}
	
	/**
	 * Returneaza ID-ul utilizatorului selectat, sau null daca nu s-a selectat nici un utilizator.
	 */
	public Long getIdOfSelectedUser() {
		UserModel selectedUser = getValue();
		String userIdAsString = (selectedUser != null) ? selectedUser.getUserId() : null;
		Long userId = (userIdAsString != null) ? Long.valueOf(userIdAsString) : null;
		return userId;
	}
}