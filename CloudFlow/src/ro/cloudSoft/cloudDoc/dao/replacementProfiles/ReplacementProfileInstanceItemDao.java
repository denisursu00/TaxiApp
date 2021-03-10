package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;

/**
 * 
 */
public interface ReplacementProfileInstanceItemDao {

	Collection<ReplacementProfileInstanceItem> getInstanceItemsForProfile(ReplacementProfile replacementProfile);
	
	void deleteProfileInstanceItems(Collection<ReplacementProfileInstanceItem> items);

	/**
	 * Returneaza item-ul instantei unui profil de inlocuire pentru documentul cu identificatorii dati,
	 * in care inlocuitorul folosit este utilizatorul cu ID-ul dat.
	 * Daca nu se gaseste item-ul, va returna null.
	 */
	ReplacementProfileInstanceItem getReplacementProfileInstanceItemForReplacement(
		String documentLocationRealName, String documentId, Long replacementUserId);
}