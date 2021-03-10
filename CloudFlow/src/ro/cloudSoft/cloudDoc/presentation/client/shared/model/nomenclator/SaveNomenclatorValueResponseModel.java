package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;

public class SaveNomenclatorValueResponseModel {

	private SaveNomenclatorValueResponseStatus status;
	private List<String> messages;
	
	public static enum SaveNomenclatorValueResponseStatus {
		SUCCESS,
		ERROR
	}
	
	public SaveNomenclatorValueResponseStatus getStatus() {
		return status;
	}
	
	public void setStatus(SaveNomenclatorValueResponseStatus status) {
		this.status = status;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}



