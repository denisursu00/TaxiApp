package ro.cloudSoft.cloudDoc.services.organization;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivationMode;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

/**
 * Clasa cu servicii legate de utilizatorii din sistemul CloudDoc
 * 
 */
public interface UserService {

    /**
     * Returneaza o lista cu toti utilizatorii din sistem
     * @return ArrayList<User>
     */
    public List<User> getAllUsers( SecurityManager userSecurity);
    
    public Set<Long> getIdsOfActiveUsers();

    public void setUser(User user, SecurityManager userSecurity) throws AppException;

    public void setUserAndSetAsManager(User user, SecurityManager userSecurity) throws AppException;
    
    /**
     * 
     * @param theUser
     */
    public void delete(User theUser, SecurityManager userSecurity);

    
    /**
     * Returneaza toate ID-urile unitatilor organizatorice din care utilizatorul
     * face parte (parintele direct si fiecare parinte al parintelui).
     * @param userId ID-ul utilizatorului
     * @return toate ID-urile unitatilor organizatorice din care utilizatorul
     * face parte
     */
    public List<Long> getOrganizationUnitIds(Long userId);
    
    /**
     * Returneaza toate ID-urile grupurilor din care utilizatorul face parte.
     * @param userId ID-ul utilizatorului
     * @return toate ID-urile grupurilor din care utilizatorul face parte
     */
    public List<Long> getGroupIds(Long userId);
    
    /**
     * Returneaza toate numele grupurilor din care utilizatorul face parte.
     * @param userId ID-ul utilizatorului
     * @return toate numele grupurilor din care utilizatorul face parte
     */
    public List<String> getGroupNames(Long userId);
    
    /**
     * Returneaza un map cu numele utilizatorilor corespunzator cu ID-ul,
     * Map<id-user, nume-user>
     * @param ids
     * @return
     */
    public Map<Long, String> getUsersNameMap(Set<Long> ids, SecurityManager userSecurity);
    
    /**
     * Returneaza lista de perechi (user, departament) 
     * @param userIds- lista de id-uri utilizator pentru care se aduce lista de perechi
     * @param userSecurity
     * @return
     * @throws AppException
     */
	public List<HashMap<String, HashMap<String,String>>> getUsersAndDepartment(String userIds,SecurityManager userSecurity) throws AppException;
	
	List<String> getEmails(Collection<Long> userIds);
	
	void moveUserToOrganizationUnit(Long idForUserToMove,
		Long idForDestinationOganizationUnit, SecurityManager userSecurity)
		throws AppException;
	
	void moveUserToOrganization(Long idForUserToMove,
		Long idForDestinationOrganization, SecurityManager userSecurity)
		throws AppException;

	/**
	 * Returneaza numele de afisare al utilizatorului cu ID-ul dat,
	 * sau null daca utilizatorul nu este gasit.
	 */
	String getDisplayName(Long userId);

	/**
	 * Returneaza superiorul utilizatorului cu ID-ul dat SAU null daca nu are superior.
	 */
	User getSuperior(Long userId);
	
	/**
	 * Returneaza ID-urile utilizatorilor direct subordonati utilizatorului cu ID-ul dat.
	 */
	Set<Long> getDirectlySubordinateUserIds(Long userId, SecurityManager userSecurity);
	
	/**
	 * Returneaza ID-urile tuturor utilizatorilor care sunt subordonati utilizatorului cu ID-ul dat.
	 */
	Set<Long> getAllSubordinateUserIds(Long userId, SecurityManager userSecurity);
	
	List<User> getUsersWithIds(Collection<Long> userIds);
	

	
	/**
	 * Verifica daca un utilizator are mai multe conturi (pot exista mai multe conturi pentru un username).
	 */
	boolean hasMultipleAccounts(String username);

	/**
	 * Cautarea dupa username NU va fi case-sensitive.
	 */
	Set<Long> getIdsOfActiveUserAccountsForUsername(String username);
	
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
	 * Returneaza utilizatorul activ care are username-ul dat.
	 * Cautarea NU este case-sensitive.
	 * Daca nu se gaseste utilizator activ cu acel username, va returna null.
	 * 
	 * @throws IllegalArgumentException daca se gasesc mai multi utilizatori activi cu acelasi username
	 */
	User getActiveUserByUsername(String username);
	
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
	 * Returneaza utilizatorul care are ID-ul dat.
	 * Daca nu se gaseste utilizatorul, va returna null.
	 */
	User getUserById(Long id, SecurityManager userSecurity);
	
	/**
	 * Returneaza toti utilizatorii care au username-ul dat, ordonati dupa nume si prenume.
	 * Cautarea NU este case-sensitive.
	 * 
	 * @throws IllegalArgumentException daca nu se gaseste nici un utilizator cu acel username
	 */
	List<User> getUsersWithUsername(String username);
	
	List<String> getAllUsernames();
	
	void updateUsers(Collection<User> users);
	
	void deactivateUserWithId(Long userId, UserDeactivationMode deactivationMode) throws AppException;
	
	boolean isUserAccountActive(User userAccount);
	
	List<User> getActiveUserAccountsOfUserWithId(Long userId);

	/**
	 * Returneaza conturile active ale utilizatorului cu username-ul dat.
	 * Cautarea dupa username NU va fi case-sensitive.
	 * Conturile sunt ordonate dupa titlu.
	 */
	List<User> getActiveUserAccountsForUsername(String username);
	
	void saveUserDeactivations(Collection<UserDeactivation> userDeactivations);
	
	void reactivateUserWithId(Long userId, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Daca nu gaseste inregistrarea pentru utilizatorul cu ID-ul dat, atunci va returna null.
	 */
	UserDeactivation getDeactivationForUserWithId(Long userId);
	
	void deleteUserDeactivation(UserDeactivation userDeactivation);
	
	NomenclatorValue getNomenclatorPersonByUserId(Long userId);
	
	User getUserByNomenclatorPersonId(Long nomenclatorPersonId);
	
	List<String> getUserRoleNames(Long userId);
	
	List<UserModel> getAllUsersWithAssignedTasks();
}