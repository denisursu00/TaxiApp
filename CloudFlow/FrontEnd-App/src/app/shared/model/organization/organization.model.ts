import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class OrganizationModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("displayName", String)
	public displayName: string = null;

	@JsonProperty("managerId", String)
	public managerId: string = null;
}