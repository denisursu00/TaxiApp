package ro.cloudSoft.cloudDoc.presentation.client.shared.data;

import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureTreeSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.UsersByActiveStatusListSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRunnableUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.loading.MultipleLoadingsHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.loading.MultipleLoadingsHelperNotifier;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Clasa reprezinta un container pentru datele comune ale aplicatiei care
 * indeplinesc urmatoarele criterii:<br>
 * - Au un volum mare.<br>
 * - Necesita un timp mare de incarcare.<br>
 * - Sunt actualizate rar.
 * 
 * 
 */
public class AppStoreCache {

	private static ListStore<GroupModel> groupListStore;
	private static TreeStore<ModelData> organizationTreeStore;
	private static ListStore<UserModel> userListStore;
	
	public static ListStore<GroupModel> getGroupListStore() {
		return getGroupListStore(GwtRunnableUtils.DO_NOTHING);
	}
	
	private static ListStore<GroupModel> getGroupListStore(final Runnable doAfterLoading) {
		if (groupListStore == null) {
			// Creeaza un nou store.
			groupListStore = new ListStore<GroupModel>();
			// Configureaza ordonarea.
			groupListStore.setStoreSorter(new StoreSorter<GroupModel>() {
				@Override
				public int compare(Store<GroupModel> store, GroupModel group1, GroupModel group2, String property) {
					return GwtCompareUtils.compareIgnoreCase(group1.getName(), group2.getName());
				}
			});
			// Nu merge sortarea de mai jos (desi ar trebui)...
			//groupListStore.setDefaultSort(GroupModel.PROPERTY_NAME, SortDir.ASC);
			/*
			 * Incarca datele asincron. Elementele de interfata care folosesc
			 * store-ul au listener ce update-eaza valorile afisate odata cu
			 * reincarcarea store-ului.
			 */
			LoadingManager.get().loading();
			GwtServiceProvider.getOrgService().getGroups(new AsyncCallback<List<GroupModel>>() {
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				@Override
				public void onSuccess(List<GroupModel> groupList) {
					
					groupListStore.removeAll();
					groupListStore.add(groupList);
					
					doAfterLoading.run();
					
					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			doAfterLoading.run();
		}
		return groupListStore;
	}
	
	public static TreeStore<ModelData> getOrganizationTreeStore() {
		return getOrganizationTreeStore(GwtRunnableUtils.DO_NOTHING);
	}
	
	private static TreeStore<ModelData> getOrganizationTreeStore(final Runnable doAfterLoading) {
		if (organizationTreeStore == null) {
			// Creeaza un nou store.
			organizationTreeStore = new TreeStore<ModelData>();
			// Configureaza ordonarea.
			organizationTreeStore.setStoreSorter(new OrganizationalStructureTreeSorter());
			/*
			 * Incarca datele asincron. Elementele de interfata care folosesc
			 * store-ul au listener ce update-eaza valorile afisate odata cu
			 * reincarcarea store-ului.
			 */
			LoadingManager.get().loading();
			GwtServiceProvider.getOrgService().getOrganization(new AsyncCallback<OrganizationModel>() {
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				public void onSuccess(OrganizationModel organization) {
					
					organizationTreeStore.removeAll();
					organizationTreeStore.add(organization, true);
					
					doAfterLoading.run();
					
					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			doAfterLoading.run();
		}
		// Returneaza referinta la store.
		return organizationTreeStore;
	}
	
	public static ListStore<UserModel> getUserListStore() {
		return getUserListStore(GwtRunnableUtils.DO_NOTHING);
	}
	
	private static ListStore<UserModel> getUserListStore(final Runnable doAfterLoading) {
		if (userListStore == null) {
			// Creeaza un nou store.
			userListStore = new ListStore<UserModel>();

			LoadingManager.get().loading();
			GwtServiceProvider.getOrgService().getIdsOfActiveUsers(new AsyncCallback<Set<Long>>() {
				
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(Set<Long> idsOfActiveUsers) {

					userListStore.setStoreSorter(new UsersByActiveStatusListSorter(idsOfActiveUsers));
					
					/*
					 * Incarca datele asincron. Elementele de interfata care folosesc
					 * store-ul au listener ce update-eaza valorile afisate odata cu
					 * reincarcarea store-ului.
					 */
					LoadingManager.get().loading();
					GwtServiceProvider.getOrgService().getUsers(new AsyncCallback<List<UserModel>>() {
						
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						
						@Override
						public void onSuccess(List<UserModel> userList) {
							
							userListStore.removeAll();
							userListStore.add(userList);
							
							doAfterLoading.run();
							
							LoadingManager.get().loadingComplete();
						}			
					});

					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			doAfterLoading.run();
		}
		// Returneaza referinta la store.
		return userListStore;
	}

	/**
	 * Incarca toate datele in cache.
	 */
	public static void loadAll() {
		loadAll(GwtRunnableUtils.DO_NOTHING);
	}
	
	/**
	 * Incarca toate datele in cache.
	 */
	public static void loadAll(final Runnable doAfterLoading) {
		
		MultipleLoadingsHelper multipleLoadingsHelper = new MultipleLoadingsHelper(3) {
			
			@Override
			protected void doAfterLoadingsComplete() {
				doAfterLoading.run();
			}
		};
		MultipleLoadingsHelperNotifier multipleLoadingsHelperNotifier = new MultipleLoadingsHelperNotifier(multipleLoadingsHelper);
		
		getGroupListStore(multipleLoadingsHelperNotifier);
		getOrganizationTreeStore(multipleLoadingsHelperNotifier);
		getUserListStore(multipleLoadingsHelperNotifier);
	}
}