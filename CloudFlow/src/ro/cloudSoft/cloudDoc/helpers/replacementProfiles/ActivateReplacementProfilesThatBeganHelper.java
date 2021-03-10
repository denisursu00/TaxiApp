package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.mail.OutOfOfficeMailService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * 
 */
public class ActivateReplacementProfilesThatBeganHelper {
	

	private final Date referenceDate;
	
	private final UserService userService;
	private final OrganizationUnitService organizationUnitService;
	private final GroupService groupService;
	private final DocumentService documentService;
	private final WorkflowExecutionService workflowExecutionService;
	private final ReplacementProfileInstanceService replacementProfileInstanceService;
	private final OutOfOfficeMailService outOfOfficeMailService;
	
	private final ReplacementProfileDao replacementProfileDao;
	
	private final ReplacementProfilesOutOfOfficeTemplatingHelper outOfOfficeTemplatingHelper;
	
	private final String customTitleTemplateForReplacedUser;
	private final String replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser;
	

	private ListMultimap<DocumentIdentifier, ReplacementProfileInstanceItem> replacementProfileInstanceItemsByDocumentIdentifier;
	private Multimap<DocumentIdentifier, Long> idsForAssignedReplacementUsersByDocumentIdentifier;
	private Collection<User> userProfilesWithChangedCustomTitleTemplates;

	
	public ActivateReplacementProfilesThatBeganHelper(Date referenceDate, UserService userService, OrganizationUnitService organizationUnitService,
			GroupService groupService, DocumentService documentService, WorkflowExecutionService workflowExecutionService,
			ReplacementProfileInstanceService replacementProfileInstanceService, OutOfOfficeMailService outOfOfficeMailService,
			ReplacementProfileDao replacementProfileDao, ReplacementProfilesOutOfOfficeTemplatingHelper outOfOfficeTemplatingHelper,
			String customTitleTemplateForReplacedUser, String replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser) {
		
		this.referenceDate = referenceDate;
		
		this.userService = userService;
		this.organizationUnitService = organizationUnitService;
		this.groupService = groupService;
		this.documentService = documentService;
		this.workflowExecutionService = workflowExecutionService;
		this.replacementProfileInstanceService = replacementProfileInstanceService;
		this.outOfOfficeMailService = outOfOfficeMailService;
		
		this.replacementProfileDao = replacementProfileDao;
		
		this.outOfOfficeTemplatingHelper = outOfOfficeTemplatingHelper;
		
		this.customTitleTemplateForReplacedUser = customTitleTemplateForReplacedUser;
		this.replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser = replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser;
	}

	public void activate() throws AppException {
		
		init();
		
		Collection<ReplacementProfile> inactiveReplacementProfilesThatBegan = replacementProfileDao.getInactiveReplacementProfilesThatBegan(referenceDate);
		for (ReplacementProfile replacementProfile : inactiveReplacementProfilesThatBegan) {
			processReplacementProfile(replacementProfile);
		}
		
		updateUsersWithChangedCustomTitleTemplate();
		
		for (DocumentIdentifier documentIdentifier : replacementProfileInstanceItemsByDocumentIdentifier.keySet()) {
			addReplacementProfileInstanceItemsForDocument(documentIdentifier);
			addReplacementEditingPermissionsForDocument(documentIdentifier);
		}
	}
	
	private void init() {
		replacementProfileInstanceItemsByDocumentIdentifier = LinkedListMultimap.create();
		idsForAssignedReplacementUsersByDocumentIdentifier = HashMultimap.create();
		userProfilesWithChangedCustomTitleTemplates = Lists.newArrayList();
	}
	
	private void processReplacementProfile(ReplacementProfile replacementProfile) throws AppException {
		
		User replacementUser = replacementProfile.getReplacement();
		
		for (User selectedRequesterUserProfile : replacementProfile.getSelectedRequesterUserProfiles()) {
			
			addReplacementAsAssignedToCurrentTasksOfReplacedUser(replacementProfile, selectedRequesterUserProfile, replacementUser);
			addReplacementAsAssignedToCurrentTasksOfOrganizationUnitsOfReplacedUser(replacementProfile, selectedRequesterUserProfile, replacementUser);
			addReplacementAsAssignedToCurrentTasksOfGroupsOfReplacedUser(replacementProfile, selectedRequesterUserProfile, replacementUser);
			
			setCustomTitleTemplateAndMarkReplacedUser(selectedRequesterUserProfile, replacementUser);
		}
		
		markReplacementProfileAsActive(replacementProfile);
		activateOutOfOfficeIfNeeded(replacementProfile);
	}
	
