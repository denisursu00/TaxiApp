package ro.cloudSoft.cloudDoc.domain.mail;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Objects;

public class EmailMessage {

	private List<String> toAddresses;
	
	private String subject;
	private String content;
	private boolean hasHtmlContent;
	
	public EmailMessage(String toAddress, String subject, String content, boolean hasHtmlContent) {
		this(Collections.singletonList(toAddress), subject, content, hasHtmlContent);
	}
	
	public EmailMessage(String toAddress, String subject, String content) {
		this(Collections.singletonList(toAddress), subject, content, false);
	}
	
	public EmailMessage(List<String> toAddresses, String subject, String content) {
		this(toAddresses, subject, content, false);
	}
	
	public EmailMessage(List<String> toAddresses, String subject, String content, boolean hasHtmlContent) {
		this.toAddresses = toAddresses;
		this.subject = subject;
		this.content = content;
		this.hasHtmlContent = hasHtmlContent;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("toAddresses", getToAddresses())
			.add("subject", subject)
			.add("content", content)
			.add("hasHtmlContent", hasHtmlContent)
			.toString();
	}
	
	public List<String> getToAddresses() {
		return toAddresses;
	}
	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isHasHtmlContent() {
		return hasHtmlContent;
	}
	public void setHasHtmlContent(boolean hasHtmlContent) {
		this.hasHtmlContent = hasHtmlContent;
	}
	
}