package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;

public interface SpecificTransitionNotificationConverter<N extends TransitionNotification, NM extends TransitionNotificationModel> {
	
	Class<N> getNotificationClass();
	
	Class<NM> getNotificationModelClass();

	N createNewInstance();
	
	NM createNewModelInstance();
	
	void setSpecificPropertiesToModel(N transitionNotification, NM transitionNotificationModel);

	void setSpecificPropertiesFromModel(NM transitionNotificationModel, N transitionNotification);
}