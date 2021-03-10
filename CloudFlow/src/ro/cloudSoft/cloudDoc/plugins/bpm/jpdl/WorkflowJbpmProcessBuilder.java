package ro.cloudSoft.cloudDoc.plugins.bpm.jpdl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.common.utils.MapUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 */
public class WorkflowJbpmProcessBuilder {

	private final Workflow workflow;
	
	private String workflowJbpmProcessName;
	
	private Set<WorkflowJbpmProcessPathWithManualAssignment> pathsWithManualAssignment;
	
	private Set<WorkflowJbpmStartNode> startNodes;
	private Set<WorkflowJbpmTaskNode> taskNodes;
	private Set<WorkflowJbpmEndNode> endNodes;
	
	private Map<WorkflowJbpmNode, Set<WorkflowJbpmTransition>> outgoingTransitionsByIncomingNode;
	
	private WorkflowJbpmStartNode startNode;
	
	public WorkflowJbpmProcessBuilder(Workflow workflow) {
		this.workflow = workflow;
	}
	
	public WorkflowJbpmProcess build() {
		
		init();
		setBasicProperties();
		processTransitions();
		processStartNodes();
		
		return createProcess();
	}
	
	private void init() {
		
		workflowJbpmProcessName = null;
		
		pathsWithManualAssignment = Sets.newLinkedHashSet();
		
		startNodes = Sets.newLinkedHashSet();
		taskNodes = Sets.newLinkedHashSet();
		endNodes = Sets.newLinkedHashSet();
		
		outgoingTransitionsByIncomingNode = Maps.newHashMap();
		
		startNode = null;
	}
	
	private void setBasicProperties() {
		workflowJbpmProcessName = workflow.getName();
	}
	
	private void processTransitions() {
		for (WorkflowTransition transition : workflow.getTransitions()) {
			
			WorkflowState startState = transition.getStartState();
			WorkflowJbpmNode incomingNode = createNode(startState);
			addToAppropriateNodeSet(incomingNode);
			
			WorkflowState finalState = transition.getFinalState();
			WorkflowJbpmNode outgoingNode = createNode(finalState);
			addToAppropriateNodeSet(outgoingNode);
			
			WorkflowJbpmTransition workflowJbpmTransition = new WorkflowJbpmTransition();
			
			workflowJbpmTransition.setName(transition.getName());
			workflowJbpmTransition.setOutgoingNodeName(outgoingNode.getName());
			workflowJbpmTransition.setWorkflowTransitionId(transition.getId());
			
			workflowJbpmTransition.setRoutingCondition(transition.getRoutingCondition());
			
			if (!(incomingNode instanceof WorkflowJbpmNodeWithOutgoingTransitions)) {
				String exceptionMessage = "Tranzitia cu numele [" + transition.getName() + "] pentru fluxul cu ID-ul " +
					"[" + workflow.getId() + "] si numele [" + workflow.getName() + "] NU are o stare de plecare corecta.";
				throw new IllegalArgumentException(exceptionMessage);
			}
			
			Set<WorkflowJbpmTransition> outgoingTransitionsForIncomingNode = MapUtils.getAndInitIfNull(
				outgoingTransitionsByIncomingNode, incomingNode, Sets.<WorkflowJbpmTransition> newLinkedHashSet());
			outgoingTransitionsForIncomingNode.add(workflowJbpmTransition);
			
			String routingType = transition.getRoutingType();
			if (StringUtils.isNotBlank(routingType) && routingType.equals(WorkflowTransition.ROUTING_MANUAL)) {
				
				WorkflowJbpmProcessPathWithManualAssignment pathWithManualAssignment = new WorkflowJbpmProcessPathWithManualAssignment();
				
				pathWithManualAssignment.setTransitionName(workflowJbpmTransition.getName());
				pathWithManualAssignment.setOutgoingNodeName(outgoingNode.getName());
				
				pathsWithManualAssignment.add(pathWithManualAssignment);
			}
		}
	}
	
