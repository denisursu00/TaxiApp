package ro.cloudSoft.cloudDoc.dao.calendar;

import java.util.Date;
import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.core.constants.CalendarConstants;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.common.utils.DateUtils;

public class CalendarEventDaoImpl extends HibernateDaoSupport implements CalendarEventDao {

	@Override
	public Long saveEvent(CalendarEvent event) {
		if (event.getId() == null) {
			return (Long) getHibernateTemplate().save(event);
		} else {
			getHibernateTemplate().saveOrUpdate(event);
			return event.getId();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public CalendarEvent getEvent(Long calendarId, Date startDate, Date endDate) {
		String query = ""
				+ " SELECT event FROM CalendarEvent event "
				+ " WHERE event.calendar.id = ? AND event.startDate = ? AND event.endDate = ? ";
		Object[] queryParameters = new Object[] {
			calendarId,
			startDate,
			endDate
		};
		
		List<CalendarEvent> events = getHibernateTemplate().find(query, queryParameters);
		return (CalendarEvent) DataAccessUtils.singleResult(events);
	}

	@Override
	@SuppressWarnings("unchecked")
	public CalendarEvent getEvent(Long calendarId, Date startDate, Date endDate, Long userId) {
		String query = ""
				+ " SELECT event FROM CalendarEvent event "
				+ " WHERE event.calendar.id = ? AND DATE(event.startDate) = DATE(?) AND DATE(event.endDate) = ? AND event.user.id = ?";
		Object[] queryParameters = new Object[] {
			calendarId,
			startDate,
			endDate,
			userId
		};
		
		List<CalendarEvent> events = getHibernateTemplate().find(query, queryParameters);
		return (CalendarEvent) DataAccessUtils.singleResult(events);
	}

	@Override
	public CalendarEvent find(Long eventId) {
		return (CalendarEvent) getHibernateTemplate().get(CalendarEvent.class, eventId);
	}
	
	@Override
	public boolean canEventBeDeleted(Long eventId) {
		
		String queryAsString = "SELECT COUNT(*) FROM CalendarEvent calendarEvent WHERE calendarEvent.id = ? AND calendarEvent.startDate > ?";
		
		Object[] queryParams = new Object[] {
				eventId,
				new Date()
		};
		
		int count = DataAccessUtils.intResult(getHibernateTemplate().find(queryAsString, queryParams));
		return count != 0;	
	}

	@Override
	public void delete(CalendarEvent calendarEvent) {
		getHibernateTemplate().delete(calendarEvent);
	}

	@Override
	public List<MeetingCalendarEvent> getCalendarEventsForSedintaAgaNotification() {
		Date today = new Date();
		Date after20DaysDate = DateUtils.addDays(today, 20);
		after20DaysDate = DateUtils.maximizeHourMinutesSeconds(after20DaysDate);
		String query = ""
				+ " SELECT event "
				+ " FROM MeetingCalendarEvent event "
				+ " WHERE event.calendar.id = ("
				+ "			SELECT id"
				+ "			FROM Calendar"
				+ "			WHERE name = ?"
				+ "		) "
				+ "		AND event.notificat = false"
				+ "		AND event.startDate < ?";
		return (List<MeetingCalendarEvent>) getHibernateTemplate().find(query, CalendarConstants.CALENDAR_NAME_VALUE_AGA_ARB, after20DaysDate);
	}
}
