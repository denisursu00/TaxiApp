package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;

/**
 * 
 */
public class GwtDocumentUtils {

	public static boolean isLockedByUser(DocumentModel document, SecurityManagerModel userSecurity) {
		if (!isLocked(document)) {
			return false;
		}
		return document.getLockedBy().equals(userSecurity.getUserIdAsString());
	}
	
	public static boolean isLocked(DocumentModel document) {
		String lockedBy = document.getLockedBy();
		return GwtStringUtils.isNotBlank(lockedBy);
	}
}