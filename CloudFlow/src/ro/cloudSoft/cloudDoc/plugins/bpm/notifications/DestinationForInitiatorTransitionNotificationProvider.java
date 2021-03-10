package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class DestinationForInitiatorTransitionNotificationProvider extends AbstractDestinationForTransitionNotificationProvider {

	private final OpenExecution execution;
	private final DocumentService documentService;
	
	public DestinationForInitiatorTransitionNotificationProvider(OpenExecution execution, DocumentService documentService) {
		this.execution = execution;
		this.documentService = documentService;
	}
	
	@Override
	public Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities() {
		
		Long initiatorUserId = WorkflowVariableHelper.getInitiatorUserId(execution);
		if (initiatorUserId == null) {
			
			String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
			String documentId = WorkflowVariableHelper.getDocumentId(execution);
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String exceptionMessage = "Pentru documentul cu atributele: " + documentLogAttributes +
				", executia cu ID-ul [" + execution.getId() + "], NU s-a putut gasi initiatorul.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		return ImmutableMultimap.of(OrganizationEntityType.USER, initiatorUserId);
	}
}