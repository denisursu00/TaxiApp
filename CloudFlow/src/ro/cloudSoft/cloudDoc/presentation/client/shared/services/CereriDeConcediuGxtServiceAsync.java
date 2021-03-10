package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public interface CereriDeConcediuGxtServiceAsync extends GxtServiceBaseAsync {

	void anuleaza(String documentLocationRealName, String documentId, AsyncCallback<Void> callback);

	/**
	 * Returneaza ID-urile utilizatorilor care sunt in concediu in perioada data.
	 */
	void getIdsForUsersInConcediu(Date dataInceput, Date dataSfarsit, AsyncCallback<Collection<Long>> callback);
}