package ro.cloudSoft.cloudDoc.dao.calendar;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;

public interface CalendarEventDao {

	Long saveEvent(CalendarEvent event);
	
	CalendarEvent getEvent(Long calendarId, Date startDate, Date endDate);
	
	CalendarEvent find(Long eventId); 
	
	boolean canEventBeDeleted(Long eventId);
	
	void delete(CalendarEvent calendarEvent);
	
	CalendarEvent getEvent(Long calendarId, Date startDate, Date endDate, Long userId);
	
	List<MeetingCalendarEvent> getCalendarEventsForSedintaAgaNotification();
}
