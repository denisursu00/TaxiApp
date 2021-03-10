package ro.cloudSoft.cloudDoc.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface FolderService {

    public List<Folder> getAllFoldersFromFolder(String parentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public void deleteFolder(String folderId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public void saveFolder(Folder folder, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public Folder getFolderById(String folderId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;
    
    /**
     * Elimina restrictia pe tipul de document permis pentru toate folderele
     * care il au ca restrictie pe tipul de document cu ID-ul dat.
     */
    public void removeDocumentTypeRestrictionForAllFolders(Long documentTypeId, SecurityManager userSecurity) throws AppException;

    /**
     * Returneaza ID-urile folderelor care fac parte din ierarhia folder-ului cu ID-ul dat.
     * Lista cu ID-uri este ordonata de la cel mai indepartat ascendent pana la parintele direct al folder-ului.
     * Exemplu: folder1\folder2\folder3\folderCuIdDat => [(ID folder1), (ID folder2), (ID folder3)]).
     */
    public List<String> getIdsForFolderHierarchy(String documentLocationRealName, String folderId) throws AppException;
 
    public String getFolderPath(String documentLocationRealName, String folderId, SecurityManager userSecurity) throws AppException ;
}