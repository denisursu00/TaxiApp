package ro.cloudSoft.cloudDoc.dao.bpm;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

public interface WorkflowInstanceDao {
	
	List<WorkflowInstance> getAllWorkflowInstances();
	
	List<WorkflowInstance> getAllWorkflowInstances(String status);
	
	void saveWorkflowInstance(WorkflowInstance workflowInstance);
	
	abstract WorkflowInstance getWorkflowInstance(Workflow workflow, Document document);
	
	WorkflowInstance findWorkflowInstanceById(Long id);
	
	WorkflowInstance findWorkflowInstanceByProcessInstanceId(String processInstanceId);
	
	List<WorkflowInstance> findWorkflowInstances(String documentId);
	
	/**
	 * Returneaza instanta de flux pentru documentul cu ID-ul dat.
	 * Daca nu se gaseste, va returna null.
	 */
	WorkflowInstance getForDocument(String documentId);
	
	WorkflowInstance findCurrentWorkflowInstances(String documentId);
	
	String getFirstTransitionName(String processInstanceId, String finalStateCode);

	Long getInitiatorUserId(String processInstanceId);
	
	boolean hasActiveWorkflowInstance(String documentLocationRealName, String documentId);
	
	boolean hasFinishedStatusWorkflowInstanceByDocumentId(String documentLocationRealName, String documentId);
	
	public List<String> getDocumentIdsByDocTypeAndStatus(Long documentTypeId, String status, String workspaceName);
	
	Set<DocumentIdentifier> getDocumentsBeforeFinishedDate(Date beforeDate);
}