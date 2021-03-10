import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class LoginRequestModel {

	@JsonProperty("username", String)
	public username: string = null;

	@JsonProperty("password", String)
	public password: string = null;

	@JsonProperty("rememberMe", Boolean)
	public rememberMe: boolean = null;
}