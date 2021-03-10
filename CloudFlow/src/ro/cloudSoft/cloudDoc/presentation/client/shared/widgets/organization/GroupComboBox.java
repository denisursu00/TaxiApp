package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.organization;

import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class GroupComboBox extends ComboBox<GroupModel> {

	public GroupComboBox() {
		setDisplayField(GroupModel.PROPERTY_NAME);
		setEditable(false);
		setStore(AppStoreCache.getGroupListStore());
		setTriggerAction(TriggerAction.ALL);
	}
	
	public String getIdOfSelectedGroup() {
		GroupModel selectedGroup = getValue();
		return (selectedGroup != null) ? selectedGroup.getId() : null;
	}
	
	public void setSelectedGroupById(String groupId) {
		
		ListStore<GroupModel> store = getStore();
		
		if (store.getModels().isEmpty()) {
			throw new IllegalStateException("ComboBox-ul NU este populat cu grupuri.");
		}
		
		if (groupId == null) {
			setValue(null);
			return;
		}
		
		GroupModel foundGroupToSelect = store.findModel(GroupModel.PROPERTY_ID, groupId);
		if (foundGroupToSelect == null) {
			// Grupul nu mai exista.
			setValue(null);
			return;
		}
		
		setValue(foundGroupToSelect);
	}
}