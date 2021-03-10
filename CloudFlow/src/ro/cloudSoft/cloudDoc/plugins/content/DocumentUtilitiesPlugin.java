package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesException;

public interface DocumentUtilitiesPlugin {

	List<String> getInfoDocumentsByName(String documentName) throws AppException;
	
	void modifyDocument(String documentLocationRealName, String documentID, String newDocumentName, String newDocumentDescription, String metadataName, String newMetadataValue) throws AppUtilitiesException;
}
