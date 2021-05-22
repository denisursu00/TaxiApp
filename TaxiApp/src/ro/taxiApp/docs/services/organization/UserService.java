package ro.taxiApp.docs.services.organization;

import java.util.List;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;

public interface UserService {
	
	/**
	 * Returneaza utilizatorul care are ID-ul dat.
	 * Daca nu se gaseste utilizatorul, va returna null.
	 */
	User getUserById(Long id);
	
	public UserModel getUserByIdAsModel(Long id) throws AppException;
	
	public Long saveUserWithRole(UserModel user, String roleName, SecurityManager userSecurity) throws AppException;
	
	public List<UserModel> getUsersWithRole(String roleName);
	
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