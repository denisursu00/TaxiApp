package ro.cloudSoft.cloudDoc.utils.bpm;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.AssignedEntityTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.InitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.ManuallyChosenEntitiesTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.UserMetadataTransitionNotification;

/**
 * 
 */
public class WorkflowCopyBuilder {

	private final Workflow workflow;
	
	private Workflow workflowCopy;
	private Map<String, WorkflowState> workflowStateOfCopyByCode;
	
	public WorkflowCopyBuilder(Workflow workflow) {
		this.workflow = workflow;
	}
	
	public Workflow build() {
		
		init();
		setBasicWorkflowProperties();
		setTransitions();
		
		return workflowCopy;
	}
	
	private void init() {
		workflowCopy = new Workflow();
		workflowStateOfCopyByCode = Maps.newHashMap();
	}
	
	private void setBasicWorkflowProperties() {
		workflowCopy.setName(workflow.getName());
		workflowCopy.setDescription(workflow.getDescription());
		workflowCopy.setDocumentTypes(Sets.newHashSet(workflow.getDocumentTypes()));
		workflowCopy.setSupervisors(Sets.newHashSet(workflow.getSupervisors()));
	}
	
	private void setTransitions() {
		Set<WorkflowTransition> transitionsOfCopy = Sets.newHashSet();
		for (WorkflowTransition transition : workflow.getTransitions()) {
			WorkflowTransition transitionOfCopy = getTransitionCopy(transition);
			transitionsOfCopy.add(transitionOfCopy);
		}
		workflowCopy.setTransitions(transitionsOfCopy);
	}
	
	private WorkflowTransition getTransitionCopy(WorkflowTransition transition) {
		
		WorkflowTransition transitionCopy = new WorkflowTransition();
		
		copyTransitionBasicProperties(transition, transitionCopy);
		copyTransitionNotifications(transition, transitionCopy);
		
		transitionCopy.setStartState(getStateCopy(transition.getStartState()));
		transitionCopy.setFinalState(getStateCopy(transition.getFinalState()));
		
		return transitionCopy;
	}
	
	private void copyTransitionBasicProperties(WorkflowTransition sourceTransition, WorkflowTransition destinationTransition) {
		destinationTransition.setName(sourceTransition.getName());
		destinationTransition.setRoutingCondition(sourceTransition.getRoutingCondition());
		destinationTransition.setRoutingType(sourceTransition.getRoutingType());
		destinationTransition.setRoutingDestinationId(sourceTransition.getRoutingDestinationId());
		destinationTransition.setRoutingDestinationParameter(sourceTransition.getRoutingDestinationParameter());
		destinationTransition.setExtraViewers(Sets.newHashSet(sourceTransition.getExtraViewers()));
		destinationTransition.setAvailableForAutomaticActionsOnly(sourceTransition.isAvailableForAutomaticActionsOnly());
		destinationTransition.setDeadlineAction(sourceTransition.isDeadlineAction());
		destinationTransition.setDeadlinePeriod(sourceTransition.getDeadlinePeriod());
		destinationTransition.setDeadlineActionType(sourceTransition.getDeadlineActionType());
		destinationTransition.setDeadlineNotifyResendInterval(sourceTransition.getDeadlineNotifyResendInterval());
	}
	
	private WorkflowState getStateCopy(WorkflowState state) {
		
		if (workflowStateOfCopyByCode.containsKey(state.getCode())) {
			return workflowStateOfCopyByCode.get(state.getCode());
		}
		
		WorkflowState stateCopy = new WorkflowState();
		
		stateCopy.setName(state.getName());
		stateCopy.setCode(state.getCode());
		stateCopy.setStateType(state.getStateType());
		stateCopy.setAttachmentsPermission(state.getAttachmentsPermission());
		
		workflowStateOfCopyByCode.put(state.getCode(), stateCopy);
		
		return stateCopy;
	}
	
	private void copyTransitionNotifications(WorkflowTransition sourceTransition, WorkflowTransition destinationTransition) {
		Set<TransitionNotification> notificationsOfCopy = Sets.newHashSet();
		for (TransitionNotification notificationOfSource : sourceTransition.getNotifications()) {
			
			TransitionNotification notificationOfCopy = null;
			
			if (notificationOfSource instanceof AssignedEntityTransitionNotification) {
				AssignedEntityTransitionNotification concreteNotificationOfCopy = new AssignedEntityTransitionNotification();
				notificationOfCopy = concreteNotificationOfCopy;
			} else if (notificationOfSource instanceof InitiatorTransitionNotification) {
				InitiatorTransitionNotification concreteNotificationOfCopy = new InitiatorTransitionNotification();
				notificationOfCopy = concreteNotificationOfCopy;
			} else if (notificationOfSource instanceof ManuallyChosenEntitiesTransitionNotification) {
				ManuallyChosenEntitiesTransitionNotification concreteNotificationOfSource = (ManuallyChosenEntitiesTransitionNotification) notificationOfSource;
				ManuallyChosenEntitiesTransitionNotification concreteNotificationOfCopy = new ManuallyChosenEntitiesTransitionNotification();
				concreteNotificationOfCopy.setManuallyChosenEntities(Sets.newHashSet(concreteNotificationOfSource.getManuallyChosenEntities()));
				notificationOfCopy = concreteNotificationOfCopy;
			} else if (notificationOfSource instanceof UserMetadataTransitionNotification) {
				UserMetadataTransitionNotification concreteNotificationOfSource = (UserMetadataTransitionNotification) notificationOfSource;
				UserMetadataTransitionNotification concreteNotificationOfCopy = new UserMetadataTransitionNotification();
				concreteNotificationOfCopy.setMetadataName(concreteNotificationOfSource.getMetadataName());
				notificationOfCopy = concreteNotificationOfCopy;
			} else if (notificationOfSource instanceof HierarchicalSuperiorOfInitiatorTransitionNotification) {
				HierarchicalSuperiorOfInitiatorTransitionNotification concreteNotificationOfCopy = new HierarchicalSuperiorOfInitiatorTransitionNotification();
				notificationOfCopy = concreteNotificationOfCopy;
			} else {
				throw new IllegalArgumentException("Tip necunoscut de notificare: [" + notificationOfSource.getClass().getName() + "]");
			}
			
			copyTransitionNotificationCommonProperties(notificationOfSource, notificationOfCopy, destinationTransition);
			
			notificationsOfCopy.add(notificationOfCopy);
		}
		destinationTransition.setNotifications(notificationsOfCopy);
	}
	
	private void copyTransitionNotificationCommonProperties(TransitionNotification sourceNotification,
			TransitionNotification destinationNotification, WorkflowTransition destinationTransition) {
		
		destinationNotification.setTransition(destinationTransition);
		
		destinationNotification.setEmailSubjectTemplate(sourceNotification.getEmailSubjectTemplate());
		destinationNotification.setEmailContentTemplate(sourceNotification.getEmailContentTemplate());
	}
}