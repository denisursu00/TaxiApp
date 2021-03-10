package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications;


public class UserMetadataTransitionNotificationModel extends TransitionNotificationModel {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_METADATA_NAME = "metadataName";
	
	public UserMetadataTransitionNotificationModel() {
		super(TransitionNotificationModelType.METADATA);
	}

	public String getMetadataName() {
		return get(PROPERTY_METADATA_NAME);
	}
	
	public void setMetadataName(String metadataName) {
		set(PROPERTY_METADATA_NAME, metadataName);
	}
}