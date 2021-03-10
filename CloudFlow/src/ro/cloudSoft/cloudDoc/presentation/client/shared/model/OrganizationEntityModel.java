package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class OrganizationEntityModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = -2972290613194657141L;
	
	public static final int TYPE_USER = 1;
	public static final int TYPE_ORG_UNIT = 2;
	public static final int TYPE_GROUP = 3;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_TYPE = "type";

	public OrganizationEntityModel()
	{
	    
	}
	public Long getId() {
		return get(PROPERTY_ID);
	}
	
	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}
	
	public Integer getType() {
		return get(PROPERTY_TYPE);
	}
	
	public void setType(Integer type) {
		set(PROPERTY_TYPE, type);
	}
}