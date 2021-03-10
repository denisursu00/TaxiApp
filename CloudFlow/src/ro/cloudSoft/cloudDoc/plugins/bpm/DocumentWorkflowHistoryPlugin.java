package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;

public interface DocumentWorkflowHistoryPlugin {
	
	List<DocumentHistory> getDocumentHistory(String documentId) throws AppException;
	
	/**
	 * Returneaza starea de tip STOP in care s-a terminat instanta de flux data.
	 * Daca nu se gaseste, va returna null.
	 * 
	 * @param workflowInstance instanta de flux
	 * @param finalStateOfTypeStopByTransitionName starile finale de tip STOP ale fluxului, grupate dupa numele tranzitiei care ajunge in acea stare
	 */
	WorkflowState getStopStateOfInstance(WorkflowInstance workflowInstance, Map<String, WorkflowState> finalStateOfTypeStopByTransitionName);
	
	/**
	 * Returneaza asignarea precedenta asignarii curente pentru instanta de flux data.
	 * Daca nu se gaseste asignarea precedenta, va returna null.
	 */
	String getPreviouslyAssignedAssignee(WorkflowInstance workflowInstance);
}