package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

/**
 * 
 */
public class ApprovedTimesheetsForLeaveVerificationRequest {

	private String secretToken;
	private LeaveDetails leaveDetails;

	public String getSecretToken() {
		return secretToken;
	}
	public LeaveDetails getLeaveDetails() {
		return leaveDetails;
	}
	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}
	public void setLeaveDetails(LeaveDetails leaveDetails) {
		this.leaveDetails = leaveDetails;
	}
}