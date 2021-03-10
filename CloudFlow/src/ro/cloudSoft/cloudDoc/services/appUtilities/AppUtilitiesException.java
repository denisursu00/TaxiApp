package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.ArrayList;
import java.util.List;

public class AppUtilitiesException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessages;
	
	public AppUtilitiesException(String errorMessage) {
		this.errorMessages = new ArrayList<>();
		this.errorMessages.add(errorMessage);
	}
	
	public AppUtilitiesException(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public List<String> getErrorMessages() {
		return errorMessages;
	}
}
