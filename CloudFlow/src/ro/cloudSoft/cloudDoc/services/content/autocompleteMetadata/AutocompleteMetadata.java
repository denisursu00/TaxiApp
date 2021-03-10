package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public abstract class AutocompleteMetadata {
	
	public AutocompleteMetadataResponseModel autocomplete(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException {
		// In eventualitatea ca vor fi chestii comune care se vor face intai/sau dupa doProcess.
    	return doAutocomplete(request, userSecurity);
	};
	
	protected abstract AutocompleteMetadataResponseModel doAutocomplete(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException;
	
	protected MetadataInstanceModel createMetadataInstance(Long metadataDefinitionId, String value) {
		MetadataInstanceModel instance = new MetadataInstanceModel();
		instance.setMetadataDefinitionId(metadataDefinitionId);
		instance.setValue(value);
		return instance;
	}
}
