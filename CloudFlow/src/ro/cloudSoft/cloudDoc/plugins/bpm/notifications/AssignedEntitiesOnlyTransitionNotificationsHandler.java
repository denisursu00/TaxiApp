package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import java.util.Collection;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.AssignedEntityTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;

public class AssignedEntitiesOnlyTransitionNotificationsHandler extends AbstractTransitionNotificationsHandler {

	private final Collection<Long> idsForAssignedUsers;
	private final Collection<Long> idsForAssignedOrganizationUnits;
	private final Collection<Long> idsForAssignedGroups;

	public AssignedEntitiesOnlyTransitionNotificationsHandler(WorkflowTransition transition, OpenExecution execution, MailService mailService,
			DocumentService documentService, UserPersistencePlugin userPersistencePlugin, OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin,
			GroupPersistencePlugin groupPersistencePlugin, Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnits,
			Collection<Long> idsForAssignedGroups) {
		
		super(transition, execution, mailService, documentService, userPersistencePlugin, organizationUnitPersistencePlugin, groupPersistencePlugin);
		
		this.idsForAssignedUsers = idsForAssignedUsers;
		this.idsForAssignedOrganizationUnits = idsForAssignedOrganizationUnits;
		this.idsForAssignedGroups = idsForAssignedGroups;
	}

	@Override
	protected boolean shouldProcess(TransitionNotification notification) {
		return (notification instanceof AssignedEntityTransitionNotification);
	}
	
	@Override
	protected AbstractDestinationForTransitionNotificationProvider getHandlerFor(TransitionNotification notification) {
		
		if (!(notification instanceof AssignedEntityTransitionNotification)) {
			throw new IllegalArgumentException("Notificarea nu este de tip entitati asignate: [" + notification.getClass().getName() + "].");
		}
		
		return new DestinationForAssignedEntitiesTransitionNotificationProvider(idsForAssignedUsers, idsForAssignedOrganizationUnits, idsForAssignedGroups);
	}
}