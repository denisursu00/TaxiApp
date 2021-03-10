package ro.cloudSoft.cloudDoc.presentation.client.client.events.listeners;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.DateMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.UserMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Listener ce se ocupa cu actualizarea listei cu posibili inlocuitori
 * pentru cererile de concediu, in functie de perioada de concediu aleasa
 * 
 * 
 */
public class InlocuitorOptionUpdaterEventListener implements Listener<BaseEvent> {
	
	private final UserMetadataField inlocuitorMetadataField;
	
	private final DateMetadataField dataInceputMetadataField;
	private final DateMetadataField dataSfarsitMetadataField;
	
	public InlocuitorOptionUpdaterEventListener(UserMetadataField inlocuitorMetadataField,
			DateMetadataField dataInceputMetadataField, DateMetadataField dataSfarsitMetadataField) {
		
		this.inlocuitorMetadataField = inlocuitorMetadataField;
		
		this.dataInceputMetadataField = dataInceputMetadataField;
		this.dataSfarsitMetadataField = dataSfarsitMetadataField;
	}
	
	/**
	 * Actualizeaza inlocuitorul selectat.
	 * Daca nu era selectat nici un inlocuitor inainte, asa va ramane si acum.
	 * Daca era selectat un inlocuitor, dar nu mai e disponibil, campul va deveni gol.
	 * Daca era selectat un inlocuitor care este si acum disponibil, inlocuitorul va ramane cel vechi.
	 */
	private void updateSelectedInlocuitor(UserModel userInOldStore) {
		
		if (userInOldStore == null) {
			return;
		}
		
		String userIdAsString = userInOldStore.getUserId();
		UserModel userInNewStore = inlocuitorMetadataField.getStore().findModel(UserModel.USER_PROPERTY_USERID, userIdAsString);
		if (userInNewStore != null) {
			inlocuitorMetadataField.setValue(userInNewStore);
		}
	}
	
	/**
	 * Verifica disponibilitatea utilizatorilor ca inlocuitori, in functie de perioada concediului,
	 * si actualizeaza lista cu cei disponibili. Daca inainte de rulare, era selectat un inlocuitor
	 * care acum nu mai e disponibil, utilizatorul va trebui sa selecteze un nou inlocuitor.
	 */
	public void checkAndUpdate() {
		
		Date dataInceput = dataInceputMetadataField.getValue();
		Date dataSfarsit = dataSfarsitMetadataField.getValue();
		
		final UserModel previouslySelectedUserAsInlocuitor = inlocuitorMetadataField.getValue();
		
		inlocuitorMetadataField.setValue(null);
		inlocuitorMetadataField.getStore().removeAll();
		
		if ((dataInceput != null) && (dataSfarsit != null)) {
			LoadingManager.get().loading();
			GwtServiceProvider.getCereriDeConcediuService().getIdsForUsersInConcediu(dataInceput, dataSfarsit, new AsyncCallback<Collection<Long>>() {
				
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(Collection<Long> idsForUsersInConcediu) {
					
					List<UserModel> allUsers = AppStoreCache.getUserListStore().getModels();
					List<UserModel> usersAvailableForInlocuitor = new LinkedList<UserModel>();
					
					for (UserModel user : allUsers) {
						
						String userIdAsString = user.getUserId();
						Long userId = Long.valueOf(userIdAsString);
						
						if (!idsForUsersInConcediu.contains(userId)) {
							usersAvailableForInlocuitor.add(user);
						}
					}
					
					inlocuitorMetadataField.getStore().add(usersAvailableForInlocuitor);

					updateSelectedInlocuitor(previouslySelectedUserAsInlocuitor);

					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			
			List<UserModel> allUsers = AppStoreCache.getUserListStore().getModels();
			inlocuitorMetadataField.getStore().add(allUsers);
			
			updateSelectedInlocuitor(previouslySelectedUserAsInlocuitor);
		}
	}

	@Override
	public void handleEvent(BaseEvent be) {
		checkAndUpdate();
	}
}