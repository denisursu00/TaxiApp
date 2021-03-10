import { PermissionModel } from "./permission.model";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentLocationModel {

	@JsonProperty("id", String, true)
	public id: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("path", String, true)
	public path: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("folderId", String, true)
	public folderId: string = null;

	@JsonProperty("documentLocationRealName", String, true)
	public documentLocationRealName: string = null;

	@JsonProperty("realName", String)
	public realName: string = null;

	@JsonProperty("entityType", String, true)
	public entityType: string = null;

	@JsonProperty("permissions", [PermissionModel])
	public permissions: PermissionModel[] = [];
	
	@JsonProperty("realPath", String)
	public realPath: string = null;
}