package ro.cloudSoft.cloudDoc.presentation.client.shared.providers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.EmailValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.RequiredFieldValidator;

/**
 * Contine validatori pentru diverse tipuri de campuri.
 * 
 * 
 */
public class ValidatorProvider {

	private static EmailValidator emailValidator;
	private static RequiredFieldValidator requiredFieldValidator;
	
	public static synchronized EmailValidator getEmailValidator() {
		if (emailValidator == null) {
			emailValidator = new EmailValidator();
		}
		return emailValidator;
	}
	
	public static synchronized RequiredFieldValidator getRequiredFieldValidator() {
		if (requiredFieldValidator == null) {
			requiredFieldValidator = new RequiredFieldValidator();
		}
		return requiredFieldValidator;
	}
}