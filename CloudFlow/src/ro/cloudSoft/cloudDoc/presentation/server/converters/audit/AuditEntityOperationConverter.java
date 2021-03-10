package ro.cloudSoft.cloudDoc.presentation.server.converters.audit;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityOperation;
import ro.cloudSoft.common.utils.enums.EnumUtils;

public class AuditEntityOperationConverter {

	public static AuditEntityOperation getFromGwt(GwtAuditEntityOperation gwtOperation) {
		return EnumUtils.convert(gwtOperation, AuditEntityOperation.class);
	}

	public static GwtAuditEntityOperation getForGwt(AuditEntityOperation operation) {
		return EnumUtils.convert(operation, GwtAuditEntityOperation.class);
	}
}