package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

public class DocumentViewVersionBundleModel {
	
	private DocumentModel document;
	private DocumentTypeModel documentType;
	private WorkflowModel workflow;
	private WorkflowStateModel workflowState;
	
	public DocumentViewVersionBundleModel() {
	}
	
	public DocumentModel getDocument() {
		return document;
	}
	public void setDocument(DocumentModel document) {
		this.document = document;
	}
	public DocumentTypeModel getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentTypeModel documentType) {
		this.documentType = documentType;
	}
	public WorkflowModel getWorkflow() {
		return workflow;
	}
	public void setWorkflow(WorkflowModel workflow) {
		this.workflow = workflow;
	}
	public WorkflowStateModel getWorkflowState() {
		return workflowState;
	}
	public void setWorkflowState(WorkflowStateModel workflowState) {
		this.workflowState = workflowState;
	}
}
