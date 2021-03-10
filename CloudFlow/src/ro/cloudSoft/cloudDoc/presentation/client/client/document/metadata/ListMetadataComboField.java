package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ListMetadataComboField extends ComboBox<ListMetadataItemModel> implements MetadataField {
	
	private String metadataDefinitionName;
	
	public ListMetadataComboField(String metadataDefinitionName, List<ListMetadataItemModel> listItems) {
		
		this.metadataDefinitionName = metadataDefinitionName;
		
		setDisplayField(ListMetadataItemModel.PROPERTY_LABEL);
		setEditable(false);
		setForceSelection(true);
		setStore(new ListStore<ListMetadataItemModel>());
		setTriggerAction(TriggerAction.ALL);
		
		store.add(listItems);
	}
	
	@Override
	public String getMetadataDefinitionName() {
		return metadataDefinitionName;
	}

	@Override
	public List<String> getMetadataValues() {
		List<String> values = new ArrayList<String>();

		ListMetadataItemModel selectedUser = getValue();
		if (selectedUser != null) {
			values.add(selectedUser.getValue());
		}
		
		return values;
	}

	@Override
	public void setMetadataValues(List<String> values) {
		if (!values.isEmpty()) {
			setValue(store.findModel(ListMetadataItemModel.PROPERTY_VALUE, values.get(0)));
		}
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
			ListMetadataItemModel selectedItem = getValue();
			if (selectedItem != null) {
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
		// Tipul de metadata nu necesita logica suplimentara la adaugarea unui document.
	}
	
	@Override
	public void onEditDocument(WorkflowStateModel currentState) {
		// Tipul de metadata nu necesita logica suplimentara la editarea unui document.
	}
}