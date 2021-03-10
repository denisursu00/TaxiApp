package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;

/**
 * 
 */
public interface DirectoryUserDao {
	
	/**
	 * Cauta utilizatori in director pe baza criteriilor specificate, utilizatori ale caror atribute CONTIN valorile specificate
	 * (se folosesc wildcards si la inceput si la sfarsit). Deci daca se introduce "on" la prenumele utilizatorului, se vor gasi
	 * si cei cu "Ion", si cei cu "Onisor", si cei cu "Ionel".
	 * Cautarea NU este case-sensitive.
	 */
	List<DirectoryUser> findUsers(DirectoryUserSearchCriteria userSearchCriteria);
	
	/**
	 * Returneaza toti utilizatorii care au ca parinte direct elementul cu DN-ul precizat.
	 */
	List<DirectoryUser> getUsersOfParent(String parentDn);
	
	/**
	 * Verifica daca exista utilizator cu username-ul dat.
	 * Cautarea NU este case-sensitive.
	 */
	boolean userExistsWithUsername(String username);
}