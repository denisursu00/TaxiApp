package ro.taxiApp.docs.presentation.client.shared.model;

public class ErrorMessageModel {
	
	private String errorCode;
	private String errorDetails;
	
	public ErrorMessageModel() {
	}
	
	public ErrorMessageModel(String errorCode) {
		this.errorCode =  errorCode;
	}
	
	public ErrorMessageModel(String errorCode, String errorDetails) {
		this(errorCode);
		this.errorDetails = errorDetails;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDetails() {
		return errorDetails;
	}
	
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
}
