package ro.cloudSoft.cloudDoc.presentation.client.shared.sorters;

import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

/**
 * Ordoneaza utilizatorii in functie de status-ul lor - daca sunt activi sau nu.
 * Utilizatorii activi apar mai intai, apoi cei inactivi.
 */
public class UsersByActiveStatusListSorter extends StoreSorter<UserModel> {
	
	private final Set<Long> idsOfActiveUsers;
	
	public UsersByActiveStatusListSorter(Set<Long> idsOfActiveUsers) {
		this.idsOfActiveUsers = idsOfActiveUsers;
	}

	@Override
	public int compare(Store<UserModel> store, UserModel user1, UserModel user2, String property) {
		
		Long idOfUser1 = user1.getUserIdAsLong();
		Long idOfUser2 = user2.getUserIdAsLong();
		
		if (idsOfActiveUsers.contains(idOfUser1) && !idsOfActiveUsers.contains(idOfUser2)) {
			// Utilizatorul 1 este activ, iar 2 inactiv, deci 1 trebuie sa apara mai sus in lista decat 2.
			return -1;
		}
		if (!idsOfActiveUsers.contains(idOfUser1) && idsOfActiveUsers.contains(idOfUser2)) {
			// Utilizatorul 1 este inactiv, iar 2 activ, deci 1 trebuie sa apara mai jos in lista decat 2.
			return 1;
		}
		
		return user1.getName().compareToIgnoreCase(user2.getName());
	}
}