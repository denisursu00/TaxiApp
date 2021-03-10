package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AttachmentModel extends BaseModel {

	private static final long serialVersionUID = -3408227358030510405L;
	
	public static final String PROPERTY_NAME = "name";
	
	@SuppressWarnings("unused")
	private AttachmentModel() {}
	
	public AttachmentModel(String name) {
		setName(name);
	}
	
	public String getName() {
		return get(PROPERTY_NAME);
	}
	
	public void setName(String name) {
		set(PROPERTY_NAME, name);
	}
}