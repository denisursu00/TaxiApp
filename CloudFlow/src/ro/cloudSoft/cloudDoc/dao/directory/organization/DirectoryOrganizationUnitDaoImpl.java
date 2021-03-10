package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import ro.cloudSoft.cloudDoc.dao.directory.AbstractDirectoryDaoImpl;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryOrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers.DirectoryOrganizationUnitContextMapper;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapClassNames;

public class DirectoryOrganizationUnitDaoImpl extends AbstractDirectoryDaoImpl implements DirectoryOrganizationUnitDao {
	
	private final DirectoryOrganizationUnitContextMapper organizationUnitContextMapper;

	public DirectoryOrganizationUnitDaoImpl(LdapTemplate ldapTemplate, LdapClassNames ldapClassNames,
			LdapAttributeNames ldapAttributeNames, DirectoryOrganizationUnitContextMapper organizationUnitContextMapper) {
		
		super(ldapTemplate, ldapClassNames, ldapAttributeNames);
		this.organizationUnitContextMapper = organizationUnitContextMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DirectoryOrganizationUnit> getOrganizationUnitsOfParent(String parentDn) {
		EqualsFilter filter = new EqualsFilter(LdapAttributeNames.ATTRIBUTE_OBJECT_CLASS, getLdapClassNames().getOrganizationUnitClass());		
		return getLdapTemplate().search(parentDn, filter.encode(), SearchControls.ONELEVEL_SCOPE, organizationUnitContextMapper);
	}
}