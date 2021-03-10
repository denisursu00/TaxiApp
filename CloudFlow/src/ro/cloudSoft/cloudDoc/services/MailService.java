package ro.cloudSoft.cloudDoc.services;

import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;

public interface MailService {
	
	void send(EmailMessage emailMessage);
}