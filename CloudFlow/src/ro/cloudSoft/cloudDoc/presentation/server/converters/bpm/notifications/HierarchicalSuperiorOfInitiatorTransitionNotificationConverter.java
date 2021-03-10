package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotificationModel;

public class HierarchicalSuperiorOfInitiatorTransitionNotificationConverter implements SpecificTransitionNotificationConverter<HierarchicalSuperiorOfInitiatorTransitionNotification, HierarchicalSuperiorOfInitiatorTransitionNotificationModel> {

	@Override
	public Class<HierarchicalSuperiorOfInitiatorTransitionNotification> getNotificationClass() {
		return HierarchicalSuperiorOfInitiatorTransitionNotification.class;
	}
	
	@Override
	public Class<HierarchicalSuperiorOfInitiatorTransitionNotificationModel> getNotificationModelClass() {
		return HierarchicalSuperiorOfInitiatorTransitionNotificationModel.class;
	}
	
	@Override
	public HierarchicalSuperiorOfInitiatorTransitionNotification createNewInstance() {
		return new HierarchicalSuperiorOfInitiatorTransitionNotification();
	}
	
	@Override
	public HierarchicalSuperiorOfInitiatorTransitionNotificationModel createNewModelInstance() {
		return new HierarchicalSuperiorOfInitiatorTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(HierarchicalSuperiorOfInitiatorTransitionNotification transitionNotification,
			HierarchicalSuperiorOfInitiatorTransitionNotificationModel transitionNotificationModel) {
		
		// Nu sunt proprietati specifice.
	}
	
	@Override
	public void setSpecificPropertiesFromModel(HierarchicalSuperiorOfInitiatorTransitionNotificationModel transitionNotificationModel,
			HierarchicalSuperiorOfInitiatorTransitionNotification transitionNotification) {
		
		// Nu sunt proprietati specifice.
	}
}