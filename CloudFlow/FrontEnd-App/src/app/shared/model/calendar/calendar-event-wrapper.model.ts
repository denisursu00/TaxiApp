import { JsonObject, JsonProperty } from "json2typescript";
import { JsonCalendarEventConverter } from "../../json-converters";
import { CalendarEventModel } from "./calendar-event.model";

@JsonObject
export class CalendarEventWrapperModel {

	@JsonProperty("calendarEvent", JsonCalendarEventConverter)
	public calendarEvent: CalendarEventModel = null;
}