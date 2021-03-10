package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.web.GwtUrlBuilder;

public class AttachmentUrlHelper {

	private static final String PARAMETER_NAME_ATTACHMENT_NAME = "attachmentName";
	private static final String PARAMETER_NAME_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	private static final String PARAMETER_NAME_DOCUMENT_ID = "documentId";
	private static final String PARAMETER_NAME_DOCUMENT_VERSION_NUMBER = "versionNumber";

	/**
	 * Pentru un atasament "temporar", se specifica doar numele (restul null).
	 * Pentru un atasament dintr-un document existent, se specifica numele 'real' al spatiului de lucru,
	 * ID-ul documentului si numele atasamentului (restul null).
	 * Pentru o versiune de document se specifica parametrii pentru un document existent PLUS numarul versiunii.
	 */
	public static String getUrlWithAttachmentParameters(String url, String documentLocationRealName, String documentId, Integer versionNumber, String attachmentName) {
		
		GwtUrlBuilder urlWithParametersBuilder = new GwtUrlBuilder(url);
		
		urlWithParametersBuilder.setParameter(PARAMETER_NAME_ATTACHMENT_NAME, attachmentName);
		
		if ((documentLocationRealName != null) && (documentId != null)) {
			
			urlWithParametersBuilder.setParameter(PARAMETER_NAME_DOCUMENT_LOCATION_REAL_NAME, documentLocationRealName);
			urlWithParametersBuilder.setParameter(PARAMETER_NAME_DOCUMENT_ID, documentId);
			
			if (versionNumber != null) {
				urlWithParametersBuilder.setParameter(PARAMETER_NAME_DOCUMENT_VERSION_NUMBER, versionNumber);
			}
		}
		
		return urlWithParametersBuilder.build();
	}
}