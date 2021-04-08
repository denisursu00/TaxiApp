import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";
import { RoleModel } from "./role.model";

@JsonObject
export class UserModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("firstName", String)
	public firstName: string = null;

    @JsonProperty("lastName", String)
	public lastName: string = null;

    @JsonProperty("password", String)
	public password: string = null;

    @JsonProperty("username", String)
	public username: string = null;

    @JsonProperty("email", String)
	public email: string = null;

    @JsonProperty("mobile", String)
	public mobile: string = null;

    @JsonProperty("roles", [RoleModel])
	public roles: RoleModel = null;

}