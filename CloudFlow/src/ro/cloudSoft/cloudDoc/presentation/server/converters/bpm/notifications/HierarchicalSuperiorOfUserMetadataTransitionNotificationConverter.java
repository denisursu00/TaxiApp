package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotificationModel;

public class HierarchicalSuperiorOfUserMetadataTransitionNotificationConverter implements 
		SpecificTransitionNotificationConverter<HierarchicalSuperiorOfUserMetadataTransitionNotification, HierarchicalSuperiorOfUserMetadataTransitionNotificationModel> {

	@Override
	public Class<HierarchicalSuperiorOfUserMetadataTransitionNotification> getNotificationClass() {
		return HierarchicalSuperiorOfUserMetadataTransitionNotification.class;
	}
	
	@Override
	public Class<HierarchicalSuperiorOfUserMetadataTransitionNotificationModel> getNotificationModelClass() {
		return HierarchicalSuperiorOfUserMetadataTransitionNotificationModel.class;
	}
	
	@Override
	public HierarchicalSuperiorOfUserMetadataTransitionNotification createNewInstance() {
		return new HierarchicalSuperiorOfUserMetadataTransitionNotification();
	}
	
	@Override
	public HierarchicalSuperiorOfUserMetadataTransitionNotificationModel createNewModelInstance() {
		return new HierarchicalSuperiorOfUserMetadataTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(HierarchicalSuperiorOfUserMetadataTransitionNotification transitionNotification,
			HierarchicalSuperiorOfUserMetadataTransitionNotificationModel transitionNotificationModel) {
		
		transitionNotificationModel.setMetadataName(transitionNotification.getMetadataName());
	}
	
	@Override
	public void setSpecificPropertiesFromModel(HierarchicalSuperiorOfUserMetadataTransitionNotificationModel transitionNotificationModel,
			HierarchicalSuperiorOfUserMetadataTransitionNotification transitionNotification) {
		
		transitionNotification.setMetadataName(transitionNotificationModel.getMetadataName());
	}
}