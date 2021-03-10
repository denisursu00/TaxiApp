package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.constants.ReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class ReplacementProfilesOutOfOfficeTemplatingHelper implements InitializingBean {

	private ReplacementProfilesOutOfOfficeConstants constants;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			constants
		);
	}
	
	public String replacePlaceholdersInText(String text, ReplacementProfile replacementProfile, User requesterUserProfile) {
		
		String textWithReplacements = text;
		
		textWithReplacements = textWithReplacements.replace(constants.getTemplatingPlaceholderForRequesterName(), requesterUserProfile.getName());
		
		textWithReplacements = textWithReplacements.replace(constants.getTemplatingPlaceholderForStartDate(), formatDate(replacementProfile.getStartDate()));
		textWithReplacements = textWithReplacements.replace(constants.getTemplatingPlaceholderForEndDate(), formatDate(replacementProfile.getEndDate()));
		
		return textWithReplacements;
	}
	
	private String formatDate(Date date) {
		return new SimpleDateFormat(ReplacementProfilesOutOfOfficeConstants.DATE_FORMAT).format(date);
	}
	
	public void setConstants(ReplacementProfilesOutOfOfficeConstants constants) {
		this.constants = constants;
	}
}