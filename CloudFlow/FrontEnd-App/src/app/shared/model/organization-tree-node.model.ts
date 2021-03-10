import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class OrganizationTreeNodeModel {

	@JsonProperty()
	public id: string = null;

	@JsonProperty()
	public parentId: string = null;

	@JsonProperty()
	public name: string = null;

	@JsonProperty()
	public type: OrganizationTreeNodeType = null;

	@JsonProperty("managerId", String, true)
	public managerId: string = null;

	@JsonProperty("title", String, true)
	public title: string = null;

	@JsonProperty("description", String, true)
	public description: string = null;

	@JsonProperty("customTitleTemplate", String, true)
	public customTitleTemplate: string = null;

	@JsonProperty("children", [OrganizationTreeNodeModel])
	public children: OrganizationTreeNodeModel[] = [];

	public isOrganization(): boolean {
		return this.type === OrganizationTreeNodeType.ORGANIZATION;
	}

	public isOrganizationUnit(): boolean {
		return this.type === OrganizationTreeNodeType.ORGANIZATION_UNIT;
	}

	public isUser(): boolean {
		return this.type === OrganizationTreeNodeType.USER;
	}
}

export enum OrganizationTreeNodeType {
	ORGANIZATION = "ORGANIZATION",
	ORGANIZATION_UNIT = "ORGANIZATION_UNIT",
	USER = "USER"
}