package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

import com.extjs.gxt.ui.client.data.ModelData;

public interface DocumentLocationGxtService extends GxtServiceBase {

    List<DocumentLocationModel> getAllDocumentLocations() throws PresentationException;

    List<ModelData> getAllDocumentLocationsAsModelData() throws PresentationException;
    
    List<ModelData> getDocumentLocation(DocumentLocationModel documentLocation);
    
    List<ModelData> getFoldersFromDocumentLocation(String documentLocationRealName) throws PresentationException;
    
    String saveDocumentLocation(DocumentLocationModel documentLocationModel) throws PresentationException;
    
    void deleteDocumentLocation(String realName) throws PresentationException;
    
    DocumentLocationModel getDocumentLocationByRealName(String realName) throws PresentationException;
}