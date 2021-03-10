package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.AssignedEntityTransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.AssignedEntityTransitionNotificationModel;

public class AssignedEntityTransitionNotificationConverter implements SpecificTransitionNotificationConverter<AssignedEntityTransitionNotification, AssignedEntityTransitionNotificationModel> {

	@Override
	public Class<AssignedEntityTransitionNotification> getNotificationClass() {
		return AssignedEntityTransitionNotification.class;
	}
	
	@Override
	public Class<AssignedEntityTransitionNotificationModel> getNotificationModelClass() {
		return AssignedEntityTransitionNotificationModel.class;
	}
	
	@Override
	public AssignedEntityTransitionNotification createNewInstance() {
		return new AssignedEntityTransitionNotification();
	}
	
	@Override
	public AssignedEntityTransitionNotificationModel createNewModelInstance() {
		return new AssignedEntityTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(AssignedEntityTransitionNotification transitionNotification,
			AssignedEntityTransitionNotificationModel transitionNotificationModel) {
		
		// Nu sunt proprietati specifice.
	}
	
	@Override
	public void setSpecificPropertiesFromModel(AssignedEntityTransitionNotificationModel transitionNotificationModel,
			AssignedEntityTransitionNotification transitionNotification) {
		
		// Nu sunt proprietati specifice.
	}
}