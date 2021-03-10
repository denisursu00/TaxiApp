package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

/**
 * Reprezinta o "cale" (tranzitie care duce la un nod) dintr-un proces care e cu asignare manuala.
 * 
 * 
 */
public class WorkflowJbpmProcessPathWithManualAssignment {

	private String transitionName;
	private String outgoingNodeName;
	
	public String getTransitionName() {
		return transitionName;
	}
	public String getOutgoingNodeName() {
		return outgoingNodeName;
	}
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}
	public void setOutgoingNodeName(String outgoingNodeName) {
		this.outgoingNodeName = outgoingNodeName;
	}
}