package ro.cloudSoft.cloudDoc.services.replacementProfiles;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;

/**
 * 
 */
public interface ReplacementProfileInstanceService {

	/**
	 * Seteaza instanta cu inlocuirile folosite pentru un document.
	 * Cele vechi, daca exista, vor fi inlaturate.
	 * Daca instanta data nu contine inlocuiri pentru documentul asociat, atunci va fi ignorata.
	 */
	void setReplacementProfileInstanceForDocument(ReplacementProfileInstance replacementProfileInstance);
	
	/**
	 * Adauga la instanta de profil de inlocuire pentru documentul cu identificatorii dati, itemii specificati.
	 */
	void addReplacementProfileInstanceItemsForDocument(String documentLocationRealName, String documentId,
		Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsToAdd);
	
	/**
	 * Elimina toate instantele profilelor de inlocuire asociate cu documentul care are identificatorii dati.
	 */
	void removeReplacementProfileInstancesForDocument(String documentLocationRealName, String documentId);
	
	Collection<ReplacementProfileInstanceItem> getReplacementProfileInstanceItemsForProfile(ReplacementProfile replacementProfile);
	
	/**
	 * Sterge itemii dati care apartin unor instante de profile de inlocuire.
	 * Pentru curatenie, vor fi eliminate instantele de profile care au ramas goale.
	 */
	void deleteReplacementProfileInstanceItems(Collection<ReplacementProfileInstanceItem> items);
	
	/**
	 * Returneaza item-ul instantei unui profil de inlocuire pentru documentul cu identificatorii dati,
	 * in care inlocuitorul folosit este utilizatorul cu ID-ul dat.
	 * Daca nu se gaseste item-ul, va returna null.
	 */
	ReplacementProfileInstanceItem getReplacementProfileInstanceItemForReplacement(
		String documentLocationRealName, String documentId, Long replacementUserId);
}