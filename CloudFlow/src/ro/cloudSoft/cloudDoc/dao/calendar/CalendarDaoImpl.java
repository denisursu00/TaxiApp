package ro.cloudSoft.cloudDoc.dao.calendar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.common.utils.DateUtils;

public class CalendarDaoImpl extends HibernateDaoSupport implements CalendarDao {

	@Override
	@Transactional(noRollbackFor = Throwable.class)
	public Long save(Calendar calendar) {
		if (calendar.getId() == null) {
			return (Long) getHibernateTemplate().save(calendar);
		} else {
			getHibernateTemplate().saveOrUpdate(calendar);
			return calendar.getId();
		}
	}

	@Override
	public Calendar find(Long calendarId) {
		return (Calendar) getHibernateTemplate().get(Calendar.class, calendarId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Calendar> getAll() {
		String query = "FROM Calendar ORDER BY name";
		List<Calendar> calendars = getHibernateTemplate().find(query);
		return calendars;
	}

	@Override
	public void delete(Calendar calendar) {
		getHibernateTemplate().delete(calendar);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Calendar> findByUserId(Long userId) {
		Set<Calendar> calendars = Sets.newHashSet();
		
		String userQuery = "SELECT calendar "
				+ " FROM"
				+ "		Calendar calendar, "
				+ "		User theUser "
				+ " JOIN calendar.usersRights usersRights "
				+ " WHERE usersRights.user.id = ? AND usersRights.user.id = theUser.id";
		List<Calendar> userCalendars = getHibernateTemplate().find(userQuery, userId);
		calendars.addAll(userCalendars);
		
		String permitAll = "SELECT calendar "
				+ " FROM"
				+ "		Calendar calendar "
				+ " WHERE calendar.permitAll = ?";
		List<Calendar> visibleForAllUsers = getHibernateTemplate().find(permitAll, true);
		calendars.addAll(visibleForAllUsers);
		
		return calendars;
	}


	@Override
	public boolean calendarHasEvents(Long calendarId) {
		String query = "SELECT count(*) FROM CalendarEvent calendarEvent WHERE calendarEvent.calendar.id = ?";
		Integer calendarEventsExists = DataAccessUtils.intResult(getHibernateTemplate().find(query, calendarId));
		return calendarEventsExists > 0;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<CalendarEvent> getCalendarEvents(final List<Long> calendarIds, final Date startDate, final Date endDate) {
		
		if (CollectionUtils.isEmpty(calendarIds)) {
			return new ArrayList<CalendarEvent>();
		}
		
		List<CalendarEvent> events = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Set<CalendarEvent> events = new HashSet<CalendarEvent>(); 
				
				String intervalEventsQueryAsString =""
						+ " SELECT intervalCalendarEvent FROM IntervalCalendarEvent intervalCalendarEvent "
						+ " WHERE intervalCalendarEvent.calendar.id in (:calendarIds) AND "
						+ " ( "
						+ " 	(intervalCalendarEvent.startDate >= (:startDate) AND intervalCalendarEvent.endDate <= (:endDate)) OR "
						+ "		(intervalCalendarEvent.startDate <= (:startDate) AND intervalCalendarEvent.endDate >= (:endDate)) OR "
						+ "		(intervalCalendarEvent.startDate <= (:startDate) AND intervalCalendarEvent.endDate <= (:endDate) AND intervalCalendarEvent.endDate > (:startDate)) OR "
						+ "		(intervalCalendarEvent.startDate >= (:startDate) AND intervalCalendarEvent.endDate >= (:endDate) AND intervalCalendarEvent.startDate < (:endDate)) "
						+ " )";
		
				Query intervalEventsQuery = session.createQuery(intervalEventsQueryAsString);
				intervalEventsQuery.setParameterList("calendarIds", calendarIds);
				intervalEventsQuery.setParameter("startDate", startDate);
				intervalEventsQuery.setParameter("endDate", endDate);
				
				events.addAll(intervalEventsQuery.list());

				Integer startMonth = DateUtils.getMonth(startDate) + 1;
				Integer endMonth = DateUtils.getMonth(endDate) + 1;	
				
				String birthdayCalendarEventsQueryAsString = "";
				if (startMonth < endMonth) {
					 birthdayCalendarEventsQueryAsString = " SELECT birthdayCalendarEvent FROM BirthdayCalendarEvent birthdayCalendarEvent "
						+ " WHERE birthdayCalendarEvent.calendar.id in (:calendarIds) AND "
//						+ " 	birthdayCalendarEvent.birthdate >= (:startDate) AND birthdayCalendarEvent.birthdate <= (:endDate) ";
						+ " EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) >= :startMonth AND EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) <= :endMonth ";
				} else {
					 birthdayCalendarEventsQueryAsString = " SELECT birthdayCalendarEvent FROM BirthdayCalendarEvent birthdayCalendarEvent "
						+ " WHERE birthdayCalendarEvent.calendar.id in (:calendarIds) AND "
						+ " ( EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) >= :startMonth AND EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) <= 12 OR "
					 	+ " EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) >= 1 AND EXTRACT(MONTH FROM birthdayCalendarEvent.birthdate) <= :endMonth )";
				}
				//TODO: am inlocuit conditia pe birthday la nivel de luna, avand in vedere ca startDate si endDate se trimit pe o plaja mai larga decat luna curenta
				//de verificat daca in anumite cazuri poate reprezenta o problema
				
				Query birthdayCalendarEventsQuery = session.createQuery(birthdayCalendarEventsQueryAsString);
				birthdayCalendarEventsQuery.setParameterList("calendarIds", calendarIds);
				
				birthdayCalendarEventsQuery.setParameter("startMonth", startMonth);
				birthdayCalendarEventsQuery.setParameter("endMonth", endMonth);
				
				events.addAll(birthdayCalendarEventsQuery.list());
				
				List<CalendarEvent> eventsAsList = new ArrayList<CalendarEvent>();
				eventsAsList.addAll(events);
				
				return eventsAsList;
			}
		});
		
		return events;
	}

	@Override
	public Calendar findByName(String calendarName) {
		String query = "FROM Calendar calendar "
				+ "		WHERE calendar.name = ?";
		List<Calendar> calendars = getHibernateTemplate().find(query, calendarName);
		if (CollectionUtils.isEmpty(calendars)) {
			return null;
		}
		return calendars.get(0);
	}

	@Override
	public List<Long> getCalendarIdsByNames(List<String> calendarNames) {
		String queryString = "SELECT calendar.id "
				+ "		FROM Calendar calendar "
				+ "		WHERE calendar.name in (:calendarNames)";
	
		List<Long> calendarIds = getHibernateTemplate().executeFind(new HibernateCallback<List<Long>>() {

			@Override
			public List<Long> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				query.setParameterList("calendarNames", calendarNames);
				return query.list();
			}
			
		});
		return calendarIds;
	}

