package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;

import org.jbpm.pvm.internal.model.ExecutionImpl;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.AbstractTransitionNotificationsHandler;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.TransitionNotificationsHandlerFactory;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;

/**
 * Se ocupa cu notificarea pe e-mail a entitatilor organizatorice asignate.
 * 
 * 
 */
public class EmailNotificationsSenderForAssignmentHelper {

	
	private final Collection<Long> idsForAssignedUsers;
	private final Collection<Long> idsForAssignedOrganizationUnits;
	private final Collection<Long> idsForAssignedGroups;
	
	private final ExecutionImpl execution;
	
	private final WorkflowService workflowService;
	
	private final TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory;
	
	
	public EmailNotificationsSenderForAssignmentHelper(Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnits,
			Collection<Long> idsForAssignedGroups, ExecutionImpl execution, WorkflowService workflowService,
			TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory) {
		
		this.idsForAssignedUsers = idsForAssignedUsers;
		this.idsForAssignedOrganizationUnits = idsForAssignedOrganizationUnits;
		this.idsForAssignedGroups = idsForAssignedGroups;
		
		this.execution = execution;
		
		this.workflowService = workflowService;
		
		this.transitionNotificationsHandlerFactory = transitionNotificationsHandlerFactory;
	}
	
	public void notifyByEmailIfNeeded() {
		
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		Long documentTypeId = WorkflowVariableHelper.getDocumentTypeId(execution);
		String transitionName = WorkflowVariableHelper.getLastTransitionName(execution);
		String stateCode = execution.getActivityName();
		
		WorkflowTransition transition = workflowService.getTransitionForDocument(
			documentLocationRealName, documentId, documentTypeId, transitionName, stateCode);
		
		AbstractTransitionNotificationsHandler transitionNotificationsHandler = transitionNotificationsHandlerFactory.forAssignedEntitiesOnly(
			transition, execution, idsForAssignedUsers, idsForAssignedOrganizationUnits, idsForAssignedGroups);
		transitionNotificationsHandler.handleNotifications();
	}
}