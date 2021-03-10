package ro.cloudSoft.cloudDoc.dao.directory;

import org.springframework.ldap.core.LdapTemplate;

import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapClassNames;

/**
 * 
 */
public abstract class AbstractDirectoryDaoImpl {
	
	private final LdapTemplate ldapTemplate;
	
	private final LdapClassNames ldapClassNames;
	private final LdapAttributeNames ldapAttributeNames;
	
	protected AbstractDirectoryDaoImpl(LdapTemplate ldapTemplate,
			LdapClassNames ldapClassNames, LdapAttributeNames ldapAttributeNames) {
		
		this.ldapTemplate = ldapTemplate;
		
		this.ldapClassNames = ldapClassNames;
		this.ldapAttributeNames = ldapAttributeNames;
	}
	
	protected LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}
	protected LdapClassNames getLdapClassNames() {
		return ldapClassNames;
	}
	protected LdapAttributeNames getLdapAttributeNames() {
		return ldapAttributeNames;
	}
}