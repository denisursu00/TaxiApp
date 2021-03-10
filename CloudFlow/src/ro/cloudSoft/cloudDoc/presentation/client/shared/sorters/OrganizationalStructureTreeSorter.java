package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;

public class OrganizationalStructureTreeSorter extends StoreSorter<ModelData> {

	@Override
	public int compare(Store<ModelData> store, ModelData m1, ModelData m2, String property) {
		
		// Daca ambele entitati sunt unitati organizatorice...
		if ((m1 instanceof OrganizationUnitModel) && (m2 instanceof OrganizationUnitModel)) {
			
			OrganizationUnitModel organizationUnit1 = (OrganizationUnitModel) m1;
			OrganizationUnitModel organizationUnit2 = (OrganizationUnitModel) m2;
			
			return organizationUnit1.getName().compareToIgnoreCase(organizationUnit2.getName());
		}
		
		// Daca ambele entitati sunt utilizatori...
		if ((m1 instanceof UserModel) && (m2 instanceof UserModel)) {
			
			UserModel user1 = (UserModel) m1;
			UserModel user2 = (UserModel) m2;
			
			// Store-ul apartine unui arbore, deci il luam ca atare.
			TreeStore<ModelData> treeStore = (TreeStore<ModelData>) store;
			/*
			 * Luam parintele primului utilizator.
			 * Presupunem ca cei doi utilizatori sunt pe acelasi nivel,
			 * deci au acelasi parinte.
			 */
			ModelData parentEntity = treeStore.getParent(user1);
			// Daca parintele este unitate organizatorica...
			if (parentEntity instanceof OrganizationUnitModel) {
				// Ia ID-ul manager-ului.
				String managerId = ((OrganizationUnitModel) parentEntity).getManagerId();
				// Daca are manager...
				if (managerId != null) {
					// Daca utilizatorul 1 este manager, atunci va aparea deasupra.
					if (managerId.equals(user1.getUserId())) {
						return -1;
					}
					// Daca utilizatorul 2 este manager, atunci va aparea deasupra.
					if (managerId.equals(user2.getUserId())) {
						return 1;
					}
				}
			}
			/*
			 * Daca utilizatorii nu au parinte sau daca parintele nu are
			 * manager, atunci se va face o comparatie pe numele celor doi.
			 */
			return user1.getName().compareToIgnoreCase(user2.getName());
		}
		
		// Unitatile organizatorice trebuie sa apara sub utilizatori.
		if ((m1 instanceof OrganizationUnitModel) && (m2 instanceof UserModel)) {
			return 1;
		}
		
		// Utilizatorii trebuie sa apara deasupra unitatilor organizatorice.
		if ((m1 instanceof UserModel) && (m2 instanceof OrganizationUnitModel)) {
			return -1;
		}
		
		return 0;
	}
}