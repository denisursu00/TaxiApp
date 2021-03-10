package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public class GwtCereriDeConcediuBusinessUtils {
	
	/**
	 * Verifica daca utilizatorul are drept de anulare pentru cererea de concediu reprezentata prin documentul dat.
	 */
	public static boolean hasPermissionForAnulare(SecurityManagerModel userSecurity, String authorUserIdAsString) {
		return (userSecurity.isUserAdmin() || userSecurity.getUserIdAsString().equals(authorUserIdAsString));
	}
}