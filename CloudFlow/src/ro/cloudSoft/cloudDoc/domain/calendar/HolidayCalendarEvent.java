package ro.cloudSoft.cloudDoc.domain.calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

@Entity
@DiscriminatorValue("HOLIDAY")
public class HolidayCalendarEvent extends IntervalCalendarEvent {
	
	private OrganizationEntity user;
	private String documentId;
	private String documentLocationRealName;
	
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(
		name = "calendar_event_oe",
		joinColumns = @JoinColumn(name = "calendar_event_id", nullable = false, referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "attendees_org_entity_id", nullable = false, referencedColumnName = "org_entity_id")
	)
	public OrganizationEntity getUser() {
		return user;
	}

	public void setUser(OrganizationEntity user) {
		this.user = user;
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
