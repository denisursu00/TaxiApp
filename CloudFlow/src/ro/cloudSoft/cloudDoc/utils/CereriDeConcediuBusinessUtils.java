package ro.cloudSoft.cloudDoc.utils;

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * 
 */
public class CereriDeConcediuBusinessUtils {

	/**
	 * Verifica daca utilizatorul are drept de anulare pentru cererea de concediu reprezentata prin documentul dat.
	 */
	public static boolean hasPermissionForAnulare(SecurityManager userSecurity, Document document) {
		return (SecurityUtils.isUserAdmin(userSecurity) || userSecurity.getUserIdAsString().equals(document.getAuthor()));
	}
}