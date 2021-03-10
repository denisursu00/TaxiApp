package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm;

import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;

public class WorkflowInstanceResponseConverter {	
	
	public static WorkflowInstanceResponseModel getResponseModelFromResponse(WorkflowInstanceResponse response) {
		WorkflowInstanceResponseModel responseModel = new WorkflowInstanceResponseModel();
		responseModel.setCandidateTransitionNames(response.getCandidateTransitionNames());
		responseModel.setChosenTransitionName(response.getChosenTransitionName());
		responseModel.setManualAssignment(response.isManualAssignment());
		responseModel.setWorkflowFinished(response.isWorkflowFinished());
		return responseModel;
	}
	
}
