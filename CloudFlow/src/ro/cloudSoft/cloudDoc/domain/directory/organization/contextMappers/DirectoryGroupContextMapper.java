package ro.cloudSoft.cloudDoc.domain.directory.organization.contextMappers;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;

import ro.cloudSoft.cloudDoc.domain.directory.contextMappers.AbstractDirectoryEntityContextMapper;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryGroup;
import ro.cloudSoft.cloudDoc.plugins.organization.constants.LdapAttributeNames;

import com.google.common.collect.Lists;

/**
 * 
 */
public class DirectoryGroupContextMapper extends AbstractDirectoryEntityContextMapper {
	
	private final LdapAttributeNames attributeNames;
	private final String baseDn;
	
	public DirectoryGroupContextMapper(LdapAttributeNames attributeNames, String baseDn) {
		this.attributeNames = attributeNames;
		this.baseDn = baseDn;
	}

	@Override
	protected Object doMapFromContext(DirContextOperations context) {
		
		DirectoryGroup group = new DirectoryGroup();
		
		populateCommonProperties(group, context);
		
		if (StringUtils.isNotBlank(attributeNames.getGroupName())) {
			group.setName(context.getStringAttribute(attributeNames.getGroupName()));
		}
		if (StringUtils.isNotBlank(attributeNames.getGroupDescription())) {
			group.setDescription(context.getStringAttribute(attributeNames.getGroupDescription()));
		}
		if (StringUtils.isNotBlank(attributeNames.getGroupMember())) {
			String[] memberDnsAsArray = context.getStringAttributes(attributeNames.getGroupMember());
			if (ArrayUtils.isNotEmpty(memberDnsAsArray)) {
				List<String> normalizedMemberDns = Lists.newArrayListWithCapacity(memberDnsAsArray.length);
				for (String memberDn : memberDnsAsArray) {
					String normalizedMemberDn = normalizeDn(memberDn);
					normalizedMemberDns.add(normalizedMemberDn);
				}
				group.setMemberDns(normalizedMemberDns);
			}
		}
		
		return group;
	}
	
	/**
	 * Normalizeaza DN-ul dat, eliminand base DN-ul din el, daca exista.
	 */
	protected String normalizeDn(String dn) {
		String baseDnAsLastEntryInDn = ("," + baseDn);
		if (dn.endsWith(baseDnAsLastEntryInDn)) {
			return dn.substring(0, dn.lastIndexOf(baseDnAsLastEntryInDn));
		} else {
			return dn;
		}
	}
}