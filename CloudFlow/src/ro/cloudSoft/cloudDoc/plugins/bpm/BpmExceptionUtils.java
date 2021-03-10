package ro.cloudSoft.cloudDoc.plugins.bpm;

import org.jbpm.api.JbpmException;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.AppExceptionUtils;

public class BpmExceptionUtils {
	
	public static void handleJbpmException(Exception e) throws AppException {
		
		AppException applicationExceptionToThrow = new AppException(AppExceptionCodes.APPLICATION_ERROR);
		
		if (e instanceof JbpmException) {
			AppException applicationExceptionAsCause = AppExceptionUtils.getAppExceptionFromExceptionCause(e);
			if (applicationExceptionAsCause != null) {
				applicationExceptionToThrow = applicationExceptionAsCause;
			}
		} else if (e instanceof AppException) {
			applicationExceptionToThrow = (AppException) e;
		}
		
		throw applicationExceptionToThrow;
	}
}