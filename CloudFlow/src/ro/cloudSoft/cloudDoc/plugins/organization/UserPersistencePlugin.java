package ro.cloudSoft.cloudDoc.plugins.organization;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.organization.User;

public interface UserPersistencePlugin {
	
	
	/**
	 * Returneaza utilizatorul care are ID-ul dat.
	 * Daca nu se gaseste utilizatorul, va returna null.
	 */
	User getUserById(Long id);
	
	/**
	 * Verifica daca exista un utilizator cu username-ul si titlul dat.
	 * Cautarea dupa username NU va fi case-sensitive.
	 * Se va exclude de la cautare utilizatorul cu ID-ul dat, daca este diferit de null.
	 */
	boolean userWithUsernameExists(String username, Long idForUserToExclude);
	
	void saveUser(User user);
	
	/**
     * Returneaza toti utilizatorii, ordonati dupa nume si prenume.
     */
    List<User> getAllUsers();
	
    void delete(User user);
	
    boolean userExists(Long id);
	
    /**
	 * Returneaza utilizatorul care are username-ul dat.
	 * Cautarea NU este case-sensitive.
	 * Daca nu se gaseste utilizator cu acel username, va returna null.
	 * 
	 * @throws IllegalArgumentException daca se gasesc mai multi utilizatori cu acelasi username
	 */
	User getUserByUsername(String username);
	
	List<String> getUserRoleNames(Long userId);
	
}