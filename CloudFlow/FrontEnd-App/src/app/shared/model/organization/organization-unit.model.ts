import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class OrganizationUnitModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("parentOrganizationId", String)
	public parentOrganizationId: string = null;

	@JsonProperty("parentOrganizationUnitId", String)
	public parentOrganizationUnitId: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("managerId", String)
	public managerId: string = null;
}