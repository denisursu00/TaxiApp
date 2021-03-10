package ro.cloudSoft.cloudDoc.dao.bpm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

public interface WorkflowDao {
	
	/**
	 * Returneaza toate fluxurile, ordonate dupa nume si dupa numarul versiunii.
	 */
	public List<Workflow> getAllWorkflows();
	
	public void saveWorkflow(Workflow workflow);
	
	Workflow saveAndGetWorkflow(Workflow workflow);
	
	public void removeWorkflow(Workflow workflow);

	/**
	 * Returneaza fluxul care are ID-ul dat, sau null daca nu se gaseste.
	 */
	public Workflow find(Long id);
	
	public void removeWorkflow(Long id);
	
	WorkflowState getWorkflowState(Long workflowId, String workflowStateCode);
	
	public WorkflowState getWorkflowStateById(Long id);
	
	public List<WorkflowState> getStatesByDocumentTypeIds(List<Long> id);
	
	/**
	 * Returneaza starile fluxului cu ID-ul dat.
	 * Starile fluxului vor fi ordonate dupa cod.
	 */
	List<WorkflowState> getWorkflowStatesByWorkflowId(Long workflowId);

	/**
	 * Returneaza fluxul asociat tipului de document cu ID-ul dat.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna null.
	 */
	public Workflow getWorkflowByDocumentType(Long documentTypeId);

	/**
	 * Returneaza fluxul asociat documentului cu identificatorii dati.
	 * Daca documentul nu are asociat un flux, atunci va returna null.
	 */
	Workflow getWorkflowForDocument(String documentLocationRealName, String documentId);
	
	public Map<DocumentIdentifier, String> getWorkflowNameByDocumentIdentifier(Collection<DocumentIdentifier> documentIdentifiers);

	/**
	 * Ia tranzitia pentru documentul cu identificatorii dati. Tranzitia trebuie sa aiba numele dat si codul starii finale dat.
	 * Tranzitia se va lua din versiunea fluxului cu care a pornit documentul pe flux.
	 * Daca nu se gaseste tranzitia, va returna null.
	 */
	WorkflowTransition getTransitionForDocument(String documentLocationRealName, String documentId, String transitionName, String endStateCode);

	/**
	 * Ia tranzitia pentru tipul de document dat. Tranzitia trebuie sa aiba numele dat si codul starii finale dat.
	 * Daca fluxul asociat tipului de document are mai multe versiuni, se va cauta in ultima versiune.
	 * Daca nu se gaseste tranzitia, va returna null.
	 */
	WorkflowTransition getTransitionForDocumentType(Long documentTypeId, String transitionName, String endStateCode);
	
	/**
	 * Returneaza tranzitia identificata prin numele ei si prin ID-ul starii sale finale.
	 * Daca nu se gaseste tranzitia, va returna null.
	 */
	WorkflowTransition getTransition(String transitionName, Long finalStateId);
	
	WorkflowTransition getTransitionById(Long id);
	
	boolean hasInstances(Long workflowId);
	
	/**
	 * Ia numele tranzitiilor care sunt disponibile doar pentru actiuni automate.
	 * 
	 * @param workflowId ID-ul fluxului
	 * @param startStateCode codul starii din care trebuie sa plece tranzitiile
	 */
	List<String> getNamesForTransitionsAvailableForAutomaticActionsOnly(Long workflowId, String startStateCode);
	
	boolean hasState(Long workflowId, String stateCode);
	
	/**
	 * Returneaza un Map cu starile finale de tip STOP ale fluxului,
	 * grupate dupa numele tranzitiei care ajunge in acea stare.
	 */
	Map<String, WorkflowState> getFinalStateOfTypeStopByTransitionName(Long workflowId);
	
	/**
	 * Returneaza numarul ultimei versiuni a fluxului cu ID-ul dat.
	 * 
	 * @throws IllegalArgumentException daca nu se gaseste nici o versiune pentru fluxul cu ID-ul dat.
	 */
	Integer getLastVersionNumberOfWorkflow(Long workflowId);
	
	/**
	 * Returneaza ID-ul ultimei versiuni de flux pentru tipul de document cu ID-ul dat.
	 * Daca nu se gaseste flux pentru tipul de document, atunci va returna null.
	 */
	Long getIdForLatestVersionOfWorkflowForDocumentType(Long documentTypeId);
	
	/**
	 * Returneaza conditia de rutare a tranzitiei cu numele dat, apartinand de instanta de flux cu ID-ul dat.
	 * Daca tranzitia nu are conditie de rutare, atunci va returna null.
	 * 
	 * @throws IllegalArgumentException daca nu se gaseste tranzitia cu numele dat
	 */
	String getTransitionRoutingConditionExpression(Long workflowInstanceId, String transitionName);
	
	List<WorkflowTransition> getOutgoingTransitionsFromState(Long stateId);
}