package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

import com.google.common.base.Objects;

/**
 * 
 */
public abstract class WorkflowJbpmNode {

	private WorkflowJbpmNodeType type;
	
	private String name;
	private String description;
	private Long workflowStateId;
	private boolean automaticRunning;
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof WorkflowJbpmNode)) {
			return false;
		}
		
		WorkflowJbpmNode other = (WorkflowJbpmNode) obj;
		
		return (
			Objects.equal(getType(), other.getType()) &&
			Objects.equal(getName(), other.getName()) &&
			Objects.equal(getDescription(), other.getDescription()) &&
			Objects.equal(getWorkflowStateId(), other.getWorkflowStateId()) &&
			Objects.equal(isAutomaticRunning(), other.isAutomaticRunning())
		);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(
			getType(),
			getName(),
			getDescription(),
			getWorkflowStateId(),
			isAutomaticRunning()
		);
	}
	
	public WorkflowJbpmNodeType getType() {
		return type;
	}
	protected void setType(WorkflowJbpmNodeType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getWorkflowStateId() {
		return workflowStateId;
	}
	public void setWorkflowStateId(Long workflowStateId) {
		this.workflowStateId = workflowStateId;
	}
	public boolean isAutomaticRunning() {
		return automaticRunning;
	}
	public void setAutomaticRunning(boolean automaticRunning) {
		this.automaticRunning = automaticRunning;
	}
}