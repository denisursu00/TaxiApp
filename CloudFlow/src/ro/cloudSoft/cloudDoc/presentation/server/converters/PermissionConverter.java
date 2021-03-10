package ro.cloudSoft.cloudDoc.presentation.server.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;

public class PermissionConverter {
	
	public static PermissionModel getModelFromPermission(Permission permission) {
		PermissionModel permissionModel = new PermissionModel();
		
		permissionModel.setEntityId(permission.getEntityId());
		permissionModel.setEntityType(permission.getEntityType());
		permissionModel.setPermission(permission.getPermission());
		
		return permissionModel;
	}
	
	public static Permission getPermissionFromModel(PermissionModel permissionModel) {
		Permission permission = new Permission();
		
		permission.setEntityId(permissionModel.getEntityId());
		permission.setEntityType(permissionModel.getEntityType());
		permission.setPermission(permissionModel.getPermission());
		
		return permission;
	}

    public static List<PermissionModel> getPermissionModelList(Collection<Permission> permisiuni){
        List<PermissionModel> models = new ArrayList<PermissionModel>();
        for (Permission perm : permisiuni){
            PermissionModel model = new PermissionModel();
            model.setPermission(perm.getPermission());
            model.setEntityId(perm.getEntityId());
            model.setEntityType(perm.getEntityType());

            models.add(model);
        }
        return models;
    }
    public static List<Permission> getPermissionList(List<PermissionModel> models){
        List<Permission> permisiuni = new ArrayList<Permission>();
        for (PermissionModel permModel : models){
            Permission perm = new Permission();
            perm.setPermission((String)permModel.get(PermissionModel.PROPERTY_PERMISSION));
            perm.setEntityId((String)permModel.get(PermissionModel.PROPERTY_ENTITY_ID));
            perm.setEntityType((Integer)permModel.get(PermissionModel.PROPERTY_ENTITY_TYPE));

            permisiuni.add(perm);
        }
        return permisiuni;
    }
}
