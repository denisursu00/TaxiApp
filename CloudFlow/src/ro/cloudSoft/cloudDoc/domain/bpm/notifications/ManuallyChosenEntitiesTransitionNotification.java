package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

import com.google.common.collect.Sets;

@Entity
@Table(name = "manual_trans_notifs")
@PrimaryKeyJoinColumn(name = "notification_id", referencedColumnName = "id")
public class ManuallyChosenEntitiesTransitionNotification extends TransitionNotification {

	private Set<OrganizationEntity> manuallyChosenEntities = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "entits_4_manual_trans_notifs",
		joinColumns = @JoinColumn(name = "man_trans_notif_id"),
		inverseJoinColumns = @JoinColumn(name = "org_entity_id")
	)
	public Set<OrganizationEntity> getManuallyChosenEntities() {
		return manuallyChosenEntities;
	}
	
	public void setManuallyChosenEntities(Set<OrganizationEntity> manuallyChosenEntities) {
		this.manuallyChosenEntities = manuallyChosenEntities;
	}
}