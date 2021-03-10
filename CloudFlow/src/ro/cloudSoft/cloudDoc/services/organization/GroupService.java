package ro.cloudSoft.cloudDoc.services.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * , Nicolae Albu
 */
public interface GroupService {

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

	/**
	 * Adauga sau modifica grupul dat.
	 * 
	 * @return ID-ul grupului salvat
	 */
	Long setGroup(Group group, SecurityManager userSecurity) throws AppException;
	
	void updateGroups(Collection<Group> groups);

	ArrayList<Group> getGroups(SecurityManager userSecurity);

	void delete(Group theGroup, SecurityManager userSecurity) throws AppException;

	List<User> getAllUsersWithGroup(Long roleId, SecurityManager userSecurity);

	List<String> getEmails(Collection<Long> groupIds);

	boolean groupExists(Long groupId);
	
	boolean groupWithNameExists(String groupName);
	
	/**
	 * Returneaza numele grupurilor de care apartine utilizatorul cu ID-ul dat, ordonate dupa nume.
	 */
	public List<String> getGroupNamesOfUserWithId(Long userId);
	
	boolean isUserInGroupWithName(Long userId, String groupName);
	
	boolean isUserInGroupWithId(Long userId, Long groupId);
}