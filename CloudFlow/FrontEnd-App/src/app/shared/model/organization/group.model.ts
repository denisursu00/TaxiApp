import { JsonObject, JsonProperty } from "json2typescript";
import { UserModel } from "./user.model";

@JsonObject
export class GroupModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("users", [UserModel])
	public users: UserModel[] = null;
}