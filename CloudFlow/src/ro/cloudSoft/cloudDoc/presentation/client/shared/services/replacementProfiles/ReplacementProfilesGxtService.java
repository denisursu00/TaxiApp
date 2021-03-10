package ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

/**
 * 
 */
public interface ReplacementProfilesGxtService extends GxtServiceBase {

	List<ReplacementProfileModel> getVisibleReplacementProfiles() throws PresentationException;
	
	void deleteReplacementProfileById(Long replacementProfileId) throws PresentationException;
	
	ReplacementProfileModel getReplacementProfileById(Long replacementProfileId) throws PresentationException;
	
	void saveReplacementProfile(ReplacementProfileModel replacementProfile) throws PresentationException, AppException;
	
	void returned(Long replacementProfileId) throws PresentationException;
	
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
	List<ReplacementProfileModel> getAvailableReplacementProfilesInWhichRequesterIsReplacement(
		Long idForRequestingReplacementProfile, Collection<Long> idsForRequesterUserProfiles,
		Date startDate, Date endDate) throws PresentationException;
}