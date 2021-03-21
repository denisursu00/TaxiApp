package ro.taxiApp.docs.presentation.client.shared.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PermissionModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = -5564656544137688401L;
	
    public final static int TYPE_USER = 1;
    public final static int TYPE_ORGANIZATION_UNIT = 2;
    public final static int TYPE_GROUP = 3;
    
    public final static String PERMISSION_COORDINATOR = "1111";
    public final static String PERMISSION_COLABORATOR = "1110";
    public final static String PERMISSION_EDITOR = "1100";
    public final static String PERMISSION_READER = "1000";

    public static final String PROPERTY_ENTITY_ID = "entityId";
    public static final String PROPERTY_ENTITY_NAME = "entityName";
	public static final String PROPERTY_ENTITY_TYPE = "entityType";
    public static final String PROPERTY_PERMISSION = "permission";

    public PermissionModel() {}

    public PermissionModel(int entityType, String permission, String userId, String entityName) {
        this();
        setEntityType(entityType);
        setPermission(permission);
        setEntityId(userId);
        setEntityName(entityName);
    }

    public Integer getEntityType() {
        return get(PROPERTY_ENTITY_TYPE);
    }

    public void setEntityType(Integer entityType) {
        set(PROPERTY_ENTITY_TYPE, entityType);
    }

    public String getPermission() {
        return get(PROPERTY_PERMISSION);
    }

    public void setPermission(String permission) {
        set(PROPERTY_PERMISSION, permission);
    }

    public String getEntityId() {
        return get(PROPERTY_ENTITY_ID);
    }

    public void setEntityId(String entityId) {
        set(PROPERTY_ENTITY_ID, entityId);
    }

    public String getEntityName() {
        return (String) get(PROPERTY_ENTITY_NAME);
    }

    public void setEntityName(String entityName) {
        set(PROPERTY_ENTITY_NAME, entityName);
    }
}