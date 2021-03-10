package ro.cloudSoft.cloudDoc.web.security;

/**
 * 
 */
public interface AuthenticationVerifier {

	boolean userExists(String username, String password);
}