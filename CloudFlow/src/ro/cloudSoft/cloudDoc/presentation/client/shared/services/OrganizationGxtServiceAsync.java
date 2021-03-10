package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GwtUserDeactivationMode;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrganizationGxtServiceAsync extends GxtServiceBaseAsync {

    public abstract void getUsers(AsyncCallback<List<UserModel>> asyncCallback);
	
	public abstract void getIdsOfActiveUsers(AsyncCallback<Set<Long>> callback);

    public abstract void getUsersFromOrgUnit(String organizationUnitId, AsyncCallback<List<UserModel>> asyncCallback);

    public abstract void getUsersFromGroup(String groupId, AsyncCallback<List<UserModel>> asyncCallback);

    /**
     * Returneaza un GroupModel dupa id-ul specificat.
     * @param id
     * @param callback
     */
	public abstract void getOrgUnitById(String id, AsyncCallback<OrganizationUnitModel> callback);

	/**
	 * Adauga sau modifica un grup (Organization unit) in organizatie. Daca obiectul 
	 * GroupModel are setat id-ul atunci face modificare altfel face adaugare.
	 * @param organizationUnitModel
	 * @param callback
	 */
	public abstract void setOrgUnit(OrganizationUnitModel organizationUnitModel, AsyncCallback<String> callback);
	
	/**
	 * Sterge un grup (Organization unit) din organizatie.
	 * @param groupModel
	 * @param callback
	 */
	public abstract void deleteOrgUnit(OrganizationUnitModel organizationUnitModel, AsyncCallback<Void> callback);

	/**
	 * Adauga sau modifica un user in organizatie. Daca obiectul UserModel are setat
	 * id-ul atunci face modificare altfel face adaugare.
	 * @param userModel
	 * @param callback
	 */
	public abstract void setUser(UserModel userModel, AsyncCallback<Void> callback);
	
	/**
	 * Sterge un user din organizatie. 
	 * @param userModel
	 * @param callback
	 */
	public abstract void deleteUser(UserModel userModel, AsyncCallback<Void> callback);

	/**
	 * Returneaza toata organizatia ca si un arbore.
	 * @param callback
	 */
	public abstract void getOrganization(AsyncCallback<OrganizationModel> callback);

	/**
	 * Returneaza un UserModel cu id-ul specificat.
	 * @param id
	 * @param callback
	 */
	public abstract void getUserById(String id, AsyncCallback<UserModel> callback);
	
	public abstract void getGroups(AsyncCallback<List<GroupModel>> asyncCallback);
	
	/**
	 * Returneaza grupul cu ID-ul dat.
	 * Daca grupul nu se gaseste, va returna null.
	 */
	public abstract void getGroupById(String id, AsyncCallback<GroupModel> callback);
	
	public abstract void setGroup(GroupModel groupModel, Collection<UserModel> userModels, AsyncCallback<String> callback);
	
	public abstract void deleteGroup(GroupModel groupModel, AsyncCallback<Void> asyncCallback);

	/**
	 * Muta un user sau un grup intr-un alt grup(sau in organizatie - in radacina).
	 * @param itemToMove
	 * @param destinationItem
	 * @param callback
	 */
	public abstract void move(ModelData itemToMove, ModelData destinationItem,
		AsyncCallback<Void> callback);
	
	public abstract void getAllUsernames(AsyncCallback<List<String>> callback);
	
	public abstract void getUsersWithUsername(String username, AsyncCallback<List<UserModel>> callback);

	/**
	 * Cauta utilizatori in director pe baza criteriilor specificate, utilizatori ale caror atribute CONTIN valorile specificate
	 * (se folosesc wildcards si la inceput si la sfarsit). Deci daca se introduce "on" la prenumele utilizatorului, se vor gasi
	 * si cei cu "Ion", si cei cu "Onisor", si cei cu "Ionel".
	 * Cautarea NU este case-sensitive.
	 */
	public abstract void findUsersInDirectory(GwtDirectoryUserSearchCriteria directoryUserSearchCriteria, AsyncCallback<List<DirectoryUserModel>> callback);
	
	public abstract void importOrganizationalStructureFromDirectory(AsyncCallback<Void> callback);
	
	public abstract void deactivateUserWithId(Long userId, GwtUserDeactivationMode deactivationMode, AsyncCallback<Void> callback);
	
	public abstract void reactivateUserWithId(Long userId, AsyncCallback<Void> callback);
	
	/**
	 * Returneaza numele grupurilor de care apartine utilizatorul cu ID-ul dat, ordonate dupa nume.
	 */
	public abstract void  getGroupNamesOfUserWithId(Long userId, AsyncCallback<List<String>> callback);
}