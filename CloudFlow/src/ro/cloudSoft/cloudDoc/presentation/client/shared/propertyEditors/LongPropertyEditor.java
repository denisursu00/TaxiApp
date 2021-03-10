package ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;

/**
 * 
 */
public class LongPropertyEditor implements PropertyEditor<Long> {
	
	@Override
	public Long convertStringValue(String value) {
		return (value == null) ? null : new Long(value);
	}
	
	@Override
	public String getStringValue(Long value) {
		return (value == null) ? null : value.toString();
	}
}