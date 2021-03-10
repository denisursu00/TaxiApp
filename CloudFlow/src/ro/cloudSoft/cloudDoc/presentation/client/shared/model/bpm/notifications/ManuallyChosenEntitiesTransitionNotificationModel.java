package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class ManuallyChosenEntitiesTransitionNotificationModel extends TransitionNotificationModel {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_MANUALLY_CHOSEN_ENTITIES = "manuallyChosenEntities";
	
	public ManuallyChosenEntitiesTransitionNotificationModel() {
		super(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES);
	}

	public List<OrganizationEntityModel> getManuallyChosenEntities() {
		return get(PROPERTY_MANUALLY_CHOSEN_ENTITIES);
	}
	
	public void setManuallyChosenEntities(List<OrganizationEntityModel> manuallyChosenEntities) {
		set(PROPERTY_MANUALLY_CHOSEN_ENTITIES, manuallyChosenEntities);
	}
}