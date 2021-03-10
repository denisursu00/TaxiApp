import { JsonConverter, JsonCustomConvert } from "json2typescript";
import { CalendarEventModel, BirthdayCalendarEventModel, MeetingCalendarEventModel, HolidayCalendarEventModel, CalendarEventType } from "../model";
import { ArrayUtils } from "../utils";
import { OrganizationEntityModel } from "../model/organization-entity.model";
import { AuditCalendarEventModel } from "../model/calendar/audit-calendar-event.model";
import { JsonCalendarEventConverter } from "./json-calendar-event.converter";

@JsonConverter
export class JsonCalendarEventsConverter implements JsonCustomConvert<CalendarEventModel[]> {
	
	private jsonCalendarEventConverter: JsonCalendarEventConverter;
	
	public constructor() {
		this.jsonCalendarEventConverter = new JsonCalendarEventConverter();
	}

	public serialize(events: CalendarEventModel[]): CalendarEventModel[] {
		if (ArrayUtils.isEmpty(events)) {
			return [];
		}

		let serializedEvents: CalendarEventModel[] = [];
		
		events.forEach((event: CalendarEventModel) => {
			let serializedEvent: CalendarEventModel = this.jsonCalendarEventConverter.serialize(event);
			serializedEvents.push(serializedEvent);
		});

		return serializedEvents;
	}

	public deserialize(eventsInJsonFormat: any[]): CalendarEventModel[] {
		if (ArrayUtils.isEmpty(eventsInJsonFormat)) {
			return [];
		}

		let deserializedEvents: CalendarEventModel[] = [];

		eventsInJsonFormat.forEach((eventInJsonFormat: any) => {
			let deserializedEvent: CalendarEventModel = this.jsonCalendarEventConverter.deserialize(eventInJsonFormat);
			deserializedEvents.push(deserializedEvent);
		});
		return deserializedEvents;
	}
}