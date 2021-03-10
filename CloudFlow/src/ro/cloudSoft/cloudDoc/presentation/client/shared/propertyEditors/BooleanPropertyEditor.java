package ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;

/**
 * 
 */
public class BooleanPropertyEditor implements PropertyEditor<Boolean> {
	
	@Override
	public Boolean convertStringValue(String value) {
		return (value == null) ? Boolean.FALSE : new Boolean(value);
	}
	
	@Override
	public String getStringValue(Boolean value) {
		return (value == null) ? null : value.toString();
	}
}