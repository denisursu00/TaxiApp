package ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.replacementProfiles;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;

/**
 * 
 */
public class ReplacementProfileReplacementDisplayNameAdditionalPropertyProvider implements AdditionalPropertyProviderForModel {

	private static final long serialVersionUID = 1L;
	
	private String propertyName;
	private ReplacementProfileModel replacementProfile;
	
	protected ReplacementProfileReplacementDisplayNameAdditionalPropertyProvider() {
		// Constructor necesar pentru serializare
	}
	
	public ReplacementProfileReplacementDisplayNameAdditionalPropertyProvider(String propertyName, ReplacementProfileModel replacementProfile) {
		this.propertyName = propertyName;
		this.replacementProfile = replacementProfile;
	}
	
	@Override
	public String getPropertyName() {
		return propertyName;
	}
	
	@Override
	public Object getPropertyValue() {
		UserModel replacement = replacementProfile.getReplacement();
		if (replacement == null) {
			return null;
		}
		return replacement.getDisplayName();
	}
}