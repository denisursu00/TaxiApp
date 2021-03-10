package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import java.util.ArrayList;
import java.util.List;

public class WorkflowGraphTrace {
	
	private String currentNodeCode;
	private List<String> transitionsNames;
	private List<String> nodesCodes;
	
	public WorkflowGraphTrace() {
		transitionsNames = new ArrayList<>();
		nodesCodes = new ArrayList<>();
	}

	public String getCurrentNodeCode() {
		return currentNodeCode;
	}

	public void setCurrentNodeCode(String currentNodeCode) {
		this.currentNodeCode = currentNodeCode;
	}

	public List<String> getTransitionsNames() {
		return transitionsNames;
	}

	public void setTransitionsNames(List<String> transitionsNames) {
		this.transitionsNames = transitionsNames;
	}

	public List<String> getNodesCodes() {
		return nodesCodes;
	}

	public void setNodesCodes(List<String> nodesCodes) {
		this.nodesCodes = nodesCodes;
	}
}
