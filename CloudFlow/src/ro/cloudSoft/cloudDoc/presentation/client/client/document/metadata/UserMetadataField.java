package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class UserMetadataField extends ComboBox<UserModel> implements MetadataField {
	
	private Long metadataDefinitionId;
	private String metadataDefinitionName;

	private boolean autoCompleteWithCurrentUser;
	private String autoCompleteWithCurrentUserStateCode;
	
	public UserMetadataField(UserMetadataDefinitionModel metadataDefinition) {
		this(metadataDefinition, Collections.<String, Object> emptyMap());
	}
	
	public UserMetadataField(UserMetadataDefinitionModel metadataDefinition, Map<String, Object> additionalParameters) {
		
		metadataDefinitionId = metadataDefinition.getId();
		metadataDefinitionName = metadataDefinition.getName();
		
		autoCompleteWithCurrentUser = metadataDefinition.isAutoCompleteWithCurrentUser();
		autoCompleteWithCurrentUserStateCode = metadataDefinition.getAutoCompleteWithCurrentUserStateCode();
		
		setEditable(false);
		setForceSelection(true);
		setTriggerAction(TriggerAction.ALL);
		setMinListWidth(350);
		setDisplayField(UserModel.USER_PROPERTY_DISPLAY_NAME);

		ListStore<UserModel> store = new ListStore<UserModel>();
		populateStoreWithUsers(store, additionalParameters);
		setStore(store);
	}
	
	/**
	 * Atentie! A nu se folosi atribute din subclasa intrucat metoda va fi apelata in
	 * constructorul superclasei (care este apelat inainte de setarea atributelor in subclasa).
	 */
	protected void populateStoreWithUsers(ListStore<UserModel> store, Map<String, Object> additionalParameters) {
		List<UserModel> usersInAppCache = AppStoreCache.getUserListStore().getModels();
		if (usersInAppCache.isEmpty()) {
			String exceptionMessage = "S-a incercat popularea unei metadate " +
				"de tip user, insa utilizatorii NU s-au incarcat in cache.";
			throw new IllegalStateException(exceptionMessage);
		}
		store.add(usersInAppCache);
	}
	
	@Override
	public String getMetadataDefinitionName() {
		return metadataDefinitionName;
	}

	@Override
	public List<String> getMetadataValues() {
		List<String> values = new ArrayList<String>();

		UserModel selectedUser = getSelectedUser();
		if (selectedUser != null) {
			values.add(selectedUser.getUserId());
		}
		
		return values;
	}

	@Override
	public void setMetadataValues(List<String> values) {
		if (!values.isEmpty()) {
			String userId = values.get(0);
			setSelectedUserById(userId);
		} else {
			resetSelectedUser();
		}
	}
	
	/**
	 * Selecteaza utilizatorul cu ID-ul dat.
	 * 
	 * @return true daca s-a gasit utilizatorul in lista, false daca nu s-a gasit
	 */
	private boolean setSelectedUserById(String userId) {
		UserModel userWithId = store.findModel(UserModel.USER_PROPERTY_USERID, userId);
		if (userWithId != null) {
			setSelectedUser(userWithId);
			return true;
		} else {
			resetSelectedUser();
			return false;
		}
	}
	
	private void resetSelectedUser() {
		setValue(null);
	}
	
	private void setSelectedUser(UserModel user) {
		setValue(user);
	}
	
	private UserModel getSelectedUser() {
		return getValue();
	}
	
	@Override
	public void setMetadataValues(String... values) {
		MetadataFieldHelper.setValues(this, values);
	}
	
	@Override
	public void setMandatory(boolean mandatory) {
		setAllowBlank(!mandatory);
	}
	
	@Override
	public boolean isRestrictedOnEdit() {
		return this.isReadOnly();
	}
	
	@Override
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		this.setReadOnly(restrictedOnEdit);
	}
	
	@Override
	protected boolean validateValue(String value) {
		
		boolean mandatory = (!getAllowBlank());
		
		if (GwtStringUtils.isBlank(value)) {
			if (mandatory) {
				markInvalid(GwtLocaleProvider.getMessages().VALIDATOR_MANDATORY_FIELD());
				return false;
			} else {
				clearInvalid();
				return true;
			}
		} else {
			UserModel selectedUser = getSelectedUser();
			if (selectedUser != null) {
				clearInvalid();
				return true;
			} else {
				markInvalid(GwtLocaleProvider.getMessages().COMBO_BOX_SELECTION_REQUIRED());
				return false;
			}
		}
	}
	
	@Override
	public void onAddDocument(WorkflowStateModel startState) {
		autoCompleteWithCurrentUserIfNeeded(startState);
	}
	
	@Override
	public void onEditDocument(WorkflowStateModel currentState) {
		autoCompleteWithCurrentUserIfNeeded(currentState);
	}
	
	private void autoCompleteWithCurrentUserIfNeeded(WorkflowStateModel currentState) {
		
		if (!autoCompleteWithCurrentUser) {
			return;
		}
		
		if (currentState == null) {
			return;
		}
		
		String currentStateCode = currentState.getCode();
		if (!currentStateCode.equals(autoCompleteWithCurrentUserStateCode)) {
			return;
		}
		
		String currentUserId = GwtRegistryUtils.getUserSecurity().getUserIdAsString();
		
		boolean currentUserFound = setSelectedUserById(currentUserId);
		if (!currentUserFound) {
			String exceptionMessage = "S-a incercat completarea automata a campului cu user-ul cu ID-ul " +
				"[" + currentUserId + "], pentru metadata de tip user cu ID-ul [" + metadataDefinitionId + "] " +
				"si numele [" + metadataDefinitionName + "], insa utilizatorul NU a fost gasit in lista.";
			throw new IllegalStateException(exceptionMessage);
		}
	}
}