	private void addReplacementAsAssignedToCurrentTasksOfReplacedUser(ReplacementProfile replacementProfile, User selectedRequesterUserProfile, User replacementUser) {
		
		Long userId = selectedRequesterUserProfile.getId();
		Long replacementUserId = replacementUser.getId();
		
		Collection<String> idsForTasksToIgnore = getIdsForTasksToIgnoreForUserToBeReplaced(userId, replacementProfile);
		
		Collection<DocumentIdentifier> documentIdentifiersForTasksWithAssignedReplacementForUser = workflowExecutionService.addReplacementAsAssignedToCurrentTasksOfUser(userId, replacementUserId, idsForTasksToIgnore);
		if (!documentIdentifiersForTasksWithAssignedReplacementForUser.isEmpty()) {
			processDocumentIdentifiersForTasksWithAssignedReplacement(documentIdentifiersForTasksWithAssignedReplacementForUser, replacementProfile, selectedRequesterUserProfile, replacementUser);
		}
	}
	
	private Collection<String> getIdsForTasksToIgnoreForUserToBeReplaced(Long userId, ReplacementProfile replacementProfileWhereUserIsRequester) {

		Map<DocumentIdentifier, String> taskIdByDocumentIdentifierForTasksAssignedToUser = workflowExecutionService.getTaskIdByDocumentIdentifierForTasksAssignedToUser(userId);
		Collection<String> idsForTasksToIgnore = Sets.newHashSet();
		
		for (Entry<DocumentIdentifier, String> entry : taskIdByDocumentIdentifierForTasksAssignedToUser.entrySet()) {
			
			DocumentIdentifier documentIdentifierOfTaskAssignedToUser = entry.getKey();
			String idOfTaskAssignedToUser = entry.getValue();
			
			ReplacementProfileInstanceItem replacementProfileInstanceItemWhereUserIsAssignedAsReplacement = replacementProfileInstanceService.getReplacementProfileInstanceItemForReplacement(
				documentIdentifierOfTaskAssignedToUser.getDocumentLocationRealName(), documentIdentifierOfTaskAssignedToUser.getDocumentId(), userId);
			
			boolean isUserAssignedAsReplacement = (replacementProfileInstanceItemWhereUserIsAssignedAsReplacement != null);
			if (!isUserAssignedAsReplacement) {
				continue;
			}
			
			boolean shouldReplaceUserIfUserIsAssignedAsReplacement = replacementProfileWhereUserIsRequester.selectedReplacementProfileInWhichRequesterIsReplacementExists(
				replacementProfileInstanceItemWhereUserIsAssignedAsReplacement.getReplacementProfileUsed());
			
			if (!shouldReplaceUserIfUserIsAssignedAsReplacement) {
				idsForTasksToIgnore.add(idOfTaskAssignedToUser);
			}
		}
		return idsForTasksToIgnore;
	}
	
	private void addReplacementAsAssignedToCurrentTasksOfOrganizationUnitsOfReplacedUser(ReplacementProfile replacementProfile, User selectedRequesterUserProfile, User replacementUser) {

		Long userId = selectedRequesterUserProfile.getId();
		Long replacementUserId = replacementUser.getId();
		
		Collection<Long> organizationUnitIdsOfUser = userService.getOrganizationUnitIds(userId);
		for (Long organizationUnitIdOfUser : organizationUnitIdsOfUser) {
			Collection<DocumentIdentifier> documentIdentifiersForTasksWithAssignedReplacementForOrganizationUnit = workflowExecutionService.addReplacementAsAssignedToCurrentTasksOfOrganizationUnit(organizationUnitIdOfUser, replacementUserId);
			if (!documentIdentifiersForTasksWithAssignedReplacementForOrganizationUnit.isEmpty()) {
				OrganizationUnit organizationUnitOfUser = organizationUnitService.getOrganizationUnitById(organizationUnitIdOfUser);
				processDocumentIdentifiersForTasksWithAssignedReplacement(documentIdentifiersForTasksWithAssignedReplacementForOrganizationUnit, replacementProfile, organizationUnitOfUser, replacementUser);
			}
		}
	}
	
