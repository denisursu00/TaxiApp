package ro.cloudSoft.cloudDoc.services.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileStatusDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatus;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.domain.validators.ReplacementProfileValidator;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.ActivateReplacementProfilesThatBeganHelper;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.ExpireReplacementProfilesThatEndedHelper;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.ReplacementProfilesOutOfOfficeTemplatingHelper;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.ReturnedReplacementProfileHelper;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.mail.OutOfOfficeMailService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

/**
 * 
 */
public class ReplacementProfilesServiceImpl implements ReplacementProfilesService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(ReplacementProfilesServiceImpl.class);

	private static final ReplacementProfileStatusOption STATUS_OPTION_FOR_NEW_REPLACEMENT_PROFILE = ReplacementProfileStatusOption.INACTIVE;
	
	private static final EnumSet<ReplacementProfileStatusOption> ALLOWED_STATUSES_FOR_REPLACEMENT_PROFILE_DELETION = EnumSet.of(ReplacementProfileStatusOption.INACTIVE);
	private static final EnumSet<ReplacementProfileStatusOption> ALLOWED_STATUSES_FOR_REPLACEMENT_PROFILE_MODIFICATION = EnumSet.of(ReplacementProfileStatusOption.INACTIVE);
	
	private static final EnumSet<ReplacementProfileStatusOption> ALLOWED_STATUSES_FOR_OVERLAPPING_REPLACEMENT_PROFILES = EnumSet.of(ReplacementProfileStatusOption.EXPIRED, ReplacementProfileStatusOption.RETURNED);
	
	private static final Set<ReplacementProfileStatusOption> ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_FOR_NEW_PROFILE = ImmutableSet.of(ReplacementProfileStatusOption.ACTIVE);
	private static final Multimap<ReplacementProfileStatusOption, ReplacementProfileStatusOption> ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_BY_REQUESTING_REPLACEMENT_PROFILE_STATUS = ImmutableMultimap.<ReplacementProfileStatusOption, ReplacementProfileStatusOption> builder()
		.putAll(ReplacementProfileStatusOption.INACTIVE, ImmutableSet.of(ReplacementProfileStatusOption.ACTIVE))
		.putAll(ReplacementProfileStatusOption.ACTIVE, ImmutableSet.copyOf(ReplacementProfileStatusOption.values()))
		.putAll(ReplacementProfileStatusOption.RETURNED, ImmutableSet.copyOf(ReplacementProfileStatusOption.values()))
		.putAll(ReplacementProfileStatusOption.EXPIRED, ImmutableSet.copyOf(ReplacementProfileStatusOption.values()))
		.build();
	
	private UserService userService;
	private OrganizationUnitService organizationUnitService;
	private GroupService groupService;
	private DocumentService documentService;
	private WorkflowExecutionService workflowExecutionService;
	private ReplacementProfileInstanceService replacementProfileInstanceService;
	private OutOfOfficeMailService outOfOfficeMailService;
	
	private AuditService auditService;
	
	private ReplacementProfileDao replacementProfileDao;
	private ReplacementProfileStatusDao replacementProfileStatusDao;
	
	private ReplacementProfilesOutOfOfficeTemplatingHelper outOfOfficeTemplatingHelper;
	
	private String customTitleTemplateForReplacedUser;
	private String replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(

			userService,
			organizationUnitService,
			groupService,
			documentService,
			workflowExecutionService,
			replacementProfileInstanceService,
			outOfOfficeMailService,
			
			auditService,
				
			replacementProfileDao,
			replacementProfileStatusDao,
			
			outOfOfficeTemplatingHelper,
			
			customTitleTemplateForReplacedUser,
			replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser
		);
	}
	
	private boolean isVisibleForUser(ReplacementProfile replacementProfile, SecurityManager userSecurity) {
		
		if (SecurityUtils.isUserAdmin(userSecurity)) {
			return true;
		}
			
		String currentUserUsername = userSecurity.getUserUsername();
		String requesterUsername = replacementProfile.getRequesterUsername();
		
		return (currentUserUsername.equalsIgnoreCase(requesterUsername));
	}
	
	private ReplacementProfileStatus obtainStatus(ReplacementProfile replacementProfile) {
		boolean isNewReplacementProfile = replacementProfile.isNew();
		if (isNewReplacementProfile) {
			return new ReplacementProfileStatus(replacementProfile, STATUS_OPTION_FOR_NEW_REPLACEMENT_PROFILE);
		} else {
			return replacementProfileStatusDao.getReplacementProfileStatusByProfile(replacementProfile);
		}
	}

	@Override
	public List<ReplacementProfile> getVisibleReplacementProfiles(SecurityManager userSecurity) throws AppException {
		if (SecurityUtils.isUserAdmin(userSecurity)) {
			return replacementProfileDao.getAllReplacementProfiles();
		} else {
			String currentUserUsername = userSecurity.getUserUsername();
			return replacementProfileDao.getReplacementProfilesForRequesterWithUsername(currentUserUsername);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteReplacementProfileById(Long replacementProfileId, SecurityManager userSecurity) throws AppException {
		
		ReplacementProfile replacementProfile = replacementProfileDao.getReplacementProfileById(replacementProfileId);
		
		ReplacementProfileStatus status = replacementProfile.getStatus();
		ReplacementProfileStatusOption statusOption = status.getStatus();
		if (!ALLOWED_STATUSES_FOR_REPLACEMENT_PROFILE_DELETION.contains(statusOption)) {
			throw new AppException(AppExceptionCodes.REPLACEMENT_PROFILE_CANNOT_BE_DELETED_DUE_TO_STATUS);
		}
		
		if (!isVisibleForUser(replacementProfile, userSecurity)) {
			
			String logMessage = "Utilizatorul cu ID-ul [" + userSecurity.getUserId() + "] si username-ul " +
				"[" + userSecurity.getUserUsername() + "] a incercat sa STEARGA profilul de inlocuire cu ID-ul " +
				"[" + replacementProfile.getId() + "] si username-ul solicitantului [" + replacementProfile.getRequesterUsername() + "], " +
				"insa utilizatorul NU are drept asupra profilului.";
			LOGGER.error(logMessage, "stergerea unui profil de inlocuire", userSecurity);
			
			throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
		}
		
		replacementProfileStatusDao.deleteReplacementProfileStatus(status);
		replacementProfileDao.deleteReplacementProfile(replacementProfile);
		
		auditService.auditReplacementProfileOperation(userSecurity, replacementProfile, AuditEntityOperation.DELETE);
	}

	@Override
	public ReplacementProfile getReplacementProfileById(Long replacementProfileId, SecurityManager userSecurity) throws AppException {
		
		ReplacementProfile replacementProfile = replacementProfileDao.getReplacementProfileById(replacementProfileId);
		
		if (!isVisibleForUser(replacementProfile, userSecurity)) {
			
			String logMessage = "Utilizatorul cu ID-ul [" + userSecurity.getUserId() + "] si username-ul " +
				"[" + userSecurity.getUserUsername() + "] a incercat sa CITEASCA profilul de inlocuire cu ID-ul " +
				"[" + replacementProfile.getId() + "] si username-ul solicitantului [" + replacementProfile.getRequesterUsername() + "], " +
				"insa utilizatorul NU are drept asupra profilului.";
			LOGGER.error(logMessage, "luarea unui profil de inlocuire dupa ID", userSecurity);
			
			throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
		}
		
		auditService.auditReplacementProfileOperation(userSecurity, replacementProfile, AuditEntityOperation.READ);
		
		return replacementProfile;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveReplacementProfile(ReplacementProfile replacementProfile, SecurityManager userSecurity) throws AppException {
		
		boolean isReplacementProfileNew = replacementProfile.isNew();
		
		ReplacementProfileStatus status = obtainStatus(replacementProfile);
		replacementProfile.setStatus(status);
		
		if (!isVisibleForUser(replacementProfile, userSecurity)) {
			
			String logMessage = "Utilizatorul cu ID-ul [" + userSecurity.getUserId() + "] si username-ul " +
				"[" + userSecurity.getUserUsername() + "] a incercat sa SALVEZE profilul de inlocuire cu ID-ul " +
				"[" + ((replacementProfile.getId() != null) ? replacementProfile.getId() : "(nou)") + "] si " +
				"username-ul solicitantului [" + replacementProfile.getRequesterUsername() + "], insa utilizatorul " +
				"NU are drept asupra profilului.";
			LOGGER.error(logMessage, "salvarea unui profil de inlocuire", userSecurity);
			
			throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
		}
		
		ReplacementProfileStatusOption statusOption = status.getStatus();
		if (!ALLOWED_STATUSES_FOR_REPLACEMENT_PROFILE_MODIFICATION.contains(statusOption)) {
			throw new AppException(AppExceptionCodes.REPLACEMENT_PROFILE_CANNOT_BE_MODIFIED_DUE_TO_STATUS);
		}
		
		new ReplacementProfileValidator(replacementProfile, ALLOWED_STATUSES_FOR_OVERLAPPING_REPLACEMENT_PROFILES,
			ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_FOR_NEW_PROFILE,
			ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_BY_REQUESTING_REPLACEMENT_PROFILE_STATUS,
			replacementProfileDao).validate();
		
		replacementProfileDao.createOrUpdateReplacementProfile(replacementProfile);
		replacementProfileStatusDao.createOrUpdateReplacementProfileStatus(status);
		
		AuditEntityOperation operation = (isReplacementProfileNew) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
		auditService.auditReplacementProfileOperation(userSecurity, replacementProfile, operation);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void returned(Long replacementProfileId, SecurityManager userSecurity) throws AppException {
		
		ReplacementProfile replacementProfile = replacementProfileDao.getReplacementProfileById(replacementProfileId);
		
		if (!replacementProfile.getStatus().getStatus().equals(ReplacementProfileStatusOption.ACTIVE)) {
			throw new AppException(AppExceptionCodes.ONLY_ACTIVE_REPLACEMENT_PROFILES_CAN_BE_RETURNED);
		}
		
		new ReturnedReplacementProfileHelper(replacementProfile, documentService,
			workflowExecutionService, replacementProfileInstanceService,
			outOfOfficeMailService, userService, replacementProfileDao)
			.returned();
		
		auditService.auditReplacementProfileOperation(userSecurity, replacementProfile, AuditEntityOperation.RETURN);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void activateReplacementProfilesThatBegan(Date referenceDate) throws AppException {
		new ActivateReplacementProfilesThatBeganHelper(referenceDate, userService, organizationUnitService, groupService,
			documentService, workflowExecutionService, replacementProfileInstanceService, outOfOfficeMailService,
			replacementProfileDao, outOfOfficeTemplatingHelper, customTitleTemplateForReplacedUser,
			replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser)
			.activate();
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void expireReplacementProfilesThatEnded(Date referenceDate) throws AppException {
		new ExpireReplacementProfilesThatEndedHelper(referenceDate, documentService, workflowExecutionService,
			replacementProfileInstanceService, outOfOfficeMailService, userService, replacementProfileDao)
			.expire();
	}
	
	@Override
	public Collection<ReplacementProfile> getReplacementProfilesWithIds(Collection<Long> replacementProfileIds) {
		return replacementProfileDao.getReplacementProfilesWithIds(replacementProfileIds);
	}
	
	@Override
	public List<ReplacementProfile> getAvailableReplacementProfilesInWhichRequesterIsReplacement(
			Long idForRequestingReplacementProfile, Collection<Long> idsForRequesterUserProfiles,
			Date startDate, Date endDate) {
		
		
		Collection<ReplacementProfileStatusOption> allowedStatuses = null;
		if (idForRequestingReplacementProfile != null) {
			ReplacementProfileStatusOption status = replacementProfileStatusDao.getStatusForReplacementProfile(idForRequestingReplacementProfile);
			allowedStatuses = ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_BY_REQUESTING_REPLACEMENT_PROFILE_STATUS.get(status);
		} else {
			allowedStatuses = ALLOWED_STATUSES_FOR_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT_FOR_NEW_PROFILE;
		}
		
		return replacementProfileDao.getReplacementProfilesOfReplacementsWithOverlappingDateIntervalWithStatuses(
			idsForRequesterUserProfiles, startDate, endDate, allowedStatuses);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setOrganizationUnitService(OrganizationUnitService organizationUnitService) {
		this.organizationUnitService = organizationUnitService;
	}
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setReplacementProfileInstanceService(ReplacementProfileInstanceService replacementProfileInstanceService) {
		this.replacementProfileInstanceService = replacementProfileInstanceService;
	}
	public void setOutOfOfficeMailService(OutOfOfficeMailService outOfOfficeMailService) {
		this.outOfOfficeMailService = outOfOfficeMailService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setReplacementProfileDao(ReplacementProfileDao replacementProfileDao) {
		this.replacementProfileDao = replacementProfileDao;
	}
	public void setReplacementProfileStatusDao(ReplacementProfileStatusDao replacementProfileStatusDao) {
		this.replacementProfileStatusDao = replacementProfileStatusDao;
	}
	public void setOutOfOfficeTemplatingHelper(ReplacementProfilesOutOfOfficeTemplatingHelper outOfOfficeTemplatingHelper) {
		this.outOfOfficeTemplatingHelper = outOfOfficeTemplatingHelper;
	}
	public void setCustomTitleTemplateForReplacedUser(String customTitleTemplateForReplacedUser) {
		this.customTitleTemplateForReplacedUser = customTitleTemplateForReplacedUser;
	}
	public void setReplacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser(String replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser) {
		this.replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser = replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser;
	}
}