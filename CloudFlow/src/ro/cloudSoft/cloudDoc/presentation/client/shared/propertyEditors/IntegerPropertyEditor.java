package ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;

public class IntegerPropertyEditor implements PropertyEditor<Integer> {
	
	@Override
	public Integer convertStringValue(String value) {
		return (value == null) ? null : Integer.valueOf(value);
	}
	
	@Override
	public String getStringValue(Integer value) {
		return (value == null) ? null : value.toString();
	}
}