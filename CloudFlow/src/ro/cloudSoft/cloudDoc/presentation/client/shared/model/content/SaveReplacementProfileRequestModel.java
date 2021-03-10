package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModelStatusOption;

public class SaveReplacementProfileRequestModel {
	
	private Long id;
	private Long requesterId;
	private Long replacementUserId;
	private Collection<UserModel> selectedRequesterUserProfiles;
	private Set<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement;
	private String comments;
	private Date startDate;
	private Date endDate;
	private boolean outOfOffice;
	private String outOfOfficeEmailSubjectTemplate;
	private String outOfOfficeEmailBodyTemplate;
	private ReplacementProfileModelStatusOption status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}
	public Long getReplacementUserId() {
		return replacementUserId;
	}
	public void setReplacementUserId(Long replacementUserId) {
		this.replacementUserId = replacementUserId;
	}
	public Collection<UserModel> getSelectedRequesterUserProfiles() {
		return selectedRequesterUserProfiles;
	}
	public void setSelectedRequesterUserProfiles(Collection<UserModel> selectedRequesterUserProfiles) {
		this.selectedRequesterUserProfiles = selectedRequesterUserProfiles;
	}
	public Set<Long> getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement() {
		return idsForSelectedReplacementProfilesInWhichRequesterIsReplacement;
	}
	public void setIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(
			Set<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement) {
		this.idsForSelectedReplacementProfilesInWhichRequesterIsReplacement = idsForSelectedReplacementProfilesInWhichRequesterIsReplacement;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public boolean isOutOfOffice() {
		return outOfOffice;
	}
	public void setOutOfOffice(boolean outOfOffice) {
		this.outOfOffice = outOfOffice;
	}
	public String getOutOfOfficeEmailSubjectTemplate() {
		return outOfOfficeEmailSubjectTemplate;
	}
	public void setOutOfOfficeEmailSubjectTemplate(String outOfOfficeEmailSubjectTemplate) {
		this.outOfOfficeEmailSubjectTemplate = outOfOfficeEmailSubjectTemplate;
	}
	public String getOutOfOfficeEmailBodyTemplate() {
		return outOfOfficeEmailBodyTemplate;
	}
	public void setOutOfOfficeEmailBodyTemplate(String outOfOfficeEmailBodyTemplate) {
		this.outOfOfficeEmailBodyTemplate = outOfOfficeEmailBodyTemplate;
	}
	public ReplacementProfileModelStatusOption getStatus() {
		return status;
	}
	public void setStatus(ReplacementProfileModelStatusOption status) {
		this.status = status;
	}
}