	private void addReplacementAsAssignedToCurrentTasksOfGroupsOfReplacedUser(ReplacementProfile replacementProfile, User selectedRequesterUserProfile, User replacementUser) {

		Long userId = selectedRequesterUserProfile.getId();
		Long replacementUserId = replacementUser.getId();
		
		Collection<Long> groupIdsOfUser = userService.getGroupIds(userId);
		for (Long groupIdOfUser : groupIdsOfUser) {
			Collection<DocumentIdentifier> documentIdentifiersForTasksWithAssignedReplacementForGroup = workflowExecutionService.addReplacementAsAssignedToCurrentTasksOfGroup(groupIdOfUser, replacementUserId);
			if (!documentIdentifiersForTasksWithAssignedReplacementForGroup.isEmpty()) {
				Group groupOfUser = groupService.getGroupById(groupIdOfUser);
				processDocumentIdentifiersForTasksWithAssignedReplacement(documentIdentifiersForTasksWithAssignedReplacementForGroup, replacementProfile, groupOfUser, replacementUser);
			}
		}
	}
	
	private void setCustomTitleTemplateAndMarkReplacedUser(User replacedUser, User replacementUser) {
		
		String replacementUserName = replacementUser.getName();
		String customTitleTemplate = customTitleTemplateForReplacedUser.replace(replacementUserNamePlaceholderInCustomTitleTemplateForReplacedUser, replacementUserName);
		
		replacedUser.setCustomTitleTemplate(customTitleTemplate);
		userProfilesWithChangedCustomTitleTemplates.add(replacedUser);
	}
	
	private void processDocumentIdentifiersForTasksWithAssignedReplacement(Collection<DocumentIdentifier> documentIdentifiersForTasksWithAssignedReplacement, ReplacementProfile replacementProfile, OrganizationEntity originallyAssignedEntity, User replacementUser) {
		for (DocumentIdentifier documentIdentifier : documentIdentifiersForTasksWithAssignedReplacement) {
			
			ReplacementProfileInstanceItem replacementProfileInstanceItem = new ReplacementProfileInstanceItem();
			
			replacementProfileInstanceItem.setOriginallyAssignedEntity(originallyAssignedEntity);
			replacementProfileInstanceItem.setReplacementUsed(replacementUser);
			replacementProfileInstanceItem.setReplacementProfileUsed(replacementProfile);
			
			replacementProfileInstanceItemsByDocumentIdentifier.put(documentIdentifier, replacementProfileInstanceItem);
			idsForAssignedReplacementUsersByDocumentIdentifier.put(documentIdentifier, replacementUser.getId());
		}
	}
	
	private void markReplacementProfileAsActive(ReplacementProfile replacementProfile) {
		replacementProfile.getStatus().setStatus(ReplacementProfileStatusOption.ACTIVE);
		replacementProfileDao.createOrUpdateReplacementProfile(replacementProfile);
	}
	
	private void activateOutOfOfficeIfNeeded(ReplacementProfile replacementProfile) throws AppException {
		
		if (!replacementProfile.isOutOfOffice()) {
			return;
		}
		
		for (User requesterUserProfile : replacementProfile.getSelectedRequesterUserProfiles()) {
			
			String requesterEmailAddress = requesterUserProfile.getEmail();
			
			String mailSubject = outOfOfficeTemplatingHelper.replacePlaceholdersInText(
				replacementProfile.getOutOfOfficeEmailSubjectTemplate(), replacementProfile, requesterUserProfile);
			String mailBody = outOfOfficeTemplatingHelper.replacePlaceholdersInText(
				replacementProfile.getOutOfOfficeEmailBodyTemplate(), replacementProfile, requesterUserProfile);
			
			outOfOfficeMailService.activateOutOfOffice(requesterEmailAddress,
				replacementProfile.getStartDate(), replacementProfile.getEndDate(),
				mailSubject, mailBody);
		}
	}
	
	private void updateUsersWithChangedCustomTitleTemplate() {
		userService.updateUsers(userProfilesWithChangedCustomTitleTemplates);
	}
	
	private void addReplacementProfileInstanceItemsForDocument(DocumentIdentifier documentIdentifier) {
		List<ReplacementProfileInstanceItem> replacementProfileInstanceItemsForDocument = replacementProfileInstanceItemsByDocumentIdentifier.get(documentIdentifier);
		replacementProfileInstanceService.addReplacementProfileInstanceItemsForDocument(documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId(), replacementProfileInstanceItemsForDocument);
	}
	
	private void addReplacementEditingPermissionsForDocument(DocumentIdentifier documentIdentifier) throws AppException {
		Collection<Long> idsForAssignedReplacementUsersForDocument = idsForAssignedReplacementUsersByDocumentIdentifier.get(documentIdentifier);
		documentService.addEditingPermissions(documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName(),
			idsForAssignedReplacementUsersForDocument, Collections.<Long> emptySet(), Collections.<Long> emptySet());
	}
}