	@Override
	public List<CalendarEvent> getCalendarEventsByCalendarIdsAndStartDateInterval(List<Long> calendarIds, Date fromStartDate, Date toStartDate) {
		
		if (CollectionUtils.isEmpty(calendarIds)) {
			return new ArrayList<CalendarEvent>();
		}
		
		List<CalendarEvent> events = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				
				String intervalEventsQueryAsString =""
						+ " SELECT intervalCalendarEvent FROM IntervalCalendarEvent intervalCalendarEvent "
						+ " WHERE intervalCalendarEvent.calendar.id in (:calendarIds) AND "
						+ " ( "
						+ " 	(intervalCalendarEvent.startDate >= (:fromStartDate) AND intervalCalendarEvent.startDate <= (:toStartDate)) "
						+ " )"
						+ " ORDER BY intervalCalendarEvent.startDate ASC";
		
				Query intervalEventsQuery = session.createQuery(intervalEventsQueryAsString);
				intervalEventsQuery.setParameterList("calendarIds", calendarIds);
				intervalEventsQuery.setParameter("fromStartDate", fromStartDate);
				intervalEventsQuery.setParameter("toStartDate", toStartDate);
				intervalEventsQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				
				return intervalEventsQuery.list();
							
								
			}
		});
		
		return events;
	}

}