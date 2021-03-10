package ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

/**
 * 
 */
public enum ReplacementProfileModelStatusOption {
	
	INACTIVE, ACTIVE, RETURNED, EXPIRED;
	
	private static final String LOCALIZED_TEXT_PREFIX = "REPLACEMENT_PROFILE_STATUS_";
	
	public String getLocalizedText() {		
		String option = name();
		String localizedTokenName = (LOCALIZED_TEXT_PREFIX + option);
		// TODO - de revenit
		return localizedTokenName; //GwtLocaleProvider.getConstants().getString(localizedTokenName);
	}
}