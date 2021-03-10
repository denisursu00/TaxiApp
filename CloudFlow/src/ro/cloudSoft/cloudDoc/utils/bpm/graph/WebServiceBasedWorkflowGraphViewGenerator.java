package ro.cloudSoft.cloudDoc.utils.bpm.graph;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Sets;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.Node;
import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.graph.WorkflowGraphViewRepresentation;
import ro.cloudSoft.cloudDoc.plugins.content.MimeType;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraph;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphEdge;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphNode;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphTrace;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphView;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphViewGenerationWebServiceClient;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphViewGenerationWebServiceClientFactory;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
/**
 * 
 */
public class WebServiceBasedWorkflowGraphViewGenerator implements WorkflowGraphViewGenerator {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(WebServiceBasedWorkflowGraphViewGenerator.class);
	
	private static final String CURRENT_NODE_COLOR = "#76a1ef";
	private static final String CURRENT_NODE_BORDER_COLOR = "#406fc4";

	private static final String TRACE_NODE_COLOR = "#bcd1f7";
	
	private static final String NODE_BORDER_COLOR = "#94b6f2";
	private static final String NODE_COLOR = "#FFFFFF";
	
	private static final String FONT_COLOR = "#2f5cac";
	private static final String FONT_SIZE = "10";

	private static final String TRACE_LINK_COLOR = "#1547a2";
	private static final String LINK_COLOR = "#76a1ef";

