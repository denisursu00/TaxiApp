package ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.organization;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;

/**
 * 
 */
public class UserDisplayNameAdditionalPropertyProvider implements AdditionalPropertyProviderForModel {

	private static final long serialVersionUID = 1L;

	private String propertyName;
	private UserModel user;
	
	protected UserDisplayNameAdditionalPropertyProvider() {
		// Constructor necesar pentru serializare
	}

	public UserDisplayNameAdditionalPropertyProvider(String propertyName, UserModel user) {
		this.propertyName = propertyName;
		this.user = user;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
	
	@Override
	public Object getPropertyValue() {
		return user.getDisplayName();
	}
}