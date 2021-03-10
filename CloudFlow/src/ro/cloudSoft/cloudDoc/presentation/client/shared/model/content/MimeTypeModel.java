package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MimeTypeModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 4221110769420937592L;

	public static final String PROPERTY_ID = "id";
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_EXTENSION = "extension";
	public static final String PROPERTY_ALLOWED = "allowed";
	
	public MimeTypeModel() {}
	
	public void setId(Long id){
		set(PROPERTY_ID, id);
	}
	
	public Long getId(){
		return get(PROPERTY_ID); 
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return get(PROPERTY_NAME); 
	}
	
	public void setExtension(String extension){
		set(PROPERTY_EXTENSION, extension);
	}
	
	public String getExtension(){
		return get(PROPERTY_EXTENSION); 
	}
	
	public void setAllowed(Boolean allowed){
		if (allowed == null)
			set(PROPERTY_ALLOWED, new Boolean(false));
		else
			set(PROPERTY_ALLOWED, allowed);
	}
	
	public Boolean isAllowed(){
		return (Boolean)get(PROPERTY_ALLOWED); 
	}
	
	@Override
	public String toString() {
		return this.getName() + " (" + this.getExtension() + ")";
	}
}