package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FolderGxtServiceAsync extends GxtServiceBaseAsync {

    public void getFoldersFromFolder(String documentLocationRealName, String parentId, AsyncCallback<List<ModelData>> asyncCallback);
    
    public void getFolderById(String folderId, String documentLocationRealName, AsyncCallback<FolderModel> callback);

    public void saveFolder(FolderModel folderModel, AsyncCallback<Void> asyncCallback);

    public void deleteFolder(String folderId, String wkspRealName, AsyncCallback<Void> asyncCallback);

    public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName, AsyncCallback<Void> callback);
    
    /**
     * Returneaza ID-urile folderelor care fac parte din ierarhia folder-ului cu ID-ul dat.
     * Lista cu ID-uri este ordonata de la cel mai indepartat ascendent pana la parintele direct al folder-ului.
     * Exemplu: folder1\folder2\folder3\folderCuIdDat => [(ID folder1), (ID folder2), (ID folder3)]).
     */
    public void getIdsForFolderHierarchy(String documentLocationRealName, String folderId, AsyncCallback<List<String>> callback);
}