package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.FolderPlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class FolderServiceImpl implements FolderService, InitializingBean {

	private final static String FOLDER_PATH_SEPARATOR = "\\";
	
	private DocumentLocationService documentLocationService;	
    private FolderPlugin plugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentLocationService,
			plugin
		);
	}

    @Override
    public void saveFolder(Folder fol, String wrkspName, SecurityManager userSecurity) throws AppException {
        plugin.saveFolder(fol, wrkspName, userSecurity);
    }

    @Override
    public Folder getFolderById(String folId, String wkspName, SecurityManager userSecurity) throws AppException {
        return plugin.getFolderById(folId, wkspName, userSecurity);
    }

    @Override
    public List<Folder> getAllFoldersFromFolder(String parentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
        return plugin.getAllFoldersFromFolder(parentId, documentLocationRealName, userSecurity);
    }

    @Override
    public void deleteFolder(String folderId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
        plugin.deleteFolder(folderId, documentLocationRealName, userSecurity);
    }

    @Override
    public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
        plugin.moveFolder(folderToMoveId, destinationFolderId, documentLocationRealName, userSecurity);
    }
    
    @Override
    public void removeDocumentTypeRestrictionForAllFolders(Long documentTypeId, SecurityManager userSecurity) throws AppException {
    	Collection<String> allDocumentLocationRealNames = documentLocationService.getAllDocumentLocationRealNames();
    	plugin.removeDocumentTypeRestrictionForAllFolders(allDocumentLocationRealNames, documentTypeId, userSecurity);
    }
    
    @Override
    public List<String> getIdsForFolderHierarchy(String documentLocationRealName, String folderId) throws AppException {
    	return plugin.getIdsForFolderHierarchy(documentLocationRealName, folderId);
    }

	@Override
	public String getFolderPath(String documentLocationRealName, String folderId, SecurityManager userSecurity) throws AppException {
		List<String> folderPathItems = Lists.newArrayList();
		
		String documentLocationName = documentLocationService.getDocumentLocationByRealName(documentLocationRealName, userSecurity).getName();
		if (StringUtils.isBlank(folderId)) {
			return documentLocationName.concat(FOLDER_PATH_SEPARATOR);
		}
		
		folderPathItems.add(documentLocationName);
		
		folderPathItems.addAll(plugin.getNamesForFolderHierarchy(documentLocationRealName, folderId));
		
		String currentFolderName = getFolderById(folderId, documentLocationRealName, userSecurity).getName();
		folderPathItems.add(currentFolderName);

		return StringUtils.join(folderPathItems, FOLDER_PATH_SEPARATOR).concat(FOLDER_PATH_SEPARATOR);
    	
	}
	
    public void setDocumentLocationService(DocumentLocationService documentLocationService) {
		this.documentLocationService = documentLocationService;
	}
    public void setPlugin(FolderPlugin plugin) {
        this.plugin = plugin;
    }

}