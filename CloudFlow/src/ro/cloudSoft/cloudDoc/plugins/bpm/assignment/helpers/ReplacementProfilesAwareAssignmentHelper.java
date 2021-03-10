package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jbpm.api.task.Assignable;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.TransitionNotificationsHandlerFactory;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Helper de asignare care tine cont de profilele de inlocuire
 * 
 * 
 */
public class ReplacementProfilesAwareAssignmentHelper implements AssignmentHelper {
	
	
	private final Collection<Long> idsForOriginallyAssignedUsers;
	private final Collection<Long> idsForOriginallyAssignedOrganizationUnits;
	private final Collection<Long> idsForOriginallyAssignedGroups;
	
	private final ExecutionImpl execution;
	private final Assignable assignable;
	
	private final String documentLocationRealName;
	private final String documentId;
	
	private final GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
	
	private final ReplacementProfileInstanceService replacementProfileInstanceService;
	private final WorkflowService workflowService;
	private final DocumentService documentService;
	
	private final TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory;
	
	private final SecurityManager userSecurity;
	
	
	private Set<Long> idsForAssignedReplacementUsers;
	private List<ReplacementProfileInstanceItem> replacementProfileInstanceItems;
	
	
	public ReplacementProfilesAwareAssignmentHelper(Collection<Long> idsForOriginallyAssignedUsers, Collection<Long> idsForOriginallyAssignedOrganizationUnits,
			Collection<Long> idsForOriginallyAssignedGroups, ExecutionImpl execution, Assignable assignable,
			GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper,
			ReplacementProfileInstanceService replacementProfileInstanceService, WorkflowService workflowService,
			DocumentService documentService, TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory,
			SecurityManager userSecurity) {
		
		this.idsForOriginallyAssignedUsers = idsForOriginallyAssignedUsers;
		this.idsForOriginallyAssignedOrganizationUnits = idsForOriginallyAssignedOrganizationUnits;
		this.idsForOriginallyAssignedGroups = idsForOriginallyAssignedGroups;
		
		this.execution = execution;
		this.assignable = assignable;
		
		this.documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		this.documentId = WorkflowVariableHelper.getDocumentId(execution);
		
		this.gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
		
		this.replacementProfileInstanceService = replacementProfileInstanceService;
		this.workflowService = workflowService;
		this.documentService = documentService;
		
		this.transitionNotificationsHandlerFactory = transitionNotificationsHandlerFactory;
		
		this.userSecurity = userSecurity;
	}

	private void init() {
		idsForAssignedReplacementUsers = Sets.newHashSet();
		replacementProfileInstanceItems = Lists.newArrayList();
	}
	
	private void gatherReplacementsByReplacementProfiles() throws AppException {
		
		ReplacementProfilesGathererForAssignmentHelper replacementProfilesGatherer = new ReplacementProfilesGathererForAssignmentHelper(idsForOriginallyAssignedUsers,
			idsForOriginallyAssignedOrganizationUnits, idsForOriginallyAssignedGroups, gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper);
		replacementProfilesGatherer.gatherReplacementsByReplacementProfiles();
		
		idsForAssignedReplacementUsers = replacementProfilesGatherer.getIdsForReplacementUsers();
		replacementProfileInstanceItems = replacementProfilesGatherer.getReplacementProfileInstanceItems();
	}
	
	private void assign() throws AppException {
		
		Collection<Long> idsForAssignedUsers = Lists.newLinkedList();
		idsForAssignedUsers.addAll(idsForOriginallyAssignedUsers);
		idsForAssignedUsers.addAll(idsForAssignedReplacementUsers);
		
		Collection<Long> idsForAssignedOrganizationUnits = idsForOriginallyAssignedOrganizationUnits;
		Collection<Long> idsForAssignedGroups = idsForOriginallyAssignedGroups;
		
		AssignTaskAssignmentHelper assignTaskHelper = new AssignTaskAssignmentHelper(idsForAssignedUsers,
			idsForAssignedOrganizationUnits, idsForAssignedGroups, execution, assignable, userSecurity);
		assignTaskHelper.assign();
	}
	
	private void notifyByEmailIfNeeded() {
		
		Collection<Long> idsForAssignedUsers = Lists.newLinkedList();
		idsForAssignedUsers.addAll(idsForOriginallyAssignedUsers);
		idsForAssignedUsers.addAll(idsForAssignedReplacementUsers);
		
		Collection<Long> idsForAssignedOrganizationUnits = idsForOriginallyAssignedOrganizationUnits;
		Collection<Long> idsForAssignedGroups = idsForOriginallyAssignedGroups;
		
		EmailNotificationsSenderForAssignmentHelper emailNotificationsSender = new EmailNotificationsSenderForAssignmentHelper(
			idsForAssignedUsers, idsForAssignedOrganizationUnits, idsForAssignedGroups, execution, workflowService,
			transitionNotificationsHandlerFactory);
		emailNotificationsSender.notifyByEmailIfNeeded();
	}
	
	private void saveReplacementProfileInstance() {
		
		ReplacementProfileInstance replacementProfileInstance = new ReplacementProfileInstance();
		
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		
		replacementProfileInstance.setDocumentIdentifiers(documentLocationRealName, documentId);
		replacementProfileInstance.setItems(replacementProfileInstanceItems);
		
		replacementProfileInstanceService.setReplacementProfileInstanceForDocument(replacementProfileInstance);
	}
	
	private void changePermissions() throws AppException {
		
		String senderUserIdAsString = userSecurity.getUserIdAsString();
		
		List<Long> idsForUsersThatWillReceiveDocument = Lists.newLinkedList();
		idsForUsersThatWillReceiveDocument.addAll(idsForOriginallyAssignedUsers);
		idsForUsersThatWillReceiveDocument.addAll(idsForAssignedReplacementUsers);

		List<Long> idsForOrganizationUnitsThatWillReceiveDocument = Lists.newLinkedList(idsForOriginallyAssignedOrganizationUnits);
		List<Long> idsForGroupsThatWillReceiveDocument = Lists.newLinkedList(idsForOriginallyAssignedGroups);
		
		documentService.changePermissionsForWorkflow(documentId, documentLocationRealName, senderUserIdAsString,
			idsForUsersThatWillReceiveDocument, idsForOrganizationUnitsThatWillReceiveDocument, idsForGroupsThatWillReceiveDocument);
	}

	@Override
	public void handleAssignment() throws AppException {
		init();
		gatherReplacementsByReplacementProfiles();
		assign();
		notifyByEmailIfNeeded();
		saveReplacementProfileInstance();
		changePermissions();
	}
}