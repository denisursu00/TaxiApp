package ro.cloudSoft.cloudDoc.domain.directory.contextMappers;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;

import ro.cloudSoft.cloudDoc.domain.directory.AbstractDirectoryEntity;

public abstract class AbstractDirectoryEntityContextMapper extends AbstractContextMapper {

	protected void populateCommonProperties(AbstractDirectoryEntity entity, DirContextOperations context) {
		entity.setDn(context.getDn().toString());
	}
}