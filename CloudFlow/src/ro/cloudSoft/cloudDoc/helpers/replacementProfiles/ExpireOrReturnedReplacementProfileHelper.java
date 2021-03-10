package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.mail.OutOfOfficeMailService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * 
 */
public abstract class ExpireOrReturnedReplacementProfileHelper {
	
	
	private final ReplacementProfile replacementProfile;
	private final ReplacementProfileStatusOption statusToSet;
	
	private final DocumentService documentService;
	private final WorkflowExecutionService workflowExecutionService;
	private final ReplacementProfileInstanceService replacementProfileInstanceService;
	private final OutOfOfficeMailService outOfOfficeMailService;
	private final UserService userService;
	
	private final ReplacementProfileDao replacementProfileDao;

	
	private Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsToDelete;
	private Multimap<DocumentIdentifier, Long> idsForAssignedReplacementUsersToMakeReadersByDocumentIdentifier;
	
	
	protected ExpireOrReturnedReplacementProfileHelper(ReplacementProfile replacementProfile, ReplacementProfileStatusOption statusToSet, DocumentService documentService,
			WorkflowExecutionService workflowExecutionService, ReplacementProfileInstanceService replacementProfileInstanceService, OutOfOfficeMailService outOfOfficeMailService,
			UserService userService, ReplacementProfileDao replacementProfileDao) {
		
		this.replacementProfile = replacementProfile;
		this.statusToSet = statusToSet;
		
		this.documentService = documentService;
		this.workflowExecutionService = workflowExecutionService;
		this.replacementProfileInstanceService = replacementProfileInstanceService;
		this.outOfOfficeMailService = outOfOfficeMailService;
		this.userService = userService;
		
		this.replacementProfileDao = replacementProfileDao;
	}

	protected void expireOrReturned() throws AppException {
		
		init();
		
		Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsForProfile = replacementProfileInstanceService.getReplacementProfileInstanceItemsForProfile(replacementProfile);
		for (ReplacementProfileInstanceItem replacementProfileInstanceItem : replacementProfileInstanceItemsForProfile) {
			
			Long idForReplacementUser = replacementProfileInstanceItem.getReplacementUsed().getId();
			
			ReplacementProfileInstance replacementProfileInstance = replacementProfileInstanceItem.getProfileInstance();
			
			Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsForReplacementsOfReplacement = collectReplacementProfileInstanceItemsForReplacementsOfReplacement(idForReplacementUser, replacementProfileInstance);
			
			String documentLocationRealName = replacementProfileInstance.getDocumentLocationRealName();
			String documentId = replacementProfileInstance.getDocumentId();
			
			Collection<Long> idsForUsersThatWereAssignedAsReplacements = Sets.newHashSet();
			idsForUsersThatWereAssignedAsReplacements.add(idForReplacementUser);
			for (ReplacementProfileInstanceItem replacementProfileInstanceItemForReplacementsOfReplacement : replacementProfileInstanceItemsForReplacementsOfReplacement) {
				Long idForReplacementOfReplacement = replacementProfileInstanceItemForReplacementsOfReplacement.getReplacementUsed().getId();
				idsForUsersThatWereAssignedAsReplacements.add(idForReplacementOfReplacement);
			}
			
			workflowExecutionService.removeAssignedUsersFromDocumentAssociatedTask(idsForUsersThatWereAssignedAsReplacements, documentLocationRealName, documentId);
			
			DocumentIdentifier documentIdentifier = new DocumentIdentifier(documentLocationRealName, documentId);
			idsForAssignedReplacementUsersToMakeReadersByDocumentIdentifier.putAll(documentIdentifier, idsForUsersThatWereAssignedAsReplacements);
			
			replacementProfileInstanceItemsToDelete.add(replacementProfileInstanceItem);
			replacementProfileInstanceItemsToDelete.addAll(replacementProfileInstanceItemsForReplacementsOfReplacement);
		}
		
		markReplacementProfileWithStatus();
		deleteReplacementProfileInstanceItems();
		deactivateOutOfOfficeIfSet();
		removeCustomTitleTemplatesFromUsersThatWereReplaced();
		makeReplacementsReadersInDocuments();
	}

