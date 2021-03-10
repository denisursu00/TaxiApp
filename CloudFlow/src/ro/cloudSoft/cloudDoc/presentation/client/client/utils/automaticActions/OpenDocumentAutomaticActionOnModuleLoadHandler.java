package ro.cloudSoft.cloudDoc.presentation.client.client.utils.automaticActions;

import ro.cloudSoft.cloudDoc.presentation.client.client.WindowManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRequestUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

public class OpenDocumentAutomaticActionOnModuleLoadHandler implements AutomaticActionOnModuleLoadHandler {

	private static final String PARAMETER_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	private static final String PARAMETER_DOCUMENT_ID = "documentId";
	
	@Override
	public void handle() {
		
		String documentLocationRealName = GwtRequestUtils.getRequestParameterValue(PARAMETER_DOCUMENT_LOCATION_REAL_NAME);
		String documentId = GwtRequestUtils.getRequestParameterValue(PARAMETER_DOCUMENT_ID);
		
		if (GwtStringUtils.isBlank(documentLocationRealName) || GwtStringUtils.isBlank(documentId)) {
			throw new IllegalStateException("Nu s-au specificat identificatorii pentru document.");
		}
		
		WindowManager.getDocumentWindow().prepareForViewOrEdit(documentId, documentLocationRealName);
	}
}