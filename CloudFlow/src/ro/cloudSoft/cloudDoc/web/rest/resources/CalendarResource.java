package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetCalendarsEventsRequestModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;

@Component
@Path("/Calendar")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CalendarResource extends BaseResource {

	@Autowired
	private CalendarService calendarService;
	
	@POST
	@Path("/getCalendar/{calendarId}")
	public CalendarModel getCalendar(@PathParam("calendarId") Long calendarId) throws PresentationException {
		CalendarModel calendar = calendarService.get(calendarId);
		return calendar;
	}
	
	@POST
	@Path("/saveCalendar")
	public void saveCalendar(CalendarModel calendarModel) throws PresentationException {
		calendarService.saveCalendar(calendarModel);
	}
	
	@POST
	@Path("/getAllCalendars")
	public List<CalendarModel> getAllCalendars() throws PresentationException {
		return calendarService.getAllCalendars();
	}

	@POST
	@Path("/eventsExistsByCalendarId/{calendarId}")
	public boolean eventsExistsByCalendarId(@PathParam("calendarId") Long calendarId) {
		return calendarService.eventsExistsByCalendarId(calendarId);
	}

	@POST
	@Path("/deleteCalendar/{calendarId}")
	public void deleteCalendar(@PathParam("calendarId") Long calendarId) throws PresentationException {
		try {
			this.calendarService.deleteCalendar(calendarId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getAllCalendarsForUser/{userId}")
	public List<CalendarModel> getAllCalendarsForUser(@PathParam("userId") Long userId ) throws PresentationException {
		return calendarService.getAllCalendarByUserId(userId);
	}

	@POST
	@Path("/getCalendarsEvents")
	public CalendarEventsWrapperModel getCalendarsEvents(GetCalendarsEventsRequestModel requestModel) {
		return calendarService.getCalendarsEvents(requestModel.getCalendarIds(), requestModel.getStartDate(), requestModel.getEndDate());
	}

	@POST
	@Path("/saveEvent")
	public void saveEvent(CalendarEventWrapperModel calendarEventWrapperModel) throws PresentationException {
		try {
			calendarService.saveCalendarEvent(calendarEventWrapperModel.getCalendarEvent(), getSecurity(), true);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getCalendarEvent/{eventId}")
	public CalendarEventWrapperModel getCalendarEvent(@PathParam("eventId") Long eventId) {
		CalendarEventWrapperModel calendarEventWrapperModel = new CalendarEventWrapperModel();
		calendarEventWrapperModel.setCalendarEvent(calendarService.getCalendarEvent(eventId));
		return calendarEventWrapperModel;
	}

	@POST
	@Path("/deleteEvent/{eventId}")
	public void deleteEvent(@PathParam("eventId") Long eventId) throws PresentationException {
		try {
			calendarService.deleteCalendarEvent(eventId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
}
