package ro.cloudSoft.cloudDoc.presentation.server.services.content;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.FolderGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.FolderConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.FolderService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.extjs.gxt.ui.client.data.ModelData;

public class FolderGxtServiceImpl extends GxtServiceImplBase implements FolderGxtService, InitializingBean {
	
	private FolderService folderService;

    public FolderGxtServiceImpl() {}

    public FolderGxtServiceImpl(FolderService folderSvc) {
        folderService = folderSvc;
    }
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			folderService
		);
	}

	@Override
    public List<ModelData> getFoldersFromFolder(String documentLocationRealName, String parentId) throws PresentationException {
    	List<ModelData> folderModels = new ArrayList<ModelData>();
    	
    	List<Folder> folders = null;
    	try {
    		folders = folderService.getAllFoldersFromFolder(parentId, documentLocationRealName, getSecurity());
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
	public FolderModel getFolderById(String folderId, String documentLocationRealName) throws PresentationException {
		Folder folder = null;
		try {
			folder = folderService.getFolderById(folderId, documentLocationRealName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		FolderModel folderModel = FolderConverter.getModelFromFolder(folder);
		return folderModel;
	}

	@Override
    public void saveFolder(FolderModel folderModel) throws PresentationException {
        Folder folder = FolderConverter.getFolderFromModel(folderModel);
        String documentLocationRealName = folderModel.getDocumentLocationRealName();

        try {
			folderService.saveFolder(folder, documentLocationRealName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
    }

	@Override
    public void deleteFolder(String folderId, String documentLocationRealName) throws PresentationException {
        try {
			folderService.deleteFolder(folderId, documentLocationRealName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
    }

	@Override
    public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName) throws PresentationException {
    	try {
            folderService.moveFolder(folderToMoveId, destinationFolderId, documentLocationRealName, getSecurity());
    	} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
    	}
    }
	
	@Override
	public List<String> getIdsForFolderHierarchy(String documentLocationRealName, String folderId) throws PresentationException {
		try {
			return folderService.getIdsForFolderHierarchy(documentLocationRealName, folderId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
    	}
	}
}