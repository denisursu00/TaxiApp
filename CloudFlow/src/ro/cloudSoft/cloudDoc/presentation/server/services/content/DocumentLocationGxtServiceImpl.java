package ro.cloudSoft.cloudDoc.presentation.server.services.content;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentLocationGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentLocationConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.FolderConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.DocumentLocationService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.common.collect.Lists;

public class DocumentLocationGxtServiceImpl extends GxtServiceImplBase implements DocumentLocationGxtService, InitializingBean {
	
	private DocumentLocationService documentLocationService;

    public DocumentLocationGxtServiceImpl() {}

    public DocumentLocationGxtServiceImpl(DocumentLocationService docLocationSvc) {
        documentLocationService = docLocationSvc;
    }
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentLocationService
		);
	}

	@Override
    public List<DocumentLocationModel> getAllDocumentLocations() throws PresentationException {
        List<DocumentLocationModel> lista = new ArrayList<DocumentLocationModel>();

        List<DocumentLocation> listaDocloc = null;
        try {
            listaDocloc = documentLocationService.getAllDocumentLocations(getSecurity());
        } catch (AppException ae) {
            Logger.getLogger(DocumentLocationGxtServiceImpl.class.getName()).log(Level.SEVERE, null, ae);
            throw PresentationExceptionUtils.getPresentationException(ae);
        }
   
        if (listaDocloc != null) {
            for (DocumentLocation docloc : listaDocloc) {
                DocumentLocationModel model = DocumentLocationConverter.getModelFromDocumentLocation(docloc);
                lista.add(model);
            }
        }

        return lista;
    }
	
	@Override
	public List<ModelData> getAllDocumentLocationsAsModelData() throws PresentationException {
		
		List<DocumentLocation> allDocumentLocations = null;
		try {
			allDocumentLocations = documentLocationService.getAllDocumentLocations(getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		List<ModelData> allDocumentLocationModelsAsModelData = Lists.newArrayListWithCapacity(allDocumentLocations.size());
		for (DocumentLocation documentLocation : allDocumentLocations) {
			DocumentLocationModel documentLocationModel = DocumentLocationConverter.getModelFromDocumentLocation(documentLocation);
			allDocumentLocationModelsAsModelData.add(documentLocationModel);
		}
		return allDocumentLocationModelsAsModelData;
	}

	@Override
    public DocumentLocationModel getDocumentLocationByRealName(String realName) throws PresentationException {
        DocumentLocation docloc = null;
		try {
			docloc = documentLocationService.getDocumentLocationByRealName(realName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
        DocumentLocationModel model = DocumentLocationConverter.getModelFromDocumentLocation(docloc);    
        return model;
    }

	@Override
    public List<ModelData> getDocumentLocation(DocumentLocationModel documentLocation) {
    	List<ModelData> documentLocationList = new ArrayList<ModelData>();
    	documentLocationList.add(documentLocation);
    	return documentLocationList;
    }

	@Override
    public List<ModelData> getFoldersFromDocumentLocation(String documentLocationRealName) throws PresentationException {
    	List<ModelData> folderModels = new ArrayList<ModelData>();
    	
    	List <Folder> folders = null;
    	try {
    		folders = documentLocationService.getAllFolders(documentLocationRealName, getSecurity());
    	} catch (AppException ae) {
    		throw PresentationExceptionUtils.getPresentationException(ae);
    	}
    	
    	for (Folder folder : folders) {
    		FolderModel folderModel = FolderConverter.getModelFromFolder(folder);
    		folderModel.setDocumentLocationRealName(documentLocationRealName);
    		folderModels.add(folderModel);
    	}
    	
    	return folderModels;
    }

	@Override
    public String saveDocumentLocation(DocumentLocationModel documentLocationModel) throws PresentationException {    	
        DocumentLocation documentLocation = DocumentLocationConverter.getDocLocationFromModel(documentLocationModel);
        try {
			return documentLocationService.saveDocumentLocation(documentLocation, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
    }

	@Override
    public void deleteDocumentLocation(String realName) throws PresentationException {
        try {
			documentLocationService.deleteDocumentLocation(realName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
    }
}