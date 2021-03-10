package ro.cloudSoft.cloudDoc.web.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Reprezinta cazul cand un utilizator are mai multe conturi (cu acelasi username) si trebuie specificat si contul.
 * 
 * 
 */
public class MultipleAccountsAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MESSAGE = "You have multiple accounts and must choose one.";
	
	private final String username;
	private final String password;
	
	public MultipleAccountsAuthenticationException(String username, String password) {
		
		super(EXCEPTION_MESSAGE);
		
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}