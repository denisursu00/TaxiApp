package ro.cloudSoft.cloudDoc.presentation.client.client;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentWindow;

public class WindowManager {

	private static final DocumentWindow DOCUMENT_WINDOW = new DocumentWindow();
	
	public static DocumentWindow getDocumentWindow() {
		return DOCUMENT_WINDOW;
	}
}