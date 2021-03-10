package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;

public class WorkflowInstanceResponseModel extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CANDIDATE_TRANSITION_NAMES = "candidateTransitionNames";
	private static final String MANUAL_ASSIGNMENT = "manualAssignment";
	private static final String CHOSEN_TRANSITION_NAME = "chosenTransitionName";
	private static final String WORKFLOW_FINISHED = "workflowFinished";
	private static final String DOCUMENT = "document";
	
	public WorkflowInstanceResponseModel() {
		setManualAssignment(false);
		setWorkflowFinished(false);
		setCandidateTransitionNames(new ArrayList<String>());
	}
	
	public void setCandidateTransitionNames(List<String> candidateTransitionNames){
		set(CANDIDATE_TRANSITION_NAMES, candidateTransitionNames);
	}
	
	public List<String> getCandidateTransitionNames(){
		return get(CANDIDATE_TRANSITION_NAMES);
	}
	
	public void setManualAssignment(boolean manualAssignment){
		set(MANUAL_ASSIGNMENT, manualAssignment);
	}
	
	public boolean getManualAssignment(){
		return get(MANUAL_ASSIGNMENT);
	}
	
	public void setChosenTransitionName(String chosenTransitionName){
		set(CHOSEN_TRANSITION_NAME, chosenTransitionName);
	}
	
	public String getChosenTransitionName(){
		return get(CHOSEN_TRANSITION_NAME);
	}
	
	public void setWorkflowFinished(boolean workflowFinished) {
		set(WORKFLOW_FINISHED, workflowFinished);
	}
	
	public boolean isWorkflowFinished() {
		return get(WORKFLOW_FINISHED);
	}
	
	public void setDocument(DocumentModel document) {
		set(DOCUMENT, document);
	}
	
	public DocumentModel getDocument() {
		return get(DOCUMENT);
	}	
}
