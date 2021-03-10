package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

/**
 * 
 */
public class WorkflowGraphEdge {
	private String label;

	private WorkflowGraphNode startNode;
	private WorkflowGraphNode endNode;
	
	public WorkflowGraphEdge() {}
	
	public WorkflowGraphEdge(String label, WorkflowGraphNode startNode, WorkflowGraphNode endNode) {
		this.label = label;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public WorkflowGraphNode getStartNode() {
		return startNode;
	}
	public void setStartNode(WorkflowGraphNode startNode) {
		this.startNode = startNode;
	}
	public WorkflowGraphNode getEndNode() {
		return endNode;
	}
	public void setEndNode(WorkflowGraphNode endNode) {
		this.endNode = endNode;
	}
}