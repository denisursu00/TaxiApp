package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.InitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.InitiatorTransitionNotificationModel;

public class InitiatorTransitionNotificationConverter implements SpecificTransitionNotificationConverter<InitiatorTransitionNotification, InitiatorTransitionNotificationModel> {

	@Override
	public Class<InitiatorTransitionNotification> getNotificationClass() {
		return InitiatorTransitionNotification.class;
	}
	
	@Override
	public Class<InitiatorTransitionNotificationModel> getNotificationModelClass() {
		return InitiatorTransitionNotificationModel.class;
	}
	
	@Override
	public InitiatorTransitionNotification createNewInstance() {
		return new InitiatorTransitionNotification();
	}
	
	@Override
	public InitiatorTransitionNotificationModel createNewModelInstance() {
		return new InitiatorTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(InitiatorTransitionNotification transitionNotification,
			InitiatorTransitionNotificationModel transitionNotificationModel) {
		
		// Nu sunt proprietati specifice.
	}
	
	@Override
	public void setSpecificPropertiesFromModel(InitiatorTransitionNotificationModel transitionNotificationModel,
			InitiatorTransitionNotification transitionNotification) {
		
		// Nu sunt proprietati specifice.
	}
}