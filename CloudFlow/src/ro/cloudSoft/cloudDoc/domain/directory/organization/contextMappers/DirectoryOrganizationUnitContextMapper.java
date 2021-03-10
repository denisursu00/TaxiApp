package ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;

import ro.cloudSoft.cloudDoc.domain.directory.contextMappers.AbstractDirectoryEntityContextMapper;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryOrganizationUnit;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;

/**
 * 
 */
public class DirectoryOrganizationUnitContextMapper extends AbstractDirectoryEntityContextMapper {
	
	private final LdapAttributeNames attributeNames;
	
	public DirectoryOrganizationUnitContextMapper(LdapAttributeNames attributeNames) {
		this.attributeNames = attributeNames;
	}
	
	@Override
	protected Object doMapFromContext(DirContextOperations context) {
		
		DirectoryOrganizationUnit organizationUnit = new DirectoryOrganizationUnit();

		populateCommonProperties(organizationUnit, context);
		
		if (StringUtils.isNotBlank(attributeNames.getOrganizationUnitName())) {
			organizationUnit.setName(context.getStringAttribute(attributeNames.getOrganizationUnitName()));
		}
		if (StringUtils.isNotBlank(attributeNames.getOrganizationUnitDescription())) {
			organizationUnit.setDescription(context.getStringAttribute(attributeNames.getOrganizationUnitDescription()));
		}
		
		return organizationUnit;
	}
}