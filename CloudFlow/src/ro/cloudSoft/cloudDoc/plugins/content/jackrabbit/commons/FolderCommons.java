package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons;

import static ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

public class FolderCommons {
	
	public static boolean isFolder(Node node) throws RepositoryException {
		return node.isNodeType(JackRabbitConstants.FOLDER_NODE_TYPE);
	}
	
	public static String getName(Node folderNode) throws RepositoryException {
		return folderNode.getProperty(JackRabbitConstants.FOLDER_PROPERTY_NAME).getString();
	}
	
	/**
	 * Ia toate folder-ele dintr-un nod (reprezentand un folder sau un
	 * document location) pentru care utilizatorul are drept de vizualizare.
	 * @param parentNode nodul "parinte" in care se afla folder-ele
	 * @param userSecurity datele legate de utilizatorul curent
	 * @return o lista cu toate folder-ele
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public static List<Folder> getAllFoldersFromNode(Node parentNode, SecurityManager userSecurity) throws RepositoryException, IOException {
		List<Folder> folders = new ArrayList<Folder>();
		
		NodeIterator folderNodes = parentNode.getNodes(JackRabbitConstants.FOLDER_NODE_PREFIX + "*");
		while (folderNodes.hasNext()) {
			Node folderNode = folderNodes.nextNode();
			if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, folderNode, userSecurity)) {
				Folder folder = getFolderFromNode(folderNode);
				folders.add(folder);
			}
		}
		
		return folders;
	}
	
	/**
	 * Obtine folder-ul salvat intr-un nod in repository.
	 * @param node nodul ce reprezinta folder-ul
	 * @return folder-ul
	 * @throws RepositoryException
	 */
	public static Folder getFolderFromNode(Node node) throws RepositoryException {
		Folder folder = new Folder();
		folder.setAcl(SecurityCommons.getAclFromNode(node));
		populateFolderAttributesFromNode(folder, node);
		return folder;
	}

	public static void setFolderPropertiesToNode(Folder folder, Node node) throws RepositoryException {
		node.setProperty(JackRabbitConstants.FOLDER_PROPERTY_NAME, folder.getName());
		if (folder.getDocumentTypeId() != null) {
			node.setProperty(JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID, folder.getDocumentTypeId());
		} else {
			if (node.hasProperty(JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID)) {
				node.getProperty(JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID).remove();
			}
		}
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, folder.getDescription());
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY, folder.getLockedBy());
		// Daca folder-ul este nou, i se va seta autorul si data crearii.
		if (folder.getId() == null) {
			node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR, folder.getAuthor());
			node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED, folder.getCreated());
		}
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED, folder.getLastUpdate());
	}
	
	public static void populateFolderAttributesFromNode(Folder folder, Node node) throws RepositoryException {
		folder.setId(node.getUUID());
		if (isFolder(node.getParent())) {
			folder.setParentId(node.getParent().getUUID());
		}
		folder.setName(getName(node));
		if (node.hasProperty(JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID)) {
			folder.setDocumentTypeId(node.getProperty(JackRabbitConstants.FOLDER_PROPERTY_DOCUMENT_TYPE_ID).getLong());
		}
		folder.setAuthor(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR).getString());
		if (node.hasProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION)) {
			folder.setDescription(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION).getString());
		}
		if (node.hasProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY)) {
			folder.setLockedBy(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY).getString());
		}
		folder.setCreated(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED).getDate());
		folder.setLastUpdate(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED).getDate());
	}
	
	/**
	 * Verifica daca numele unui folder este unic in folder-ul parinte.
	 * @param session sesiunea activa
	 * @param parentPath calea parintelui
	 * @param folderId ID-ul folder-ului
	 * @param folderName numele folder-ului
	 * @return <code>true</code> daca numele este unic, <code>false</code> altfel
	 * @throws RepositoryException
	 */
	public static boolean hasUniqueName(Session session, String parentPath, String folderId, String folderName) throws RepositoryException {
		// Daca folder-ul exista deja in parinte, trebuie ignorat in cautare.
		String idRestriction = (folderId != null) ? "@" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + " != '" + folderId + "' and ": "";
		// Daca parintele este nodul radacina, trebuie sa tinem cont de asta.
		String path = (parentPath.equals("/")) ? parentPath : parentPath + "/";
		/*
		 * Se vor cauta noduri de tip folder care nu sunt folder-ul de verificat,
		 * se afla in folder-ul parinte si au acelasi nume.
		 */
		String queryString = "/jcr:root" + path + "element(*, " + JackRabbitConstants.FOLDER_NODE_TYPE + ")[" + idRestriction + "@" + JackRabbitConstants.FOLDER_PROPERTY_NAME + " = '" + folderName + "']";
		// Executa interogarea si obtine rezultatele.
		QueryManager queryManager = session.getWorkspace().getQueryManager();
		Query query = queryManager.createQuery(queryString, Query.XPATH);
		QueryResult queryResult = query.execute();
		NodeIterator resultNodes = queryResult.getNodes();
		/*
		 * Daca exista noduri ce respecta criteriile de mai sus, numele
		 * folder-ului nu este unic.
		 */
		return (resultNodes.hasNext()) ? false : true;
	}
	
	/**
	 * Elimina restrictia unui folder pe tipul de document permis.
	 * 
	 * @param folderNode nodul ce reprezinta folder-ul
	 */
	public static void removeDocumentTypeRestriction(Node folderNode) throws RepositoryException {
		
		if (!isFolder(folderNode)) {
			throw new IllegalArgumentException("Nodul [" + folderNode.getPath() + "] nu reprezinta un folder.");
		}
		
		if (folderNode.hasProperty(FOLDER_PROPERTY_DOCUMENT_TYPE_ID)) {
			folderNode.getProperty(FOLDER_PROPERTY_DOCUMENT_TYPE_ID).remove();
		}
	}
}