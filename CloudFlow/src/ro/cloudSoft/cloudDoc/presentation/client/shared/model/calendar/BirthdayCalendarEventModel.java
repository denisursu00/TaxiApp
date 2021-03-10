package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.Date;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarRepeatEventEnum;

public class BirthdayCalendarEventModel extends CalendarEventModel {
	
	public BirthdayCalendarEventModel() {
		super(CalendarEventType.BIRTHDAY);
	}
	
	private CalendarRepeatEventEnum repeat;
	private Date birthdate;
	private Date birthdateEvent;
	
	public CalendarRepeatEventEnum getRepeat() {
		return repeat;
	}
	public void setRepeat(CalendarRepeatEventEnum repeat) {
		this.repeat = repeat;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public Date getBirthdateEvent() {
		return birthdateEvent;
	}
	public void setBirthdateEvent(Date birthdateEvent) {
		this.birthdateEvent = birthdateEvent;
	}
}
