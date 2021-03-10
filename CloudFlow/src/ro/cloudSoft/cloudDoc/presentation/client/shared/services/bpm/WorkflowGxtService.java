package ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface WorkflowGxtService extends GxtServiceBase {
	
	List<WorkflowModel> getAllWorkflows() throws PresentationException;
	
	WorkflowModel getWorkflowById(Long id) throws PresentationException;
	
	void deleteWorkflow(Long id) throws PresentationException;
	
	void saveWorkflow(WorkflowModel workflowModel) throws PresentationException;
	
	/**
	 * Creeaza o noua versiune a fluxului cu ID-ul dat.
	 * 
	 * @return ID-ul fluxului care reprezinta versiunea noua
	 */
	Long createNewVersion(Long workflowId) throws PresentationException;
	
	List<WorkflowStateModel> getStatesByDocumentTypeIds(List<Long> ids) throws PresentationException;;

	/**
	 * Returneaza fluxul asociat tipului de document cu ID-ul dat.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna null.
	 */
	WorkflowModel getWorkflowByDocumentType(Long documentTypeId);

	/**
	 * Returneaza starile fluxului asociat tipului de document cu ID-ul dat.
	 * Starile fluxului vor fi ordonate dupa cod.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna o lista goala.
	 */
	List<WorkflowStateModel> getWorkflowStatesByDocumentType(Long documentTypeId);

	/**
	 * Returneaza fluxul asociat documentului cu identificatorii dati.
	 * Daca documentul nu are asociat un flux, atunci va returna fluxul asociat tipului de document.
	 * Daca nici tipul de document nu are asociat un flux, atunci va returna null.
	 */
	WorkflowModel getWorkflowForDocument(String documentLocationRealName, String documentId, Long documentTypeId);
	
	WorkflowStateModel getCurrentState(WorkflowModel workflowModel, DocumentModel documentModel) throws PresentationException;
	
	Boolean checkSendingRights(WorkflowModel workflowModel, DocumentModel documentModel) throws PresentationException;
	
	boolean hasInstances(Long workflowId);
}