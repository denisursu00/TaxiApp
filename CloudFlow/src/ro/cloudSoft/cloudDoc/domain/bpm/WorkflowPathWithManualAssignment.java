package ro.cloudSoft.cloudDoc.domain.bpm;

/**
 * Reprezinta o "cale" (tranzitie care duce intr-o stare) dintr-un flux care e cu asignare manuala.
 * 
 * 
 */
public class WorkflowPathWithManualAssignment {

	private String transitionName;
	private String finalStateCode;
	
	public String getTransitionName() {
		return transitionName;
	}
	public String getFinalStateCode() {
		return finalStateCode;
	}
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}
	public void setFinalStateCode(String finalStateCode) {
		this.finalStateCode = finalStateCode;
	}
}