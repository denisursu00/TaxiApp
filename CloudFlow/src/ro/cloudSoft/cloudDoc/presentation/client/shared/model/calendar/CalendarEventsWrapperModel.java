package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.List;

public class CalendarEventsWrapperModel {
	
	private List<CalendarEventModel> calendarEvents;

	public List<CalendarEventModel> getCalendarEvents() {
		return calendarEvents;
	}

	public void setCalendarEvents(List<CalendarEventModel> calendarEvents) {
		this.calendarEvents = calendarEvents;
	}
}