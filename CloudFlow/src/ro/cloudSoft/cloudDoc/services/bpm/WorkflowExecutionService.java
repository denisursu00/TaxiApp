package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;

public interface WorkflowExecutionService {
	
	WorkflowInstanceResponse startWorkflowInstance(Long workflowId, Document document, String workspaceName, SecurityManager userSecurity) throws AppException;
	
	WorkflowInstanceResponse completeWorkflowInstanceTask(WorkflowInstance workflowInstance, String transitionName, String manualAssignmentDestinationId, Document document, SecurityManager userSecurity) throws AppException;
	
	WorkflowInstance findProcessInstance(Long processInstanceId, SecurityManager userSecurity);
	
	List<WorkflowInstance> getAllWorkflowInstances(SecurityManager userSecurity);
	
	WorkflowInstance getWorkflowInstance(String documentId, SecurityManager userSecurity);
	
	List<TaskInstance> getCurrentTasks(SecurityManager userSecurity) throws AppException;
	
	WorkflowState getCurrentState(Workflow workflow, Document document, SecurityManager userSecurity) throws AppException;
	
	Map<String, TaskInstance> getCurrentTaskInstanceMap(List<Document> documents);
	
	boolean checkSendingRights(Document document, Workflow workflow, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Verifica daca un document are un flux activ asociat.
	 * 
	 * @param documentLocationRealName numele 'real' al spatiului de lucru in care se gaseste documentul 
	 * @param documentId ID-ul documentului
	 * @param userSecurity datele utilizatorului curent
	 * @return da / nu
	 */
	boolean hasActiveWorkflowInstance(String documentLocationRealName, String documentId, SecurityManager userSecurity);
	
	/**
	 * Cauta fluxul activ documentului identificat prin parametrii dati si,
	 * daca il gaseste, il finalizeaza plecand pe tranzitia avand numele cu prefixul dat.
	 * Tranzitia trebuie sa duca spre o stare de tip STOP. In caz contrar, metoda va arunca exceptie.
	 */
	void endWorkflowInstanceIfActive(String documentLocationRealName, String documentId,
		String leavingTransitionNamePrefix, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Adauga inlocuitorul cu ID-ul dat ca asignat la task-urile utilizatorului cu ID-ul specificat.
	 * Vor fi ignorate task-urile cu ID-urile date.
	 * 
	 * @return identificatorii documentelor asociate task-urilor pentru care s-au adaugat asignari.
	 */
	Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfUser(Long userId, Long replacementUserId, Collection<String> idsForTasksToIgnore);
	
	/**
	 * Adauga inlocuitorul cu ID-ul dat ca asignat la task-urile unitatii organizatorice cu ID-ul specificat.
	 * 
	 * @return identificatorii documentelor asociate task-urilor pentru care s-au adaugat asignari.
	 */
	Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfOrganizationUnit(Long organizationUnitId, Long replacementUserId);
	
	/**
	 * Adauga inlocuitorul cu ID-ul dat ca asignat la task-urile grupului cu ID-ul specificat.
	 * 
	 * @return identificatorii documentelor asociate task-urilor pentru care s-au adaugat asignari.
	 */
	Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfGroup(Long groupId, Long replacementUserId);
	
	void removeAssignedUsersFromDocumentAssociatedTask(Collection<Long> idsForAssignedUsersToRemove,
		String documentLocationRealName, String documentId) throws AppException;
	
	Map<DocumentIdentifier, String> getTaskIdByDocumentIdentifierForTasksAssignedToUser(Long userId);

	/**
	 * Returneaza asignarea precedenta asignarii curente pentru documentul cu identificatorii dati.
	 * Daca nu se gaseste asignarea precedenta, va returna null.
	 */
	String getPreviouslyAssignedAssignee(String documentLocationRealName, String documentId);
	
	void completeWorkflowAutomaticTasks() throws AppException;
}