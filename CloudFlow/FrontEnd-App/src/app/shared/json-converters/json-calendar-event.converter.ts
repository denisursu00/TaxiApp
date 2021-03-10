import { JsonConverter, JsonCustomConvert } from "json2typescript";
import { CalendarEventModel, BirthdayCalendarEventModel, MeetingCalendarEventModel, HolidayCalendarEventModel, CalendarEventType, CalendarEventFactory } from "../model";
import { ArrayUtils, ObjectUtils } from "../utils";
import { OrganizationEntityModel } from "../model/organization-entity.model";
import { AuditCalendarEventModel } from "../model/calendar/audit-calendar-event.model";

@JsonConverter
export class JsonCalendarEventConverter implements JsonCustomConvert<CalendarEventModel> {
	
	public serialize(event: CalendarEventModel): CalendarEventModel {
		if (ObjectUtils.isNullOrUndefined(event)) {
			return null;
		}

		let calendarEventFactory: CalendarEventFactory = new CalendarEventFactory();
		let eventModel: CalendarEventModel = calendarEventFactory.makeEvent(event.type);
		
		this.setCommonPropertiesToSerializedEvent(event, eventModel);
		this.setSpecificPropertiesToSerializedEvent(event, eventModel);
		
		return eventModel;
	}

	private setCommonPropertiesToSerializedEvent(eventForSerialization: CalendarEventModel, serializedEvent: CalendarEventModel): void {
		serializedEvent.id = eventForSerialization.id;
		serializedEvent.subject = eventForSerialization.subject;
		serializedEvent.description = eventForSerialization.description;
		serializedEvent.calendarId = eventForSerialization.calendarId;
		serializedEvent.color = eventForSerialization.color;
		serializedEvent.type = eventForSerialization.type;
		serializedEvent.allDay = eventForSerialization.allDay;
	}

	private setSpecificPropertiesToSerializedEvent(eventForSerialization: CalendarEventModel, serializedEvent: CalendarEventModel): void {
		if (eventForSerialization.type === CalendarEventType.BIRTHDAY) {
			(<BirthdayCalendarEventModel>serializedEvent).repeat = (<BirthdayCalendarEventModel>eventForSerialization).repeat;
			(<BirthdayCalendarEventModel>serializedEvent).birthdate = (<BirthdayCalendarEventModel>eventForSerialization).birthdate;
		} else if (eventForSerialization.type === CalendarEventType.MEETING) {
			(<MeetingCalendarEventModel>serializedEvent).startDate = (<MeetingCalendarEventModel>eventForSerialization).startDate;
			(<MeetingCalendarEventModel>serializedEvent).endDate = (<MeetingCalendarEventModel>eventForSerialization).endDate;
			(<MeetingCalendarEventModel>serializedEvent).location = (<MeetingCalendarEventModel>eventForSerialization).location;
			(<MeetingCalendarEventModel>serializedEvent).reminderMinutes = (<MeetingCalendarEventModel>eventForSerialization).reminderMinutes;
			(<MeetingCalendarEventModel>serializedEvent).repeat = (<MeetingCalendarEventModel>eventForSerialization).repeat;
			(<MeetingCalendarEventModel>serializedEvent).attendees = (<MeetingCalendarEventModel>eventForSerialization).attendees;
			(<MeetingCalendarEventModel>serializedEvent).reprezentantExtern = (<MeetingCalendarEventModel>eventForSerialization).reprezentantExtern;
		} else if (eventForSerialization.type === CalendarEventType.AUDIT) {
			(<AuditCalendarEventModel>serializedEvent).startDate = (<MeetingCalendarEventModel>eventForSerialization).startDate;
			(<AuditCalendarEventModel>serializedEvent).endDate = (<AuditCalendarEventModel>eventForSerialization).endDate;
			(<AuditCalendarEventModel>serializedEvent).location = (<AuditCalendarEventModel>eventForSerialization).location;
			(<AuditCalendarEventModel>serializedEvent).attendees = (<AuditCalendarEventModel>eventForSerialization).attendees;
		} else if (eventForSerialization.type === CalendarEventType.HOLIDAY) {
			(<HolidayCalendarEventModel>serializedEvent).startDate = (<HolidayCalendarEventModel>eventForSerialization).startDate;
			(<HolidayCalendarEventModel>serializedEvent).endDate = (<HolidayCalendarEventModel>eventForSerialization).endDate;
			(<HolidayCalendarEventModel>serializedEvent).userId =  (<HolidayCalendarEventModel>eventForSerialization).userId;
		}
	}

