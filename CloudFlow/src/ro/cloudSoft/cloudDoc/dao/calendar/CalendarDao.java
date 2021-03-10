package ro.cloudSoft.cloudDoc.dao.calendar;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;

public interface CalendarDao {
	
	Long save(Calendar calendar);
	
	Calendar find(Long calendarId);

	Calendar findByName(String calendarName);
	
	List<Calendar> getAll();
	
	void delete(Calendar calendar);
	
	Set<Calendar> findByUserId(Long userId);
	
	boolean calendarHasEvents(Long calendarId);
	
	List<CalendarEvent> getCalendarEvents(List<Long> calendarIds, Date startDate, Date endDate);

	List<CalendarEvent> getCalendarEventsByCalendarIdsAndStartDateInterval(List<Long> calendarIds, Date fromStartDate, Date toStartDate);

	List<Long> getCalendarIdsByNames(List<String> calendarNames);
}
