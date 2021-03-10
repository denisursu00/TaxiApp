package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import java.util.Arrays;
import java.util.Collection;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Validator ce inglobeaza logica de validare a mai multor validatoare
 * 
 * 
 */
public class CompositeValidator implements Validator {

	private final Collection<Validator> validators;
	
	public CompositeValidator(Validator... validators) {
		this.validators = Arrays.asList(validators);
	}
	
	@Override
	public String validate(Field<?> field, String value) {
		for (Validator validator : validators) {
			String validationMessage = validator.validate(field, value);
			if (validationMessage != null) {
				return validationMessage;
			}
		}
		return null;
	}
}