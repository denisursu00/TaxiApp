package ro.cloudSoft.cloudDoc.utils.bpm.graph;

import java.util.Set;

import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraph;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphEdge;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphNode;

/**
 * 
 */
public class WorkflowGraphBuilder {

	public static WorkflowGraph buildGraph(Workflow workflow) {
		
		WorkflowGraph graph = new WorkflowGraph();
		
		graph.setLabel(workflow.getName());
		
		Set<WorkflowGraphNode> nodes = Sets.newHashSet();
		Set<WorkflowGraphEdge> edges = Sets.newHashSet();
		
		for (WorkflowTransition transition : workflow.getTransitions()) {
			
			WorkflowState startState = transition.getStartState();
			WorkflowGraphNode startNode = new WorkflowGraphNode();
			startNode.setIdentifier(startState.getCode());
			startNode.setLabel(startState.getName());
			
			WorkflowState endState = transition.getFinalState();
			WorkflowGraphNode endNode = new WorkflowGraphNode();
			endNode.setIdentifier(endState.getCode());
			endNode.setLabel(endState.getName());
			
			nodes.add(startNode);
			nodes.add(endNode);
			
			WorkflowGraphEdge edge = new WorkflowGraphEdge();
			edge.setLabel(transition.getName());
			edge.setStartNode(startNode);
			edge.setEndNode(endNode);
			
			edges.add(edge);
		}
		
		graph.getNodes().addAll(nodes);
		graph.getEdges().addAll(edges);
		
		return graph;
	}
}