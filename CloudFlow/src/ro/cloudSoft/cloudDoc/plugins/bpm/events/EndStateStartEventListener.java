package ro.cloudSoft.cloudDoc.plugins.bpm.events;

import org.jbpm.api.listener.EventListenerExecution;

import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

/**
 * Reprezinta un listener pentru evenimentul de "intrare" intr-o stare finala.
 * 
 * 
 */
public class EndStateStartEventListener extends StateStartEventListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		
		super.notify(execution);
		
		// Iau identificatorii documentului de pe flux.
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		
		ReplacementProfileInstanceService replacementProfileInstanceService = SpringContextUtils.getBean("replacementProfileInstanceService");
		replacementProfileInstanceService.removeReplacementProfileInstancesForDocument(documentLocationRealName, documentId);
		
		// Obtin serviciul pentru documente.
		DocumentService documentService = SpringContextUtils.getBean("documentService");
		
		// Marchez documentul ca fiind read-only.
		documentService.setReadOnly(documentLocationRealName, documentId);
	}
}