package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;

public class GetCurrentStateRequestModel {
	
	private WorkflowModel workflowModel;
	private DocumentModel documentModel;
	
	public WorkflowModel getWorkflowModel() {
		return workflowModel;
	}
	public void setWorkflowModel(WorkflowModel workflowModel) {
		this.workflowModel = workflowModel;
	}
	public DocumentModel getDocumentModel() {
		return documentModel;
	}
	public void setDocumentModel(DocumentModel documentModel) {
		this.documentModel = documentModel;
	}
}
