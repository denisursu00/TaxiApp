package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.google.common.base.Objects;

/**
 * 
 */
public class WorkflowGraphNode implements Comparable<WorkflowGraphNode> {
	
	private String identifier;
	private String label;

	public WorkflowGraphNode() {}
	
	public WorkflowGraphNode(String identifier, String label) {
		this.identifier = identifier;
		this.label = label;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof WorkflowGraphNode)) {
			return false;
		}
		
		WorkflowGraphNode other = (WorkflowGraphNode) obj;
		
		return Objects.equal(getIdentifier(), other.getIdentifier());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdentifier());
	}
	
	@Override
	public int compareTo(WorkflowGraphNode other) {
		return new CompareToBuilder()
			.append(getIdentifier(), other.getIdentifier())
			.toComparison();
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public String getLabel() {
		return label;
	}	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}