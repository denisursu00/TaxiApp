package ro.cloudSoft.cloudDoc.services.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * Serviciul folosit pentru manipularea DocLocations din sistem. Aceasta interfata este implementata de
 * <b> DocLocartionServiceImpl</b>
 * @see DocumentLocationServiceImpl
 * 
 */
public interface DocumentLocationService {

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
    public DocumentLocation getDocumentLocationByRealName(String wkspName, SecurityManager userSecurity) throws AppException;

    /**
     * Sterge un document location.
     * @param realName numele real al document location-ului
     * @param userSecurity datele utilizatorului curent
     * @throws AppException
     */
    public void deleteDocumentLocation(String realName, SecurityManager userSecurity) throws AppException;
    
    Set<String> getAllDocumentLocationRealNames() throws AppException;
}