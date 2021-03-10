package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

/**
 * 
 */
public class WorkflowJbpmTaskNode extends WorkflowJbpmNodeWithOutgoingTransitions {

	public WorkflowJbpmTaskNode() {
		setType(WorkflowJbpmNodeType.TASK);
	}
}