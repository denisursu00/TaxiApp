import { JsonObject, JsonProperty } from "json2typescript";
import { StringUtils } from "./../../utils/string-utils";
import { RoleModel } from "./role.model";

@JsonObject
export class UserModel {

	@JsonProperty("userId", String)
	public userId: string = null;

	@JsonProperty("organizationId", String)
	public organizationId: string = null;

	@JsonProperty("organizationUnitId", String)
	public organizationUnitId: string = null;

	@JsonProperty("userName", String)
	public userName: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("firstName", String)
	public firstName: string = null;

	@JsonProperty("lastName", String)
	public lastName: string = null;

	@JsonProperty("password", String)
	public password: string = null;

	@JsonProperty("title", String)
	public title: string = null;

	@JsonProperty("customTitleTemplate", String)
	public customTitleTemplate: string = null;

	@JsonProperty("phone", String)
	public phone: string = null;

	@JsonProperty("fax", String)
	public fax: string = null;

	@JsonProperty("mobile", String)
	public mobile: string = null;

	@JsonProperty("email", String)
	public email: string = null;

	@JsonProperty("substituteId", String)
	public substituteId: string = null;

	@JsonProperty("isManager", Boolean, true)
	public isManager: boolean = false;

	@JsonProperty("employeeNumber", String)
	public employeeNumber: string = null;

	@JsonProperty("type", String)
	public type: string = null;

	public get displayName(): string {	
		if (StringUtils.isNotBlank(this.title)) {
			if (StringUtils.isNotBlank(this.customTitleTemplate)) {
				return this.getUserNameWithTitle(this.name, this.getUserTitleByTemplate(this.customTitleTemplate, this.title));
			} else {
				return this.getUserNameWithTitle(this.name, this.title);
			}
		} else {
			return this.name;
		}
	}

	private getUserNameWithTitle(name: string, title: string): string {
		return name + " - " + title;
	}

	private getUserTitleByTemplate(template: string, title: string): string {
		return template.replace("{title}", title);
	}
	@JsonProperty("roles", [RoleModel])
	public roles: RoleModel[] = [];
}

export enum UserTypeEnum {
	PERSON = "PERSON",
	SIMPLE_USER = "SIMPLE_USER",
	HIDDEN = "HIDDEN"
}