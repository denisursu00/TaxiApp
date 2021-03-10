import { PermissionModel } from "./permission.model";
import { JsonObject, JsonProperty } from "json2typescript";
import { ObjectUtils } from "../utils/object-utils";

@JsonObject
export class FolderModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("path", String)
	public path: string = null;

	@JsonProperty("realPath", String)
	public realPath: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("folderId", String, true)
	public folderId: string = null;

	@JsonProperty("author", String)
	public author: string = null;

	@JsonProperty("entityType", String, true)
	public entityType: string = null;

	@JsonProperty("parentId", String)
	public parentId: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentTypeId", Number)
	public documentTypeId: number = null;

	@JsonProperty("permissions", [PermissionModel])
	public permissions: PermissionModel[] = null;

	public hasDocumentType(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.documentTypeId);
	}
}