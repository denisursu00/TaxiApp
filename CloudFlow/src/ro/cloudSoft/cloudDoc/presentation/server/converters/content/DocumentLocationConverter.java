package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.PermissionConverter;

public class DocumentLocationConverter {

	public static DocumentLocationModel getModelFromDocumentLocation(DocumentLocation documentLocation) {
		DocumentLocationModel documentLocationModel = new DocumentLocationModel();
		
		documentLocationModel.setRealName(documentLocation.getRealName());
		documentLocationModel.setName(documentLocation.getName());
		documentLocationModel.setDescription(documentLocation.getDescription());		

		List<PermissionModel> permissionModels = new ArrayList<PermissionModel>();
		if (documentLocation.getAcl() != null) {
			for (Permission permission : documentLocation.getAcl().getPermissions()) {
				PermissionModel permissionModel = PermissionConverter.getModelFromPermission(permission);
				permissionModels.add(permissionModel);
			}
		}
		documentLocationModel.setPermissions(permissionModels);
		
		return documentLocationModel;
	}
	
	public static DocumentLocation getDocLocationFromModel(DocumentLocationModel documentLocationModel) {
		DocumentLocation documentLocation = new DocumentLocation();
		
		documentLocation.setRealName(documentLocationModel.getRealName());
		documentLocation.setName(documentLocationModel.getName());
		documentLocation.setDescription(documentLocationModel.getDescription());
		
		List<Permission> permissions = new ArrayList<Permission>();
		for (PermissionModel permissionModel : documentLocationModel.getPermissions()) {
			Permission permission = PermissionConverter.getPermissionFromModel(permissionModel);
			permissions.add(permission);
		}
		ACL acl = new ACL();
		acl.setPermissions(permissions);
		documentLocation.setAcl(acl);
		
		return documentLocation;
	}
}