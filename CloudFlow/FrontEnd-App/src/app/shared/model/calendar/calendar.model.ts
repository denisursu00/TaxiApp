import { JsonObject, JsonProperty } from "json2typescript";
import { CalendarUserRightsModel } from "./calendar-user-rights.model";

@JsonObject
export class CalendarModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("color", String)
	public color: string = null;

	@JsonProperty("usersRights", [CalendarUserRightsModel])
	public usersRights: CalendarUserRightsModel[] = [];

	@JsonProperty("permitAll", Boolean)
	public permitAll: boolean = false;
}