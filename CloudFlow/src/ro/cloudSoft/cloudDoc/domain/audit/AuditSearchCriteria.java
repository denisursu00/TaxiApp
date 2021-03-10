package ro.cloudSoft.cloudDoc.domain.audit;

import java.util.Date;

public class AuditSearchCriteria {

	private Date startDate;
	private Date endDate;
	
	private Long userId;
	
	private AuditEntityType entityType;
	
	private String entityIdentifierTextFragment;
	private String entityDisplayNameTextFragment;
	
	private AuditEntityOperation operation;

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Long getUserId() {
		return userId;
	}

	public AuditEntityType getEntityType() {
		return entityType;
	}

	public String getEntityIdentifierTextFragment() {
		return entityIdentifierTextFragment;
	}

	public String getEntityDisplayNameTextFragment() {
		return entityDisplayNameTextFragment;
	}

	public AuditEntityOperation getOperation() {
		return operation;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setEntityType(AuditEntityType entityType) {
		this.entityType = entityType;
	}

	public void setEntityIdentifierTextFragment(String entityIdentifierTextFragment) {
		this.entityIdentifierTextFragment = entityIdentifierTextFragment;
	}

	public void setEntityDisplayNameTextFragment(String entityDisplayNameTextFragment) {
		this.entityDisplayNameTextFragment = entityDisplayNameTextFragment;
	}

	public void setOperation(AuditEntityOperation operation) {
		this.operation = operation;
	}
}