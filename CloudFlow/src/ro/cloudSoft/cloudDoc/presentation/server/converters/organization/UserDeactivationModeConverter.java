package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;

import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivationMode;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GwtUserDeactivationMode;

public class UserDeactivationModeConverter {

	public static UserDeactivationMode getFromGwt(GwtUserDeactivationMode gwtUserDeactivationMode) {
		return UserDeactivationMode.valueOf(gwtUserDeactivationMode.name());
	}
}