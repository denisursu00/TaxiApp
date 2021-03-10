package ro.cloudSoft.cloudDoc.dao.directory.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryGroup;

public interface DirectoryGroupDao {

	/**
	 * Ia toate grupurile gasite in director.
	 */
	List<DirectoryGroup> getAllGroups();
}