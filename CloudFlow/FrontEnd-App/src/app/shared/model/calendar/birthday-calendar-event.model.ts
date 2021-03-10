import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationEntityModel } from "../organization-entity.model";
import { JsonDateConverter } from "../../json-mapper";
import { CalendarEventModel } from "./calendar-event.model";

@JsonObject
export class BirthdayCalendarEventModel extends CalendarEventModel {

	@JsonProperty("repeat", String)
	public repeat: string = null;
	
	@JsonProperty("birthdate", JsonDateConverter)
	public birthdate: Date = null;

	@JsonProperty("birthdateEvent", JsonDateConverter)
	public birthdateEvent: Date = null;
}