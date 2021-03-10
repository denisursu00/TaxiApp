package ro.cloudSoft.cloudDoc.presentation.client.client;

import ro.cloudSoft.cloudDoc.presentation.client.client.utils.automaticActions.AutomaticActionOnModuleLoad;
import ro.cloudSoft.cloudDoc.presentation.client.shared.GwtUncaughtExceptionHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtConstantsPayload;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.timers.KeepSessionAliveTimer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtLogUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRequestUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClientModuleEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		GWT.setUncaughtExceptionHandler(new GwtUncaughtExceptionHandler());
		
		LoadingManager.get().loading();
		GwtServiceProvider.getAclService().getSecurityManager(new AsyncCallback<SecurityManagerModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayErrorWithDefaultSettings(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(final SecurityManagerModel userSecurity) {
				LoadingManager.get().loading();
				GwtServiceProvider.getAppService().getConstants(new AsyncCallback<GwtConstantsPayload>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayErrorWithDefaultSettings(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(GwtConstantsPayload constantsPayload) {
						
						GwtRegistryUtils.init(userSecurity,
							constantsPayload.getGuiConstants(),
							constantsPayload.getBusinessConstants(),
							constantsPayload.getCereriDeConcediuConstants(),
							constantsPayload.getWebConstants(),
							constantsPayload.getAppComponentsAvailabilityConstants(),
							constantsPayload.getReplacementProfilesOutOfOfficeConstants(),
							constantsPayload.getSupportedAttachmentTypesForPreviewConstants());
						
						KeepSessionAliveTimer.activate();
						
						AppStoreCache.loadAll(new Runnable() {
							
							@Override
							public void run() {
								initMain();
								handleAutomaticActions();
							}
						});
						
						LoadingManager.get().loadingComplete();
					}
				});
				LoadingManager.get().loadingComplete();	
			}
		});
	}

	private void initMain() {
		new ClientDesktop();
	}
	
	private void handleAutomaticActions() {
		
		String automaticActionParameterName = AutomaticActionOnModuleLoad.URL_PARAMETER_NAME;
		String automaticActionParameterValue = GwtRequestUtils.getRequestParameterValue(automaticActionParameterName);
		if (automaticActionParameterValue == null) {
			return;
		}
		
		AutomaticActionOnModuleLoad automaticAction = null;
		try {
			automaticAction = AutomaticActionOnModuleLoad.valueOf(automaticActionParameterValue);
		} catch (Exception e) {
			String logMessage = "Nu s-a putut determina actiunea automata pentru modulul " +
				"client. Valoarea parametrului este: [" + automaticActionParameterValue + "].";
			GwtLogUtils.log(logMessage);
			return;
		}
		
		automaticAction.getHandler().handle();
	}
}