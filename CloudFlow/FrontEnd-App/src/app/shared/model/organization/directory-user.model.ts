import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DirectoryUserModel {

	@JsonProperty("username", String)
	public username: string = null;

	@JsonProperty("firstName", String)
	public firstName: string = null;
	
	@JsonProperty("lastName", String)
	public lastName: string = null;
	
	@JsonProperty("email", String)
	public email: string = null;
	
	@JsonProperty("title", String)
	public title: string = null;
	
	@JsonProperty("employeeNumber", String)
	public employeeNumber: string = null;

	public organizationId: string = null;
	public organizationUnitId: string = null;
}