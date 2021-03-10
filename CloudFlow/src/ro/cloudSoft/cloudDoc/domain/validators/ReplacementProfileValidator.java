package ro.cloudSoft.cloudDoc.domain.validators;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import com.google.common.collect.Multimap;

/**
 * 
 */
public class ReplacementProfileValidator {

	private final ReplacementProfile replacementProfile;
	
	private final Set<ReplacementProfileStatusOption> allowedStatusesForOverlappingReplacementProfiles;
	private final Set<ReplacementProfileStatusOption> allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementForNewProfile;
	private final Multimap<ReplacementProfileStatusOption, ReplacementProfileStatusOption> allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementByRequestingReplacementProfileStatus;
	
	private final ReplacementProfileDao replacementProfileDao;

	public ReplacementProfileValidator(ReplacementProfile replacementProfile, Set<ReplacementProfileStatusOption> allowedStatusesForOverlappingReplacementProfiles,
			Set<ReplacementProfileStatusOption> allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementForNewProfile,
			Multimap<ReplacementProfileStatusOption, ReplacementProfileStatusOption> allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementByRequestingReplacementProfileStatus,
			ReplacementProfileDao replacementProfileDao) {
		
		this.replacementProfile = replacementProfile;
		
		this.allowedStatusesForOverlappingReplacementProfiles = allowedStatusesForOverlappingReplacementProfiles;
		this.allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementForNewProfile = allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementForNewProfile;
		this.allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementByRequestingReplacementProfileStatus = allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementByRequestingReplacementProfileStatus;
		
		this.replacementProfileDao = replacementProfileDao;
	}
	
	public void validate() throws AppException {
		validateRequiredFields();
		validateConstraints();
	}
	
	private void validateRequiredFields() throws AppException {

		if (StringUtils.isBlank(replacementProfile.getRequesterUsername())) {
			throw new AppException(AppExceptionCodes.REQUIRED_FIELDS_NOT_COMPLETED);
		}
		
		if (replacementProfile.getReplacement() == null) {
			throw new AppException(AppExceptionCodes.REQUIRED_FIELDS_NOT_COMPLETED);
		}

		if (CollectionUtils.isEmpty(replacementProfile.getSelectedRequesterUserProfiles())) {
			throw new AppException(AppExceptionCodes.REQUIRED_FIELDS_NOT_COMPLETED);
		}
		
		if ((replacementProfile.getStartDate() == null) || (replacementProfile.getEndDate() == null)) {
			throw new AppException(AppExceptionCodes.REQUIRED_FIELDS_NOT_COMPLETED);
		}
		
		if (replacementProfile.isOutOfOffice()) {
			if (StringUtils.isBlank(replacementProfile.getOutOfOfficeEmailSubjectTemplate())
					|| StringUtils.isBlank(replacementProfile.getOutOfOfficeEmailBodyTemplate())) {
				
				throw new AppException(AppExceptionCodes.REQUIRED_FIELDS_NOT_COMPLETED);
			}
		}
	}
	
	private void validateConstraints() throws AppException {
		
		String requesterUsername = replacementProfile.getRequesterUsername();
		
		String replacementUsername = replacementProfile.getReplacement().getUsername();
		if (replacementUsername.equalsIgnoreCase(requesterUsername)) {
			throw new AppException(AppExceptionCodes.REPLACEMENT_CANNOT_BE_THE_REQUESTER);
		}
		
		validateSelectedRequesterUserProfilesConstraints();
		validateSelectedReplacementProfilesInWhichRequesterIsReplacementConstraints();
		validateDateConstraints();
		
		boolean otherSimilarReplacementProfilesExist = replacementProfileDao.replacementProfilesForUserProfilesInPeriodExist(
			replacementProfile.getSelectedRequesterUserProfiles(), replacementProfile.getStartDate(),
			replacementProfile.getEndDate(), replacementProfile.getId(), allowedStatusesForOverlappingReplacementProfiles);
		if (otherSimilarReplacementProfilesExist) {
			throw new AppException(AppExceptionCodes.OTHER_REPLACEMENT_PROFILES_EXIST_WITH_OVERLAPING_PERIOD);
		}
	}
	
