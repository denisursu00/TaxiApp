package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GroupModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESCRIPTION = "description";
	public static final String PROPERTY_USERS = "users";
	
	public GroupModel() {}

    public GroupModel(String id, String name) {
        super();
        setId(id);
        setName(name);
    }
    
    public void setId(String id){
    	set(PROPERTY_ID,id);
    }
    
	public String getId() {
		return get(PROPERTY_ID);
	}
	
	public void setName(String name){
		set(PROPERTY_NAME,name);
	}
	
	public String getName() {
		return get(PROPERTY_NAME);
	}
	
	public void setDescription(String description){
		set(PROPERTY_DESCRIPTION,description);
	}
	
	public String getDescription() {
		return get(PROPERTY_DESCRIPTION);
	}
	
	public List<UserModel> getUsers() {
		return get(PROPERTY_USERS);
	}
	
	public void setUsers(List<UserModel> users) {
		set(PROPERTY_USERS, users);
	}
}