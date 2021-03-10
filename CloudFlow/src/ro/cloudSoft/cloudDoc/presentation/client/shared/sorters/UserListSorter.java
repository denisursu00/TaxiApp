package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

public class UserListSorter extends StoreSorter<UserModel> {

	@Override
	public int compare(Store<UserModel> store, UserModel user1, UserModel user2, String property) {
		return user1.getName().compareToIgnoreCase(user2.getName());
	}
}