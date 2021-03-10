package ro.cloudSoft.cloudDoc.presentation.server.converters.audit;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityType;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntry;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityOperation;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityType;

import com.google.common.collect.Lists;

public class AuditEntryConverter {

	public static AuditEntryModel getModel(AuditEntry auditEntry) {
		
		AuditEntryModel auditEntryModel = new AuditEntryModel();
		
		auditEntryModel.setId(auditEntry.getId());
		
		auditEntryModel.setDateTime(auditEntry.getDateTime());
		
		auditEntryModel.setUserId(auditEntry.getUserId());
		auditEntryModel.setUserDisplayName(auditEntry.getUserDisplayName());
		
		AuditEntityType entityType = auditEntry.getEntityType();
		GwtAuditEntityType gwtEntityType = AuditEntityTypeConverter.getForGwt(entityType);
		auditEntryModel.setEntityType(gwtEntityType);
		
		auditEntryModel.setEntityIdentifier(auditEntry.getEntityIdentifier());
		auditEntryModel.setEntityDisplayName(auditEntry.getEntityDisplayName());
		
		AuditEntityOperation operation = auditEntry.getOperation();
		GwtAuditEntityOperation gwtOperation = AuditEntityOperationConverter.getForGwt(operation);
		auditEntryModel.setOperation(gwtOperation);
		
		return auditEntryModel;
	}
	
	public static List<AuditEntryModel> getModels(List<AuditEntry> auditEntries) {
		List<AuditEntryModel> auditEntryModels = Lists.newArrayListWithCapacity(auditEntries.size());
		for (AuditEntry auditEntry : auditEntries) {
			AuditEntryModel auditEntryModel = getModel(auditEntry);
			auditEntryModels.add(auditEntryModel);
		}
		return auditEntryModels;
	}
}