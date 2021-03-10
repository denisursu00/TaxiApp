package ro.cloudSoft.cloudDoc.services.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.domain.calendar.BirthdayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarUserRights;
import ro.cloudSoft.cloudDoc.domain.calendar.HolidayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.calendar.CalendarConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.calendar.CalendarEventConverter;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class CalendarServiceImpl implements CalendarService, InitializingBean {
	
	private CalendarDao calendarDao;
	private CalendarEventDao calendarEventDao;
	private CalendarUserRightsDao calendarUserRightsDao;
	
	private CalendarConverter calendarConverter;
	private CalendarEventConverter calendarEventConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			calendarDao,
			calendarEventDao,
			calendarUserRightsDao,
			
			calendarConverter,
			calendarEventConverter
		);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long saveCalendar(CalendarModel calendarModel) {
		
		List<Long> usersRightsIds = new ArrayList<Long>();
		if (calendarModel.getId() != null) {
			List<CalendarUserRights> calendarUsersRights = calendarUserRightsDao.getCalendarUsersRightsByCalendarId(calendarModel.getId());
			for (CalendarUserRights calendarUserRights : calendarUsersRights) {
				usersRightsIds.add(calendarUserRights.getId());
			}
		}

		List<Long> usersRightsToRemoveIds = new ArrayList<Long>();
		Calendar newEntity = calendarConverter.toEntity(calendarModel);
		
		if (CollectionUtils.isNotEmpty(usersRightsIds)) {
			for (Long persistedUserRightsId : usersRightsIds) {
				boolean found = false;
				for (CalendarUserRights newEntityUserRights : newEntity.getUsersRights()) {
					if (newEntityUserRights.getId() != null && newEntityUserRights.getId().equals(persistedUserRightsId)) {
						found = true;
					}
				}
				if (!found) {
					usersRightsToRemoveIds.add(persistedUserRightsId);
				}
			}
			if (CollectionUtils.isNotEmpty(usersRightsToRemoveIds)) {
				calendarUserRightsDao.deleteCalendarUsersRights(usersRightsToRemoveIds);
			}			
		}
		
		return calendarDao.save(newEntity);
	}

	@Override
	public CalendarModel get(Long calendarId) {
		Calendar calendarEntity = calendarDao.find(calendarId);
		return calendarConverter.toModel(calendarEntity);
	}
	
	@Override
	public List<CalendarModel> getAllCalendars() {
		List<Calendar> entities = calendarDao.getAll();
		return calendarConverter.toModels(entities);
	}
	
	@Override
	public boolean eventsExistsByCalendarId(Long calendarId) {
		return calendarDao.calendarHasEvents(calendarId);
	}
	
	@Override
	public Long saveCalendarEvent(CalendarEventModel calendarEventModel, SecurityManager userSecurity, boolean checkUserRights) throws AppException {
		
		CalendarEvent calendarEvent = calendarEventConverter.toEntity(calendarEventModel);
		if (checkUserRights) {
			if (!canEventBeSaved(calendarEvent, userSecurity.getUserId())) {
				throw new AppException(AppExceptionCodes.EVENT_CANNOT_BE_SAVED);
			}
		}
		
		return calendarEventDao.saveEvent(calendarEvent);
	}
	
	private boolean canEventBeSaved(CalendarEvent calendarEvent, Long loggedInUserId) throws AppException {
		
		if (calendarEvent.getCalendar().isPermitAll()) {
			return true;
		}
		
		boolean userCanSaveEvent = false;
		if (calendarEvent.getId() == null) {
			for (CalendarUserRights userRights : calendarEvent.getCalendar().getUsersRights()) {
				if (userRights.getUser().getId().equals(loggedInUserId)) {
					if (!userRights.isAdd()) {
						throw new AppException(AppExceptionCodes.USER_DONT_HAVE_RIGHTS_TO_SAVE_THIS_EVENT);
					}
					userCanSaveEvent = true;
				}
			}
			return userCanSaveEvent;
		} else {
			for (CalendarUserRights userRights : calendarEvent.getCalendar().getUsersRights()) {
				if (userRights.getUser().getId().equals(loggedInUserId)) {
					if (!userRights.isEdit()) {
						throw new AppException(AppExceptionCodes.USER_DONT_HAVE_RIGHTS_TO_EDIT_THIS_EVENT);
					}
					userCanSaveEvent = true;
				}
			}
		}
		return userCanSaveEvent;
	}
	
	@Override
	public void deleteCalendar(Long calendarId) throws AppException {
		boolean calendarHasEvents = calendarDao.calendarHasEvents(calendarId);
		if (calendarHasEvents) {
			throw new AppException(AppExceptionCodes.CANNOT_DELETE_CALENDAR_BECAUSE_EVENTS_EXISTS_ON_THIS_CALENDAR);
		}
		Calendar calendarToBeDeleted = calendarDao.find(calendarId);
		calendarDao.delete(calendarToBeDeleted);
	}
	
	@Override
	public List<CalendarModel> getAllCalendarByUserId(Long userId) {
		Set<Calendar> calendars = calendarDao.findByUserId(userId);
		return calendarConverter.toModels(new ArrayList<Calendar>(calendars));
	}
	
	@Override
	public CalendarEventsWrapperModel getCalendarsEvents(List<Long> calendarIds, Date startDate, Date endDate) {
		List<CalendarEvent> events = calendarDao.getCalendarEvents(calendarIds, startDate, endDate);
		
		CalendarEventsWrapperModel calendarEventModelWrapper = new CalendarEventsWrapperModel();
		calendarEventModelWrapper.setCalendarEvents(calendarEventConverter.toModels(events));
		
		return calendarEventModelWrapper;
	}

	@Override
	public CalendarEventModel getCalendarEvent(Long eventId) {
		CalendarEvent calendarEvent = calendarEventDao.find(eventId);
		return calendarEventConverter.toModel(calendarEvent);
	}
	
	@Override
	public void deleteCalendarEvent(Long eventId, SecurityManager userSecurity) throws AppException {
		CalendarEvent calendarEvent = calendarEventDao.find(eventId);
		if (!canEventBeDeleted(calendarEvent, userSecurity.getUserId())) {
			return;
		}
		calendarEventDao.delete(calendarEvent);
	}
	
	private boolean canEventBeDeleted(CalendarEvent calendarEvent, Long loggedInUserId) throws AppException {

		boolean userCanDeleteEvent = false;
		if (calendarEvent instanceof HolidayCalendarEvent) {
			throw new AppException(AppExceptionCodes.HOLIDAY_EVENTS_CANNOT_BE_MANUALLY_DELETED);
		} else if (calendarEvent instanceof BirthdayCalendarEvent) {
			return true;
		} else if (calendarEvent instanceof MeetingCalendarEvent) {
			userCanDeleteEvent = calendarEventDao.canEventBeDeleted(calendarEvent.getId());
			if (!userCanDeleteEvent) {
				throw new AppException(AppExceptionCodes.EVENT_CANNOT_BE_DELETED);
			}
		}
		
		
		if (calendarEvent.getCalendar().isPermitAll()) {
			return true;
		}
		
		for (CalendarUserRights userRights : calendarEvent.getCalendar().getUsersRights()) {
			if (userRights.getUser().getId().equals(loggedInUserId)) {
				if (!userRights.isDelete()) {
					throw new AppException(AppExceptionCodes.USER_DONT_HAVE_RIGHTS_TO_DELETE_THIS_EVENT);
				}
				userCanDeleteEvent = true;
			}
		}
		
		
		return userCanDeleteEvent;
	}
	
	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}
	
	public void setCalendarEventDao(CalendarEventDao calendarEventDao) {
		this.calendarEventDao = calendarEventDao;
	}
	
	public void setCalendarUserRightsDao(CalendarUserRightsDao calendarUserRightsDao) {
		this.calendarUserRightsDao = calendarUserRightsDao;
	}
	
	public void setCalendarConverter(CalendarConverter calendarConverter) {
		this.calendarConverter = calendarConverter;
	}
	
	public void setCalendarEventConverter(CalendarEventConverter calendarEventConverter) {
		this.calendarEventConverter = calendarEventConverter;
	}
}
