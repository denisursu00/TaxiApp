package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import java.util.Collection;
import java.util.Date;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;

/**
 * 
 */
public interface CereriDeConcediuGxtService extends GxtServiceBase {

	void anuleaza(String documentLocationRealName, String documentId) throws PresentationException;
	
	/**
	 * Returneaza ID-urile utilizatorilor care sunt in concediu in perioada data.
	 */
	Collection<Long> getIdsForUsersInConcediu(Date dataInceput, Date dataSfarsit);
}