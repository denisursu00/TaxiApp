package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

import com.extjs.gxt.ui.client.data.ModelData;

public interface FolderGxtService extends GxtServiceBase {

    public List<ModelData> getFoldersFromFolder(String documentLocationRealName, String parentId) throws PresentationException;
    
    public FolderModel getFolderById(String folderId, String documentLocationRealName) throws PresentationException;
    
    public void saveFolder(FolderModel folderModel) throws PresentationException;
    
    public void deleteFolder(String folderId, String documentLocationRealName) throws PresentationException;
    
    public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName) throws PresentationException;

    /**
     * Returneaza ID-urile folderelor care fac parte din ierarhia folder-ului cu ID-ul dat.
     * Lista cu ID-uri este ordonata de la cel mai indepartat ascendent pana la parintele direct al folder-ului.
     * Exemplu: folder1\folder2\folder3\folderCuIdDat => [(ID folder1), (ID folder2), (ID folder3)]).
     */
    public List<String> getIdsForFolderHierarchy(String documentLocationRealName, String folderId) throws PresentationException;
}