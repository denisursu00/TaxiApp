package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MonthMetadataDefinitionModel extends MetadataDefinitionModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_AUTO_COMPLETE_WITH_CURRENT_MONTH = "autoCompleteWithCurrentMonth";
	
	public boolean isAutoCompleteWithCurrentMonth() {
		Boolean autoCompleteWithCurrentMonth = get(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_MONTH);
		return GwtBooleanUtils.isTrue(autoCompleteWithCurrentMonth);
	}
	
	public void setAutoCompleteWithCurrentMonth(boolean autoCompleteWithCurrentMonth) {
		set(PROPERTY_AUTO_COMPLETE_WITH_CURRENT_MONTH, autoCompleteWithCurrentMonth);
	}
}