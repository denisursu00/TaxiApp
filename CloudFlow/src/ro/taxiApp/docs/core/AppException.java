package ro.taxiApp.docs.core;

import static ro.taxiApp.docs.core.AppExceptionCodes.APPLICATION_ERROR;

public class AppException extends Exception {

	private static final long serialVersionUID = 1L;

	private final AppExceptionCodes code;

	public AppException() {
		this(APPLICATION_ERROR);
	}

	public AppException(AppExceptionCodes code) {
		this.code = code;
	}
	
	public AppExceptionCodes getCode() {
		return code;
	}
	
	@Override
	public String getMessage() {
		return code.name();
	}
}