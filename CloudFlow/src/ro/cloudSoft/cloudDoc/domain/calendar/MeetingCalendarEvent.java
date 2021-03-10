package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

@Entity
@DiscriminatorValue("MEETING")
public class MeetingCalendarEvent extends IntervalCalendarEvent {

	private String location;
	private Integer reminderMinutes;
	private CalendarRepeatEventEnum repeat;
	private Set<OrganizationEntity> attendees;
	private String documentId;
	private String documentLocationRealName;
	private NomenclatorValue reprezentantExtern;
	private boolean notificat;

	@Column(name = "location")
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Column(name = "reminder_minutes" )
	public Integer getReminderMinutes() {
		return reminderMinutes;
	}
	
	public void setReminderMinutes(Integer reminderMinutes) {
		this.reminderMinutes = reminderMinutes;
	}
	
	@Column(name = "repeat")
	@Enumerated(EnumType.STRING)
	public CalendarRepeatEventEnum getRepeat() {
		return repeat;
	}

	public void setRepeat(CalendarRepeatEventEnum repeat) {
		this.repeat = repeat;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(
		name = "calendar_event_oe",
		joinColumns = @JoinColumn(name = "calendar_event_id", nullable = false, referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "attendees_org_entity_id", nullable = false, referencedColumnName = "org_entity_id")
	)
	public Set<OrganizationEntity> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<OrganizationEntity> attendees) {
		this.attendees = attendees;
	}

	@Column( name ="jr_document_id" )
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column( name ="jr_document_location_real_name" )
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	public boolean isNotificat() {
		return notificat;
	}

	public void setNotificat(boolean notificat) {
		this.notificat = notificat;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reprezentant_extern_nom_value_id")
	public NomenclatorValue getReprezentantExtern() {
		return reprezentantExtern;
	}

	public void setReprezentantExtern(NomenclatorValue reprezentantExtern) {
		this.reprezentantExtern = reprezentantExtern;
	}
}
