package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "sup_of_user_mtadta_trans_notif")
@PrimaryKeyJoinColumn(name = "notification_id", referencedColumnName = "id")
public class HierarchicalSuperiorOfUserMetadataTransitionNotification extends TransitionNotification {


	private String metadataName;
	
	@Column(name = "metadata_name", nullable = false)
	public String getMetadataName() {
		return metadataName;
	}
	
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
}