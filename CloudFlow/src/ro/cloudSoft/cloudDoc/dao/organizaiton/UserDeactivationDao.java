package ro.cloudSoft.cloudDoc.dao.organizaiton;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;

public interface UserDeactivationDao {
	
	void saveAll(Collection<UserDeactivation> userDeactivations);

	/**
	 * Daca nu gaseste inregistrarea pentru utilizatorul cu ID-ul dat, atunci va returna null.
	 */
	UserDeactivation getForUserWithId(Long userId);
	
	void delete(UserDeactivation userDeactivation);
}