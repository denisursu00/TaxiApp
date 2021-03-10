import { JsonObject, JsonProperty } from "json2typescript/src/json2typescript/json-convert-decorators";

@JsonObject
export class SecurityManagerModel {
	
	@JsonProperty("userIdAsString", String)
	public userIdAsString: string = null;

	@JsonProperty("userUsername", String)
	public userUsername: string = null;

	@JsonProperty("userTitle", String)
	public userTitle: string = null;

	@JsonProperty("organizationUnitIds", [Number])
	public organizationUnitIds: number[] = [];

	@JsonProperty("groupIds", [Number])
	public groupIds: number[] = [];

	@JsonProperty("groupNames", [String])
	public groupNames: string[] = [];
	
	@JsonProperty("userAdmin", Boolean)
	public userAdmin: boolean = false;
}