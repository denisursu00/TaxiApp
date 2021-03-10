package ro.cloudSoft.cloudDoc.presentation.client.client.utils.automaticActions;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRequestUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.google.gwt.user.client.Window;

public class ShowMessageAutomaticActionOnModuleLoadHandler implements AutomaticActionOnModuleLoadHandler {

	private static final String PARAMETER_MESSAGE = "message";
	
	@Override
	public void handle() {
		String message = GwtRequestUtils.getRequestParameterValue(PARAMETER_MESSAGE);
		if (GwtStringUtils.isBlank(message)) {
			return;
		}
		Window.alert(message);
	}
}