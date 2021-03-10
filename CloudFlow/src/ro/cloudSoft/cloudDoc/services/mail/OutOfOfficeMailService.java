package ro.cloudSoft.cloudDoc.services.mail;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;

/**
 * 
 */
public interface OutOfOfficeMailService {

	void activateOutOfOffice(String emailAddress, Date startDate, Date endDate,
		String outOfOfficeEmailSubject, String outOfOfficeEmailBody) throws AppException;
	
	void deactivateOutOfOffice(String emailAddress) throws AppException;
}