package ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.replacementProfiles.ReplacementProfileReplacementDisplayNameAdditionalPropertyProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.replacementProfiles.ReplacementProfileStatusLocalizedTextAdditionalPropertyProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.BaseModelWithAdditionalProperties;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 */
public class ReplacementProfileModel extends BaseModelWithAdditionalProperties implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ID = "id";
	
	public static final String PROPERTY_REQUESTER_USERNAME = "requesterUsername";
	public static final String PROPERTY_REPLACEMENT = "replacement";
	public static final String PROPERTY_SELECTED_REQUESTER_USER_PROFILES = "selectedRequesterUserProfiles";
	
	public static final String PROPERTY_IDS_FOR_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT = "idsForSelectedReplacementProfilesInWhichRequesterIsReplacement";
	
	public static final String PROPERTY_COMMENTS = "comments";
	
	public static final String PROPERTY_START_DATE = "startDate";
	public static final String PROPERTY_END_DATE = "endDate";
	
	public static final String PROPERTY_OUT_OF_OFFICE = "outOfOffice";
	public static final String PROPERTY_OUT_OF_OFFICE_EMAIL_SUBJECT_TEMPLATE = "outOfOfficeEmailSubjectTemplate";
	public static final String PROPERTY_OUT_OF_OFFICE_EMAIL_BODY_TEMPLATE = "outOfOfficeEmailBodyTemplate";
	
	public static final String PROPERTY_STATUS = "status";

	public static final String PROPERTY_REPLACEMENT_DISPLAY_NAME = "replacementDisplayName";
	public static final String PROPERTY_STATUS_LOCALIZED_TEXT = "statusLocalizedText";
	
	@Override
	public AdditionalPropertyProviderForModel[] getAdditionalPropertyProviders() {
		return new AdditionalPropertyProviderForModel[] {
			new ReplacementProfileReplacementDisplayNameAdditionalPropertyProvider(PROPERTY_REPLACEMENT_DISPLAY_NAME, this),
			new ReplacementProfileStatusLocalizedTextAdditionalPropertyProvider(PROPERTY_STATUS_LOCALIZED_TEXT, this)
		};
	}
	
	public Long getId() {
		return get(PROPERTY_ID);
	}
	
	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}
	
	public String getRequesterUsername() {
		return get(PROPERTY_REQUESTER_USERNAME);
	}
	
	public void setRequesterUsername(String requesterUsername) {
		set(PROPERTY_REQUESTER_USERNAME, requesterUsername);
	}
	
	public UserModel getReplacement() {
		return get(PROPERTY_REPLACEMENT);
	}
	
	public void setReplacement(UserModel replacement) {
		set(PROPERTY_REPLACEMENT, replacement);
	}
	
	public Collection<UserModel> getSelectedRequesterUserProfiles() {
		return get(PROPERTY_SELECTED_REQUESTER_USER_PROFILES);
	}
	
	public void setSelectedRequesterUserProfiles(Collection<UserModel> selectedRequesterUserProfiles) {
		set(PROPERTY_SELECTED_REQUESTER_USER_PROFILES, selectedRequesterUserProfiles);
	}
	
	public Set<Long> getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement() {
		return get(PROPERTY_IDS_FOR_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT);
	}
	
	public void setIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(Set<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement) {
		set(PROPERTY_IDS_FOR_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT, idsForSelectedReplacementProfilesInWhichRequesterIsReplacement);
	}
	
	public String getComments() {
		return get(PROPERTY_COMMENTS);
	}
	
	public void setComments(String comments) {
		set(PROPERTY_COMMENTS, comments);
	}
	
	public Date getStartDate() {
		return get(PROPERTY_START_DATE);
	}
	
	public void setStartDate(Date startDate) {
		set(PROPERTY_START_DATE, startDate);
	}
	
	public Date getEndDate() {
		return get(PROPERTY_END_DATE);
	}
	
	public void setEndDate(Date endDate) {
		set(PROPERTY_END_DATE, endDate);
	}
	
	public boolean isOutOfOffice() {
		Boolean outOfOffice = get(PROPERTY_OUT_OF_OFFICE);
		return GwtBooleanUtils.isTrue(outOfOffice);
	}
	
	public void setOutOfOffice(boolean outOfOffice) {
		set(PROPERTY_OUT_OF_OFFICE, outOfOffice);
	}
	
	public String getOutOfOfficeEmailSubjectTemplate() {
		return get(PROPERTY_OUT_OF_OFFICE_EMAIL_SUBJECT_TEMPLATE);
	}
	
	public void setOutOfOfficeEmailSubjectTemplate(String outOfOfficeEmailSubjectTemplate) {
		set(PROPERTY_OUT_OF_OFFICE_EMAIL_SUBJECT_TEMPLATE, outOfOfficeEmailSubjectTemplate);
	}

	public String getOutOfOfficeEmailBodyTemplate() {
		return get(PROPERTY_OUT_OF_OFFICE_EMAIL_BODY_TEMPLATE);
	}
	
	public void setOutOfOfficeEmailBodyTemplate(String outOfOfficeEmailBodyTemplate) {
		set(PROPERTY_OUT_OF_OFFICE_EMAIL_BODY_TEMPLATE, outOfOfficeEmailBodyTemplate);
	}
	
	public ReplacementProfileModelStatusOption getStatus() {
		return get(PROPERTY_STATUS);
	}
	
	public void setStatus(ReplacementProfileModelStatusOption status) {
		set(PROPERTY_STATUS, status);
	}
}