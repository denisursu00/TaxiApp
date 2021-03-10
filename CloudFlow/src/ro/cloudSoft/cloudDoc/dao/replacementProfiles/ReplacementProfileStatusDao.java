package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.NoSuchElementException;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatus;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;

/**
 * 
 */
public interface ReplacementProfileStatusDao {
	
	void deleteReplacementProfileStatus(ReplacementProfileStatus replacementProfileStatus);

	/**
	 * Returneaza starea pentru profilul dat.
	 * 
	 * @throws NoSuchElementException daca nu se gaseste starea pentru profilul dat
	 */
	ReplacementProfileStatus getReplacementProfileStatusByProfile(ReplacementProfile profile);

	/**
	 * Returneaza starea pentru profilul cu ID-ul dat.
	 * 
	 * @throws NoSuchElementException daca nu se gaseste starea pentru profilul cu ID-ul dat
	 */
	ReplacementProfileStatusOption getStatusForReplacementProfile(Long replacementProfileId);
	
	void createOrUpdateReplacementProfileStatus(ReplacementProfileStatus replacementProfileStatus);
}