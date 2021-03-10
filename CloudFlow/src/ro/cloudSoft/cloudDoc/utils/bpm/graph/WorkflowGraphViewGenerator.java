package ro.cloudSoft.cloudDoc.utils.bpm.graph;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.graph.WorkflowGraphViewRepresentation;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphTrace;

/**
 * 
 */
public interface WorkflowGraphViewGenerator {
	
	WorkflowGraphViewRepresentation generateGraphView(Workflow workflow) throws AppException;
	
	WorkflowGraphViewRepresentation generateGraphView(Workflow workflow, WorkflowGraphTrace workflowGraphTrace) throws AppException;
}