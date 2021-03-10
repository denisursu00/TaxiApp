package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

/**
 * 
 */
public class WorkflowJbpmStartNode extends WorkflowJbpmNodeWithOutgoingTransitions {

	public WorkflowJbpmStartNode() {
		setType(WorkflowJbpmNodeType.START);
	}
}