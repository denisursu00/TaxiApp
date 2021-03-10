package ro.cloudSoft.cloudDoc.plugins.organization;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.UserAndNomenclatorPersonRelation;

public interface UserPersistencePlugin {

	void saveUser(User user);
   
    /**
     * Returneaza toti utilizatorii, ordonati dupa nume si prenume.
     */
    List<User> getAllUsers();
    
    Set<Long> getIdsOfUsersInGroupWithName(String groupName);
    

    Set<User> getUsersInGroupWithName(String groupName);

    void delete(User user);
    
    /**
     * Returneaza toate ID-urile unitatilor organizatorice din care utilizatorul
     * face parte (parintele direct si fiecare parinte al parintelui).
     */
    List<Long> getOrganizationUnitIds(Long userId);
    
    /**
     * Returneaza toate ID-urile grupurilor din care utilizatorul face parte.
     */
    List<Long> getGroupIds(Long userId);
    
    /**
     * Returneaza toate numele grupurilor din care utilizatorul face parte.
     */
    List<String> getGroupNames(Long userId);
    
    /**
     * Returneaza un map cu numele utilizatorilor corespunzator cu ID-ul,
     * Map<id-user, nume-user>
     * @param ids
     * @return
     */
    Map<Long, String> getUsersNameMap(Set<Long> ids);
	
	List<HashMap<String, HashMap<String,String>>> getUsersAndDepartment(String userIds);
    
	List<String> getEmails(Collection<Long> userIds);
	
	/**
	 * Returneaza adresa de e-mail pentru utilizatorul cu ID-ul dat.
	 * 
	 * @throws IllegalArgumentException daca NU se poate gasi e-mail-ul pentru utilizatorul cu ID-ul dat
	 */
	String getEmail(Long userId);

	/**
	 * Returneaza numele de afisare al utilizatorului cu ID-ul dat,
	 * sau null daca utilizatorul nu este gasit.
	 */
	String getDisplayName(Long userId);
	
	Map<Long, String> getEmailByUserId(Collection<Long> userIds);
	
	Map<Long, String> getOrganizationUnitNameByUserId(Collection<Long> userIds);
	
	Map<String, String> getOrganizationUnitNameByUserEmailInLowerCase(Collection<String> userEmails);

	List<User> getUsersWithIds(Collection<Long> userIds);
	
	/**
	 * Verifica daca un utilizator are mai multe conturi (pot exista mai multe conturi pentru un username).
	 */
	boolean hasMultipleAccounts(String username);
	
	boolean userExists(Long id);
	
	/**
	 * Returneaza utilizatorul care are username-ul dat.
	 * Cautarea NU este case-sensitive.
	 * Daca nu se gaseste utilizator cu acel username, va returna null.
	 * 
	 * @throws IllegalArgumentException daca se gasesc mai multi utilizatori cu acelasi username
	 */
	User getUserByUsername(String username);
	
	/**
	 * Returneaza utilizatorul cu username-ul si titlul dat.
	 * Cautarea dupa username NU este case-sensitive.
	 * Daca nu se gaseste utilizator, atunci va returna null.
	 * 
	 * @throws IllegalStateException daca se gasesc mai multi utilizatori cu criteriile date
	 */
	User getUserByUsernameAndTitle(String username, String title);
	
	/**
	 * Returneaza utilizatorul care are ID-ul dat.
	 * Daca nu se gaseste utilizatorul, va returna null.
	 */
	User getUserById(Long id);

	/**
	 * Returneaza toti utilizatorii care au username-ul dat, ordonati dupa nume si prenume.
	 * Cautarea NU este case-sensitive.
	 * 
	 * @throws IllegalArgumentException daca nu se gaseste nici un utilizator cu acel username
	 */
	List<User> getUsersWithUsername(String username);
	
	/**
	 * Ia ID-urile de utilizatori pentru toti utilizatorii care au username-ul printre cele
	 * ale utilizatorilor cu ID-urile date (mai multi utilizatori pot avea acelasi username).
	 */
	Collection<Long> getIdsForUsersMatchedByUsernameInLowercase(Collection<Long> idsForKnownUsers);
	
	/**
	 * Verifica daca exista un utilizator cu username-ul si titlul dat.
	 * Cautarea dupa username NU va fi case-sensitive.
	 * Se va exclude de la cautare utilizatorul cu ID-ul dat, daca este diferit de null.
	 */
	boolean userWithUsernameAndTitleExists(String username, String title, Long idForUserToExclude);
	
	/**
	 * Returneaza toate username-urile distincte.
	 */
	List<String> getAllUsernames();
	
	void updateUsers(Collection<User> users);
	
	List<User> getUserAccountsForUserWithIdInGroupWithName(Long userId, String groupName);
	
	/**
	 * Cautarea dupa username NU va fi case-sensitive.
	 */
	Set<Long> getIdsOfUserAccountsWithUsernameInGroupWithName(String username, String groupName);
	
	/**
	 * Returneaza conturile utilizatorului cu username-ul dat care apartin grupului cu numele dat.
	 * Cautarea dupa username NU va fi case-sensitive.
	 * Conturile sunt ordonate dupa titlu.
	 */
	List<User> getUserAccountsWithUsernameInGroupWithName(String username, String groupName);
	
	void saveUserAndNomenclatorPersonRelation(UserAndNomenclatorPersonRelation userAndPersonOfNomenclator);
	
	UserAndNomenclatorPersonRelation getUserAndNomenclatorPersonRelationByUserId(Long userId);
	
	NomenclatorValue getNomenclatorPersonByUserId(Long userId);
	
	User getUserByNomenclatorPersonId(Long nomenclatorPersonId);
	
	void deleteUserAndNomenclatorPersonRelation(UserAndNomenclatorPersonRelation relation);
	
	List<String> getUserRoleNames(Long userId);
	
	List<User> getAllUsersWithAssignedTasks();

}