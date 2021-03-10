package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.content.DocumentLocationServiceImpl;

/**
 * Interfata implementata de diverse plugin-uri in functie de tehnologia folosita
 * Toate functionalitatile implemetate in aceasta interfata sunt folosite de
 * componentele sistemului prin apelul DocumentLocationService.
 * Exista numai o singura implementare pentru JackRabbit
 * @see DocumentLocationServiceImpl
 * @see JR_DocumentLocationPlugin 
 * 
 */
public interface DocumentLocationPlugin {

	public ArrayList<DocumentLocation> getAllDocumentLocations(SecurityManager userSecurity) throws AppException;

    /**
     * Adauga sau modifica un document location.
     * @param documentLocation document location-ul
     * @param userSecurity datele utilizatorului curent
     * @return numele real al document location-ului
     * @throws AppException
     */
	public String saveDocumentLocation(DocumentLocation documentLocation, SecurityManager userSecurity) throws AppException;

	public List<Folder> getAllFolders(String documentLocationRealName, SecurityManager userSecurity) throws AppException;

	/**
	* Preluarea unui documentlocation dupa un nume real specificat
	* @param wkspName - numele real al obiectului DocLocation care identifica DL de unde se doreste sa se efectueze stergerea
	* @param userSecuity - datele de identificare ale utilizatorului logat
	* @return (doclocation)intoarce obiectul DocLocation care identifica Document Location din sistem
	* @throws ro.cloudSoft.cloudDoc.core.AppException
	*/
	public DocumentLocation getDocumentLocationByRealName(String realName, SecurityManager userSecurity) throws AppException;

    /**
     * Sterge un document location.
     * @param realName numele real al document location-ului
     * @param userSecurity datele utilizatorului curent
     * @throws AppException
     */
	public void deleteDocumentLocation(String realName, SecurityManager userSecurity) throws AppException;
	
	/** Verifica daca exista documente de un anumit tip in TOATE document location-urile. */
	boolean existDocumentsOfType(Long documentTypeId, SecurityManager userSecurity) throws AppException;
	
	Set<String> getAllDocumentLocationRealNames() throws AppException;
}