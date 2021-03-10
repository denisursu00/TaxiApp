package ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AuditEntryModel extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	
	public static final String PROPERTY_DATE_TIME = "dateTime";
	
	public static final String PROPERTY_USER_ID = "userId";
	public static final String PROPERTY_USER_DISPLAY_NAME = "userDisplayName";
	
	public static final String PROPERTY_ENTITY_TYPE = "entityType";
	
	public static final String PROPERTY_ENTITY_IDENTIFIER = "entityIdentifier";
	public static final String PROPERTY_ENTITY_DISPLAY_NAME = "entityDisplayName";
	
	public static final String PROPERTY_OPERATION = "operation";

	public Long getId() {
		return get(PROPERTY_ID);
	}

	public Date getDateTime() {
		return get(PROPERTY_DATE_TIME);
	}

	public Long getUserId() {
		return get(PROPERTY_USER_ID);
	}

	public String getUserDisplayName() {
		return get(PROPERTY_USER_DISPLAY_NAME);
	}

	public GwtAuditEntityType getEntityType() {
		return get(PROPERTY_ENTITY_TYPE);
	}

	public String getEntityIdentifier() {
		return get(PROPERTY_ENTITY_IDENTIFIER);
	}

	public String getEntityDisplayName() {
		return get(PROPERTY_ENTITY_DISPLAY_NAME);
	}

	public GwtAuditEntityOperation getOperation() {
		return get(PROPERTY_OPERATION);
	}

	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}

	public void setDateTime(Date dateTime) {
		set(PROPERTY_DATE_TIME, dateTime);
	}

	public void setUserId(Long userId) {
		set(PROPERTY_USER_ID, userId);
	}

	public void setUserDisplayName(String userDisplayName) {
		set(PROPERTY_USER_DISPLAY_NAME, userDisplayName);
	}

	public void setEntityType(GwtAuditEntityType entityType) {
		set(PROPERTY_ENTITY_TYPE, entityType);
	}

	public void setEntityIdentifier(String entityIdentifier) {
		set(PROPERTY_ENTITY_IDENTIFIER, entityIdentifier);
	}

	public void setEntityDisplayName(String entityDisplayName) {
		set(PROPERTY_ENTITY_DISPLAY_NAME, entityDisplayName);
	}

	public void setOperation(GwtAuditEntityOperation operation) {
		set(PROPERTY_OPERATION, operation);
	}
}