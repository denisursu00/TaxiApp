package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.calendar.CalendarRepeatEventEnum;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class MeetingCalendarEventModel extends IntervalCalendarEventModel {
	
	public MeetingCalendarEventModel() {
		super(CalendarEventType.MEETING);
	}
	
	private String location;
	private Integer reminderMinutes;
	private CalendarRepeatEventEnum repeat;
	private List<OrganizationEntityModel> attendees;
	private String reprezentantExtern;
	private String documentId;
	private String documentLocationRealName;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getReminderMinutes() {
		return reminderMinutes;
	}
	public void setReminderMinutes(Integer reminderMinutes) {
		this.reminderMinutes = reminderMinutes;
	}
	public CalendarRepeatEventEnum getRepeat() {
		return repeat;
	}
	public void setRepeat(CalendarRepeatEventEnum repeat) {
		this.repeat = repeat;
	}
	public List<OrganizationEntityModel> getAttendees() {
		return attendees;
	}
	public void setAttendees(List<OrganizationEntityModel> attendees) {
		this.attendees = attendees;
	}
	public String getReprezentantExtern() {
		return reprezentantExtern;
	}
	public void setReprezentantExtern(String reprezentantExtern) {
		this.reprezentantExtern = reprezentantExtern;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
}
