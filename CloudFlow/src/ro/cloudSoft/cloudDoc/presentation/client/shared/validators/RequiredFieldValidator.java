package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class RequiredFieldValidator implements Validator {

	@Override
	public String validate(Field<?> field, String value) {
		if (!GwtValidateUtils.isCompleted(value)) {
			return GwtLocaleProvider.getMessages().REQUIRED_FIELD();
		}
		return null;
	}
}