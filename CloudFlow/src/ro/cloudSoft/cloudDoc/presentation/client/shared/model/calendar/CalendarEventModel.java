package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

public class CalendarEventModel {
	
	private Long id;
	private String subject;
	private String description;
	private String color;
	private Long calendarId;
	private boolean allDay;
	
	private CalendarEventType type;

	public CalendarEventModel(CalendarEventType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}
	public CalendarEventType getType() {
		return type;
	}
	public void setType(CalendarEventType type) {
		this.type = type;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

}
