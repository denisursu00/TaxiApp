package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

@Entity
@DiscriminatorValue("AUDIT")
public class AuditCalendarEvent extends IntervalCalendarEvent {
	
	private String location;
	private Set<OrganizationEntity> attendees;
	private String documentId;
	private String documentLocationRealName;
	
	@Column(name = "location")
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
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
	
}
