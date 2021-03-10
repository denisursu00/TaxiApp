package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtConstantsPayload;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppGxtServiceAsync extends GxtServiceBaseAsync {

	void setLocale(String locale, AsyncCallback<Void> callback);
	
	void getConstants(AsyncCallback<GwtConstantsPayload> callback);

	/**
	 * Returneaza perioada, in secunde, dupa care sesiunea utilizatorului pe server va expira,
	 * in cazul in care acesta nu mai face request-uri.
	 */
	void getSessionTimeoutInSeconds(AsyncCallback<Integer> callback);

	/**
	 * Mentine sesiunea de lucru a utilizatorului pe server.
	 */
	void keepSessionAlive(AsyncCallback<Void> callback);
}