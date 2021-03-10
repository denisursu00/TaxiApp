package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 
 */
public abstract class WorkflowJbpmNodeWithOutgoingTransitions extends WorkflowJbpmNode {

	private Set<WorkflowJbpmTransition> outgoingTransitions = Sets.newLinkedHashSet();
	
	public Set<WorkflowJbpmTransition> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public void setOutgoingTransitions(Set<WorkflowJbpmTransition> outgoingTransitions) {
		this.outgoingTransitions = outgoingTransitions;
	}
}