package ro.cloudSoft.cloudDoc.services.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentLocationPlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Implementarea interfetei <b>DocumentLocationService</b>.
 * In fiecare metoda este apelata metoda omoloaga din plugin-ul folosit pentru implementare
 * @see DocumentLocationPlugin
 * 
 */
public class DocumentLocationServiceImpl implements DocumentLocationService, InitializingBean {

    private DocumentLocationPlugin plugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			plugin
		);
	}

    @Override
    public ArrayList<DocumentLocation> getAllDocumentLocations(SecurityManager userSecurity) throws AppException {
        return plugin.getAllDocumentLocations(userSecurity);
    }

    @Override
    public String saveDocumentLocation(DocumentLocation documentLocation, SecurityManager userSecurity) throws AppException {
        return plugin.saveDocumentLocation(documentLocation, userSecurity);
    }

    @Override
    public DocumentLocation getDocumentLocationByRealName(String realName, SecurityManager userSecurity) throws AppException {
        return plugin.getDocumentLocationByRealName(realName, userSecurity);
    }

    @Override
    public void deleteDocumentLocation(String realName, SecurityManager userSecurity) throws AppException {
        plugin.deleteDocumentLocation(realName, userSecurity);
    }

    @Override
    public List<Folder> getAllFolders(String documentLocationRealName, SecurityManager userSecurity) throws AppException {
        return plugin.getAllFolders(documentLocationRealName, userSecurity);
    }
    
    @Override
    public Set<String> getAllDocumentLocationRealNames() throws AppException {
    	return plugin.getAllDocumentLocationRealNames();
    }

    public void setPlugin(DocumentLocationPlugin plugin) {
        this.plugin = plugin;
    }
}