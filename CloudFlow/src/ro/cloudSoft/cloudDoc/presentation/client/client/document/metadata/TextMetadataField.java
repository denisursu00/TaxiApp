package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.widget.form.TextField;

public class TextMetadataField extends TextField<String> implements MetadataField {
	
	private String metadataDefinitionName;
	
	public TextMetadataField(String metadataDefinitionName) {
		this.metadataDefinitionName = metadataDefinitionName;
	}
	
	@Override
	public String getMetadataDefinitionName() {
		return metadataDefinitionName;
	}
	
	@Override
	public List<String> getMetadataValues() {		
		List<String> values = new ArrayList<String>();

		String textFieldValue = getValue();
		if (GwtValidateUtils.isCompleted(textFieldValue)) {
			values.add(textFieldValue);
		}
		
		return values;
	}
	
	@Override
	public void setMetadataValues(List<String> values) {
		if (!values.isEmpty()) {
			setValue(values.get(0));
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
	public void onAddDocument(WorkflowStateModel startState) {
		// Tipul de metadata nu necesita logica suplimentara la adaugarea unui document.
	}
	
	@Override
	public void onEditDocument(WorkflowStateModel currentState) {
		// Tipul de metadata nu necesita logica suplimentara la editarea unui document.
	}
}