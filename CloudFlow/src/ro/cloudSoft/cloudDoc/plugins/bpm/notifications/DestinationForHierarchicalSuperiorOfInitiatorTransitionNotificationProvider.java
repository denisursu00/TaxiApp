package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class DestinationForHierarchicalSuperiorOfInitiatorTransitionNotificationProvider extends AbstractDestinationForTransitionNotificationProvider {

	private final OpenExecution execution;
	
	private final DocumentService documentService;
	private final UserService userService;
	
	public DestinationForHierarchicalSuperiorOfInitiatorTransitionNotificationProvider(
			OpenExecution execution, DocumentService documentService, UserService userService) {
		
		this.execution = execution;
		
		this.documentService = documentService;
		this.userService = userService;
	}
	
	@Override
	public Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities() {
		
		Long initiatorUserId = getInitiatorUserId();
		Long hierarchicalSuperiorUserId = getHierarchicalSuperiorUserId(initiatorUserId);
		
		return ImmutableMultimap.of(OrganizationEntityType.USER, hierarchicalSuperiorUserId);
	}
	
	private Long getInitiatorUserId() {
		Long initiatorUserId = WorkflowVariableHelper.getInitiatorUserId(execution);
		if (initiatorUserId == null) {
			
			String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
			String documentId = WorkflowVariableHelper.getDocumentId(execution);
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String exceptionMessage = "Pentru documentul cu atributele: " + documentLogAttributes +
				", executia cu ID-ul [" + execution.getId() + "], NU s-a putut gasi initiatorul.";
			throw new IllegalStateException(exceptionMessage);
		}
		return initiatorUserId;
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