	private void validateSelectedRequesterUserProfilesConstraints() throws AppException {
		
		String requesterUsername = replacementProfile.getRequesterUsername();
		
		Set<User> selectedRequesterUserProfiles = replacementProfile.getSelectedRequesterUserProfiles();
		for (User selectedRequesterUserProfile : selectedRequesterUserProfiles) {
			String selectedRequesterUserProfileUsername = selectedRequesterUserProfile.getUsername();
			if (!selectedRequesterUserProfileUsername.equalsIgnoreCase(requesterUsername)) {
				// Nu ar trebui sa fie permis acest lucru din interfata grafica.
				throw new AppException();
			}
		}
	}
	
	private void validateSelectedReplacementProfilesInWhichRequesterIsReplacementConstraints() throws AppException {
		for (ReplacementProfile selectedReplacementProfilesInWhichRequesterIsReplacement :
				replacementProfile.getSelectedReplacementProfilesInWhichRequesterIsReplacement()) {
			
			if (replacementIsNotRequester(selectedReplacementProfilesInWhichRequesterIsReplacement)) {
				// Nu ar trebui sa fie permis acest lucru din interfata grafica.
				throw new AppException();
			}
			
			validateSelectedReplacementProfilesInWhichRequesterIsReplacementStatus(selectedReplacementProfilesInWhichRequesterIsReplacement);
			validateSelectedReplacementProfilesInWhichRequesterIsReplacementPeriod(selectedReplacementProfilesInWhichRequesterIsReplacement);
		}
	}
	
	/**
	 * Verifica daca inlocuitorul profilului dat de verificat este titularul profilului pentru care se face validarea.
	 */
	private boolean replacementIsNotRequester(ReplacementProfile replacementProfileToCheck) {
		return (!replacementProfile.getSelectedRequesterUserProfiles().contains(replacementProfileToCheck.getReplacement()));
	}
	
	private void validateSelectedReplacementProfilesInWhichRequesterIsReplacementStatus(ReplacementProfile replacementProfileToCheck) throws AppException {
		ReplacementProfileStatusOption statusForReplacementProfileToCheck = replacementProfileToCheck.getStatus().getStatus();
		if (isReplacementProfileNew()) {
			if (!allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementForNewProfile.contains(statusForReplacementProfileToCheck)) {
				// Nu ar trebui sa fie permis acest lucru din interfata grafica.
				throw new AppException();
			}
		} else {
			Collection<ReplacementProfileStatusOption> allowedStatuses = allowedStatusesForAvailableReplacementProfilesInWhichRequesterIsReplacementByRequestingReplacementProfileStatus.get(replacementProfile.getStatus().getStatus());
			if (!allowedStatuses.contains(statusForReplacementProfileToCheck)) {
				// Nu ar trebui sa fie permis acest lucru din interfata grafica.
				throw new AppException();
			}
		}
	}
	
	private boolean isReplacementProfileNew() {
		return replacementProfile.isNew();
	}
	
	private void validateSelectedReplacementProfilesInWhichRequesterIsReplacementPeriod(ReplacementProfile replacementProfileToCheck) throws AppException {
		if (!ro.cloudSoft.common.utils.DateUtils.intervalsIntersect(replacementProfileToCheck.getStartDate(), replacementProfileToCheck.getEndDate(), replacementProfile.getStartDate(), replacementProfile.getEndDate())) {
			// Nu ar trebui sa fie permis acest lucru din interfata grafica.
			throw new AppException();
		}
	}
	
	private void validateDateConstraints() throws AppException {
		
		Date startDate = replacementProfile.getStartDate();
		Date endDate = replacementProfile.getEndDate();
		
		if (startDate.after(endDate) || endDate.before(startDate)) {
			// Nu ar trebui sa fie permis acest lucru din interfata grafica.
			throw new AppException();
		}
		
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		Date tomorrow = DateUtils.addDays(today, 1);
		
		if (startDate.before(tomorrow) || endDate.before(tomorrow)) {
			throw new AppException(AppExceptionCodes.REPLACEMENT_PROFILE_MUST_BE_IN_THE_FUTURE);
		}
	}
}