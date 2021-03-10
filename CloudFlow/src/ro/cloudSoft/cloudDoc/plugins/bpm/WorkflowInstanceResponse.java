package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.ArrayList;
import java.util.List;

public class WorkflowInstanceResponse {

	private List<String> candidateTransitionNames;
	private boolean manualAssignment;
	private String chosenTransitionName;
	private boolean workflowFinished;
	
	public WorkflowInstanceResponse() {
		candidateTransitionNames = new ArrayList<String>();
	}
	
	public boolean wasDocumentSent() {
		
		if (getCandidateTransitionNames().size() > 1) {
			return false;
		}
		
		if (isManualAssignment()) {
			return false;
		}
		
		return true;
	}

	public List<String> getCandidateTransitionNames() {
		return candidateTransitionNames;
	}

	public void setCandidateTransitionNames(List<String> candidateTransitionNames) {
		this.candidateTransitionNames = candidateTransitionNames;
	}

	public boolean isManualAssignment() {
		return manualAssignment;
	}

	public void setManualAssignment(boolean manualAssignment) {
		this.manualAssignment = manualAssignment;
	}

	public String getChosenTransitionName() {
		return chosenTransitionName;
	}
	
	public void setChosenTransitionName(String chosenTransitionName) {
		this.chosenTransitionName = chosenTransitionName;
	}
	
	public void addCandidateTransitionName(String candidateTransitionName) {
		candidateTransitionNames.add(candidateTransitionName);
	}
	
	public void setWorkflowFinished(boolean workflowFinished) {
		this.workflowFinished = workflowFinished;
	}
	
	public boolean isWorkflowFinished() {
		return workflowFinished;
	}
}
