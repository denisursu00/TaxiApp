package ro.cloudSoft.cloudDoc.presentation.server.converters.replacementProfiles;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatus;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.SaveReplacementProfileRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModelStatusOption;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserConverter;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfilesService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ReplacementProfileConverter implements InitializingBean {

	private OrganizationService organizationService;
	private OrganizationUnitService organizationUnitService;
	private ReplacementProfilesService replacementProfilesService;
	private UserService userService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			organizationService,
			organizationUnitService,
			replacementProfilesService,
			userService
		);
	}
	
	public ReplacementProfileModel getModel(SaveReplacementProfileRequestModel replacementProfile) {
		ReplacementProfileModel replacementProfileModel = new ReplacementProfileModel();
		
		replacementProfileModel.setId(replacementProfile.getId());
		
		String requesterUserName = userService.getUserById(Long.valueOf(replacementProfile.getRequesterId())).getUsername();
		replacementProfileModel.setRequesterUsername(requesterUserName);

		
		User replacement = userService.getUserById(replacementProfile.getReplacementUserId());
		replacementProfileModel.setReplacement(UserConverter.getModelFromUser(replacement));

		replacementProfileModel.setSelectedRequesterUserProfiles(replacementProfile.getSelectedRequesterUserProfiles());
		replacementProfileModel.setIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(replacementProfile.getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement());
		replacementProfileModel.setComments(replacementProfile.getComments());
		replacementProfileModel.setStartDate(replacementProfile.getStartDate());
		replacementProfileModel.setEndDate(replacementProfile.getEndDate());
		replacementProfileModel.setOutOfOffice(replacementProfile.isOutOfOffice());
		replacementProfileModel.setOutOfOfficeEmailSubjectTemplate(replacementProfile.getOutOfOfficeEmailSubjectTemplate());
		replacementProfileModel.setOutOfOfficeEmailBodyTemplate(replacementProfile.getOutOfOfficeEmailBodyTemplate());
		replacementProfileModel.setStatus(replacementProfile.getStatus());
		
		return replacementProfileModel;
	}

	public List<ReplacementProfileModel> getModels(Collection<ReplacementProfile> replacementProfiles) {
		return getModels(replacementProfiles, ReplacementProfileConversionMode.INCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT);
	}
	
	public List<ReplacementProfileModel> getModels(Collection<ReplacementProfile> replacementProfiles, ReplacementProfileConversionMode conversionMode) {
		List<ReplacementProfileModel> replacementProfileModels = Lists.newArrayList();
		for (ReplacementProfile replacementProfile : replacementProfiles) {
			ReplacementProfileModel replacementProfileModel = getModel(replacementProfile);
			replacementProfileModels.add(replacementProfileModel);
		}
		return replacementProfileModels;
	}
	
	public ReplacementProfileModel getModel(ReplacementProfile replacementProfile) {
		return getModel(replacementProfile, ReplacementProfileConversionMode.INCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT);
	}
	
	public ReplacementProfileModel getModel(ReplacementProfile replacementProfile, ReplacementProfileConversionMode conversionMode) {
		
		ReplacementProfileModel replacementProfileModel = new ReplacementProfileModel();
		
		replacementProfileModel.setId(replacementProfile.getId());
		
		replacementProfileModel.setRequesterUsername(replacementProfile.getRequesterUsername());
		
		User replacement = replacementProfile.getReplacement();
		if (replacement != null) {
			UserModel replacementModel = UserConverter.getModelFromUser(replacement);
			replacementProfileModel.setReplacement(replacementModel);
		} else {
			replacementProfileModel.setReplacement(null);
		}
		
		Set<User> selectedRequesterUserProfiles = replacementProfile.getSelectedRequesterUserProfiles();
		if (CollectionUtils.isNotEmpty(selectedRequesterUserProfiles)) {
			List<UserModel> selectedRequesterUserProfileModels = Lists.newLinkedList();
			for (User selectedRequesterUserProfile : selectedRequesterUserProfiles) {
				UserModel selectedRequesterUserProfileModel = UserConverter.getModelFromUser(selectedRequesterUserProfile);
				selectedRequesterUserProfileModels.add(selectedRequesterUserProfileModel);
			}
			replacementProfileModel.setSelectedRequesterUserProfiles(selectedRequesterUserProfileModels);
		} else {
			replacementProfileModel.setSelectedRequesterUserProfiles(Lists.<UserModel> newLinkedList());
		}
		
		if (conversionMode.equals(ReplacementProfileConversionMode.INCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT)) {
			Set<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement = Sets.newHashSet();
			for (ReplacementProfile selectedReplacementProfileInWhichRequesterIsReplacement : replacementProfile.getSelectedReplacementProfilesInWhichRequesterIsReplacement()) {
				idsForSelectedReplacementProfilesInWhichRequesterIsReplacement.add(selectedReplacementProfileInWhichRequesterIsReplacement.getId());
			}
			replacementProfileModel.setIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(idsForSelectedReplacementProfilesInWhichRequesterIsReplacement);
		}
		
		replacementProfileModel.setComments(replacementProfile.getComments());
		
		replacementProfileModel.setStartDate(replacementProfile.getStartDate());
		replacementProfileModel.setEndDate(replacementProfile.getEndDate());
		
		replacementProfileModel.setOutOfOffice(replacementProfile.isOutOfOffice());
		replacementProfileModel.setOutOfOfficeEmailSubjectTemplate(replacementProfile.getOutOfOfficeEmailSubjectTemplate());
		replacementProfileModel.setOutOfOfficeEmailBodyTemplate(replacementProfile.getOutOfOfficeEmailBodyTemplate());
		
		ReplacementProfileStatus status = replacementProfile.getStatus();
		if (status != null) {
			ReplacementProfileStatusOption statusOption = status.getStatus();
			ReplacementProfileModelStatusOption modelStatusOption = ReplacementProfileModelStatusOption.valueOf(statusOption.name());
			replacementProfileModel.setStatus(modelStatusOption);
		} else {
			replacementProfileModel.setStatus(null);
		}
		
		return replacementProfileModel;
	}
	
	public ReplacementProfile getFromModel(ReplacementProfileModel replacementProfileModel, SecurityManager userSecurity) throws AppException {
		
		ReplacementProfile replacementProfile = new ReplacementProfile();
		
		replacementProfile.setId(replacementProfileModel.getId());
		
		replacementProfile.setRequesterUsername(replacementProfileModel.getRequesterUsername());
		
		UserModel replacementModel = replacementProfileModel.getReplacement();
		
		if (replacementModel != null) {
			User userEntity = null;
			if (replacementModel.getUserIdAsLong() != null) {
				userEntity = userService.getUserById(replacementModel.getUserIdAsLong());
			}
			User replacement = UserConverter.getUserFromModel(replacementModel, userEntity, organizationService, organizationUnitService, userSecurity);
			replacementProfile.setReplacement(replacement);
		} else {
			replacementProfile.setReplacement(null);
		}
		
		Collection<UserModel> selectedRequesterUserProfileModels = replacementProfileModel.getSelectedRequesterUserProfiles();
		if (CollectionUtils.isNotEmpty(selectedRequesterUserProfileModels)) {
			Set<User> selectedRequesterUserProfiles = Sets.newHashSet();
			for (UserModel selectedRequesterUserProfileModel : selectedRequesterUserProfileModels) {
				User userEntitySelectedRequester = null;
				if (selectedRequesterUserProfileModel.getUserIdAsLong() != null) {
					userEntitySelectedRequester = userService.getUserById(selectedRequesterUserProfileModel.getUserIdAsLong());
				}
				User selectedRequesterUserProfile = UserConverter.getUserFromModel( selectedRequesterUserProfileModel, userEntitySelectedRequester, organizationService, organizationUnitService, userSecurity);
				selectedRequesterUserProfiles.add(selectedRequesterUserProfile);
			}
			replacementProfile.setSelectedRequesterUserProfiles(selectedRequesterUserProfiles);
		} else {
			replacementProfile.setSelectedRequesterUserProfiles(Sets.<User> newHashSet());
		}
		
		if (replacementProfileModel.getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement().isEmpty()) {
			replacementProfile.setSelectedReplacementProfilesInWhichRequesterIsReplacement(Collections.<ReplacementProfile> emptySet());
		} else {
			Collection<ReplacementProfile> selectedReplacementProfilesInWhichRequesterIsReplacement = replacementProfilesService.getReplacementProfilesWithIds(
				replacementProfileModel.getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement()
			);
			replacementProfile.setSelectedReplacementProfilesInWhichRequesterIsReplacement(Sets.newHashSet(selectedReplacementProfilesInWhichRequesterIsReplacement));
		}
		
		replacementProfile.setComments(replacementProfileModel.getComments());
		
		replacementProfile.setStartDate(replacementProfileModel.getStartDate());
		replacementProfile.setEndDate(replacementProfileModel.getEndDate());
		
		replacementProfile.setOutOfOffice(replacementProfileModel.isOutOfOffice());
		replacementProfile.setOutOfOfficeEmailSubjectTemplate(replacementProfileModel.getOutOfOfficeEmailSubjectTemplate());
		replacementProfile.setOutOfOfficeEmailBodyTemplate(replacementProfileModel.getOutOfOfficeEmailBodyTemplate());
		
		ReplacementProfileModelStatusOption modelStatusOption = replacementProfileModel.getStatus();
		if (modelStatusOption != null) {
			ReplacementProfileStatusOption statusOption = ReplacementProfileStatusOption.valueOf(modelStatusOption.name());
			ReplacementProfileStatus status = new ReplacementProfileStatus(replacementProfile, statusOption);			
			replacementProfile.setStatus(status);
		} else {
			replacementProfile.setStatus(null);
		}
		
		return replacementProfile;
	}
	
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
	public void setOrganizationUnitService(OrganizationUnitService organizationUnitService) {
		this.organizationUnitService = organizationUnitService;
	}
	public void setReplacementProfilesService(ReplacementProfilesService replacementProfilesService) {
		this.replacementProfilesService = replacementProfilesService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 
	 */
	public static enum ReplacementProfileConversionMode {
		INCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT,
		EXCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT
	}
}