package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * 
 */
@JsonTypeName("taskNotFound")
public class CreateTimesheetForLeaveTaskNotFoundFailure extends CreateTimesheetForLeaveFailure {

    private String projectName;
    private String requestedTaskName;

    public String getProjectName() {
        return projectName;
    }

    public String getRequestedTaskName() {
        return requestedTaskName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setRequestedTaskName(String requestedTaskName) {
        this.requestedTaskName = requestedTaskName;
    }
}