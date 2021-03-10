package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 
 */
public class WorkflowGraph {
	
	private String label;
	
	private Set<WorkflowGraphNode> nodes = Sets.newLinkedHashSet();
	private Set<WorkflowGraphEdge> edges = Sets.newLinkedHashSet();
	
	public String getLabel() {
		return label;
	}
	public Set<WorkflowGraphNode> getNodes() {
		return nodes;
	}
	public Set<WorkflowGraphEdge> getEdges() {
		return edges;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setNodes(Set<WorkflowGraphNode> nodes) {
		this.nodes = nodes;
	}
	public void setEdges(Set<WorkflowGraphEdge> edges) {
		this.edges = edges;
	}
}