	private void init() {
		replacementProfileInstanceItemsToDelete = Lists.newLinkedList();
		idsForAssignedReplacementUsersToMakeReadersByDocumentIdentifier = HashMultimap.create();
	}
	
	private Collection<ReplacementProfileInstanceItem> collectReplacementProfileInstanceItemsForReplacementsOfReplacement(
			Long idForReplacementUser, ReplacementProfileInstance replacementProfileInstanceWhereToLook) {

		Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsWhereAReplacementWasReplaced = Lists.newArrayList();
		
		Long idForReplacedUserToFind = idForReplacementUser;
		do {
			boolean isReplacedUserFound = false;
			
			for (ReplacementProfileInstanceItem replacementProfileInstanceItemForDocument : replacementProfileInstanceWhereToLook.getItems()) {
				OrganizationEntity originallyAssignedEntity = replacementProfileInstanceItemForDocument.getOriginallyAssignedEntity();
				if (originallyAssignedEntity.isUser() && originallyAssignedEntity.getId().equals(idForReplacedUserToFind)) {
					
					Long idForReplacementUserUsed = replacementProfileInstanceItemForDocument.getReplacementUsed().getId();
					replacementProfileInstanceItemsWhereAReplacementWasReplaced.add(replacementProfileInstanceItemForDocument);					
					idForReplacedUserToFind = idForReplacementUserUsed;
					
					isReplacedUserFound = true;
					break;
				}
			}
			
			if (!isReplacedUserFound) {
				break;
			}
		} while (true);
		
		return replacementProfileInstanceItemsWhereAReplacementWasReplaced;
	}
	
	private void markReplacementProfileWithStatus() {
		replacementProfile.getStatus().setStatus(statusToSet);
		replacementProfileDao.createOrUpdateReplacementProfile(replacementProfile);
	}
	
	private void deleteReplacementProfileInstanceItems() {
		
		for (ReplacementProfileInstanceItem replacementProfileInstanceItemToDelete : replacementProfileInstanceItemsToDelete) {
			replacementProfileInstanceItemToDelete.getProfileInstance().getItems().remove(replacementProfileInstanceItemToDelete);
		}
		
		replacementProfileInstanceService.deleteReplacementProfileInstanceItems(replacementProfileInstanceItemsToDelete);
	}
	
	private void deactivateOutOfOfficeIfSet() throws AppException {
		Collection<String> requesterEmailAddresses = replacementProfile.getRequesterEmailAddresses();
		for (String requesterEmailAddress : requesterEmailAddresses) {
			outOfOfficeMailService.deactivateOutOfOffice(requesterEmailAddress);
		}
	}
	
	private void removeCustomTitleTemplatesFromUsersThatWereReplaced() {
		Collection<User> usersThatWereReplaced = replacementProfile.getSelectedRequesterUserProfiles();
		for (User userThatWasReplaced : usersThatWereReplaced) {
			userThatWasReplaced.setCustomTitleTemplate(null);
		}
		userService.updateUsers(usersThatWereReplaced);
	}
	
	private void makeReplacementsReadersInDocuments() throws AppException {
		for (DocumentIdentifier documentIdentifier : idsForAssignedReplacementUsersToMakeReadersByDocumentIdentifier.keySet()) {
			
			String documentId = documentIdentifier.getDocumentId();
			String documentLocationRealName = documentIdentifier.getDocumentLocationRealName();
			
			Collection<Long> idsForAssignedReplacementUsersToMakeReadersForDocument = idsForAssignedReplacementUsersToMakeReadersByDocumentIdentifier.get(documentIdentifier);
			
			documentService.makeReadersIfEditors(documentId, documentLocationRealName, idsForAssignedReplacementUsersToMakeReadersForDocument);
		}
	}
}