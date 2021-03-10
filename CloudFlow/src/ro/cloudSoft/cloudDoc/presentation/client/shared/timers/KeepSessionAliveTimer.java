package ro.cloudSoft.cloudDoc.presentation.client.shared.timers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Timer care se ocupa cu mentinerea sesiunii de lucru a utilizatorului pe server
 * atat timp cat are aplicatia deschisa in browser
 * 
 * 
 */
public class KeepSessionAliveTimer extends Timer {
	
	private static KeepSessionAliveTimer instance = new KeepSessionAliveTimer();
	
	/**
	 * Activeaza timer-ul ce va rula periodic.
	 */
	public static void activate() {		
		GwtServiceProvider.getAppService().getSessionTimeoutInSeconds(new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable exception) {}
			@Override
			public void onSuccess(Integer sessionTimeoutInSeconds) {
				
				int timerPeriodInSeconds = (sessionTimeoutInSeconds / 2);
				int timerPeriodInMilliseconds = (timerPeriodInSeconds * 1000);

				instance.scheduleRepeating(timerPeriodInMilliseconds);
			}
		});
	}

	@Override
	public void run() {
		GwtServiceProvider.getAppService().keepSessionAlive(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable exception) {}
			@Override
			public void onSuccess(Void nothing) {}
		});
	}
}