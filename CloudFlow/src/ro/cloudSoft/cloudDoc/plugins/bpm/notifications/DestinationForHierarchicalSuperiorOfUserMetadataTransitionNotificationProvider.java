package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import java.util.Collection;

import org.jbpm.api.model.OpenExecution;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowMetadataVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;

public class DestinationForHierarchicalSuperiorOfUserMetadataTransitionNotificationProvider extends AbstractDestinationForTransitionNotificationProvider {

	
	private final HierarchicalSuperiorOfUserMetadataTransitionNotification notification;
	private final OpenExecution execution;
	private final DocumentService documentService;
	private final UserService userService;

	public DestinationForHierarchicalSuperiorOfUserMetadataTransitionNotificationProvider(HierarchicalSuperiorOfUserMetadataTransitionNotification notification, 
			OpenExecution execution, DocumentService documentService, UserService userService) {
		this.notification = notification;
		this.execution = execution;
		this.documentService = documentService;
		this.userService = userService;
	}

	@Override
	public Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities() {
		
		String metadataName = notification.getMetadataName();
		if (metadataName == null) {
			throw new IllegalStateException("Notificarea de tip metadata de tip user cu ID-ul [" + notification.getId() + "] NU are completat numele metadatei.");
		}
		
		Collection<String> metadataValues = WorkflowMetadataVariableHelper.getMetadataValues(execution, metadataName);
		String metadataValue = Iterables.getOnlyElement(metadataValues);
		if (metadataValue == null) {
			
			String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
			String documentId = WorkflowVariableHelper.getDocumentId(execution);
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String exceptionMessage = "Pentru documentul cu atributele: " + documentLogAttributes + ", metadata " +
				"cu numele [" + metadataName + "], necesara notificarii prin e-mail, NU este completata.";
			throw new IllegalStateException(exceptionMessage);
		}
		Long userIdInMetadata = MetadataValueHelper.getUserId(metadataValue);
		
		return ImmutableMultimap.of(OrganizationEntityType.USER, getHierarchicalSuperiorUserId(userIdInMetadata));
	}
	
	private Long getHierarchicalSuperiorUserId(Long userId) {
		User superior = userService.getSuperior(userId);
		if (superior == null) {
			String exceptionMessage = "NU s-a gasit superiorul pentru utilizatorul cu ID-ul [" + userId + "].";
			throw new IllegalStateException(exceptionMessage);
		}
		return superior.getId();
	}
}