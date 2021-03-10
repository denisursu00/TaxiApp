package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;


import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithDisplayName;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class OrganizationUnitModel extends BaseTreeModel implements ModelWithId, ModelWithDisplayName, IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_PARENT_ORGANIZATION_ID = "parentOrganizationId";
	public static final String PROPERTY_PARENT_ORGANIZATION_UNIT_ID = "parentOrganizationUnitId";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESCRIPTION = "description";
	public static final String PROPERTY_MANAGER_ID = "managerId";
	
	public OrganizationUnitModel() {
		super();
	}
	
	public OrganizationUnitModel(String id, String name) {
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
    
    public String getParentOrganizationId() {
    	return get(PROPERTY_PARENT_ORGANIZATION_ID);
    }
    
    public void setParentOrganizationId(String parentOrganizationId) {
    	set(PROPERTY_PARENT_ORGANIZATION_ID, parentOrganizationId);
    }
    
    public String getParentOrganizationUnitId() {
    	return get(PROPERTY_PARENT_ORGANIZATION_UNIT_ID);
    }
    
    public void setParentOrganizationUnitId(String parentOrganizationUnitId) {
    	set(PROPERTY_PARENT_ORGANIZATION_UNIT_ID, parentOrganizationUnitId);
    }
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return (String)get(PROPERTY_NAME);
	}
	
	@Override
	public String getDisplayName() {
		return getName();
	}
	
	public void setDescription(String description){
		set(PROPERTY_DESCRIPTION, description);
	}
	
	public String getDescription() {
		return (String)get(PROPERTY_DESCRIPTION);
	}
	
	public void setManagerId(String managerId) {
		set(PROPERTY_MANAGER_ID, managerId);
	}
	
	public String getManagerId(){
		return (String)get(PROPERTY_MANAGER_ID);
	}
	
	@Override
	public String toString() {
		return ("Unitate organizatorica (" + getName() + ")");
	}
}