package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Objects;

/**
 * 
 */
public class LeaveDetails {
	
	private String requesterEmail;
	private String requesterDepartmentName;
	
	private Date startDate;
	private Date endDate;
	
	public LeaveDetails() {}
	
	public LeaveDetails(String requesterEmail,
			String requesterDepartmentName,
			Date startDate, Date endDate) {
		
		this.requesterEmail = requesterEmail;
		this.requesterDepartmentName = requesterDepartmentName;
		
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getRequesterEmail() {
		return requesterEmail;
	}
	public String getRequesterDepartmentName() {
		return requesterDepartmentName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setRequesterEmail(String requesterEmail) {
		this.requesterEmail = requesterEmail;
	}
	public void setRequesterDepartmentName(String requesterDepartmentName) {
		this.requesterDepartmentName = requesterDepartmentName;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof LeaveDetails)) {
			return false;
		}
		
		LeaveDetails other = (LeaveDetails) obj;
		
		String requesterEmail = getRequesterEmail();
		String requesterEmailInLowerCase = StringUtils.lowerCase(requesterEmail);
		
		String otherRequesterEmail = other.getRequesterEmail();
		String otherRequesterEmailInLowerCase = StringUtils.lowerCase(otherRequesterEmail);
		
		return (
			Objects.equal(requesterEmailInLowerCase, otherRequesterEmailInLowerCase) &&
			Objects.equal(getRequesterDepartmentName(), other.getRequesterDepartmentName()) &&
			Objects.equal(getStartDate(), other.getStartDate()) &&
			Objects.equal(getEndDate(), other.getEndDate())
		);
	}
	
	@Override
	public int hashCode() {
		
		String requesterEmail = getRequesterEmail();
		String requesterEmailInLowerCase = StringUtils.lowerCase(requesterEmail);
		
		return Objects.hashCode(requesterEmailInLowerCase,
			getRequesterDepartmentName(), getStartDate(), getEndDate());
	}
}