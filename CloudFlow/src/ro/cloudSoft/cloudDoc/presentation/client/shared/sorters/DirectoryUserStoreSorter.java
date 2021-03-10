package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

public class DirectoryUserStoreSorter extends StoreSorter<DirectoryUserModel> {
	
	@Override
	public int compare(Store<DirectoryUserModel> store, DirectoryUserModel user1, DirectoryUserModel user2, String property) {
		return GwtCompareUtils.compareIgnoreCase(user1.getUsername(), user2.getUsername());
	}
}