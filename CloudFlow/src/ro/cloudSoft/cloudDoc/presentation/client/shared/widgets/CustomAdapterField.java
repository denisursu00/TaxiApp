package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.google.gwt.user.client.ui.Widget;

public abstract class CustomAdapterField extends AdapterField {

	protected CustomAdapterField(Widget widget) {
		super(widget);
	}
	
	@Override
	public boolean isValid(boolean param) {
		/*
		 * Metoda a trebuit suprascrisa intrucat AdapterField returneaza
		 * intotdeauna true.
		 */
		return validateValue(null);
	}
	
	@Override
	public String getRawValue() {
		try {
			return super.getRawValue();
		} catch (Exception e) {
			/*
			 * Cateodata, la luarea valorii, va da NullPointerException.
			 * Tinand cont ca noi avem un field custom, cu elemente care nu au legatura neaparata
			 * cu un input HTML care are un raw value (String), NU avem nevoie de raw value.
			 */
			return "";
		}
	}
	
	@Override
	protected boolean validateValue(String param) {
		return validateCustomAdapterField();
	}
	
	protected abstract boolean validateCustomAdapterField();
}