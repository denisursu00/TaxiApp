package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idForRequestingReplacementProfile;
	private Collection<Long> idsForRequesterUserProfiles;
	private Date startDate;
	private Date endDate;
	
	public Long getIdForRequestingReplacementProfile() {
		return idForRequestingReplacementProfile;
	}
	public void setIdForRequestingReplacementProfile(Long idForRequestingReplacementProfile) {
		this.idForRequestingReplacementProfile = idForRequestingReplacementProfile;
	}
	public Collection<Long> getIdsForRequesterUserProfiles() {
		return idsForRequesterUserProfiles;
	}
	public void setIdsForRequesterUserProfiles(Collection<Long> idsForRequesterUserProfiles) {
		this.idsForRequesterUserProfiles = idsForRequesterUserProfiles;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
