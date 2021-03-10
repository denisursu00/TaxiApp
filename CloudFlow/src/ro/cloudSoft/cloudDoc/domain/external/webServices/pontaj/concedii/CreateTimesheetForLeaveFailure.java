package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * 
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "failureType")
@JsonSubTypes({
    @Type(name = "personNotFound", value = CreateTimesheetForLeavePersonNotFoundFailure.class),
    @Type(name = "projectNotFound", value = CreateTimesheetForLeaveProjectNotFoundFailure.class),
    @Type(name = "taskNotFound", value = CreateTimesheetForLeaveTaskNotFoundFailure.class),
    @Type(name = "timesheetsExist", value = CreateTimesheetForLeaveTimesheetsExistFailure.class)
})
public abstract class CreateTimesheetForLeaveFailure {

    protected String requesterEmail;
    protected String requesterDepartmentName;
    protected Date leaveDay;

    public String getRequesterEmail() {
        return requesterEmail;
    }
    
    public String getRequesterDepartmentName() {
		return requesterDepartmentName;
	}

    public Date getLeaveDay() {
        return leaveDay;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }
    
    public void setRequesterDepartmentName(String requesterDepartmentName) {
		this.requesterDepartmentName = requesterDepartmentName;
	}

    public void setLeaveDay(Date leaveDay) {
        this.leaveDay = leaveDay;
    }
}