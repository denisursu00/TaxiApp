package ro.cloudSoft.cloudDoc.webServices.client;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDayDetails;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDetails;

/**
 * 
 */
public interface TimesheetsForLeavesWebServiceClient {

	Collection<CreateTimesheetForLeaveFailure> createTimesheets(
		List<LeaveDayDetails> leaveDayDetailsList) throws AppException;
	
	boolean hasApprovedTimesheets(LeaveDetails leaveDetails) throws AppException;
}