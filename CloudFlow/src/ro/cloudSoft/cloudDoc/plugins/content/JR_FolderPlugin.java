package ro.cloudSoft.cloudDoc.plugins.content;

import static ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants.FOLDER_NODE_TYPE;
import static ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.XPathQueryTemplate;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.FolderCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.SecurityCommons;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.JackRabbitUtils;

import com.google.common.collect.Lists;

public class JR_FolderPlugin extends JR_PluginBase implements FolderPlugin, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_FolderPlugin.class);

	public JR_FolderPlugin() throws Exception{}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		super.afterPropertiesSet();
	}

	@Override
	public void saveFolder(Folder folder, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		boolean isEdit = false;
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			Node folderNode = null;
			if (folder.getId() == null) {
				Node parentNode = null;
				if (folder.getParentId() == null) {
					parentNode = session.getRootNode();
				} else {
					parentNode = session.getNodeByIdentifier(folder.getParentId());
				}
				
				String parentFolderName = null;
				if (FolderCommons.isFolder(parentNode)) {
					parentFolderName = FolderCommons.getName(parentNode);
				}
				
				if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.CREATE_CHILD, parentNode, userSecurity)) {
					LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat ADAUGAREA unui director cu numele [" + folder.getName() + "] in directorul ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + parentNode.getUUID() + "] si nume [" + parentFolderName + "]) , insa acesta NU are drepturi suficiente.", "saveFolder", userSecurity);
					throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
				}
				/*
				 * Numele nodului folder-ului NU trebuie sa contina spatii.
				 * Altfel, NU va merge cautarea in el cu restrictie pe cale
				 * (cum se foloseste la verificarea unicitatii numelui).
				 */
				String folderNodeName = JackRabbitConstants.FOLDER_NODE_PREFIX + System.currentTimeMillis();
				// Adauga nodul folder-ului in nodul parinte.
				folderNode = parentNode.addNode(folderNodeName, JackRabbitConstants.FOLDER_NODE_TYPE);
				// Daca este folder nou, atunci ii setez data crearii.
		        folder.setAuthor(userSecurity.getUserIdAsString());
				folder.setCreated(Calendar.getInstance());
			} else {
				isEdit = true;
				folderNode = session.getNodeByUUID(folder.getId());
			}
			// Setez data modificarii.
			folder.setLastUpdate(Calendar.getInstance());
			// Verifica daca in nodul destinatie nu mai exista un folder cu acelasi nume.
			if (!FolderCommons.hasUniqueName(session, folderNode.getParent().getPath(), folder.getId(), folder.getName())) {
				throw new AppException(AppExceptionCodes.FOLDER_WITH_NAME_EXISTS);
			}
			// Daca este editare, verifica daca are drept.
			if (isEdit && !SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, folderNode, userSecurity)) {
				String folderName = FolderCommons.getName(folderNode);
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat EDITAREA directorului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + folder.getId( ) +" si nume " + folderName + "]) , insa acesta NU are drepturi suficiente", "saveFolder", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			SecurityCommons.setAclForNode(folderNode, folder.getAcl());
			
			FolderCommons.setFolderPropertiesToNode(folder, folderNode);
			
			session.save();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "saveFolder", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public Folder getFolderById(String id, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		Folder folder = null;
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Cauta nodul in care s-a salvat folder-ul.
			Node folderNode = session.getNodeByUUID(id);
			String folderName = FolderCommons.getName(folderNode);
			// Daca utilizatorul nu are drept de vizualizare, arunca o exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, folderNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA directorului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + id + "] si nume [" + folderName + "]) , insa acesta NU are drepturi suficiente.", "getFolderById", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Obtine folder-ul din nodul in care s-a salvat.
			folder = FolderCommons.getFolderFromNode(folderNode);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getFolderById", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return folder;
	}

	@Override
	public List<Folder> getAllFoldersFromFolder(String parentId, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		List<Folder> folders = new ArrayList<Folder>();
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			Node parentNode = session.getNodeByIdentifier(parentId);
			folders = FolderCommons.getAllFoldersFromNode(parentNode, userSecurity);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAllFoldersFromFolder", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return folders;
	}

	@Override
	public void deleteFolder(String id, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Cauta nodul in care s-a salvat folder-ul.
			Node folderNode = session.getNodeByIdentifier(id);
			String folderName = FolderCommons.getName(folderNode);
			// Daca utilizatorul nu are drept de stergere, arunca o exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.DELETE, folderNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat STERGEREA directorului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + id + "] si nume [" + folderName + "]) , insa acesta NU are drepturi suficiente.", "deleteFolder", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Stergem nodul in care s-a salvat folder-ul.
			folderNode.remove();
			// Salvam modificarile.
			session.save();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "deleteFolder", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public void moveFolder(String folderToMoveId, String destinationFolderId, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);			
			// Obtinem nodul ce reprezinta folder-ul ce va fi mutat si calea acestuia.
			Node folderToMoveNode = session.getNodeByIdentifier(folderToMoveId);
			String folderToMoveName = FolderCommons.getName(folderToMoveNode);
			String sourcePath = folderToMoveNode.getPath();
			/*
			 * Obtinem nodul ce reprezinta folder-ul destinatie. Daca nu s-a
			 * trimis un ID, inseamna ca se va muta in radacina.
			 * In acelasi timp, construieste calea destinatie.
			 */
			Node destinationFolderNode = null;
			StringBuilder destinationPath = new StringBuilder();
			if (destinationFolderId != null) {
				destinationFolderNode = session.getNodeByIdentifier(destinationFolderId);
				destinationPath.append(destinationFolderNode.getPath());
			} else {
				destinationFolderNode = session.getRootNode();
			}
			String destinationFolderName = FolderCommons.getName(destinationFolderNode);
			destinationPath.append('/');
			destinationPath.append(folderToMoveNode.getName());
			// Daca nu are permisiunile necesare, va arunca o exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.DELETE, folderToMoveNode, userSecurity)
					&& !SecurityCommons.hasPermisssionForOperation(ContentOperation.CREATE_CHILD, destinationFolderNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat MUTAREA directorului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + folderToMoveId + "] si nume [" + folderToMoveName + "]) in directorul cu ID [" + destinationFolderId + "] si numele [" + destinationFolderName + "], insa acesta NU are drepturi suficiente.", "moveFolder", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Ia folder-ul de mutat pentru a-l verifica de unicitate in folder-ul destinatie.
			Folder folderToMove = getFolderById(folderToMoveId, documentLocationRealName, userSecurity);
			// Verifica daca in nodul destinatie nu mai exista un folder cu acelasi nume.
			if (!FolderCommons.hasUniqueName(session, destinationFolderNode.getPath(), folderToMove.getId(), folderToMove.getName())) {
				throw new AppException(AppExceptionCodes.FOLDER_WITH_NAME_EXISTS);
			}
			// Muta folder-ul in destinatie.
			session.move(sourcePath, destinationPath.toString());
			// Salveaza modificarile.
			session.save();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "moveFolder", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void removeDocumentTypeRestrictionForAllFolders(Collection<String> documentLocationRealNames,
			Long documentTypeId, SecurityManager userSecurity) throws AppException {
		
		for (String documentLocationRealName : documentLocationRealNames) {
			Session session = null;
			try {
				
				session = repository.login(credentials, documentLocationRealName);
				
				String query = "//element(*, " + FOLDER_NODE_TYPE + ")[@" + FOLDER_PROPERTY_DOCUMENT_TYPE_ID + " = " + documentTypeId + "]";
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Se va executa urmatoarea interogare: " + query + ".",
						"removeDocumentTypeRestrictionForAllFolders", userSecurity);
				}
				NodeIterator foundFolderNodes = XPathQueryTemplate.find(session, query);
				
				while (foundFolderNodes.hasNext()) {
					Node foundFolderNode = foundFolderNodes.nextNode();
					FolderCommons.removeDocumentTypeRestriction(foundFolderNode);
				}
				
				session.save();
			} catch (Exception e) {
				LOGGER.error("Exceptie", e, "removeDocumentTypeRestrictionForAllFolders", userSecurity);
				throw new AppException();
			} finally {
				JackRabbitCommons.logout(session);
			}
		}
	}
	
	@Override
	public List<String> getIdsForFolderHierarchy(String documentLocationRealName, String folderId) throws AppException {
		
		List<String> idsForFolderHierarchy = Lists.newArrayList();
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			Node folderNode = session.getNodeByIdentifier(folderId);

			Node rootNode = session.getRootNode();
			Node currentNode = folderNode.getParent();
			while (!currentNode.equals(rootNode) && FolderCommons.isFolder(currentNode)) {
				idsForFolderHierarchy.add(currentNode.getIdentifier());
				currentNode = currentNode.getParent();
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getIdsForFolderHierarchy");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		Collections.reverse(idsForFolderHierarchy);
		return idsForFolderHierarchy;
	}

	@Override
	public List<String> getNamesForFolderHierarchy(String documentLocationRealName, String folderId) throws AppException {
		List<String> namesForFolderHierarchy = Lists.newArrayList();
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			Node folderNode = session.getNodeByIdentifier(folderId);

			Node rootNode = session.getRootNode();
			Node currentNode = folderNode.getParent();
			while (!currentNode.equals(rootNode) && FolderCommons.isFolder(currentNode)) {
				namesForFolderHierarchy.add(FolderCommons.getName(currentNode));
				currentNode = currentNode.getParent();
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getNamesForFolderHierarchy");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		Collections.reverse(namesForFolderHierarchy);
		return namesForFolderHierarchy;
	}
}