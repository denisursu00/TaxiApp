package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

public class SendDocumentToWorkflowResponseModel {
	
	private WorkflowInstanceResponseModel workflowInstanceResponse;
	private boolean needUiSendConfirmation;
	
	public WorkflowInstanceResponseModel getWorkflowInstanceResponse() {
		return workflowInstanceResponse;
	}
	
	public void setWorkflowInstanceResponse(WorkflowInstanceResponseModel workflowInstanceResponse) {
		this.workflowInstanceResponse = workflowInstanceResponse;
	}
	
	public boolean isNeedUiSendConfirmation() {
		return needUiSendConfirmation;
	}
	
	public void setNeedUiSendConfirmation(boolean needUiSendConfirmation) {
		this.needUiSendConfirmation = needUiSendConfirmation;
	}
}
