package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

public class WorkflowStateConverter {

	public static WorkflowStateModel getModelFromWorkflowState(WorkflowState state){
		WorkflowStateModel  model = new WorkflowStateModel();
		model.setId(state.getId());
		model.setCode(state.getCode());
		model.setName(state.getName());
		model.setStateType(state.getStateType());
		model.setAttachmentsPermission(state.getAttachmentsPermission());
		model.setAutomaticRunning(state.isAutomaticRunning());
		model.setClassPath(state.getClassPath());
		return model;
	}
	
	public static WorkflowState getWorkflowStateFromModel(WorkflowStateModel model){
		WorkflowState  state = new WorkflowState();
		state.setId(model.getId());
		state.setCode(model.getCode());
		state.setName(model.getName());
		state.setStateType(model.getStateType());
		state.setAttachmentsPermission(model.getAttachmentsPermission());
		state.setAutomaticRunning(model.isAutomaticRunning());
		state.setClassPath(model.getClassPath());
		return state;
	}
	
}
