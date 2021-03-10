package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class AuditCalendarEventModel extends IntervalCalendarEventModel {
	
	public AuditCalendarEventModel() {
		super(CalendarEventType.AUDIT);
	}
	
	private String location;
	private List<OrganizationEntityModel> attendees;
	private String documentId;
	private String documentLocationRealName;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<OrganizationEntityModel> getAttendees() {
		return attendees;
	}
	public void setAttendees(List<OrganizationEntityModel> attendees) {
		this.attendees = attendees;
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
