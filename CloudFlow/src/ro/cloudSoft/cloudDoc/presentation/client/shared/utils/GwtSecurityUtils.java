package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;

/**
 * 
 */
public class GwtSecurityUtils {
	
	public static boolean canAccessLockedDocument(DocumentModel document, SecurityManagerModel userSecurity) {
		if (!GwtDocumentUtils.isLocked(document)) {
			String exceptionMessage = "Documentul cu ID-ul [" + document.getId() + "] " +
				"si numele [" + document.getDocumentName() + "] NU este blocat.";
			throw new IllegalArgumentException(exceptionMessage);
		}
		return (GwtDocumentUtils.isLockedByUser(document, userSecurity) || userSecurity.isUserAdmin());
	}
}