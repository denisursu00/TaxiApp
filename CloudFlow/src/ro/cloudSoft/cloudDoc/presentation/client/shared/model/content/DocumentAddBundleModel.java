package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

public class DocumentAddBundleModel {
	
	private DocumentTypeModel documentType;
	private WorkflowModel workflow;
	private WorkflowStateModel currentState;
	private Boolean canUserSend;
	
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
	public WorkflowStateModel getCurrentState() {
		return currentState;
	}
	public void setCurrentState(WorkflowStateModel currentState) {
		this.currentState = currentState;
	}	
	public void setCanUserSend(Boolean canSend) {
		this.canUserSend = canSend;
	}
	public Boolean getCanUserSend() {
		return canUserSend;
	}
}
