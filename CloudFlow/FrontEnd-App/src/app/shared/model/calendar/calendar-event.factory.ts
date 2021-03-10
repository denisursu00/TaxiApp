import { CalendarEventType, CalendarEventModel } from "./calendar-event.model";
import { BirthdayCalendarEventModel } from "./birthday-calendar-event.model";
import { MeetingCalendarEventModel } from "./meeting-calendar-event.model";
import { AuditCalendarEventModel } from "./audit-calendar-event.model";
import { HolidayCalendarEventModel } from "./holiday-calendar-event.model";

export class CalendarEventFactory {

	public makeEvent(eventType: String): CalendarEventModel {
		if (eventType === CalendarEventType.BIRTHDAY) {
			return new BirthdayCalendarEventModel();
		} else if (eventType === CalendarEventType.MEETING) {
			return new MeetingCalendarEventModel();
		} else if (eventType === CalendarEventType.AUDIT) {
			return new AuditCalendarEventModel();
		} else if (eventType === CalendarEventType.HOLIDAY) {
			return new HolidayCalendarEventModel();
		}
		throw new Error("Unknown event type [" + eventType + "]");
	}
}