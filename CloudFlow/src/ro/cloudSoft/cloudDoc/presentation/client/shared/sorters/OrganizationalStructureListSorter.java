package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

/**
 * 
 */
public class OrganizationalStructureListSorter extends StoreSorter<ModelData> {
	
	@Override
	public int compare(Store<ModelData> store, ModelData model1, ModelData model2, String property) {
		
		String displayNameForModel1 = "";
		String displayNameForModel2 = "";
		
		if (model1 instanceof UserModel) {
			displayNameForModel1 = ((UserModel) model1).getDisplayName();
		} else if (model1 instanceof OrganizationUnitModel) {
			displayNameForModel1 = ((OrganizationUnitModel) model1).getName();
		} else if (model1 instanceof GroupModel) {
			displayNameForModel1 = ((GroupModel) model1).getName();
		}
		
		if (model2 instanceof UserModel) {
			displayNameForModel2 = ((UserModel) model2).getDisplayName();
		} else if (model2 instanceof OrganizationUnitModel) {
			displayNameForModel2 = ((OrganizationUnitModel) model2).getName();
		} else if (model2 instanceof GroupModel) {
			displayNameForModel2 = ((GroupModel) model2).getName();
		}
		
		return displayNameForModel1.compareToIgnoreCase(displayNameForModel2);
	}
}