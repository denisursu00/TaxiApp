package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;

public abstract class ContentEntityModel extends BaseModel {

	private static final long serialVersionUID = 5252279832025310358L;
	
	public static final String CONTENT_ENTITY_TYPE_WORKSPACE = "workspace";
    public static final String CONTENT_ENTITY_TYPE_FOLDER = "folder";
    public static final String CONTENT_ENTITY_TYPE_DOCUMENT = "document";

    public static final String PROPERTY_PERMISSIONS = "permissions";

    /** realName in cazul docLocationului, id in cazul folder/document*/
    public abstract String getId();
    public abstract String getDocumentLocationRealName();
    public abstract String getFolderId();  // null in cazul wksp
    public abstract String getPath();
    public abstract String getName();
    public abstract String getEntityType();
    
    /** Override to implement desired behaviour*/
    public String getRealPath(){
        return null;
    }
    
    public List<PermissionModel> getPermissions() {
    	return get(PROPERTY_PERMISSIONS);
    }
    
    public void setPermissions(List<PermissionModel> permissions) {
    	set(PROPERTY_PERMISSIONS, permissions);
    }
}