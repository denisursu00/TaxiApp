package ro.taxiApp.docs.services;

import ro.taxiApp.docs.domain.mail.EmailMessage;

public interface MailService {
	
	void send(EmailMessage emailMessage);
}