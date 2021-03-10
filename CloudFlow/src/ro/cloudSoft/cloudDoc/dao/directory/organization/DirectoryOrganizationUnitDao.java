package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryOrganizationUnit;

public interface DirectoryOrganizationUnitDao {

	/**
	 * Returneaza toate unitatile organizatorice care au ca parinte direct elementul cu DN-ul precizat.
	 */
	List<DirectoryOrganizationUnit> getOrganizationUnitsOfParent(String parentDn);
}