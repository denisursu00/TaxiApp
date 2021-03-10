package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;

/**
 * 
 */
@Entity
@Table(name = "replacement_profile_inst_items")
public class ReplacementProfileInstanceItem {

	private ReplacementProfileInstanceItemPk id = new ReplacementProfileInstanceItemPk();
	private OrganizationEntity originallyAssignedEntity;
	private User replacementUsed;
	private ReplacementProfile replacementProfileUsed;
	
	@EmbeddedId
	public ReplacementProfileInstanceItemPk getId() {
		return id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "originally_assigned_entity_id", referencedColumnName = "org_entity_id", nullable = false)
	public OrganizationEntity getOriginallyAssignedEntity() {
		return originallyAssignedEntity;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "replacement_used_user_id", referencedColumnName = "org_entity_id", nullable = false)
	public User getReplacementUsed() {
		return replacementUsed;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "replacement_profile_used_id", referencedColumnName = "id", nullable = false)
	public ReplacementProfile getReplacementProfileUsed() {
		return replacementProfileUsed;
	}
	
	@Transient
	public void setProfileInstance(ReplacementProfileInstance profileInstance) {
		getId().setProfileInstance(profileInstance);
	}

	@Transient
	public ReplacementProfileInstance getProfileInstance() {
		return getId().getProfileInstance();
	}
	
	@Transient
	public void setIndex(int index) {
		getId().setIndex(index);
	}
	
	public void setId(ReplacementProfileInstanceItemPk id) {
		this.id = id;
	}
	public void setOriginallyAssignedEntity(OrganizationEntity originallyAssignedEntity) {
		this.originallyAssignedEntity = originallyAssignedEntity;
	}
	public void setReplacementUsed(User replacementUsed) {
		this.replacementUsed = replacementUsed;
	}
	public void setReplacementProfileUsed(ReplacementProfile replacementProfileUsed) {
		this.replacementProfileUsed = replacementProfileUsed;
	}
}