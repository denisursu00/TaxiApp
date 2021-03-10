package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentLocationGxtServiceAsync extends GxtServiceBaseAsync {

    void getAllDocumentLocations(AsyncCallback<List<DocumentLocationModel>> callback);

    void getAllDocumentLocationsAsModelData(AsyncCallback<List<ModelData>> callback);
    
    void getDocumentLocation(DocumentLocationModel documentLocation, AsyncCallback<List<ModelData>> callback);

    void getFoldersFromDocumentLocation(String documentLocationRealName, AsyncCallback<List<ModelData>> callback);

    void saveDocumentLocation(DocumentLocationModel documentLocationModel, AsyncCallback<String> callback);

    void deleteDocumentLocation(String realName, AsyncCallback<Void> callback);

    void getDocumentLocationByRealName(String realName, AsyncCallback<DocumentLocationModel> callback);
}