	@Override
	public WorkflowGraphViewRepresentation generateGraphView(Workflow workflow) throws AppException {
		
		WorkflowGraph workflowGraph = WorkflowGraphBuilder.buildGraph(workflow);
		workflowGraph.setNodes(Sets.newTreeSet(workflowGraph.getNodes()));
		
		WorkflowGraphView workflowGraphView =  new WorkflowGraphView();
		
		try {
			workflowGraphView.setContent(getSimpleGraph(workflowGraph));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		workflowGraphView.setMimeType(MimeType.OCTET_STREAM.getMimeType());
		return getRepresentation(workflowGraphView);
	}
	
	private byte[] getSimpleGraph(WorkflowGraph workflowGraph) throws IOException {
		
		Graph graph = graph(workflowGraph.getLabel()).directed().named(workflowGraph.getLabel());
		Node rootNode;
		for (WorkflowGraphNode workflowGraphNode : workflowGraph.getNodes()) {
			
			rootNode = prepareNode(workflowGraphNode.getLabel());
			
			for (WorkflowGraphEdge edge : workflowGraph.getEdges()) {
				if (edge.getStartNode().equals(workflowGraphNode)) {
					Node endNode = prepareNode(edge.getEndNode().getLabel());
					rootNode = addLink(rootNode, endNode, edge.getLabel());
				}
			}
			graph = graph.with(rootNode);
		}
		
		ByteArrayOutputStream graphAsOutputStream = new ByteArrayOutputStream();
		Graphviz.fromGraph(graph).render(Format.PNG).toOutputStream(graphAsOutputStream);
		
		return graphAsOutputStream.toByteArray();
	}
	
	@Override
	public WorkflowGraphViewRepresentation generateGraphView(Workflow workflow, WorkflowGraphTrace workflowGraphTrace) throws AppException {
		
		WorkflowGraph workflowGraph = WorkflowGraphBuilder.buildGraph(workflow);
		workflowGraph.setNodes(Sets.newTreeSet(workflowGraph.getNodes()));
		
		WorkflowGraphView workflowGraphView =  new WorkflowGraphView();
		
		try {
			workflowGraphView.setContent(getGraphWithWorkflowTrace(workflowGraph, workflowGraphTrace));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		workflowGraphView.setMimeType(MimeType.OCTET_STREAM.getMimeType());
		return getRepresentation(workflowGraphView);
	}
	
	private byte[] getGraphWithWorkflowTrace(WorkflowGraph workflowGraph, WorkflowGraphTrace workflowGraphTrace) throws IOException {
		
		Graph graph = graph(workflowGraph.getLabel()).directed().named(workflowGraph.getLabel());
		Node rootNode;
		for (WorkflowGraphNode workflowGraphNode : workflowGraph.getNodes()) {
			
			if (workflowGraphTrace.getCurrentNodeCode().equals(workflowGraphNode.getIdentifier())) {
				rootNode = prepareCurrentNode(workflowGraphNode.getLabel());
			} else {
				rootNode = prepareNode(workflowGraphNode.getLabel());
			}
			
			for (WorkflowGraphEdge edge : workflowGraph.getEdges()) {
				if (edge.getStartNode().equals(workflowGraphNode)) {
					
					boolean nodeFound = false;
					for (String nodeCode : workflowGraphTrace.getNodesCodes()) {
						if (nodeCode.equals(workflowGraphNode.getIdentifier())) {
							nodeFound = true;
						}
					}
					
					Node endNode = null;
					if (nodeFound && !workflowGraphTrace.getCurrentNodeCode().equals(workflowGraphNode.getIdentifier())) {
						endNode = node(edge.getEndNode().getLabel());
						rootNode = prepareTraceNode(rootNode);
					} else {
						endNode = node(edge.getEndNode().getLabel());
					}
					
					boolean transitionFound = false;
					for (String transitionName : workflowGraphTrace.getTransitionsNames()) {
						if (transitionName.equals(edge.getLabel())) {
							transitionFound = true;
						}
					}
					
					if (transitionFound) {
						rootNode = addTraceLink(rootNode, endNode, edge.getLabel());
					} else {
						rootNode = addLink(rootNode, endNode, edge.getLabel());
					}
				}
			}
			graph = graph.with(rootNode);
		}
		
		ByteArrayOutputStream graphAsOutputStream = new ByteArrayOutputStream();
		Graphviz.fromGraph(graph).render(Format.PNG).toOutputStream(graphAsOutputStream);
		
		return graphAsOutputStream.toByteArray();
	}
	
	private Node prepareNode(String label) {
		return node(label)
				.with("color", NODE_BORDER_COLOR)
				.with("fillcolor", NODE_COLOR)
				.with("fontcolor", FONT_COLOR)
				.with("fontsize", FONT_SIZE);
	}
	
	private Node prepareCurrentNode(String label) {
		return node(label)
				.with(Style.FILLED)
				.with("color", CURRENT_NODE_BORDER_COLOR)
				.with("fillcolor", CURRENT_NODE_COLOR)
				.with("fontcolor", FONT_COLOR)
				.with("fontsize", FONT_SIZE);
	}
	
	private Node prepareTraceNode(Node node) {
		return node
				.with(Style.FILLED)
				.with("color", NODE_BORDER_COLOR)
				.with("fillcolor", TRACE_NODE_COLOR)
				.with("fontcolor", FONT_COLOR)
				.with("fontsize", FONT_SIZE);
	}
	
	private Node addLink(Node fromNode, Node toNode, String linkLabel) {
		return fromNode.link(
				Link.to(toNode)
					.with(Style.SOLID, Label.of(linkLabel))
					.with("color", LINK_COLOR)
					.with("fontcolor", FONT_COLOR)
					.with("fontsize", FONT_SIZE)
					.with("forcelabels", false)
		);
	}
	
	private Node addTraceLink(Node fromNode, Node toNode, String linkLabel) {
		return fromNode.link(
				Link.to(toNode)
					.with(Style.SOLID, Label.of(linkLabel))
					.with("color", TRACE_LINK_COLOR)
					.with("fontcolor", FONT_COLOR)
					.with("fontsize", FONT_SIZE)
					.with("forcelabels", false)
		);
	}
	
	private WorkflowGraphViewRepresentation getRepresentation(WorkflowGraphView workflowGraphView) {
		return new WorkflowGraphViewRepresentation(workflowGraphView.getMimeType(), workflowGraphView.getContent());
	}
}