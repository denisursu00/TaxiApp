package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import ro.cloudSoft.cloudDoc.dao.directory.AbstractDirectoryDaoImpl;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryGroup;
import ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers.DirectoryGroupContextMapper;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapClassNames;

public class DirectoryGroupDaoImpl extends AbstractDirectoryDaoImpl implements DirectoryGroupDao {
	
	private final DirectoryGroupContextMapper groupContextMapper;

	public DirectoryGroupDaoImpl(LdapTemplate ldapTemplate, LdapClassNames ldapClassNames,
			LdapAttributeNames ldapAttributeNames, DirectoryGroupContextMapper groupContextMapper) {
		
		super(ldapTemplate, ldapClassNames, ldapAttributeNames);
		this.groupContextMapper = groupContextMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DirectoryGroup> getAllGroups() {
		EqualsFilter filter = new EqualsFilter(LdapAttributeNames.ATTRIBUTE_OBJECT_CLASS, getLdapClassNames().getGroupClass());
		return getLdapTemplate().search("", filter.encode(), SearchControls.SUBTREE_SCOPE, groupContextMapper);
	}
}