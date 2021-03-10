package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;

/**
 * 
 */
public interface ReplacementProfileDao {

	/**
	 * Returneaza toate profilele de inlocuire, ordonate dupa username-ul
	 * solicitantului (crescator), apoi dupa data de inceput (descrescator).
	 */
	List<ReplacementProfile> getAllReplacementProfiles();
	
	/**
	 * Returneaza profilele de inlocuire in care utilizatorul cu username-ul
	 * dat este titular, ordonate dupa data de inceput (descrescator).
	 * Cautarea dupa username NU este case-sensitive.
	 */
	List<ReplacementProfile> getReplacementProfilesForRequesterWithUsername(String requesterUsername);
	
	/**
	 * Returneaza profilul de inlocuire care are ID-ul dat.
	 * 
	 * @throws NoSuchElementException daca nu se gaseste profilul de inlocuire cu ID-ul dat
	 */
	ReplacementProfile getReplacementProfileById(Long replacementProfileId);
	
	void deleteReplacementProfile(ReplacementProfile replacementProfile);
	
	void createOrUpdateReplacementProfile(ReplacementProfile replacementProfile);
	
	/**
	 * Verifica daca exista profile de inlocuire care au printre profilele de utilizatori selectate
	 * ale titularului ORICARE din profilele date si care au perioada ce se intersecteaza cu cea specificata.
	 * Din cautare se va exclude profilul de inlocuire cu ID-ul dat, daca ID-ul este diferit de null.
	 * Din cautare vor fi excluse profilele de inlocuire cu status-urile date.
	 */
	boolean replacementProfilesForUserProfilesInPeriodExist(Collection<User> requesterUserProfiles,
		Date startDate, Date endDate, Long idForReplacementProfileToBeExcluded,
		Collection<ReplacementProfileStatusOption> statusesToBeExcluded);
	
	/**
	 * Returneaza profilul de inlocuire activ pentru utilizatorul cu ID-ul dat.
	 * Daca nu se gaseste un profil activ, atunci va returna null.
	 * 
	 * @throws IllegalStateException daca se gaseste mai mult de un profil activ
	 */
	ReplacementProfile getActiveReplacementProfileForUser(Long userId);
	
	/**
	 * Returneaza profilele de inlocuire active pentru utilizatorii cu ID-urile date.
	 * Daca nu se gasesc profile, atunci va fi returnata o colectie goala.
	 */
	Collection<ReplacementProfile> getActiveReplacementProfileForUsers(Collection<Long> userIds);
	
	/**
	 * Returneaza profilul de inlocuire activ in care inlocuitorul profilului dat este titular si pentru care exista o legatura cu profilul dat.
	 * Daca nu se gaseste un profil, va returna null.
	 */
	ReplacementProfile getActiveReplacementProfileWhereReplacementIsRequester(ReplacementProfile replacementProfileWithReplacement);
	
	/**
	 * Returneaza profilele de inlocuire inactive care au inceput mai devreme sau in data de referinta data.
	 */
	Collection<ReplacementProfile> getInactiveReplacementProfilesThatBegan(Date referenceDate);
	
	/**
	 * Returneaza profilele de inlocuire active care s-au terminat mai devreme sau in data de referinta data.
	 */
	Collection<ReplacementProfile> getActiveReplacementProfilesThatEnded(Date referenceDate);
	
	Collection<ReplacementProfile> getReplacementProfilesWithIds(Collection<Long> replacementProfileIds);
	
	/**
	 * Returneaza profilele de inlocuire ale inlocuitorilor cu ID-urile date
	 * care au perioada intersectandu-se cu intervalul [data inceput, data sfarsit]
	 * si care au unul din status-urile specificate.
	 */
	List<ReplacementProfile> getReplacementProfilesOfReplacementsWithOverlappingDateIntervalWithStatuses(
		Collection<Long> idsForReplacementUsers, Date startDate, Date endDate,
		Collection<ReplacementProfileStatusOption> allowedStatuses);
}