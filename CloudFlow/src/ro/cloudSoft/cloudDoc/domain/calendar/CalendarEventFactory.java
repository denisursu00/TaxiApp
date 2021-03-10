package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.HashSet;
import java.util.Set;

import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.BirthdayCalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.HolidayCalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.MeetingCalendarEventModel;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DateUtils;

public class CalendarEventFactory {
	
	private CalendarDao calendarDao;
	private UserService userService;
	
	
	public CalendarEvent createFromModel(CalendarEventModel model) {

		if (model instanceof BirthdayCalendarEventModel) {
			BirthdayCalendarEvent birthdayCalendarEvent = new BirthdayCalendarEvent();
			birthdayCalendarEvent.setId(model.getId());
			birthdayCalendarEvent.setSubject(model.getSubject());
			birthdayCalendarEvent.setDescription(model.getDescription());
			birthdayCalendarEvent.setBirthdate(((BirthdayCalendarEventModel)model).getBirthdate());
			
			birthdayCalendarEvent.setAllDay(true);
			birthdayCalendarEvent.setRepeat(CalendarRepeatEventEnum.YEAR);
			
			Calendar calendar = calendarDao.find(model.getCalendarId());
			birthdayCalendarEvent.setCalendar(calendar);
			
			return birthdayCalendarEvent;
		} else if (model instanceof MeetingCalendarEventModel) {
			
			MeetingCalendarEvent meetingCalendarEvent = new MeetingCalendarEvent();
			meetingCalendarEvent.setId(model.getId());
			meetingCalendarEvent.setSubject(model.getSubject());
			meetingCalendarEvent.setDescription(((MeetingCalendarEventModel)model).getDescription());
			
			if (model.isAllDay()) {
				meetingCalendarEvent.setStartDate(DateUtils.nullHourMinutesSeconds(((MeetingCalendarEventModel)model).getStartDate()));
				meetingCalendarEvent.setEndDate(DateUtils.maximizeHourMinutesSeconds(((MeetingCalendarEventModel)model).getEndDate()));
			} else {
				meetingCalendarEvent.setStartDate(((MeetingCalendarEventModel)model).getStartDate());
				meetingCalendarEvent.setEndDate(((MeetingCalendarEventModel)model).getEndDate());
			}
			
			meetingCalendarEvent.setLocation(((MeetingCalendarEventModel)model).getLocation());
			meetingCalendarEvent.setReminderMinutes(((MeetingCalendarEventModel)model).getReminderMinutes());
			meetingCalendarEvent.setAllDay(model.isAllDay());
			meetingCalendarEvent.setRepeat(((MeetingCalendarEventModel)model).getRepeat());
			
			Calendar calendar = calendarDao.find(model.getCalendarId());
			meetingCalendarEvent.setCalendar(calendar);
			
			Set<OrganizationEntity> attendees = new HashSet<OrganizationEntity>();
			for (OrganizationEntityModel attendee : ((MeetingCalendarEventModel)model).getAttendees()) {
				attendees.add(userService.getUserById(attendee.getId()));
			}
			meetingCalendarEvent.setAttendees(attendees);
			
			return meetingCalendarEvent;
			
		} else if (model instanceof HolidayCalendarEventModel) {
			HolidayCalendarEvent holidayCalendarEvent = new HolidayCalendarEvent();
			holidayCalendarEvent.setId(model.getId());
			holidayCalendarEvent.setSubject(model.getSubject());
			holidayCalendarEvent.setDescription(model.getDescription());
			holidayCalendarEvent.setStartDate(((HolidayCalendarEventModel)model).getStartDate());
			holidayCalendarEvent.setEndDate(((HolidayCalendarEventModel)model).getEndDate());
			holidayCalendarEvent.setAllDay(true);
			
			Calendar calendar = calendarDao.find(model.getCalendarId());
			holidayCalendarEvent.setCalendar(calendar);
			
			if (((HolidayCalendarEventModel)model).getUserId() == null) {
				throw new RuntimeException("User user cannot be null");
			}
			holidayCalendarEvent.setUser(userService.getUserById(((HolidayCalendarEventModel)model).getUserId()));
			
			return holidayCalendarEvent;
		}
		throw new RuntimeException("CalendarEventModel instance is not an BirthdayCalendarEventModel instance "
				+ "or MeetingCalendarEventModel instance or HolidayCalendarEventModel instance.");
	}
	
	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
