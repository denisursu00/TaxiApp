package ro.cloudSoft.cloudDoc.domain.audit;

import java.util.EnumSet;

public enum AuditEntityType {
	
	USER(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.MOVE,
			AuditEntityOperation.DELETE
		)
	),
	ORGANIZATION_UNIT(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.MOVE,
			AuditEntityOperation.DELETE
		)
	),
	GROUP(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.DELETE
		)
	),
	
	DOCUMENT_TYPE(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.DELETE
		)
	),
	MIME_TYPE(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.DELETE
		)
	),
	
	WORKFLOW(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.CREATE_VERSION,
			AuditEntityOperation.DELETE
		)
	),
	
	REPLACEMENT_PROFILE(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.ADD,
			AuditEntityOperation.MODIFY,
			AuditEntityOperation.DELETE,
			AuditEntityOperation.RETURN
		)
	),
	
	ARCHIVING_PROFILE(
		EnumSet.noneOf(AuditEntityOperation.class)
	),
	
	DOCUMENT(
		EnumSet.of(
			AuditEntityOperation.READ,
			AuditEntityOperation.MOVE,
			AuditEntityOperation.DELETE,
			AuditEntityOperation.CHECK_OUT,
			AuditEntityOperation.CHECK_IN_NEW,
			AuditEntityOperation.CHECK_IN_EXISTING,
			AuditEntityOperation.SAVE_NEW,
			AuditEntityOperation.SAVE_EXISTING,
			AuditEntityOperation.UNDO_CHECK_OUT,
			AuditEntityOperation.SEND
		)
	);
	
	private final EnumSet<AuditEntityOperation> allowedOperations;
	
	private AuditEntityType(EnumSet<AuditEntityOperation> allowedOperations) {
		this.allowedOperations = allowedOperations;
	}
	
	public boolean isOperationAllowed(AuditEntityOperation operation) {
		return allowedOperations.contains(operation);
	}
}