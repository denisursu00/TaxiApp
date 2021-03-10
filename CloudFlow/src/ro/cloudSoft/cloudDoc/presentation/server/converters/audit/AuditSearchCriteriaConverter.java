package ro.cloudSoft.cloudDoc.presentation.server.converters.audit;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityType;
import ro.cloudSoft.cloudDoc.domain.audit.AuditSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityOperation;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;

public class AuditSearchCriteriaConverter {

	public static AuditSearchCriteria getFromGwt(GwtAuditSearchCriteria gwtSearchCriteria) {
		
		AuditSearchCriteria searchCriteria = new AuditSearchCriteria();
		
		searchCriteria.setStartDate(gwtSearchCriteria.getStartDate());
		searchCriteria.setEndDate(gwtSearchCriteria.getEndDate());
		
		searchCriteria.setUserId(gwtSearchCriteria.getUserId());
		
		GwtAuditEntityType gwtEntityType = gwtSearchCriteria.getEntityType();
		AuditEntityType entityType = AuditEntityTypeConverter.getFromGwt(gwtEntityType);
		searchCriteria.setEntityType(entityType);
		
		searchCriteria.setEntityIdentifierTextFragment(gwtSearchCriteria.getEntityIdentifierTextFragment());
		searchCriteria.setEntityDisplayNameTextFragment(gwtSearchCriteria.getEntityDisplayNameTextFragment());
		
		GwtAuditEntityOperation gwtOperation = gwtSearchCriteria.getOperation();
		AuditEntityOperation operation = AuditEntityOperationConverter.getFromGwt(gwtOperation);
		searchCriteria.setOperation(operation);
		
		return searchCriteria;
	}
}