package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class OrganizationModel extends BaseTreeModel implements ModelWithId, IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DISPLAY_NAME = "displayName";
	public static final String PROPERTY_MANAGER_ID = "managerId";
	
	public OrganizationModel() {
		super();
	}
	
	public OrganizationModel(String id, String name) {
		super();
		setId(id);
		setName(name);
	}
	
	public void setId(String id){
		set(PROPERTY_ID, id);
	}
	
	public String getId(){
		return (String)get(PROPERTY_ID);
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return (String)get(PROPERTY_NAME);
	}
	
	public void setDisplayName(String displayName){
		set(PROPERTY_DISPLAY_NAME, displayName);
	}
	
	public String getDisplayName(){
		return (String)get(PROPERTY_DISPLAY_NAME);
	}

	public void setManagerId(String managerId) {
		set(PROPERTY_MANAGER_ID, managerId);
	}
	
	public String getManagerId(){
		return (String)get(PROPERTY_MANAGER_ID);
	}

	@Override
	public String toString() {
		return ("Organizatie (" + getName() + ")");
	}
}