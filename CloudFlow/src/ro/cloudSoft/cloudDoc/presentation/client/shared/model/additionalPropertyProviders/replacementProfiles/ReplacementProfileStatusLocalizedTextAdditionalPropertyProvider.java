package ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.replacementProfiles;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModelStatusOption;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;

/**
 * 
 */
public class ReplacementProfileStatusLocalizedTextAdditionalPropertyProvider implements AdditionalPropertyProviderForModel {

	private static final long serialVersionUID = 1L;
	
	private String propertyName;
	private ReplacementProfileModel replacementProfile;
	
	protected ReplacementProfileStatusLocalizedTextAdditionalPropertyProvider() {
		// Constructor necesar pentru serializare
	}
	
	public ReplacementProfileStatusLocalizedTextAdditionalPropertyProvider(String propertyName, ReplacementProfileModel replacementProfile) {
		this.propertyName = propertyName;
		this.replacementProfile = replacementProfile;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
	
	@Override
	public Object getPropertyValue() {
		ReplacementProfileModelStatusOption status = replacementProfile.getStatus();
		return (status != null) ? status.getLocalizedText() : "";
	}
}