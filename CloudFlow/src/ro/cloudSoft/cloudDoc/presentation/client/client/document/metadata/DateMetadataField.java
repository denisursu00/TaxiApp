package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFormatUtils;

import com.extjs.gxt.ui.client.widget.form.DateField;

public class DateMetadataField extends DateField implements MetadataField {
	
	private String metadataDefinitionName;
	
	public DateMetadataField(String metadataDefinitionName) {
		
		this.metadataDefinitionName = metadataDefinitionName;
		
		getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		setEditable(false);
	}
	
	@Override
	public String getMetadataDefinitionName() {
		return metadataDefinitionName;
	}

	@Override
	public List<String> getMetadataValues() {
		List<String> values = new ArrayList<String>();

		Date dateFieldValue = getValue();
		if (dateFieldValue != null) {
			values.add(GwtFormatUtils.convertDateToString(GwtFormatConstants.DATE_FOR_SAVING, dateFieldValue));
		}
		
		return values;
	}
	
	@Override
	public void setMetadataValues(List<String> values) {
		if (!values.isEmpty()) {
			setValue(GwtFormatUtils.getDateFromString(GwtFormatConstants.DATE_FOR_SAVING, values.get(0)));
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