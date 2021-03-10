package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;

/**
 * 
 */
@Embeddable
public class ReplacementProfileInstanceItemPk implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ReplacementProfileInstance profileInstance;
	private Integer index;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumns({
		@JoinColumn(name = "document_location_real_name", referencedColumnName = "document_location_real_name", nullable = false),
		@JoinColumn(name = "document_id", referencedColumnName = "document_id", nullable = false)
	})
	public ReplacementProfileInstance getProfileInstance() {
		return profileInstance;
	}
	
	@Column(name = "item_index", nullable = false)
	public Integer getIndex() {
		return index;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof ReplacementProfileInstanceItemPk)) {
			return false;
		}
		
		ReplacementProfileInstanceItemPk other = (ReplacementProfileInstanceItemPk) obj;
		
		ReplacementProfileInstance profileInstance = getProfileInstance();
		ReplacementProfileInstancePk profileInstanceId = (profileInstance != null) ? profileInstance.getId() : null;

		ReplacementProfileInstance otherProfileInstance = other.getProfileInstance();
		ReplacementProfileInstancePk otherProfileInstanceId = (otherProfileInstance != null) ? otherProfileInstance.getId() : null;
		
		return (
			Objects.equal(profileInstanceId, otherProfileInstanceId) &&
			Objects.equal(getIndex(), other.getIndex())
		);
	}
	
	@Override
	public int hashCode() {
		
		ReplacementProfileInstance profileInstance = getProfileInstance();
		ReplacementProfileInstancePk profileInstanceId = (profileInstance != null) ? profileInstance.getId() : null;
		
		return Objects.hashCode(
			profileInstanceId,
			getIndex()
		);
	}
	
	public void setProfileInstance(ReplacementProfileInstance profileInstance) {
		this.profileInstance = profileInstance;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
}