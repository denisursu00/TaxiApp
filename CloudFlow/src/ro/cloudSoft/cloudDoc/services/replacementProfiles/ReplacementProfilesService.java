package ro.cloudSoft.cloudDoc.services.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * 
 */
public interface ReplacementProfilesService {

	/**
	 * Returneaza toate profilele de inlocuire vizibile utilizatorului curent,
	 * ordonate dupa username-ul solicitantului (crescator), apoi dupa data de inceput (descrescator).
	 */
	List<ReplacementProfile> getVisibleReplacementProfiles(SecurityManager userSecurity) throws AppException;
	
	/**
	 * Sterge profilul de inlocuire cu ID-ul dat.
	 * 
	 * @throws NoSuchElementException daca nu se gaseste profilul de inlocuire cu ID-ul dat
	 * @throws AppException daca NU este permisa stergerea din cauza drepturilor utilizatorului sau din cauza starii profilului 
	 */
	void deleteReplacementProfileById(Long replacementProfileId, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Returneaza profilul de inlocuire cu ID-ul dat.
	 * 
	 * @throws NoSuchElementException daca nu se gaseste profilul de inlocuire cu ID-ul dat
	 * @throws AppException daca utilizatorul NU are drept de acces asupra profilului
	 */
	ReplacementProfile getReplacementProfileById(Long replacementProfileId, SecurityManager userSecurity) throws AppException;

	/**
	 * Salveaza profilul de inlocuire dat.
	 * 
	 * @throws AppException daca profilul NU este valid sau daca NU este permisa salvarea
	 * din cauza drepturilor utilizatorului sau din cauza starii profilului 
	 */
	void saveReplacementProfile(ReplacementProfile replacementProfile, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Notifica aplicatia ca titularul a revenit.
	 */
	void returned(Long replacementProfileId, SecurityManager userSecurity) throws AppException;
	
	/**
	 * Activeaza profilele de inlocuire care au inceput mai devreme sau in data de referinta data si sunt inca inactive.
	 * Vor fi actualizate activitatile persoanelor inlocuite, cat si cele ale inlocuitorilor.
	 */
	void activateReplacementProfilesThatBegan(Date referenceDate) throws AppException;

	/**
	 * Expira profilele de inlocuire care s-au terminat mai devreme sau in data de referinta data si sunt inca active.
	 * Vor fi actualizate activitatile persoanelor inlocuite, cat si cele ale inlocuitorilor.
	 */
	void expireReplacementProfilesThatEnded(Date referenceDate) throws AppException;
	
	Collection<ReplacementProfile> getReplacementProfilesWithIds(Collection<Long> replacementProfileIds);
	
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
	List<ReplacementProfile> getAvailableReplacementProfilesInWhichRequesterIsReplacement(
		Long idForRequestingReplacementProfile, Collection<Long> idsForRequesterUserProfiles,
		Date startDate, Date endDate);
}