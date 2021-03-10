package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

import com.google.common.base.Objects;

/**
 * 
 */
public class WorkflowJbpmTransition {

	private String name;
	private String outgoingNodeName;
	private Long workflowTransitionId;
	
	private String routingCondition;
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof WorkflowJbpmTransition)) {
			return false;
		}
		
		WorkflowJbpmTransition other = (WorkflowJbpmTransition) obj;
		
		return (
			Objects.equal(getName(), other.getName()) &&
			Objects.equal(getOutgoingNodeName(), other.getOutgoingNodeName()) &&
			Objects.equal(getWorkflowTransitionId(), other.getWorkflowTransitionId())
		);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(
			getName(),
			getOutgoingNodeName(),
			getWorkflowTransitionId()
		);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOutgoingNodeName() {
		return outgoingNodeName;
	}
	public void setOutgoingNodeName(String outgoingNodeName) {
		this.outgoingNodeName = outgoingNodeName;
	}
	public Long getWorkflowTransitionId() {
		return workflowTransitionId;
	}
	public void setWorkflowTransitionId(Long workflowTransitionId) {
		this.workflowTransitionId = workflowTransitionId;
	}
	public String getRoutingCondition() {
		return routingCondition;
	}
	public void setRoutingCondition(String routingCondition) {
		this.routingCondition = routingCondition;
	}
}