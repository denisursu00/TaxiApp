package ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii;

import java.util.List;

/**
 * 
 */
public class CreateTimesheetsForLeavesRequest {

	private String secretToken;
	private List<LeaveDayDetails> leaveDayDetailsList;

	public String getSecretToken() {
		return secretToken;
	}
	public List<LeaveDayDetails> getLeaveDayDetailsList() {
		return leaveDayDetailsList;
	}
	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

    public void setLeaveDayDetailsList(List<LeaveDayDetails> leaveDayDetailsList) {
        this.leaveDayDetailsList = leaveDayDetailsList;
	}
}