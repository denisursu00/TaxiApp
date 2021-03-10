package ro.cloudSoft.cloudDoc.services.calendar;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarUserRights;

public interface CalendarUserRightsDao {
	
	CalendarUserRights getById(Long id); 
	
	List<CalendarUserRights> getCalendarUsersRightsByCalendarId(Long calendarId);
	
	void deleteCalendarUsersRights(List<Long> ids);
}
