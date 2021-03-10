import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class PermissionModel {

	public static readonly TYPE_USER: number = 1;
	public static readonly TYPE_ORGANIZATION_UNIT: number = 2;
	public static readonly TYPE_GROUP: number = 3;

	public static readonly PERMISSION_COORDINATOR: string = "1111";
	public static readonly PERMISSION_COLABORATOR: string = "1110";
	public static readonly PERMISSION_EDITOR: string = "1100";
	public static readonly PERMISSION_READER: string = "1000";

	@JsonProperty("entityId", String)
	public entityId: string = null;

	@JsonProperty("entityName", String, true)
	public entityName: string = null;

	@JsonProperty("entityType", Number)
	public entityType: number = null;

	@JsonProperty("permission", String)
	public permission: string = null;

	public isGroup(): boolean {
		return this.entityType === PermissionModel.TYPE_GROUP;
	}

	public isOrganizationUnit(): boolean {
		return this.entityType === PermissionModel.TYPE_ORGANIZATION_UNIT;
	}

	public isUser(): boolean {
		return this.entityType === PermissionModel.TYPE_USER;
	}
}