	private void processStartNodes() {
		if (startNodes.size() == 1) {
			startNode = Iterables.getOnlyElement(startNodes);
		} else if (startNodes.isEmpty()) {
			String exceptionMessage = "Nu s-a gasit un nod de tip START pentru fluxul cu " +
				"ID-ul [" + workflow.getId() + "] si numele [" + workflow.getName() + "].";
			throw new IllegalStateException(exceptionMessage);
		} else {
			Collection<String> startNodeNames = Collections2.transform(startNodes, new Function<WorkflowJbpmStartNode, String>() {
				
				@Override
				public String apply(WorkflowJbpmStartNode startNode) {
					return startNode.getName();
				}
			});
			String exceptionMessage = "Pentru fluxul cu ID-ul [" + workflow.getId() + "] si numele [" + workflow.getName() + "] " +
				"s-au gasit mai mult de un nod de tip START, si anume nodurile cu numele [" + StringUtils.join(startNodeNames, ", ") + "].";
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	private WorkflowJbpmProcess createProcess() {
		
		WorkflowJbpmProcess workflowJbpmProcess = new WorkflowJbpmProcess();
		
		workflowJbpmProcess.setName(workflowJbpmProcessName);
		
		workflowJbpmProcess.setPathsWithManualAssignment(pathsWithManualAssignment);
		
		startNode.setOutgoingTransitions(outgoingTransitionsByIncomingNode.get(startNode));
		workflowJbpmProcess.setStartNode(startNode);
		
		for (WorkflowJbpmTaskNode taskNode : taskNodes) {
			taskNode.setOutgoingTransitions(outgoingTransitionsByIncomingNode.get(taskNode));
		}
		workflowJbpmProcess.setTaskNodes(taskNodes);
		
		workflowJbpmProcess.setEndNodes(endNodes);
		
		return workflowJbpmProcess;
	}
	
	private WorkflowJbpmNode createNode(WorkflowState state) {
		if (state.getStateType().equals(WorkflowState.STATETYPE_START)) {
			WorkflowJbpmStartNode workflowJbpmStartNode = new WorkflowJbpmStartNode();
			setNodeProperties(workflowJbpmStartNode, state);
			return workflowJbpmStartNode;
		} else if (state.getStateType().equals(WorkflowState.STATETYPE_INTERMEDIATE)) {
			WorkflowJbpmTaskNode workflowJbpmTaskNode = new WorkflowJbpmTaskNode();
			setNodeProperties(workflowJbpmTaskNode, state);
			return workflowJbpmTaskNode;
		} else if (state.getStateType().equals(WorkflowState.STATETYPE_STOP)) {
			WorkflowJbpmEndNode workflowJbpmEndNode = new WorkflowJbpmEndNode();
			setNodeProperties(workflowJbpmEndNode, state);
			return workflowJbpmEndNode;
		} else {
			String exceptionMessage = "Tipul de stare cu ID-ul [" + state.getStateType() + "] NU este suportat.";
			throw new IllegalArgumentException(exceptionMessage);
		}
	}
	
	private void setNodeProperties(WorkflowJbpmNode node, WorkflowState state) {
		node.setName(state.getCode());
		node.setDescription(state.getName());
		node.setWorkflowStateId(state.getId());
		node.setAutomaticRunning(state.isAutomaticRunning());
	}
	
	private void addToAppropriateNodeSet(WorkflowJbpmNode node) {
		switch (node.getType()) {
			case START:
				startNodes.add((WorkflowJbpmStartNode) node);
				break;
			case TASK:
				taskNodes.add((WorkflowJbpmTaskNode) node);
				break;
			case END:
				endNodes.add((WorkflowJbpmEndNode) node);
				break;
			default:
				String exceptionMessage = "Nu exista caz pentru tipul de nod [" + node.getType() + "].";
				throw new IllegalArgumentException(exceptionMessage);
		}
	}
}