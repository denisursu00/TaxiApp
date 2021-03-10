package ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtAuditSearchCriteria implements IsSerializable {

	private Date startDate;
	private Date endDate;
	
	private Long userId;
	
	private GwtAuditEntityType entityType;
	
	private String entityIdentifierTextFragment;
	private String entityDisplayNameTextFragment;
	
	private GwtAuditEntityOperation operation;

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Long getUserId() {
		return userId;
	}

	public GwtAuditEntityType getEntityType() {
		return entityType;
	}

	public String getEntityIdentifierTextFragment() {
		return entityIdentifierTextFragment;
	}

	public String getEntityDisplayNameTextFragment() {
		return entityDisplayNameTextFragment;
	}

	public GwtAuditEntityOperation getOperation() {
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

	public void setEntityType(GwtAuditEntityType entityType) {
		this.entityType = entityType;
	}

	public void setEntityIdentifierTextFragment(String entityIdentifierTextFragment) {
		this.entityIdentifierTextFragment = entityIdentifierTextFragment;
	}

	public void setEntityDisplayNameTextFragment(String entityDisplayNameTextFragment) {
		this.entityDisplayNameTextFragment = entityDisplayNameTextFragment;
	}

	public void setOperation(GwtAuditEntityOperation operation) {
		this.operation = operation;
	}
}