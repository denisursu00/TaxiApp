package ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors;

import java.util.Date;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DatePropertyEditor implements PropertyEditor<Date> {
	
	private static final DateTimeFormat FORMATTER = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss,SSS");
	
	@Override
	public Date convertStringValue(String value) {
		return (value == null) ? null : FORMATTER.parse(value);
	}
	
	@Override
	public String getStringValue(Date value) {
		return (value == null) ? null : FORMATTER.format(value);
	}
}