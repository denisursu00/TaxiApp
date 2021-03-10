import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class OrganizationEntityModel {

	public static readonly TYPE_USER: number = 1;
	public static readonly TYPE_ORG_UNIT: number = 2;
	public static readonly TYPE_GROUP: number = 3;

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("type", Number)
	public type: number = null;

	public isUserType(): boolean {
		return this.type === OrganizationEntityModel.TYPE_USER;
	}

	public isOrganizationUnitType(): boolean {
		return this.type === OrganizationEntityModel.TYPE_ORG_UNIT;
	}

	public isGroupType(): boolean {
		return this.type === OrganizationEntityModel.TYPE_GROUP;
	}
}