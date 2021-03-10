package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

public class DocumentValidationResponseModel {

	private boolean valid;
	private List<String> messages;
	
	public boolean isValid() {
		return valid;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}	
}
