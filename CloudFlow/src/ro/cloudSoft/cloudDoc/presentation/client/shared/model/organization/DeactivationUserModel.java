package ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DeactivationUserModel implements IsSerializable {

	private String userId;
	private GwtUserDeactivationMode deactivationMode;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public GwtUserDeactivationMode getDeactivationMode() {
		return deactivationMode;
	}
	public void setDeactivationMode(GwtUserDeactivationMode deactivationMode) {
		this.deactivationMode = deactivationMode;
	}
}
