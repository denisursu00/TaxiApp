package ro.cloudSoft.cloudDoc.presentation.server.converters.calendar;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEventFactory;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModelFactory;

public class CalendarEventConverter {
	
	private CalendarEventFactory calendarEventFactory;
	private CalendarEventModelFactory calendarEventModelFactory;
	
	public CalendarEventModel toModel(CalendarEvent entity) {
		return calendarEventModelFactory.createFromEntity(entity);
	}
	
	public CalendarEvent toEntity(CalendarEventModel model) {
		return calendarEventFactory.createFromModel(model);
	}
	
	public List<CalendarEventModel> toModels(List<CalendarEvent> entities) {
		
		List<CalendarEventModel> models = new ArrayList<CalendarEventModel>();
		for (CalendarEvent entity : entities) {
			CalendarEventModel model = toModel(entity);
			models.add(model);
		}
		return models;
	}
	
	public void setCalendarEventFactory(CalendarEventFactory calendarEventFactory) {
		this.calendarEventFactory = calendarEventFactory;
	}
	
	public void setCalendarEventModelFactory(CalendarEventModelFactory calendarEventModelFactory) {
		this.calendarEventModelFactory = calendarEventModelFactory;
	}
}
