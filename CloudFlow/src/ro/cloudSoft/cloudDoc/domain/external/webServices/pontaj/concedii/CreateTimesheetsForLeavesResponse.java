package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import java.util.Collection;

/**
 * 
 */
public class CreateTimesheetsForLeavesResponse {

    private Collection<CreateTimesheetForLeaveFailure> failures;

    public Collection<CreateTimesheetForLeaveFailure> getFailures() {
        return failures;
    }

    public void setFailures(Collection<CreateTimesheetForLeaveFailure> failures) {
        this.failures = failures;
    }
}