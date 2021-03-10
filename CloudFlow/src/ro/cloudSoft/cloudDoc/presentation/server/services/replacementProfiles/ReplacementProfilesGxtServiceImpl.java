package ro.cloudSoft.cloudDoc.presentation.server.services.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles.ReplacementProfilesGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.replacementProfiles.ReplacementProfileConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.replacementProfiles.ReplacementProfileConverter.ReplacementProfileConversionMode;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfilesService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ReplacementProfilesGxtServiceImpl extends GxtServiceImplBase implements ReplacementProfilesGxtService, InitializingBean {

	private ReplacementProfilesService replacementProfilesService;
	private ReplacementProfileConverter replacementProfileConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			replacementProfilesService,
			replacementProfileConverter
		);
	}
	
	@Override
	public List<ReplacementProfileModel> getVisibleReplacementProfiles() throws PresentationException {
		
		List<ReplacementProfile> visibleReplacementProfiles = null;
		try {
			visibleReplacementProfiles = replacementProfilesService.getVisibleReplacementProfiles(getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		List<ReplacementProfileModel> visibleReplacementProfileModels = replacementProfileConverter.getModels(visibleReplacementProfiles);
		return visibleReplacementProfileModels;
	}
	
	@Override
	public void deleteReplacementProfileById(Long replacementProfileId) throws PresentationException {
		try {
			replacementProfilesService.deleteReplacementProfileById(replacementProfileId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public ReplacementProfileModel getReplacementProfileById(Long replacementProfileId) throws PresentationException {
		
		ReplacementProfile replacementProfile = null;
		try {
			replacementProfile = replacementProfilesService.getReplacementProfileById(replacementProfileId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		ReplacementProfileModel replacementProfileModel = replacementProfileConverter.getModel(replacementProfile);
		return replacementProfileModel;
	}
	
	@Override
	public void saveReplacementProfile(ReplacementProfileModel replacementProfileModel) throws PresentationException, AppException {
		
		SecurityManager userSecurity = getSecurity();
		
		ReplacementProfile replacementProfile = replacementProfileConverter.getFromModel(replacementProfileModel, userSecurity);
		
		try {
			replacementProfilesService.saveReplacementProfile(replacementProfile, userSecurity);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public void returned(Long replacementProfileId) throws PresentationException {
		try {
			replacementProfilesService.returned(replacementProfileId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public List<ReplacementProfileModel> getAvailableReplacementProfilesInWhichRequesterIsReplacement(
			Long idForRequestingReplacementProfile, Collection<Long> idsForRequesterUserProfiles,
			Date startDate, Date endDate) throws PresentationException {
		
		List<ReplacementProfile> replacementProfiles = replacementProfilesService.getAvailableReplacementProfilesInWhichRequesterIsReplacement(
			idForRequestingReplacementProfile, idsForRequesterUserProfiles, startDate, endDate);
		List<ReplacementProfileModel> replacementProfileModels = replacementProfileConverter.getModels(replacementProfiles,
			ReplacementProfileConversionMode.EXCLUDE_SELECTED_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT);
		return replacementProfileModels;
	}
	
	public void setReplacementProfilesService(ReplacementProfilesService replacementProfilesService) {
		this.replacementProfilesService = replacementProfilesService;
	}
	public void setReplacementProfileConverter(ReplacementProfileConverter replacementProfileConverter) {
		this.replacementProfileConverter = replacementProfileConverter;
	}
}