	public deserialize(eventInJsonFormat: any): CalendarEventModel {
		if (ObjectUtils.isNullOrUndefined(eventInJsonFormat)) {
			return null;
		}
		let calendarEventFactory: CalendarEventFactory = new CalendarEventFactory();
		let eventModel: CalendarEventModel = calendarEventFactory.makeEvent(eventInJsonFormat.type);

		this.setCommonPropertiesToDeserializedEvent(eventInJsonFormat, eventModel);
		this.setSpecificPropertiesToDeserializedEvent(eventInJsonFormat, eventModel);

		return eventModel;
	}

	private getAttendeesFromEventInJsonFormat(eventInJsonFormat: any): OrganizationEntityModel[] {
		let attendees: OrganizationEntityModel[] = [];
		eventInJsonFormat.attendees.forEach((attendee: any) => {
				let attendeeModel: OrganizationEntityModel = new OrganizationEntityModel();
				attendeeModel.id = attendee.id;
				attendeeModel.type = attendee.type;
				attendees.push(attendeeModel); 
		});
		return attendees;
	}

	private setCommonPropertiesToDeserializedEvent(eventInJsonFormat: any, deserializedEvent: CalendarEventModel): void {
		deserializedEvent.id = <number> eventInJsonFormat.id;
		deserializedEvent.subject = eventInJsonFormat.subject;
		deserializedEvent.description = eventInJsonFormat.description;
		deserializedEvent.calendarId = <number> eventInJsonFormat.calendarId;
		deserializedEvent.color = eventInJsonFormat.color;
		deserializedEvent.type = eventInJsonFormat.type;
		deserializedEvent.allDay = eventInJsonFormat.allDay;
	}

	private setSpecificPropertiesToDeserializedEvent(eventInJsonFormat: any, deserializedEvent: CalendarEventModel): void {
		if (eventInJsonFormat.type === CalendarEventType.BIRTHDAY) {
			(<BirthdayCalendarEventModel>deserializedEvent).repeat = eventInJsonFormat.repeat;
			(<BirthdayCalendarEventModel>deserializedEvent).birthdate = new Date(eventInJsonFormat.birthdate);
		} else if (eventInJsonFormat.type === CalendarEventType.MEETING) {
			(<MeetingCalendarEventModel>deserializedEvent).startDate = new Date(eventInJsonFormat.startDate);
			(<MeetingCalendarEventModel>deserializedEvent).endDate = new Date(eventInJsonFormat.endDate);
			(<MeetingCalendarEventModel>deserializedEvent).location = eventInJsonFormat.location;
			(<MeetingCalendarEventModel>deserializedEvent).reminderMinutes = <number>eventInJsonFormat.reminderMinutes;
			(<MeetingCalendarEventModel>deserializedEvent).repeat = eventInJsonFormat.repeat;
			(<MeetingCalendarEventModel>deserializedEvent).attendees = this.getAttendeesFromEventInJsonFormat(eventInJsonFormat);
			(<MeetingCalendarEventModel>deserializedEvent).reprezentantExtern = eventInJsonFormat.reprezentantExtern;
			(<MeetingCalendarEventModel>deserializedEvent).documentId = eventInJsonFormat.documentId;
			(<MeetingCalendarEventModel>deserializedEvent).documentLocationRealName = eventInJsonFormat.documentLocationRealName;
		} else if (eventInJsonFormat.type === CalendarEventType.AUDIT) {
			(<AuditCalendarEventModel>deserializedEvent).startDate = new Date(eventInJsonFormat.startDate);
			(<AuditCalendarEventModel>deserializedEvent).endDate = new Date(eventInJsonFormat.endDate);
			(<AuditCalendarEventModel>deserializedEvent).location = eventInJsonFormat.location;
			(<AuditCalendarEventModel>deserializedEvent).attendees = this.getAttendeesFromEventInJsonFormat(eventInJsonFormat);
			(<AuditCalendarEventModel>deserializedEvent).documentId = eventInJsonFormat.documentId;
			(<AuditCalendarEventModel>deserializedEvent).documentLocationRealName = eventInJsonFormat.documentLocationRealName;
		} else if (eventInJsonFormat.type === CalendarEventType.HOLIDAY) {
			(<HolidayCalendarEventModel>deserializedEvent).startDate = new Date(eventInJsonFormat.startDate);
			(<HolidayCalendarEventModel>deserializedEvent).endDate = new Date(eventInJsonFormat.endDate);
			(<HolidayCalendarEventModel>deserializedEvent).userId = <number> eventInJsonFormat.userId;
			(<HolidayCalendarEventModel>deserializedEvent).documentId = eventInJsonFormat.documentId;
			(<HolidayCalendarEventModel>deserializedEvent).documentLocationRealName = eventInJsonFormat.documentLocationRealName;
		}
	}
}