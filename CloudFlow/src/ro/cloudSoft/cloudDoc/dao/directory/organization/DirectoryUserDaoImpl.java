package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.CountNameClassPairCallbackHandler;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;

import ro.cloudSoft.cloudDoc.dao.directory.AbstractDirectoryDaoImpl;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers.DirectoryUserContextMapper;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapClassNames;

/**
 * 
 */
public class DirectoryUserDaoImpl extends AbstractDirectoryDaoImpl implements DirectoryUserDao {
	
	private final DirectoryUserContextMapper userContextMapper;

	public DirectoryUserDaoImpl(LdapTemplate ldapTemplate, LdapClassNames ldapClassNames,
			LdapAttributeNames ldapAttributeNames, DirectoryUserContextMapper userContextMapper) {
		
		super(ldapTemplate, ldapClassNames, ldapAttributeNames);
		this.userContextMapper = userContextMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DirectoryUser> findUsers(DirectoryUserSearchCriteria userSearchCriteria) {
		
		AndFilter searchFilter = new AndFilter();
		searchFilter.and(new LikeFilter(LdapAttributeNames.ATTRIBUTE_OBJECT_CLASS, getLdapClassNames().getUserClass()));
		if (StringUtils.isNotBlank(userSearchCriteria.getFirstName())) {
			searchFilter.and(new LikeFilter(getLdapAttributeNames().getUserFirstName(), addWildcards(userSearchCriteria.getFirstName())));
		}
		if (StringUtils.isNotBlank(userSearchCriteria.getLastName())) {
			searchFilter.and(new LikeFilter(getLdapAttributeNames().getUserLastName(), addWildcards(userSearchCriteria.getLastName())));
		}
		if (StringUtils.isNotBlank(userSearchCriteria.getUsername())) {
			searchFilter.and(new LikeFilter(getLdapAttributeNames().getUserUsername(), addWildcards(userSearchCriteria.getUsername())));
		}
		
		return getLdapTemplate().search("", searchFilter.encode(), SearchControls.SUBTREE_SCOPE, userContextMapper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DirectoryUser> getUsersOfParent(String parentDn) {
		EqualsFilter filter = new EqualsFilter(LdapAttributeNames.ATTRIBUTE_OBJECT_CLASS, getLdapClassNames().getUserClass());
		return getLdapTemplate().search(parentDn, filter.encode(), SearchControls.ONELEVEL_SCOPE, userContextMapper);
	}
	
	@Override
	public boolean userExistsWithUsername(String username) {
		
		AndFilter filter = new AndFilter()
			.and(new EqualsFilter(LdapAttributeNames.ATTRIBUTE_OBJECT_CLASS, getLdapClassNames().getUserClass()))
			.and(new EqualsFilter(getLdapAttributeNames().getUserUsername(), username));
		CountNameClassPairCallbackHandler countCallbackHandler = new CountNameClassPairCallbackHandler();		
		
		getLdapTemplate().search("", filter.encode(), SearchControls.SUBTREE_SCOPE, false, countCallbackHandler);
		return (countCallbackHandler.getNoOfRows() > 0);
	}
	
	/**
	 * Adauga wildcards la inceputul si la sfarsitul string-ului pentru a putea fi folosit intr-o cautare.
	 */
	private static String addWildcards(String value) {
		return ("*" + value + "*");
	}
}