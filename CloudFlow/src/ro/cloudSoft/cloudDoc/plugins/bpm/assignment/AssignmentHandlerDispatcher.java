package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.task.AssignmentHandler;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Obtine un handler potrivit pentru tipul de rutare al unei tranzitii.
 * 
 * 
 */
public class AssignmentHandlerDispatcher implements InitializingBean {
	
	private WorkflowDao workflowDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowDao
		);
	}

	public AssignmentHandler getHandlerFor(String transitionName, Long finalStateId) {
		
		WorkflowTransition transition = workflowDao.getTransition(transitionName, finalStateId);
		if (transition == null) {
			String exceptionMessage = "Nu s-a gasit tranzitia cu numele [" + transitionName + "] si ID-ul starii finale [" + finalStateId + "].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		return getHandlerFor(transition);
	}
	
	private AssignmentHandler getHandlerFor(WorkflowTransition transition) {
		if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_HIERARCHICAL_SUB)) {
			return new HierarchicalSubalternAssignmentHandler();
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_HIERARCHICAL_INF)) {
			return new HierarchicalInferiorAssignmentHandler();
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_HIERARCHICAL_SUP)) {
			return new HierarchicalSuperiorAssignmentHandler();
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_INITIATOR)) {
			return new InitiatorAssignmentHandler();
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_GROUP)) {
			return getGroupHandlerFor(transition);
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_PREVIOUS)) {
			return new PreviousAssignmentHandler();
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_MANUAL)) {
			return getManualHandlerFor(transition);
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_PARAMETER)) {
			return getParameterHandlerFor(transition);
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_OU)) {
			return getOrganizationUnitHandlerFor(transition);
		} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_PARAMETER_HIERARCHICAL_SUP)) {
			return getUserHierarchicalSuperiorParameterFor(transition);
		} else {
			String exceptionMessage = "Tipul de rutare [" + transition.getRoutingType() + "], " +
				"al tranzitiei cu ID-ul [" + transition.getId() + "], NU este suportat.";
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	private GroupAssignmentHandler getGroupHandlerFor(WorkflowTransition transition) {
		
		Long groupId = transition.getRoutingDestinationId();
		if (groupId == null) {
			String exceptionMessage = "Tranzitia cu ID-ul [" + transition.getId() + "] are " +
				"rutare de tip grup, insa ID-ul grupului NU este specificat.";
			throw new IllegalStateException(exceptionMessage);
		}
		String groupIdAsString = groupId.toString();
		
		GroupAssignmentHandler assignmentHandler = new GroupAssignmentHandler();
		assignmentHandler.setGroupId(groupIdAsString);
		return assignmentHandler;
	}
	
	private ManualAssignmentHandler getManualHandlerFor(WorkflowTransition transition) {
		ManualAssignmentHandler assignmentHandler = new ManualAssignmentHandler();
		assignmentHandler.setIncomingTransitionName(transition.getName());
		return assignmentHandler;
	}
	
	private ParameterAssignmentHandler getParameterHandlerFor(WorkflowTransition transition) {
		
		String userMetadataName = transition.getRoutingDestinationParameter();
		if (StringUtils.isBlank(userMetadataName)) {
			String exceptionMessage = "Tranzitia cu ID-ul [" + userMetadataName + "] are rutare de " +
				"tip parametru, insa numele metadatei de tip user, necesara ca parametru NU este completata.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		ParameterAssignmentHandler assignmentHandler = new ParameterAssignmentHandler();
		assignmentHandler.setUserMetadataDefinition(userMetadataName);
		return assignmentHandler;
	}

	
	private ParameterHierarchicalSuperiorAssignmentHandler getUserHierarchicalSuperiorParameterFor(WorkflowTransition transition) {
		
		String userMetadataName = transition.getRoutingDestinationParameter();
		if (StringUtils.isBlank(userMetadataName)) {
			String exceptionMessage = "Tranzitia cu ID-ul [" + userMetadataName + "] are rutare de " +
				"tip parametru, insa numele metadatei de tip user, necesara ca parametru NU este completata.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		ParameterHierarchicalSuperiorAssignmentHandler assignmentHandler = new ParameterHierarchicalSuperiorAssignmentHandler();
		assignmentHandler.setUserMetadataDefinition(userMetadataName);
		return assignmentHandler;
	}
	
	private OrganizationUnitAssignmentHandler getOrganizationUnitHandlerFor(WorkflowTransition transition) {
		
		Long organizationUnitId = transition.getRoutingDestinationId();
		if (organizationUnitId == null) {
			String exceptionMessage = "Tranzitia cu ID-ul [" + transition.getId() + "] are " +
				"rutare de tip unitate organizatorica, insa ID-ul unitatii NU este specificat.";
			throw new IllegalStateException(exceptionMessage);
		}
		String organizationUnitIdAsString = organizationUnitId.toString();
		
		OrganizationUnitAssignmentHandler assignmentHandler = new OrganizationUnitAssignmentHandler();
		assignmentHandler.setOrganizationUnitId(organizationUnitIdAsString);
		return assignmentHandler;
	}
	
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
}