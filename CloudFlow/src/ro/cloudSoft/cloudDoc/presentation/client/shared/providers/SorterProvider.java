package ro.cloudSoft.cloudDoc.presentation.client.shared.providers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.DirectoryUserStoreSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.GroupListSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureListSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureTreeSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.UserListSorter;

/**
 * Clasa este folosita pentru obtinerea unor sortere.
 * 
 * 
 */
public class SorterProvider {

	private static GroupListSorter groupListSorter;
	private static OrganizationalStructureTreeSorter organizationalStructureTreeSorter;
	private static OrganizationalStructureListSorter organizationalStructureListSorter;
	private static UserListSorter userListSorter;
	private static DirectoryUserStoreSorter directoryUserListSorter;
	
	public static GroupListSorter getGroupListSorter() {
		if (groupListSorter == null) {
			groupListSorter = new GroupListSorter();
		}
		return groupListSorter;
	}
	
	public static OrganizationalStructureTreeSorter getOrganizationalStructureTreeSorter() {
		if (organizationalStructureTreeSorter == null) {
			organizationalStructureTreeSorter = new OrganizationalStructureTreeSorter();
		}
		return organizationalStructureTreeSorter;
	}
	
	public static OrganizationalStructureListSorter getOrganizationalStructureListSorter() {
		if (organizationalStructureListSorter == null) {
			organizationalStructureListSorter = new OrganizationalStructureListSorter();
		}
		return organizationalStructureListSorter;
	}
	
	public static UserListSorter getUserListSorter() {
		if (userListSorter == null) {
			userListSorter = new UserListSorter();
		}
		return userListSorter;
	}
	
	public static DirectoryUserStoreSorter getDirectoryUserStoreSorter() {
		if (directoryUserListSorter == null) {
			directoryUserListSorter = new DirectoryUserStoreSorter();
		}
		return directoryUserListSorter;
	}
}