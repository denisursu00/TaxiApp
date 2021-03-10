package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications;

public class HierarchicalSuperiorOfUserMetadataTransitionNotificationModel extends TransitionNotificationModel {
	
	private static final long serialVersionUID = 1L;

	private String metadataName;
	
	public HierarchicalSuperiorOfUserMetadataTransitionNotificationModel() {
		super(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA);
	}

	public String getMetadataName() {
		return metadataName;
	}

	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
}
