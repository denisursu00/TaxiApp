package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table(name = "replacement_profile_statuses")
public class ReplacementProfileStatus {

	private Long id;
	private ReplacementProfile profile;
	private ReplacementProfileStatusOption status;
	
	public ReplacementProfileStatus() {}
	
	public ReplacementProfileStatus(ReplacementProfile profile, ReplacementProfileStatusOption status) {
		this.profile = profile;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false, unique = true)
	public ReplacementProfile getProfile() {
		return profile;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 100)
	public ReplacementProfileStatusOption getStatus() {
		return status;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setProfile(ReplacementProfile profile) {
		this.profile = profile;
	}
	public void setStatus(ReplacementProfileStatusOption status) {
		this.status = status;
	}
}