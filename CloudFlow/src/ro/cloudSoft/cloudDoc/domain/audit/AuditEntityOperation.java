package ro.cloudSoft.cloudDoc.domain.audit;

public enum AuditEntityOperation {
	READ,
	ADD,
	MODIFY,
	CREATE_VERSION,
	MOVE,
	DELETE,
	CHECK_IN_NEW,
	CHECK_IN_EXISTING,
	CHECK_OUT,
	SAVE_NEW,
	SAVE_EXISTING,
	UNDO_CHECK_OUT,
	SEND,
	RETURN
}