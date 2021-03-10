package ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WorkflowGxtServiceAsync extends GxtServiceBaseAsync {

	void getAllWorkflows(AsyncCallback<List<WorkflowModel>> callback);

	void getWorkflowById(Long id, AsyncCallback<WorkflowModel> callback);

	void deleteWorkflow(Long id, AsyncCallback<Void> callback);

	void saveWorkflow(WorkflowModel workflowModel, AsyncCallback<Void> callback);
	
	/**
	 * Creeaza o noua versiune a fluxului cu ID-ul dat.
	 * Returneaza prin callback ID-ul fluxului care reprezinta versiunea noua.
	 */
	void createNewVersion(Long workflowId, AsyncCallback<Long> callback);

	void getStatesByDocumentTypeIds(List<Long> ids, AsyncCallback<List<WorkflowStateModel>> callback);
	
	/**
	 * Returneaza fluxul asociat tipului de document cu ID-ul dat.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna null.
	 */
	void getWorkflowByDocumentType(Long documentTypeId, AsyncCallback<WorkflowModel> callback);

	/**
	 * Returneaza starile fluxului asociat tipului de document cu ID-ul dat.
	 * Starile fluxului vor fi ordonate dupa cod.
	 * Daca fluxul are mai multe versiuni, va returna ultima versiune.
	 * Daca tipul de document nu are asociat un flux, atunci va returna o lista goala.
	 */
	void getWorkflowStatesByDocumentType(Long documentTypeId, AsyncCallback<List<WorkflowStateModel>> callback);

	/**
	 * Returneaza fluxul asociat documentului cu identificatorii dati.
	 * Daca documentul nu are asociat un flux, atunci va returna fluxul asociat tipului de document.
	 * Daca nici tipul de document nu are asociat un flux, atunci va returna null.
	 */
	void getWorkflowForDocument(String documentLocationRealName, String documentId, Long documentTypeId, AsyncCallback<WorkflowModel> callback);
	
	void getCurrentState(WorkflowModel workflowModel, DocumentModel documentModel, AsyncCallback<WorkflowStateModel> callback);
	
	void checkSendingRights(WorkflowModel workflowModel, DocumentModel documentModel, AsyncCallback<Boolean> callback);
	
	void hasInstances(Long workflowId, AsyncCallback<Boolean> callback);
}