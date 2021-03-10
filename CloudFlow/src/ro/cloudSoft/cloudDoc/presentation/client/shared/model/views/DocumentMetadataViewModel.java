package ro.cloudSoft.cloudDoc.presentation.client.shared.model.views;

import com.extjs.gxt.ui.client.data.BaseModel;

public class DocumentMetadataViewModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_LABEL = "label";
	public static final String PROPERTY_VALUE = "value";
	
	public String getName() {
		return this.get(PROPERTY_NAME);
	}
	public void setName(String documentId) {
		this.set(PROPERTY_NAME, documentId);
	}
	public String getLabel() {
		return this.get(PROPERTY_LABEL);
	}
	public void setLabel(String documentId) {
		this.set(PROPERTY_LABEL, documentId);
	}
	public String getValue() {
		return this.get(PROPERTY_VALUE);
	}
	public void setValue(String documentId) {
		this.set(PROPERTY_VALUE, documentId);
	}
}