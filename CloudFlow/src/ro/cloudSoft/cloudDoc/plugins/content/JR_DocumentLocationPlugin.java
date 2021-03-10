package ro.cloudSoft.cloudDoc.plugins.content;


import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.JACKRABBIT_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.XPathQueryTemplate;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentLocationCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.FolderCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.SecurityCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants.DocumentLocationConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query.VisibleDocumentLocationsQueryBuilder;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class JR_DocumentLocationPlugin extends JR_PluginBase implements DocumentLocationPlugin, InitializingBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DocumentLocationPlugin.class);

	public JR_DocumentLocationPlugin() throws Exception {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		super.afterPropertiesSet();
	}

	@Override
	public String saveDocumentLocation(DocumentLocation documentLocation, SecurityManager userSecurity) throws AppException {
		String realName = null;
		
		boolean isEdit = false;
		
		Session session = null;
		try {
			// Daca nu vine dinspre editare, creez workspace-ul d.p.d.v. fizic.
			if (documentLocation.getRealName() == null) {
				String newRealName = DocumentLocationCommons.getUniqueName();
				session = repository.login(credentials);
				((JackrabbitWorkspace) session.getWorkspace()).createWorkspace(newRealName);
				JackRabbitCommons.logout(session);
				documentLocation.setRealName(newRealName);
			} else {
				isEdit = true;
			}
			
			// Ma logez in noul workspace creat.
			session = repository.login(credentials, documentLocation.getRealName());
			Node rootNode = session.getRootNode();

			String documentLocationName = documentLocation.getName();
			if (isEdit) {
				Node descriptionNode = rootNode.getNode(DocumentLocationConstants.NODE_NAME);
				documentLocationName = DocumentLocationCommons.getName(descriptionNode);
			}
			
			/*
			 * Daca vine dinspre editare, verific daca utilizatorul are drept
			 * de editare.
			 */
			if (isEdit && !SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, rootNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat MODIFICAREA spatiului de lucru  cu numele 'real' [" + documentLocation.getRealName() + "] si numele [" + documentLocationName + "], insa acesta NU are drepturi suficiente.", "saveDocumentLocation", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			// Setez permisiunile.
			SecurityCommons.setAclForNode(session.getRootNode(), documentLocation.getAcl());
			
			// Setez nodul ce va descrie document location-ul.
			if (!rootNode.hasNode(DocumentLocationConstants.NODE_NAME)) {
				rootNode.addNode(DocumentLocationConstants.NODE_NAME, DocumentLocationConstants.NODE_TYPE);
			}

			Node descriptionNode = rootNode.getNode(DocumentLocationConstants.NODE_NAME);
			DocumentLocationCommons.setDocumentLocationPropertiesToNode(documentLocation, descriptionNode);
			
			// Salvez si inchid document location-ul.
			session.save();
			JackRabbitCommons.logout(session);
			
			// Setez entitatile ce pot accesa document location-ul (in workspace-ul "security").
			session = repository.login(credentials, DocumentLocationConstants.WORKSPACE_NAME_SECURITY);
			Node securityRootNode = session.getRootNode();
			// Daca exista noduri vechi, le sterg.
			DocumentLocationCommons.removeAccessNode(securityRootNode, documentLocation.getRealName());
			Node securityNode = securityRootNode.addNode(documentLocation.getRealName() + DocumentLocationConstants.SEPARATOR_NAMES + documentLocation.getName(), DocumentLocationConstants.SECURITY_NODE_TYPE);
			DocumentLocationCommons.setAccessPropertiesToNode(documentLocation.getAcl(), securityNode);
			// Salvez si inchid sesiunea legata de securitate.
			session.save();
			JackRabbitCommons.logout(session);
			
			realName = documentLocation.getRealName();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "saveDocumentLocation", userSecurity);
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return realName;
	}

	@Override
	public ArrayList<DocumentLocation> getAllDocumentLocations(SecurityManager userSecurity) throws AppException {
		ArrayList<DocumentLocation> documentLocations = new ArrayList<DocumentLocation>();
		
		Session session = null;
		try {
			session = repository.login(credentials, DocumentLocationConstants.WORKSPACE_NAME_SECURITY);
			
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(VisibleDocumentLocationsQueryBuilder.getQuery(userSecurity), Query.XPATH);
			QueryResult queryResult = query.execute();
			NodeIterator resultNodes = queryResult.getNodes();
			
			while (resultNodes.hasNext()) {
				Node resultNode = resultNodes.nextNode();
				String namesString = resultNode.getName();
				String[] names = namesString.split(DocumentLocationConstants.SEPARATOR_NAMES);
				
				String realName = names[0];
				String name = names[1];
				
				DocumentLocation documentLocation = new DocumentLocation();
				documentLocation.setRealName(realName);
				documentLocation.setName(name);
				documentLocations.add(documentLocation);
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAllNamesForDocLocations", userSecurity);
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return documentLocations;
	}

	@Override
	public DocumentLocation getDocumentLocationByRealName(String realName, SecurityManager userSecurity) throws AppException {
		DocumentLocation documentLocation = null;
		
		Session session = null;
		try {
			session = repository.login(credentials, realName);
			Node rootNode = session.getRootNode();
			
			Node descriptionNode = rootNode.getNode(DocumentLocationConstants.NODE_NAME);
			String documentLocationName = DocumentLocationCommons.getName(descriptionNode);
			
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, rootNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA spatiului de lucru  cu nume 'real' [" + realName + "] si numele [" + documentLocationName + "], insa acesta NU are drepturi suficiente.", "getDocumentLocationByRealName", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			documentLocation = new DocumentLocation();
			documentLocation.setRealName(realName);
			documentLocation.setAcl(SecurityCommons.getAclFromNode(rootNode));
			
			DocumentLocationCommons.populateDocumentLocationAttributesFromNode(documentLocation, descriptionNode);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentLocationByRealName", userSecurity);
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return documentLocation;
	}

	@Override
	public void deleteDocumentLocation(String realName, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, realName);
			Node rootNode = session.getRootNode();
			Node documentDescriptionNode = rootNode.getNode(DocumentLocationConstants.NODE_NAME);
			String documentLocationName = DocumentLocationCommons.getName(documentDescriptionNode);
			// Verificam daca are drept de stergere.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.DELETE, rootNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat STERGEREA spatiului de lucru  cu numele 'real' [" + realName + "] si numele [" + documentLocationName + "], insa acesta NU are drepturi suficiente.", "deleteDocumentLocation", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			// Stergem permisiunile document location-ului.
			SecurityCommons.setAclForNode(rootNode, null);
			// Salvez si inchid sesiunea legata de document location.
			session.save();
			JackRabbitCommons.logout(session);
			
			// Nimeni nu va mai putea accesa document location-ul.
			session = repository.login(credentials, DocumentLocationConstants.WORKSPACE_NAME_SECURITY);
			Node securityRootNode = session.getRootNode();
			DocumentLocationCommons.removeAccessNode(securityRootNode, realName);
			// Salvez si inchid sesiunea legata de securitate.
			session.save();
			JackRabbitCommons.logout(session);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "deleteDocumentLocation", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public List<Folder> getAllFolders(String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		List<Folder> folders = new ArrayList<Folder>();
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			Node rootNode = session.getRootNode();
			
			folders = FolderCommons.getAllFoldersFromNode(rootNode, userSecurity);			
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAllFolders", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return folders;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean existDocumentsOfType(Long documentTypeId, SecurityManager userSecurity) throws AppException {
		
		Set<String> allDocumentLocationRealNames = getAllDocumentLocationRealNames();		
		for (String documentLocationRealName : allDocumentLocationRealNames) {
			Session session = null;
			try {
				
				session = repository.login(credentials, documentLocationRealName);
				
				String restriction = "@" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + " = '" + documentTypeId + "'";
				String query = "//element(*, " + JackRabbitConstants.DOCUMENT_NODE_TYPE + ")[" + restriction + "]";
				
				if (LOGGER.isDebugEnabled()) {
					String message = "Voi executa urmatoarea interogare XPath in workspace-ul [%s]: %s.";
					message = String.format(message, documentLocationRealName, query);
					LOGGER.debug(message, "Plugin-ul JackRabbit pt. document location",
						"Luarea tuturor numelor reale ale document location-urilor", userSecurity);
				}
				
				NodeIterator results = XPathQueryTemplate.find(session, query);
				if (results.hasNext()) {
					return true;
				}
			} catch (RepositoryException re) {
				if (re instanceof NoSuchWorkspaceException) {
					String message = "A ramas un descriptor pentru un workspace care nu mai exista: [%s].";
					message = String.format(message, documentLocationRealName);
					LOGGER.warn(message, re, "Plugin-ul JackRabbit pt. document location",
						"Verificarea daca exista documente de un anumit tip", userSecurity);
				} else {
					LOGGER.error("Exceptie JackRabbit", re, "Plugin-ul JackRabbit pt. document location",
						"Verificarea daca exista documente de un anumit tip", userSecurity);
					throw new AppException(JACKRABBIT_ERROR);
				}
			} finally {
				JackRabbitCommons.logout(session);
			}
		}
		
		return false;
	}
	
	@Override
	public Set<String> getAllDocumentLocationRealNames() throws AppException {
		
		Set<String> allRealNames = Sets.newHashSet();
		
		Session session = null;
		
		try {
			
			session = repository.login(credentials, DocumentLocationConstants.WORKSPACE_NAME_SECURITY);
			
			String query = "/jcr:root/element(*, " + DocumentLocationConstants.SECURITY_NODE_TYPE + ")";
			
			if (LOGGER.isDebugEnabled()) {
				String message = "Voi executa urmatoarea interogare XPath in workspace-ul [%s]: %s.";
				message = String.format(message, DocumentLocationConstants.WORKSPACE_NAME_SECURITY, query);
				LOGGER.debug(message, "Plugin-ul JackRabbit pt. document location",
					"Luarea tuturor numelor reale ale document location-urilor");
			}
			
			NodeIterator results = XPathQueryTemplate.find(session, query);
			while (results.hasNext()) {
				
				Node node = results.nextNode();
				
				String joinedNames = node.getName();
				String realName = joinedNames.split(DocumentLocationConstants.SEPARATOR_NAMES)[0];
				allRealNames.add(realName);				
			}
		} catch (RepositoryException re) {
			LOGGER.error("Exceptie JackRabbit", re, "Plugin-ul JackRabbit pt. document location",
				"Luarea tuturor numelor reale ale document location-urilor");
			throw new AppException(JACKRABBIT_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return allRealNames;
	}
}