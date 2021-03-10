package ro.cloudSoft.cloudDoc.plugins.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 *
 * 
 */
public interface GroupPersistencePlugin {
    /**
	 * Get Group by id
	 * @param id the identifier of the Group
	 * @return the Group
	 */
    Group getGroup(Long id, SecurityManager userSecurity);

	/**
	 * Returneaza grupul cu ID-ul dat.
	 * Daca nu se gaseste grupul, va returna null.
	 */
    Group getGroupById(Long id);
	
	/**
	 * Cauta si returneaza grupul cu numele dat.
	 * Daca grupul nu este gasit, va returna null.
	 * 
	 * @throws IllegalStateException daca se gasesc mai multe grupuri cu numele dat
	 */
	Group getGroupByName(String groupName);

    void addUserToGroup(User user, String groupName, SecurityManager userSecurity);

    /**
	 * Get Group by name
	 * @param name the name of the Group
	 * @return the Group
	 */
	Group getGroup(String name, SecurityManager userSecurity);

    /**
     * Returneaza o lista cu toate rolurile din organizatie
     * @return ArrayList<Group>
     */
    ArrayList<Group> getGroups( SecurityManager userSecurity);

	/**
	 * Adds a Group to the organization or updates a existing one
	 * @param rl the Group
	 */
	Long setGroup( Group rl) throws AppException;
	
	void updateGroups(Collection<Group> groups);

    void delete(Group theGroup, SecurityManager userSecurity);

    List<User> getAllUsersWithGroup(Long groupId, SecurityManager userSecurity);
    
    List<String> getEmails(Collection<Long> groupIds);
    
    boolean groupExists(Long groupId);
    
    Collection<Long> getIdsForUsersInGroup(Long groupId);
	
	/**
	 * Returneaza numele grupurilor de care apartine utilizatorul cu ID-ul dat, ordonate dupa nume.
	 */
	public List<String> getGroupNamesOfUserWithId(Long userId);
	
	boolean isUserInGroupWithName(Long userId, String groupName);
	
	boolean isUserInGroupWithId(Long userId, Long groupId);
}