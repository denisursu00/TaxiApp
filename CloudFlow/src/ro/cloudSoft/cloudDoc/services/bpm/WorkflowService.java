package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * 
 * 
 *
 */
public interface WorkflowService {
	
	public List<Workflow> getAllWorkflows(SecurityManager userSecurity) throws AppException;
	
	public List<Workflow> getAllWorkflowsForDisplay(SecurityManager userSecurity) throws AppException;
	
	/**
	 * Returneaza fluxul care are ID-ul dat, sau null daca nu se gaseste.
	 */
	public Workflow getWorkflowById(Long id);
	
	/**
	 * Returneaza fluxul care are ID-ul dat, sau null daca nu se gaseste.
	 */
	public Workflow getWorkflowById(Long id, SecurityManager userSecurity);
	
	public void saveWorkflow(Workflow workflow, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Creeaza o noua versiune a fluxului cu ID-ul dat.
	 * 
	 * @return ID-ul fluxului care reprezinta versiunea noua
	 */
	public Long createNewVersion(Long workflowId, SecurityManager userSecurity) throws AppException;
	
	public void deleteWorkflow(Long id, SecurityManager userSecurity) throws AppException;	

	public List<WorkflowState> getStatesByDocumentTypeIds(List<Long> ids, SecurityManager userSecurity) throws AppException;

	/**
	 * Returneaza fluxul asociat tipului de document cu ID-ul dat.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna null.
	 */
	public Workflow getWorkflowByDocumentType(Long documentTypeId);

	/**
	 * Returneaza starile fluxului asociat tipului de document cu ID-ul dat.
	 * Starile fluxului vor fi ordonate dupa cod.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna o lista goala.
	 */
	List<WorkflowState> getWorkflowStatesByDocumentType(Long documentTypeId);

	/**
	 * Returneaza fluxul asociat documentului cu identificatorii dati.
	 * Daca documentul nu are asociat un flux, atunci va returna fluxul asociat tipului de document.
	 * Daca nici tipul de document nu are asociat un flux, atunci va returna null.
	 */
	Workflow getWorkflowForDocument(String documentLocationRealName, String documentId, Long documentTypeId);
	
	Workflow getWorkflowForDocument(String documentLocationRealName, String documentId);
	
	public Map<DocumentIdentifier, String> getWorkflowNameByDocumentIdentifier(Collection<DocumentIdentifier> documentIdentifiers);

	WorkflowTransition getTransitionForDocument(String documentLocationRealName, String documentId,
		Long documentTypeId, String transitionName, String endStateCode);
	
	WorkflowTransition getTransitionById(Long id);
	
	WorkflowTransition getTransition(String transitionName, Long finalStateId);
	
	boolean hasInstances(Long workflowId);
	
	boolean workflowHasState(Long workflowId, String stateCode);
	
	WorkflowState getWorkflowStateById(Long workflowStateId);
	
	WorkflowState getWorkflowState(Long workflowId, String workflowStateCode);
	
	void deployImportedWorkflows();
	
	List<WorkflowTransition> getOutgoingTransitionsFromState(Long stateId);
	
	List<Long> getWorkflowFinalStateIdsByDocumentType(Long documentTypeId);
}