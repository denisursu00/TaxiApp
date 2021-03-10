package ro.cloudSoft.cloudDoc.presentation.server.converters.constants;

import ro.cloudSoft.cloudDoc.core.constants.ReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtReplacementProfilesOutOfOfficeConstants;

public class ReplacementProfilesOutOfOfficeConstantsConverter {

	public static GwtReplacementProfilesOutOfOfficeConstants getForGwt(ReplacementProfilesOutOfOfficeConstants constants) {
		
		GwtReplacementProfilesOutOfOfficeConstants gwtConstants = new GwtReplacementProfilesOutOfOfficeConstants();
		
		gwtConstants.setTemplatingPlaceholderForRequesterName(constants.getTemplatingPlaceholderForRequesterName());
		gwtConstants.setTemplatingPlaceholderForStartDate(constants.getTemplatingPlaceholderForStartDate());
		gwtConstants.setTemplatingPlaceholderForEndDate(constants.getTemplatingPlaceholderForEndDate());
		
		gwtConstants.setDefaultTemplateForEmailSubject(constants.getDefaultTemplateForEmailSubject());
		gwtConstants.setDefaultTemplateForEmailBody(constants.getDefaultTemplateForEmailBody());
		
		return gwtConstants;
	}
}