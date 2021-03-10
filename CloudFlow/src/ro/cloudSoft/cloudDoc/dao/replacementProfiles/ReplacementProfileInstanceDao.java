package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;

/**
 * 
 */
public interface ReplacementProfileInstanceDao {

	/**
	 * Elimina toate instantele profilelor de inlocuire asociate cu documentul care are identificatorii dati.
	 */
	void deleteProfileInstancesForDocument(String documentLocationRealName, String documentId);
	
	void createNewProfileInstance(ReplacementProfileInstance replacementProfileInstance);
	
	void createOrUpdateProfileInstance(ReplacementProfileInstance replacementProfileInstance);
	
	ReplacementProfileInstance getProfileInstanceForDocument(String documentLocationRealName, String documentId);
	
	void deleteProfileInstancesWithNoItems();
}