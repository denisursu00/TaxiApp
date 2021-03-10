package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

public class GroupListSorter extends StoreSorter<GroupModel> {

	@Override
	public int compare(Store<GroupModel> store, GroupModel group1, GroupModel group2, String property) {
		return group1.getName().compareToIgnoreCase(group2.getName());
	}
}