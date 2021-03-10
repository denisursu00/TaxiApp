package ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;

import ro.cloudSoft.cloudDoc.domain.directory.contextMappers.AbstractDirectoryEntityContextMapper;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;

/**
 * 
 */
public class DirectoryUserContextMapper extends AbstractDirectoryEntityContextMapper {
	
	private final LdapAttributeNames attributeNames;
	
	public DirectoryUserContextMapper(LdapAttributeNames attributeNames) {
		this.attributeNames = attributeNames;
	}
	
	@Override
	protected Object doMapFromContext(DirContextOperations context) {
		
		DirectoryUser user = new DirectoryUser();

		populateCommonProperties(user, context);
		
		if (StringUtils.isNotBlank(attributeNames.getUserUsername())) {
			user.setUsername(context.getStringAttribute(attributeNames.getUserUsername()));
		}
		if (StringUtils.isNotBlank(attributeNames.getUserFirstName())) {
			user.setFirstName(context.getStringAttribute(attributeNames.getUserFirstName()));
		}
		if (StringUtils.isNotBlank(attributeNames.getUserLastName())) {
			user.setLastName(context.getStringAttribute(attributeNames.getUserLastName()));
		}
		if (StringUtils.isNotBlank(attributeNames.getUserPassword())) {
			byte[] passwordAsBytes = (byte[]) context.getObjectAttribute(attributeNames.getUserPassword());
			if (passwordAsBytes != null) {
				String password = new String(passwordAsBytes);
				user.setPassword(password);
			}
		}
		if (StringUtils.isNotBlank(attributeNames.getUserMail())) {
			user.setEmail(context.getStringAttribute(attributeNames.getUserMail()));
		}
		if (StringUtils.isNotBlank(attributeNames.getUserTitle())) {
			user.setTitle(context.getStringAttribute(attributeNames.getUserTitle()));
		}
		if (StringUtils.isNotBlank(attributeNames.getUserEmployeeNumber())) {
			user.setEmployeeNumber(context.getStringAttribute(attributeNames.getUserEmployeeNumber()));
		}
		
		return user;
	}
}