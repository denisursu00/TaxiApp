package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.UserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.UserMetadataTransitionNotificationModel;

public class UserMetadataTransitionNotificationConverter implements SpecificTransitionNotificationConverter<UserMetadataTransitionNotification, UserMetadataTransitionNotificationModel> {

	@Override
	public Class<UserMetadataTransitionNotification> getNotificationClass() {
		return UserMetadataTransitionNotification.class;
	}
	
	@Override
	public Class<UserMetadataTransitionNotificationModel> getNotificationModelClass() {
		return UserMetadataTransitionNotificationModel.class;
	}
	
	@Override
	public UserMetadataTransitionNotification createNewInstance() {
		return new UserMetadataTransitionNotification();
	}
	
	@Override
	public UserMetadataTransitionNotificationModel createNewModelInstance() {
		return new UserMetadataTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(UserMetadataTransitionNotification transitionNotification,
			UserMetadataTransitionNotificationModel transitionNotificationModel) {
		
		transitionNotificationModel.setMetadataName(transitionNotification.getMetadataName());
	}
	
	@Override
	public void setSpecificPropertiesFromModel(UserMetadataTransitionNotificationModel transitionNotificationModel,
			UserMetadataTransitionNotification transitionNotification) {
		
		transitionNotification.setMetadataName(transitionNotificationModel.getMetadataName());
	}
}