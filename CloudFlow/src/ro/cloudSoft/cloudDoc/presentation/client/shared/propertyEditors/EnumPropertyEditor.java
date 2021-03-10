package ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;


public class EnumPropertyEditor<E extends Enum<E>> implements PropertyEditor<E> {
	
	private final Class<E> enumClass;
	
	public EnumPropertyEditor(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public E convertStringValue(String value) {
		if (GwtStringUtils.isNotBlank(value)) {
			return Enum.valueOf(enumClass, value);
		} else {
			return null;
		}
	}
	
	@Override
	public String getStringValue(E value) {
		if (value != null) {
			return value.name();
		} else {
			return null;
		}
	}
}