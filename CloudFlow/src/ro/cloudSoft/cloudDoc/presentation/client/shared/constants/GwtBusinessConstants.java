package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Contine valori pentru constante legate de business-ul aplicatiei
 * 
 * 
 */
public class GwtBusinessConstants implements IsSerializable {

	private String groupNameAdmins;
	
	@SuppressWarnings("unused")
	private GwtBusinessConstants() {}
	
	/**
	 * Construieste o instanta noua.
	 * Acest constructor trebuie apelat pe partea de server, unde sunt stiute valorile necesare.
	 */
	public GwtBusinessConstants(String groupNameAdmins) {
		this.groupNameAdmins = groupNameAdmins;
	}
	
	public String getGroupNameAdmins() {
		return groupNameAdmins;
	}
}