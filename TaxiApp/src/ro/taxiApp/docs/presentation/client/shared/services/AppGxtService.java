package ro.taxiApp.docs.presentation.client.shared.services;


public interface AppGxtService extends GxtServiceBase {
	
	/**
	 * Returneaza perioada, in secunde, dupa care sesiunea utilizatorului pe server va expira,
	 * in cazul in care acesta nu mai face request-uri.
	 */
	Integer getSessionTimeoutInSeconds();
	
	/**
	 * Mentine sesiunea de lucru a utilizatorului pe server.
	 */
	void keepSessionAlive();
	
}