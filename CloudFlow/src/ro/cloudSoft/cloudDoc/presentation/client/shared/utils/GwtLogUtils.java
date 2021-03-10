package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GwtLogUtils {

	public static void log(final String message) {
		GwtServiceProvider.getLogService().logException(message, new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable exception) {
				Window.alert(message);
			}
			
			@Override
			public void onSuccess(Void nothing) {}
		});
	}
}