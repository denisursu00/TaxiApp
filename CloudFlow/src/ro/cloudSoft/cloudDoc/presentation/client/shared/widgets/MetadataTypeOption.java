package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class MetadataTypeOption extends BaseModelData {

	private static final long serialVersionUID = -4948465545516609445L;
	
	public static final String PROPERTY_VALUE = "value";
	public static final String PROPERTY_LABEL = "label";
	
	public MetadataTypeOption(String value, String label) {
		set(PROPERTY_VALUE, value);
		set(PROPERTY_LABEL, label);
	}
	
	public String getValue() {
		return get(PROPERTY_VALUE);
	}
}