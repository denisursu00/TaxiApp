package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DateMetadataDefinitionModel extends MetadataDefinitionModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_AUTO_COMPLETE_WITH_CURRENT_DATE = "autoCompleteWithCurrentDate";
	
	public boolean isAutoCompleteWithCurrentDate() {
		Boolean autoCompleteWithCurrentDate = get(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_DATE);
		return GwtBooleanUtils.isTrue(autoCompleteWithCurrentDate);
	}
	
	public void setAutoCompleteWithCurrentDate(boolean autoCompleteWithCurrentDate) {
		set(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_DATE, autoCompleteWithCurrentDate);
	}
}