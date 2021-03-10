import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export abstract class CalendarEventModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("subject", String)
	public subject: string = null;

	@JsonProperty("description", String)
	public description: string = null;
	
	@JsonProperty("calendarId", Number)
	public calendarId: number = null;
	
	@JsonProperty("color", String)
	public color: string = null;

	@JsonProperty("type", String)
	public type: string = null;
	
	@JsonProperty("allDay", Boolean)
	public allDay: boolean = null;

}

export enum CalendarEventType {

	BIRTHDAY = "BIRTHDAY",
	MEETING = "MEETING",
	AUDIT = "AUDIT",
	HOLIDAY = "HOLIDAY"
}

export enum CalendarRepeatEvent {

	DAY = "DAY",
	WEEK = "WEEK",
	MOUNTH = "MOUNTH",
	YEAR = "YEAR"
}