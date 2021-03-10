package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.ModelData;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GwtUserDeactivationMode;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

public interface OrganizationGxtService extends GxtServiceBase {

	public List<UserModel> getUsers();
	
	public List<UserModel> getAllActiveUsers();
	
	public Set<Long> getIdsOfActiveUsers();

	public List<UserModel> getUsersFromOrgUnit(String orgUnitId);

	public List<UserModel> getUsersFromGroup(String groupId);
	
	public List<UserModel> getUsersFromGroupByGroupName(String groupName);

	public OrganizationUnitModel getOrgUnitById(String id);

	public String setOrgUnit(OrganizationUnitModel orgUnitModel) throws PresentationException;

	public void deleteOrgUnit(OrganizationUnitModel orgUnitModel) throws PresentationException;

	public void setUser(UserModel userModel) throws PresentationException;

	public UserModel getUserById(String id);

	public void deleteUser(UserModel userModel);

	public List<GroupModel> getGroups();

	/**
	 * Returneaza grupul cu ID-ul dat.
	 * Daca grupul nu se gaseste, va returna null.
	 */
	public GroupModel getGroupById(String id);

	public String setGroup(GroupModel groupModel, Collection<UserModel> userModels) throws PresentationException;

	public void deleteGroup(GroupModel groupModel) throws PresentationException;

	public OrganizationModel getOrganization();

	public void move(ModelData itemToMove, ModelData destinationItem) throws PresentationException;
	
	public List<String> getAllUsernames() throws PresentationException;
	
	public List<UserModel> getUsersWithUsername(String username) throws PresentationException;

	/**
	 * Cauta utilizatori in director pe baza criteriilor specificate, utilizatori ale caror atribute CONTIN valorile specificate
	 * (se folosesc wildcards si la inceput si la sfarsit). Deci daca se introduce "on" la prenumele utilizatorului, se vor gasi
	 * si cei cu "Ion", si cei cu "Onisor", si cei cu "Ionel".
	 * Cautarea NU este case-sensitive.
	 */
	public List<DirectoryUserModel> findUsersInDirectory(GwtDirectoryUserSearchCriteria directoryUserSearchCriteria) throws PresentationException;
	
	public void importOrganizationalStructureFromDirectory() throws PresentationException;
	
	public void deactivateUserWithId(Long userId, GwtUserDeactivationMode deactivationMode) throws PresentationException;
	
	public void reactivateUserWithId(Long userId) throws PresentationException;
	
	/**
	 * Returneaza numele grupurilor de care apartine utilizatorul cu ID-ul dat, ordonate dupa nume.
	 */
	public List<String> getGroupNamesOfUserWithId(Long userId);
	
}