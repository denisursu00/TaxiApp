package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Objects;

/**
 * 
 */
public class LeaveDayDetails {

	private String requesterEmail;
	private String requesterDepartmentName;

    private Date day;
    
    public LeaveDayDetails() {}

    public LeaveDayDetails(String requesterEmail, String requesterDepartmentName, Date day) {
		this.requesterEmail = requesterEmail;
		this.requesterDepartmentName = requesterDepartmentName;
		this.day = day;
	}

	public String getRequesterEmail() {
        return requesterEmail;
    }

    public String getRequesterDepartmentName() {
        return requesterDepartmentName;
    }

    public Date getDay() {
        return day;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public void setRequesterDepartmentName(String requesterDepartmentName) {
        this.requesterDepartmentName = requesterDepartmentName;
    }

    public void setDay(Date day) {
        this.day = day;
    }
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof LeaveDayDetails)) {
			return false;
		}
		
		LeaveDayDetails other = (LeaveDayDetails) obj;
		
		String requesterEmail = getRequesterEmail();
		String requesterEmailInLowerCase = StringUtils.lowerCase(requesterEmail);
		
		String otherRequesterEmail = other.getRequesterEmail();
		String otherRequesterEmailInLowerCase = StringUtils.lowerCase(otherRequesterEmail);
		
		return (
			Objects.equal(requesterEmailInLowerCase, otherRequesterEmailInLowerCase) &&
			Objects.equal(getRequesterDepartmentName(), other.getRequesterDepartmentName()) &&
			Objects.equal(getDay(), other.getDay())
		);
	}
	
	@Override
	public int hashCode() {
		
		String requesterEmail = getRequesterEmail();
		String requesterEmailInLowerCase = StringUtils.lowerCase(requesterEmail);
		
		return Objects.hashCode(requesterEmailInLowerCase, getRequesterDepartmentName(), getDay());
	}
}