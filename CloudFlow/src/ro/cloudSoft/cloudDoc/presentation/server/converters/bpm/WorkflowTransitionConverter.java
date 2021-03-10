package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications.TransitionNotificationConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class WorkflowTransitionConverter {

	public static WorkflowTransitionModel getModelFromWorkflowTransition(WorkflowTransition transition){
		WorkflowTransitionModel  model = new WorkflowTransitionModel();
		model.setId(transition.getId());
		model.setName(transition.getName());
		model.setStartState(WorkflowStateConverter.getModelFromWorkflowState(transition.getStartState()));
		model.setFinalState(WorkflowStateConverter.getModelFromWorkflowState(transition.getFinalState()));
		model.setRoutingCondition(transition.getRoutingCondition());
		
		List<OrganizationEntityModel> listExtraViewers = new ArrayList<OrganizationEntityModel>();
		if (transition.getExtraViewers() != null){
			for (OrganizationEntity oe : transition.getExtraViewers()){
				OrganizationEntityModel oeModel = OrganizationEntityConverter.getModelFromOrganizationEntity(oe);
				listExtraViewers.add(oeModel);
			}
		}
		model.setExtraViewers(listExtraViewers);
		
		model.setRoutingType(transition.getRoutingType());
		model.setRoutingDestinationId(transition.getRoutingDestinationId());
		model.setRoutingDestinationParameter(transition.getRoutingDestinationParameter());
		
		model.setAvailableForAutomaticActionsOnly(transition.isAvailableForAutomaticActionsOnly());
		
		model.setDeadlineAction(transition.isDeadlineAction());
		model.setDeadlineActionType(transition.getDeadlineActionType());
		model.setDeadlinePeriod(transition.getDeadlinePeriod());
		model.setDeadlineNotifyResendInterval(transition.getDeadlineNotifyResendInterval());
		
		List<TransitionNotificationModel> notificationModels = Lists.newArrayList();
		if (transition.getNotifications() != null) {
			for (TransitionNotification notification : transition.getNotifications()) {
				TransitionNotificationModel notificationModel = TransitionNotificationConverter.getModel(notification);
				notificationModels.add(notificationModel);
			}
		}
		model.setNotifications(notificationModels);
		model.setUiSendConfirmation(transition.isUiSendConfirmation());
		
		return model;
	}
	
	public static WorkflowTransition getWorkflowTransitionFromModel(WorkflowTransitionModel model){
		WorkflowTransition  transition = new WorkflowTransition();
		transition.setId(model.getId());
		transition.setName(model.getName());
		transition.setStartState(WorkflowStateConverter.getWorkflowStateFromModel(model.getStartState()));
		transition.setFinalState(WorkflowStateConverter.getWorkflowStateFromModel(model.getFinalState()));
		transition.setRoutingCondition(model.getRoutingCondition());
		
		Set<OrganizationEntity> listExtraViewers = new HashSet<OrganizationEntity>();
		if (model.getExtraViewers() != null){
			for (OrganizationEntityModel oeModel : model.getExtraViewers()){
				OrganizationEntity oe = OrganizationEntityConverter.getOrganizationEntityFromModel(oeModel);
				listExtraViewers.add(oe);
			}
		}
		transition.setExtraViewers(listExtraViewers);
		
		transition.setRoutingType(model.getRoutingType());
		if (model.getRoutingDestinationId() != null)
			transition.setRoutingDestinationId(model.getRoutingDestinationId());
		transition.setRoutingDestinationParameter(model.getRoutingDestinationParameter());
		
		transition.setAvailableForAutomaticActionsOnly(model.isAvailableForAutomaticActionsOnly());
		
		transition.setDeadlineAction(model.getDeadlineAction());
		transition.setDeadlineActionType(model.getDeadlineActionType());
		transition.setDeadlinePeriod(model.getDeadlinePeriod());
		transition.setDeadlineNotifyResendInterval(model.getDeadlineNotifyResendInterval());
		
		Set<TransitionNotification> notifications = Sets.newHashSet();
		if (model.getNotifications() != null) {
			for (TransitionNotificationModel notificationModel : model.getNotifications()) {
				TransitionNotification notification = TransitionNotificationConverter.getFromModel(notificationModel, transition);
				notifications.add(notification);
			}
		}
		transition.setNotifications(notifications);
		transition.setUiSendConfirmation(model.isUiSendConfirmation());
		
		return transition;
	}	
}