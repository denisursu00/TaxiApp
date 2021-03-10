package ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.EnumWithLocalizedLabel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

public enum GwtAuditEntityOperation implements EnumWithLocalizedLabel {
	
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
	RETURN;
	
	private static final String PREFIX_LOCALIZED_LABEL_TOKEN_NAME = "AUDIT_OPERATION_";
	
	@Override
	public String getLocalizedLabel() {
		String tokenName = (PREFIX_LOCALIZED_LABEL_TOKEN_NAME + name());
		return GwtLocaleProvider.getConstants().getString(tokenName);
	}
}