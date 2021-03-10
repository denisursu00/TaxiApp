package ro.cloudSoft.cloudDoc.presentation.client.client.utils.automaticActions;

public enum AutomaticActionOnModuleLoad {

	SHOW_MESSAGE(new ShowMessageAutomaticActionOnModuleLoadHandler()),
	OPEN_DOCUMENT(new OpenDocumentAutomaticActionOnModuleLoadHandler());
	
	public static final String URL_PARAMETER_NAME = "automaticAction";
	
	private final AutomaticActionOnModuleLoadHandler handler;
	
	private AutomaticActionOnModuleLoad(AutomaticActionOnModuleLoadHandler handler) {
		this.handler = handler;
	}
	
	public AutomaticActionOnModuleLoadHandler getHandler() {
		return handler;
	}
}