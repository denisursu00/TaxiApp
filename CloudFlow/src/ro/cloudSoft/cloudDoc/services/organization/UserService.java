package ro.cloudSoft.cloudDoc.services.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface UserService {
	
	/**
	 * Returneaza utilizatorul care are ID-ul dat.
	 * Daca nu se gaseste utilizatorul, va returna null.
	 */
	User getUserById(Long id);
	
	public void setUser(User user, SecurityManager userSecurity) throws AppException;

	/**
     * Returneaza o lista cu toti utilizatorii din sistem
     * @return ArrayList<User>
     */
    public List<User> getAllUsers(SecurityManager userSecurity);
    
    /**
     * 
     * @param theUser
     */
    public void delete(User theUser, SecurityManager userSecurity);
    
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