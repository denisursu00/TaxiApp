package ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public interface ReplacementProfilesGxtServiceAsync extends GxtServiceBaseAsync {

	void getVisibleReplacementProfiles(AsyncCallback<List<ReplacementProfileModel>> callback);
	
	void deleteReplacementProfileById(Long replacementProfileId, AsyncCallback<Void> callback);
	
	void getReplacementProfileById(Long replacementProfileId, AsyncCallback<ReplacementProfileModel> callback);
	
	void saveReplacementProfile(ReplacementProfileModel replacementProfile, AsyncCallback<Void> callback);
	
	void returned(Long replacementProfileId, AsyncCallback<Void> callback);
	
	/**
	 * Returneaza profilele de inlocuire in care solicitantul este inlocuitor,
	 * disponibile pentru selectare pentru profilul de inlocuire care face cererea (poate fi nou).
	 * Profilele de returnat trebuie sa aiba perioada intersectandu-se cu intervalul [data inceput, data sfarsit]
	 * si status-ul permis de aplicatie.
	 * 
	 * @param idForRequestingReplacementProfile ID-ul profilului de inlocuire pentru care se doreste selectarea, poate fi null (daca profilul este nou)
	 * @param idsForRequesterUserProfiles ID-urile profilelor persoanei care este solicitant si trebuie sa fie inlocuitor in profilele de returnat
	 * @param startDate data inceput
	 * @param endDate data sfarsit
	 */
	void getAvailableReplacementProfilesInWhichRequesterIsReplacement(Long idForRequestingReplacementProfile,
		Collection<Long> idsForRequesterUserProfiles, Date startDate, Date endDate,
		AsyncCallback<List<ReplacementProfileModel>> callback);
}