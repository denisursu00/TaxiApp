package ro.taxiApp.docs.web.security;

/**
 * 
 */
public interface AuthenticationVerifier {

	boolean userExists(String username, String password);
}