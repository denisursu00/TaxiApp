package ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class OrganizationalStructureIconProvider implements ModelIconProvider<ModelData> {
	
	private final TreeStore<ModelData> treeStore;
	
	public OrganizationalStructureIconProvider(TreeStore<ModelData> treeStore) {
		this.treeStore = treeStore;
	}

	@Override
	public AbstractImagePrototype getIcon(ModelData model) {
		if (model instanceof OrganizationModel) {
			return IconHelper.createStyle("icon-ogranization", 16, 16);
		} else if (model instanceof OrganizationUnitModel) {
			return IconHelper.createStyle("icon-orgUnit", 16, 16);
		} else if (model instanceof UserModel) {
			UserModel user = (UserModel) model;
			
			// Ia parintele utilizatorului.
			ModelData parentEntity = treeStore.getParent(user);
			
			// Daca parintele este unitate organizatorica...
			if (parentEntity instanceof OrganizationUnitModel) {
				// Ia ID-ul manager-ului.
				String managerId = ((OrganizationUnitModel) parentEntity).getManagerId();
				// Daca are manager si este utilizatorul la care s-a ajuns...
				if ((managerId != null) && managerId.equals(user.getUserId())) {
					return IconHelper.createStyle("icon-userManager", 16, 16);
				}
			}
			else if (parentEntity instanceof OrganizationModel) {
				// Ia ID-ul manager-ului.
				String managerId = ((OrganizationModel) parentEntity).getManagerId();
				// Daca are manager si este utilizatorul la care s-a ajuns...
				if ((managerId != null) && managerId.equals(user.getUserId())) {
					return IconHelper.createStyle("icon-userManager", 16, 16);
				}
			}
			return IconHelper.createStyle("icon-user", 16, 16);
		} else {
			return null;
		}
	}
}