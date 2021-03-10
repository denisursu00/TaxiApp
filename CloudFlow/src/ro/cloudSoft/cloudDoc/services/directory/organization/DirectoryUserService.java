package ro.cloudSoft.cloudDoc.services.directory.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;

/**
 * 
 */
public interface DirectoryUserService {

	/**
	 * Cauta utilizatori in director pe baza criteriilor specificate, utilizatori ale caror atribute CONTIN valorile specificate
	 * (se folosesc wildcards si la inceput si la sfarsit). Deci daca se introduce "on" la prenumele utilizatorului, se vor gasi
	 * si cei cu "Ion", si cei cu "Onisor", si cei cu "Ionel".
	 * Cautarea NU este case-sensitive.
	 */
	List<DirectoryUser> findUsers(DirectoryUserSearchCriteria directoryUserSearchCriteria);
}