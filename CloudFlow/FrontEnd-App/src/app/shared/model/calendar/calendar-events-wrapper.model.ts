import { JsonObject, JsonProperty } from "json2typescript";
import { JsonCalendarEventsConverter } from "../../json-converters";
import { CalendarEventModel } from "./calendar-event.model";

@JsonObject
export class CalendarEventsWrapperModel {

	@JsonProperty("calendarEvents", JsonCalendarEventsConverter)
	public calendarEvents: CalendarEventModel[] = [];
}