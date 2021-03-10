package ro.cloudSoft.cloudDoc.services.organization;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface OrganizationalStructureImportService {

	void importOrganizationalStructureFromDirectory(SecurityManager userSecurity) throws AppException;
}