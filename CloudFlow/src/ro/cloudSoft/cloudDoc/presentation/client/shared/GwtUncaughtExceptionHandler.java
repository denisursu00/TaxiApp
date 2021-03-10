package ro.cloudSoft.cloudDoc.presentation.client.shared;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.NavigationUtils;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public class GwtUncaughtExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void onUncaughtException(Throwable exception) {
		final String exceptionDetails = exception.toString();
		GwtServiceProvider.getLogService().logException(exceptionDetails, new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable exception) {
				showMessageToUser(exceptionDetails);
			}
			
			@Override
			public void onSuccess(Void nothing) {
				showMessageToUser(exceptionDetails);
			}
		});
		
	}
	
	private void showMessageToUser(String exceptionDetails) {
		String confirmMessage = GwtLocaleProvider.getMessages().UNRECOVERABLE_EXCEPTION(exceptionDetails);
		boolean reload = Window.confirm(confirmMessage);
		if (reload) {
			NavigationUtils.refresh();
		}
	}
}