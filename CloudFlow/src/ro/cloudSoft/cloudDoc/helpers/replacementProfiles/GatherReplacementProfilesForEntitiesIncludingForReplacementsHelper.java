package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;

/**
 * 
 */
public interface GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper {
	
	/**
	 * Returneaza profilele de inlocuire active pentru un utilizator.
	 * Vor fi incluse si profilele de inlocuire ale inlocuitorilor (al utilizatorului si al inlocuitorului inlocuitorului s.a.m.d.).
	 * Profilele de inlocuire vor fi returnate sub forma unui Map in care cheia este titularul profilului de inlocuire, care este valoarea.
	 */
	Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfilesForUserIncludingForReplacements(Long userId);
	
	/**
	 * Returneaza profilele de inlocuire active pentru utilizatorii care fac parte din unitatea organizatorica cu ID-ul dat.
	 * Vor fi incluse si profilele de inlocuire ale inlocuitorilor (al fiecarui utilizator si al inlocuitorului inlocuitorului s.a.m.d.).
	 * Profilele de inlocuire vor fi returnate sub forma unui Map in care cheia este fie unitatea organizatorica
	 * (in cazul in care utilizatorul care face parte din unitatea organizatorica este inlocuit)
	 * sau titularul profilului de inlocuire (daca este vorba de un inlocuitor inlocuit).
	 */
	Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfileForOrganizationUnitIncludingForReplacements(Long organizationUnitId);

	/**
	 * Returneaza profilele de inlocuire active pentru utilizatorii care fac parte din grupul cu ID-ul dat.
	 * Vor fi incluse si profilele de inlocuire ale inlocuitorilor (al fiecarui utilizator si al inlocuitorului inlocuitorului s.a.m.d.).
	 * Profilele de inlocuire vor fi returnate sub forma unui Map in care cheia este fie grupul
	 * (in cazul in care utilizatorul care face parte din grup este inlocuit)
	 * sau titularul profilului de inlocuire (daca este vorba de un inlocuitor inlocuit).
	 */
	Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfileForGroupIncludingForReplacements(Long groupId);
}