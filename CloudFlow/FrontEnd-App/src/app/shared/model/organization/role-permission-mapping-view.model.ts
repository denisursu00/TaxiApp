import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RolePermissionMappingViewModel {

	@JsonProperty("role", String)
	public role: string = null;

	@JsonProperty("permission", String)
	public permission: string = null;

	@JsonProperty("permissionGroup", String)
	public permissionGroup: string = null;

	@JsonProperty("description", String)
	public description: string = null;
}