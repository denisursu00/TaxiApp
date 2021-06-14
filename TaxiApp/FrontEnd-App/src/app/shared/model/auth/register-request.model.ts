import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RegisterRequestModel {

	@JsonProperty("username", String)
	public username: string = null;

	@JsonProperty("password", String)
	public password: string = null;

	@JsonProperty("firstName", String)
	public firstName: string = null;

	@JsonProperty("lastName", String)
	public lastName: string = null;

	@JsonProperty("email", String)
	public email: string = null;

	@JsonProperty("mobile", String)
	public mobile: string = null;
    
}