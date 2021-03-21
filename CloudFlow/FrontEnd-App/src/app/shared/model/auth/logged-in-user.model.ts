import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class LoggedInUserModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("username", String)
	public username: string = null;
	
	@JsonProperty("firstName", String)
	public firstName: string = null;

	@JsonProperty("lastName", String)
	public lastName: string = null;

	@JsonProperty("permissions", [String])
	public permissions: string[] = [];
}