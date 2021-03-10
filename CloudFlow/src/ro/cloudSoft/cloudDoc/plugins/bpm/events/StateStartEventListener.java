package ro.cloudSoft.cloudDoc.plugins.bpm.events;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;

/**
 * Reprezinta un listener pentru evenimentul de "intrare" intr-o stare.
 * 
 * 
 */
public class StateStartEventListener implements EventListener {

	private static final long serialVersionUID = 1L;
	
	private Long stateId;
	
	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		// Iau identificatorii documentului de pe flux.
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		// Obtin serviciul pentru documente.
		DocumentService documentService = SpringContextUtils.getBean("documentService");
		
		// Actualizez starea curenta a documentului.
		documentService.setDocumentState(documentId, documentLocationRealName, this.stateId.toString());
	}
	
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
}