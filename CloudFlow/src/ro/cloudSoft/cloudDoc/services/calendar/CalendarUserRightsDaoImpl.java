package ro.cloudSoft.cloudDoc.services.calendar;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarUserRights;

public class CalendarUserRightsDaoImpl extends HibernateDaoSupport implements CalendarUserRightsDao {

	@Override
	public CalendarUserRights getById(Long id) {
		return (CalendarUserRights) getHibernateTemplate().get(CalendarUserRights.class, id);
	}
	
	@Override
	@SuppressWarnings(value = "unchecked")
	public List<CalendarUserRights> getCalendarUsersRightsByCalendarId(Long calendarId) {

		String query = ""
				+ " SELECT calendarUserRights FROM CalendarUserRights calendarUserRights "
				+ " WHERE calendarUserRights.calendar.id = ? ";
		
		List<CalendarUserRights> currentCalendarUserRights = getHibernateTemplate().find(query, calendarId);
		
		return currentCalendarUserRights;
	}
	
	@Override
	public void deleteCalendarUsersRights(List<Long> ids) {
		for (Long calendarUserRightsId : ids) {
			CalendarUserRights calendarUserRights = getById(calendarUserRightsId);
			getHibernateTemplate().delete(calendarUserRights);
		}
		
	}
}
