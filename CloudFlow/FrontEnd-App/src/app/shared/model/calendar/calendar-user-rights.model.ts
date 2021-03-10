import { OrganizationEntityModel } from "../organization-entity.model";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CalendarUserRightsModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("calendarId", Number)
	public calendarId: number = null;

	@JsonProperty("user", OrganizationEntityModel)
	public user: OrganizationEntityModel = null;

	@JsonProperty("view", Boolean)
	public view: boolean = false;

	@JsonProperty("add", Boolean)
	public add: boolean = false;

	@JsonProperty("edit", Boolean)
	public edit: boolean = false;

	@JsonProperty("delete", Boolean)
	public delete: boolean = false;
}