package ro.cloudSoft.cloudDoc.domain.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "audit_entries")
public class AuditEntry {

	private Long id;
	
	private Date dateTime;
	
	private Long userId;
	private String userDisplayName;
	
	private AuditEntityType entityType;
	
	private String entityIdentifier;
	private String entityDisplayName;
	
	private AuditEntityOperation operation;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_time", nullable = false)
	public Date getDateTime() {
		return dateTime;
	}

	@Column(name = "user_id", nullable = false)
	public Long getUserId() {
		return userId;
	}

	@Column(name = "user_display_name", nullable = false)
	public String getUserDisplayName() {
		return userDisplayName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false)
	public AuditEntityType getEntityType() {
		return entityType;
	}

	@Column(name = "entity_identifier", nullable = false)
	public String getEntityIdentifier() {
		return entityIdentifier;
	}

	@Column(name = "entity_display_name", nullable = false)
	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "operation", nullable = false)
	public AuditEntityOperation getOperation() {
		return operation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public void setEntityType(AuditEntityType entityType) {
		this.entityType = entityType;
	}

	public void setEntityIdentifier(String entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	public void setOperation(AuditEntityOperation operation) {
		this.operation = operation;
	}
}