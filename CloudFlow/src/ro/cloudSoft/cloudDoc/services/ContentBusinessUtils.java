package ro.cloudSoft.cloudDoc.services;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;

public class ContentBusinessUtils {
	
	public static boolean canUserAccessLockedDocument(Document document, ro.cloudSoft.cloudDoc.domain.security.SecurityManager userSecurity) {
		boolean canUserAccessLockedDocument = false;		    
	    if (StringUtils.isNotBlank(document.getLockedBy())) {		    	
	    	if (SecurityUtils.isUserAdmin(userSecurity)) {
	    		canUserAccessLockedDocument = true;
	    	} else {
	    		canUserAccessLockedDocument = document.getLockedBy().equals(userSecurity.getUserIdAsString());
	    	}
	    }
		return canUserAccessLockedDocument;
	}
}
