package ro.taxiApp.docs.spring.services;

import org.springframework.mail.SimpleMailMessage;

import ro.taxiApp.docs.domain.mail.EmailMessage;

public class SimpleMailMessageConverter {

	public static SimpleMailMessage getSimpleMailMessage(String fromAddress, EmailMessage emailMessage) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setFrom(fromAddress);
		simpleMailMessage.setTo(emailMessage.getToAddresses().toArray(new String[emailMessage.getToAddresses().size()]));
		
		simpleMailMessage.setSubject(emailMessage.getSubject());
		simpleMailMessage.setText(emailMessage.getContent());
		
		return simpleMailMessage;
	}
}