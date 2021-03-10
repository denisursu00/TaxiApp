import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { CalendarEventModel } from "./calendar-event.model";

@JsonObject
export abstract class IntervalCalendarEvent extends CalendarEventModel {
	
	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;
	
	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;
}