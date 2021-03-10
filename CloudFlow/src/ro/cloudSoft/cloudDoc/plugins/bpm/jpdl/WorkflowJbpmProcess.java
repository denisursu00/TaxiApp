package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 
 */
public class WorkflowJbpmProcess {

	private String name;
	
	private Set<WorkflowJbpmProcessPathWithManualAssignment> pathsWithManualAssignment = Sets.newLinkedHashSet();
	
	private WorkflowJbpmStartNode startNode;
	private Set<WorkflowJbpmTaskNode> taskNodes = Sets.newLinkedHashSet();
	private Set<WorkflowJbpmEndNode> endNodes = Sets.newLinkedHashSet();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<WorkflowJbpmProcessPathWithManualAssignment> getPathsWithManualAssignment() {
		return pathsWithManualAssignment;
	}
	public void setPathsWithManualAssignment(Set<WorkflowJbpmProcessPathWithManualAssignment> pathsWithManualAssignment) {
		this.pathsWithManualAssignment = pathsWithManualAssignment;
	}
	public WorkflowJbpmStartNode getStartNode() {
		return startNode;
	}
	public void setStartNode(WorkflowJbpmStartNode startNode) {
		this.startNode = startNode;
	}
	public Set<WorkflowJbpmTaskNode> getTaskNodes() {
		return taskNodes;
	}
	public void setTaskNodes(Set<WorkflowJbpmTaskNode> taskNodes) {
		this.taskNodes = taskNodes;
	}
	public Set<WorkflowJbpmEndNode> getEndNodes() {
		return endNodes;
	}
	public void setEndNodes(Set<WorkflowJbpmEndNode> endNodes) {
		this.endNodes = endNodes;
	}
}