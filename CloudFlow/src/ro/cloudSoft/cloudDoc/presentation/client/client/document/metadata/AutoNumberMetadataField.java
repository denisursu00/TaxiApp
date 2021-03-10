package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.widget.form.TextField;

public class AutoNumberMetadataField extends TextField<String> implements MetadataField {
	
	private String metadataDefinitionName;
	
	public AutoNumberMetadataField(String metadataDefinitionName) {
		
		this.metadataDefinitionName = metadataDefinitionName;
		
		setEmptyText("(" + GwtLocaleProvider.getConstants().AUTO_GENERATED() + ")");
		setReadOnly(true);
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
		/*
		 * Metadatele de tip auto number se genereaza automat, deci nu trebuie
		 * validate in formular.
		 */
	}
	
	@Override
	public boolean isRestrictedOnEdit() {
		return true;
	}
	
	@Override
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		/*
		 * Campurile pentru metadate de tip auto number sunt tot timpul
		 * read only. Deci nu trebuie permisa schimbarea acestei setari.
		 */
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