package ro.taxiApp.docs.core;

/**
 * 
 */
public enum AppExceptionCodes {
	
	APPLICATION_ERROR,
	USER_EXISTS,
	
	PARAMETER_VALUE_CANNOT_BE_NULL,
	PARAMETER_VALUE_IS_NOT_OF_TYPE_NUMBER,
	PARAMETER_VALUE_IS_NOT_OF_TYPE_DATE,
	PARAMETER_VALUE_IS_NOT_OF_TYPE_BOOLEAN,
	PARAMETER_NOT_FOUND,
	
	BAD_CREDENTIALS,
	INVALID_PASSWORD,
	AUTHENTICATION_EXCEPTION,
	UNAUTHORIZED
}