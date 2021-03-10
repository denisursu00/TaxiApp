package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

public class RolePermissionMappingViewModel {
	
	private String role;
	private String permissionGroup;
	private String permission;
	private String description;
	
	public RolePermissionMappingViewModel() {
	}
	
	public RolePermissionMappingViewModel(String role, String permissionGroup, String permission, String description) {
		this.role = role;
		this.permissionGroup = permissionGroup;
		this.permission = permission;
		this.description = description;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPermissionGroup() {
		return permissionGroup;
	}
	public void setPermissionGroup(String permissionGroup) {
		this.permissionGroup = permissionGroup;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
