package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.PermissionConverter;

public class FolderConverter {

	public static FolderModel getModelFromFolder(Folder folder) {
		FolderModel folderModel = new FolderModel();
		
		folderModel.setId(folder.getId());
		folderModel.setName(folder.getName());
		folderModel.setDescription(folder.getDescription());
		// TODO Un folder trebuie sa aiba si numele workspace-ului
		//folderModel.setDocumentLocationRealName(folder.getDocumentLocationRealName());
		folderModel.setParentId(folder.getParentId());
		folderModel.setDocumentTypeId(folder.getDocumentTypeId());
		folderModel.setAuthor(folder.getAuthor());
		folderModel.setPath(folder.getPath());
		folderModel.setRealPath(folder.getRealPath());
		
		List<PermissionModel> permissionModels = new ArrayList<PermissionModel>();
		if ((folder.getAcl() != null) && (folder.getAcl().getPermissions() != null)) {
			for (Permission permission : folder.getAcl().getPermissions()) {
				PermissionModel permissionModel = PermissionConverter.getModelFromPermission(permission);
				permissionModels.add(permissionModel);
			}
		}
		folderModel.setPermissions(permissionModels);
		
		return folderModel;
	}
	
	public static Folder getFolderFromModel(FolderModel folderModel) {
		Folder folder = new Folder();
		
		folder.setId(folderModel.getId());
		folder.setName(folderModel.getName());
		folder.setDescription(folderModel.getDescription());
		// TODO Un folder trebuie sa aiba si numele workspace-ului
		//folder.setDocumentLocationRealName(folderModel.getDocumentLocationRealName());
		folder.setParentId(folderModel.getParentId());
		folder.setDocumentTypeId(folderModel.getDocumentTypeId());
		folder.setAuthor(folderModel.getAuthor());
		folder.setPath(folderModel.getPath());
		folder.setRealPath(folderModel.getRealPath());
		
		List<Permission> permissions = new ArrayList<Permission>();
		for (PermissionModel permissionModel : folderModel.getPermissions()) {
			Permission permission = PermissionConverter.getPermissionFromModel(permissionModel);
			permissions.add(permission);
		}
		ACL acl = new ACL();
		acl.setPermissions(permissions);
		folder.setAcl(acl);
		
		return folder;
	}
}