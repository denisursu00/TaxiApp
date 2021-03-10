package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;

public class SendDocumentToWorkflowRequestModel {
	
	private Long workflowId;
	private String transitionName;
	private String manualAssignmentDestinationId;
	private DocumentModel document;
	private boolean uiSendConfirmed;
	
	public Long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}
	public String getTransitionName() {
		return transitionName;
	}
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}
	public String getManualAssignmentDestinationId() {
		return manualAssignmentDestinationId;
	}
	public void setManualAssignmentDestinationId(String manualAssignmentDestinationId) {
		this.manualAssignmentDestinationId = manualAssignmentDestinationId;
	}
	public DocumentModel getDocument() {
		return document;
	}
	public void setDocument(DocumentModel document) {
		this.document = document;
	}
	public boolean isUiSendConfirmed() {
		return uiSendConfirmed;
	}
	public void setUiSendConfirmed(boolean uiSendConfirmed) {
		this.uiSendConfirmed = uiSendConfirmed;
	}
}
