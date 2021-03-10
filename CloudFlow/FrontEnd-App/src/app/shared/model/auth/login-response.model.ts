import { JsonObject, JsonProperty } from "json2typescript";
import { LoggedInUserModel } from "./logged-in-user.model";

@JsonObject
export class LoginResponseModel {

	@JsonProperty("authToken", String)
	public authToken: string = null;

	@JsonProperty("loggedInUser", LoggedInUserModel)
	public loggedInUser: LoggedInUserModel = null;
}