package ro.cloudSoft.cloudDoc.presentation.server.converters.audit;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityType;
import ro.cloudSoft.common.utils.enums.EnumUtils;

public class AuditEntityTypeConverter {

	public static AuditEntityType getFromGwt(GwtAuditEntityType gwtEntityType) {
		return EnumUtils.convert(gwtEntityType, AuditEntityType.class);
	}

	public static GwtAuditEntityType getForGwt(AuditEntityType entityType) {
		return EnumUtils.convert(entityType, GwtAuditEntityType.class);
	}
}