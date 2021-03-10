package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.AssignedEntityTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.InitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.ManuallyChosenEntitiesTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.UserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

public class AllExceptAssignedEntitiesTransitionNotificationsHandler extends AbstractTransitionNotificationsHandler {
	
	private final OpenExecution execution;
	
	private final DocumentService documentService;
	private final UserService userService;

	public AllExceptAssignedEntitiesTransitionNotificationsHandler(WorkflowTransition transition, OpenExecution execution, MailService mailService,
			DocumentService documentService, UserService userService, UserPersistencePlugin userPersistencePlugin,
			OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin, GroupPersistencePlugin groupPersistencePlugin) {
		
		super(transition, execution, mailService, documentService, userPersistencePlugin, organizationUnitPersistencePlugin, groupPersistencePlugin);
		
		this.execution = execution;
		
		this.documentService = documentService;
		this.userService = userService;
	}

	@Override
	protected boolean shouldProcess(TransitionNotification notification) {
		return (!(notification instanceof AssignedEntityTransitionNotification));
	}
	
	@Override
	protected AbstractDestinationForTransitionNotificationProvider getHandlerFor(TransitionNotification notification) {
		if (notification instanceof InitiatorTransitionNotification) {
			return new DestinationForInitiatorTransitionNotificationProvider(execution, documentService);
		} else if (notification instanceof HierarchicalSuperiorOfInitiatorTransitionNotification) {
			return new DestinationForHierarchicalSuperiorOfInitiatorTransitionNotificationProvider(execution, documentService, userService);
		} else if (notification instanceof ManuallyChosenEntitiesTransitionNotification) {
			ManuallyChosenEntitiesTransitionNotification manuallyChosenEntitiesNotification = (ManuallyChosenEntitiesTransitionNotification) notification;
			return new DestinationForManuallyChosenEntitiesTransitionNotificationProvider(manuallyChosenEntitiesNotification);
		} else if (notification instanceof UserMetadataTransitionNotification) {
			UserMetadataTransitionNotification userMetadataNotification = (UserMetadataTransitionNotification) notification;
			return new DestinationForUserMetadataTransitionNotificationProvider(userMetadataNotification, execution, documentService);
		} else if (notification instanceof  HierarchicalSuperiorOfUserMetadataTransitionNotification) {
			HierarchicalSuperiorOfUserMetadataTransitionNotification hierarchicalSuperiorOfUserMetadataTransitionNotification = (HierarchicalSuperiorOfUserMetadataTransitionNotification) notification;
			return new DestinationForHierarchicalSuperiorOfUserMetadataTransitionNotificationProvider(hierarchicalSuperiorOfUserMetadataTransitionNotification, execution, documentService, userService);
		} else {
			throw new IllegalArgumentException("Tipul de notificare [" + notification.getClass().getName() + "] NU este suportat.");
		}
	}
}