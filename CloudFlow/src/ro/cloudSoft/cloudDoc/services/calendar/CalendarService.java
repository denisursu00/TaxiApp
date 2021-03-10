package ro.cloudSoft.cloudDoc.services.calendar;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarModel;

public interface CalendarService {
	
	Long saveCalendar(CalendarModel calendarModel);
	
	CalendarModel get(Long calendarId);
	
	List<CalendarModel> getAllCalendars();
	
	boolean eventsExistsByCalendarId(Long calendarId);
	
	Long saveCalendarEvent(CalendarEventModel calendarEventModel, SecurityManager userSecurity, boolean checkUserRights) throws AppException ;
	
	void deleteCalendar(Long calendarId) throws AppException;
	
	List<CalendarModel> getAllCalendarByUserId(Long userId);
	
	CalendarEventsWrapperModel getCalendarsEvents(List<Long> calendarIds, Date startDate, Date endDate);
	
	CalendarEventModel getCalendarEvent(Long eventId);
	
	void deleteCalendarEvent(Long eventId, SecurityManager userSecurity) throws AppException;
}
