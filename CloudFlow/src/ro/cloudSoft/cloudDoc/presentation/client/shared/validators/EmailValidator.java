package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class EmailValidator implements Validator {
	
	private static final String REGEX = "[a-zA-Z\\-_\\.0-9]+@[a-zA-Z\\-\\.0-9]+\\.[a-zA-Z]{2,3}";

	@Override
	public String validate(Field<?> field, String value) {
		if ((value == null) || (!value.matches(REGEX))) {
			return GwtLocaleProvider.getMessages().INVALID_EMAIL();
		}
		return null;
	}
}