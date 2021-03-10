package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DateTimeMetadataDefinitionModel extends MetadataDefinitionModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	private boolean autoCompleteWithCurrentDateTime;
	
	public boolean isAutoCompleteWithCurrentDateTime() {
		return autoCompleteWithCurrentDateTime;
	}
	
	public void setAutoCompleteWithCurrentDateTime(boolean autoCompleteWithCurrentDateTime) {
		this.autoCompleteWithCurrentDateTime = autoCompleteWithCurrentDateTime;
	}
}