package ro.taxiApp.docs.spring.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.domain.mail.EmailMessage;
import ro.taxiApp.docs.services.MailService;

public class SpringMailService implements MailService, InitializingBean {
	
	private JavaMailSenderImpl mailSender;
	private String fromAddress;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			mailSender,
			fromAddress
		);
	}
	
	@Override
	public void send(EmailMessage emailMessage) {
		if (emailMessage.isHasHtmlContent()) {
			sendWithHtmlContent(emailMessage);
		} else {
			sendSimpleMail(emailMessage);
		}
	}
	
	private void sendSimpleMail(EmailMessage emailMessage) {
		SimpleMailMessage simpleMailMessage = SimpleMailMessageConverter.getSimpleMailMessage(this.fromAddress, emailMessage);
		
		this.mailSender.send(simpleMailMessage);
	}
	
	private void sendWithHtmlContent(EmailMessage emailMsg){
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		mimeMessage.setContent(emailMsg.getContent(), "text/html");
		helper.setTo(emailMsg.getToAddresses().toArray(new String[emailMsg.getToAddresses().size()]));
		helper.setSubject(emailMsg.getSubject());
		helper.setFrom(this.fromAddress);
		} catch (MessagingException e) {
			throw new RuntimeException(e.getMessage());
		}
		this.mailSender.send(mimeMessage);
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
}