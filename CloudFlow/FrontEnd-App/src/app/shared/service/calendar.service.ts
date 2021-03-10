import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AppError, CalendarModel, GetCalendarsEventsRequestModel, CalendarEventModel } from "../model";
import { ApiPathConstants } from "../constants";
import { AsyncCallback } from "../async-callback";
import { ApiPathUtils } from "../utils";
import { CalendarEventsWrapperModel } from "../model/calendar/calendar-events-wrapper.model";
import { CalendarEventWrapperModel } from "../model/calendar/calendar-event-wrapper.model";

@Injectable()
export class CalendarService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}
	
	public getAllCalendars(callback: AsyncCallback<CalendarModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_CALENDARS, null, CalendarModel, callback);
	}
	
	public getCalendar(calendarId: number, callback: AsyncCallback<CalendarModel, AppError>): void {
		let getCalendarResourcePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_CALENDAR, calendarId.toString());
		this.apiCaller.call(getCalendarResourcePath, null, CalendarModel, callback);
	}
	
	public saveCalendar(calendar: CalendarModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_CALENDAR, calendar, null, callback);
	}
	
	public deleteCalendar(calendarId: number, callback: AsyncCallback<null, AppError>): void {
		let deleteCalendarResourcePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_CALENDAR, calendarId.toString());
		this.apiCaller.call(deleteCalendarResourcePath, null, null, callback);
	}

	public getAllCalendarsForUser(userId: number, callback: AsyncCallback<CalendarModel[], AppError>): void {
		let getAllCalendarsForUserResourcePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_CALENDARS_FOR_USER, userId.toString());
		this.apiCaller.call(getAllCalendarsForUserResourcePath, null, CalendarModel, callback);
	}

	public getCalendarsEvents(requestModel: GetCalendarsEventsRequestModel, callback: AsyncCallback<CalendarEventsWrapperModel , AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_CALENDARS_EVENTS, requestModel, CalendarEventsWrapperModel, callback);
	}

	public saveEvent(calendarEventWrapperModel: CalendarEventWrapperModel, callback: AsyncCallback<null , AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_EVENT, calendarEventWrapperModel, null, callback);
	}

	public getCalendarEvent(eventId: number, callback: AsyncCallback<CalendarEventWrapperModel, AppError>): void {
		let getCalendarEventResourcePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_CALENDAR_EVENT, eventId.toString());
		this.apiCaller.call(getCalendarEventResourcePath, null, CalendarEventWrapperModel, callback);
	}

	public deleteEvent(eventId: Number, callback: AsyncCallback<null , AppError>): void {
		let deleteEventResourcePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_EVENT, eventId.toString());
		this.apiCaller.call(deleteEventResourcePath, null, null, callback);
	}
}