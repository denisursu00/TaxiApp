package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;
import ro.cloudSoft.cloudDoc.plugins.bpm.EmailEvaluator;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class AbstractTransitionNotificationsHandler {
	
	private final LogHelper logger = LogHelper.getInstance(getClass());
	
	private final WorkflowTransition transition;
	
	private final OpenExecution execution;
	
	private final MailService mailService;
	private final DocumentService documentService;
	
	private final UserPersistencePlugin userPersistencePlugin;
	private final OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	private final GroupPersistencePlugin groupPersistencePlugin;

	protected AbstractTransitionNotificationsHandler(WorkflowTransition transition, OpenExecution execution, MailService mailService, DocumentService documentService,
			UserPersistencePlugin userPersistencePlugin, OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin, GroupPersistencePlugin groupPersistencePlugin) {
		
		this.transition = transition;
		
		this.execution = execution;
		
		this.mailService = mailService;
		this.documentService = documentService;
		
		this.userPersistencePlugin = userPersistencePlugin;
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
		this.groupPersistencePlugin = groupPersistencePlugin;
	}

	public void handleNotifications() {
		for (TransitionNotification notification : transition.getNotifications()) {
			
			if (!shouldProcess(notification)) {
				continue;
			}
			
			AbstractDestinationForTransitionNotificationProvider destinationForNotificationProvider = getHandlerFor(notification);
			Multimap<OrganizationEntityType, Long> idsForDestinationOrganizationEntities = destinationForNotificationProvider.getIdsForDestinationOrganizationEntities();
			if (idsForDestinationOrganizationEntities.isEmpty()) {
				
				String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
				String documentId = WorkflowVariableHelper.getDocumentId(execution);
				
				DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
				
				String exceptionMessage = "Pentru documentul cu atributele: " + documentLogAttributes + ", tranzitia cu ID-ul " +
					"[" + transition.getId() + "] si numele [" + transition.getName() + "], notificarea cu ID-ul [" + notification.getId() + "] " +
					"si tipul [" + notification.getClass().getName() + "], NU s-au putut determina destinatarii notificarii prin e-mail.";
				throw new RuntimeException(exceptionMessage);
			}
			List<String> emailAddresses = getEmailAddresses(idsForDestinationOrganizationEntities);
			
			EmailMessage emailMessage = getEmailMessage(notification, emailAddresses);
			try {
				mailService.send(emailMessage);
			} catch (RuntimeException re) {
				
				String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
				String documentId = WorkflowVariableHelper.getDocumentId(execution);
				
				DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
				
				String logMessage = "Exceptie la trimiterea prin e-mail pentru documentul cu atributele: " + documentLogAttributes + ", " +
					"tranzitia cu ID-ul [" + transition.getId() + "] si numele [" + transition.getName() + "], notificarea " +
					"cu ID-ul [" + notification.getId() + "] si tipul [" + notification.getClass().getName() + "]. " +
					"Mesajul de e-mail este: [" + emailMessage + "].";
				logger.error(logMessage, re, "notificarea prin e-mail");
			}
		}
	}
	
	protected abstract boolean shouldProcess(TransitionNotification notification);
	
	protected abstract AbstractDestinationForTransitionNotificationProvider getHandlerFor(TransitionNotification notification);
	
	private List<String> getEmailAddresses(Multimap<OrganizationEntityType, Long> idsForDestinationOrganizationEntities) {
			
		List<String> emailAddresses = Lists.newLinkedList();
		
		Collection<Long> idsForDestinationUsers = idsForDestinationOrganizationEntities.get(OrganizationEntityType.USER);
		if (!idsForDestinationUsers.isEmpty()) {
			List<String> emailsFromOriginallyAssignedUsers = userPersistencePlugin.getEmails(idsForDestinationUsers);
			emailAddresses.addAll(emailsFromOriginallyAssignedUsers);
		}

		Collection<Long> idsForDestinationOrganizationUnits = idsForDestinationOrganizationEntities.get(OrganizationEntityType.ORGANIZATION_UNIT);
		if (!idsForDestinationOrganizationUnits.isEmpty()) {
			List<String> emailsFromOriginallyAssignedOrganizationUnits = organizationUnitPersistencePlugin.getEmails(idsForDestinationOrganizationUnits);
			emailAddresses.addAll(emailsFromOriginallyAssignedOrganizationUnits);
		}

		Collection<Long> idsForDestinationGroups = idsForDestinationOrganizationEntities.get(OrganizationEntityType.GROUP);
		if (!idsForDestinationGroups.isEmpty()) {
			List<String> emailsFromOriginallyAssignedGroups = groupPersistencePlugin.getEmails(idsForDestinationGroups);
			emailAddresses.addAll(emailsFromOriginallyAssignedGroups);
		}
		
		return emailAddresses;
	}
	
	private EmailMessage getEmailMessage(TransitionNotification notification, List<String> emailAddresses) {

		EmailEvaluator emailEvaluator = new EmailEvaluator(execution);
		
		List<String> toAddresses = emailAddresses;			
		String subject = emailEvaluator.replacePlaceholders(notification.getEmailSubjectTemplate());
		String content = emailEvaluator.replacePlaceholders(notification.getEmailContentTemplate());
		
		EmailMessage emailMessage = new EmailMessage(toAddresses, subject, content, false);
		return emailMessage;
	}
}