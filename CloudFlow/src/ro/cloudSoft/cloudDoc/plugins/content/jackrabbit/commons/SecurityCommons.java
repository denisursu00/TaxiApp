package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import ro.cloudSoft.cloudDoc.core.constants.AppConstants;
import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;

public class SecurityCommons {

	public static boolean hasPermisssionForOperation(ContentOperation operation, Node node, SecurityManager userSecurity) throws RepositoryException {
		// Daca utilizatorul este "super user"...
		if (SecurityUtils.isUserAdmin(userSecurity)) {
			return true;
		}
		// Ia permisiunea utilizatorului.
		String permission = SecurityUtils.getPermission(getAclFromNode(node), userSecurity);
		// Daca utilizatorul nu are permisiune pentru entitate...
		if (permission == null) {
			return false;
		}
		// Dreptul i se da in functie de permisiune si de operatia ceruta.
		switch (operation) {
			case VIEW:
				if (permission.equals(AppConstants.ACL_ROLE_COORDINATOR)
						|| permission.equals(AppConstants.ACL_ROLE_COLLABORATOR)
						|| permission.equals(AppConstants.ACL_ROLE_EDITOR)
						|| permission.equals(AppConstants.ACL_ROLE_READER)) {
					return true;
				} else {
					return false;
				}
			case EDIT:
				if (permission.equals(AppConstants.ACL_ROLE_COORDINATOR)
						|| permission.equals(AppConstants.ACL_ROLE_COLLABORATOR)
						|| permission.equals(AppConstants.ACL_ROLE_EDITOR)) {
					return true;
				} else {
					return false;
				}
			case CREATE_CHILD:
				if (permission.equals(AppConstants.ACL_ROLE_COORDINATOR)
						|| permission.equals(AppConstants.ACL_ROLE_COLLABORATOR)) {
					return true;
				} else {
					return false;
				}
			case DELETE:
				if (permission.equals(AppConstants.ACL_ROLE_COORDINATOR)) {
					// Daca nodul este de tip document location sau folder...
					if (!node.isNodeType(JackRabbitConstants.DOCUMENT_NODE_TYPE)) {
						// Ia toate nodurile copil de tip document.
						NodeIterator documentNodes = node.getNodes(JackRabbitConstants.DOCUMENT_NODE_PREFIX + "*");
						while (documentNodes.hasNext()) {
							Node documentNode = documentNodes.nextNode();
							// Verifica daca utilizatorul are permisiune de stergere a documentului.
							boolean hasPermissionToDelete = hasPermisssionForOperation(ContentOperation.DELETE, documentNode, userSecurity);
							// Verifica daca documentul este blocat si nu poate accesa documentul blocat.
							boolean isLockedAndCantAccessLockedDocument = (DocumentCommons.isLocked(documentNode) && !SecurityCommons.canAccessLockedDocument(documentNode, userSecurity));
							/*
							 * Daca nu are permisiune de stergere asupra documentului sau documentul este blocat
							 * si nu poate accesa documentul blocat, nu are drept de stergere asupra entitatii
							 * principale (document location sau folder).
							 */
							if (!hasPermissionToDelete || isLockedAndCantAccessLockedDocument) {
								return false;
							}
						}
						// Ia toate nodurile copil de tip folder.
						NodeIterator folderNodes = node.getNodes(JackRabbitConstants.FOLDER_NODE_PREFIX + "*");
						while (folderNodes.hasNext()) {
							Node folderNode = folderNodes.nextNode();
							/*
							 * Daca nu are permisiune de stergere asupra
							 * folder-ului, nu are drept de stergere
							 * asupra entitatii principale (document location
							 * sau folder).
							 */
							if (!hasPermisssionForOperation(ContentOperation.DELETE, folderNode, userSecurity)) {
								return false;
							}
						}
					}
					return true;
				} else {
					return false;
				}
		}
		return false;
	}
	
	public static boolean canAccessLockedDocument(Node documentNode, SecurityManager userSecurity) throws RepositoryException {
		if (!DocumentCommons.isLocked(documentNode)) {
			throw new IllegalArgumentException("Documentul cu ID-ul [" + documentNode.getUUID() + "] NU este blocat.");
		}
		return (DocumentCommons.isLockedByUser(documentNode, userSecurity) || SecurityUtils.isUserAdmin(userSecurity));
	}
	
	/**
	 * Seteaza permisiunile in nodul ce reprezinta o entitate de continut.
	 * @param node nodul ce reprezinta entitatea
	 * @param acl obiectul ce tine permisiunile
	 * @throws RepositoryException
	 */
	public static void setAclForNode(Node node, ACL acl) throws RepositoryException {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (acl == null) {
			node.setProperty(JackRabbitConstants.ACL_PROPERTY_NAME, new String[] {});
			return;
		}
		String[] aclStorage = JackRabbitCommons.prepareAclToJrValue(acl);
		node.setProperty(JackRabbitConstants.ACL_PROPERTY_NAME, aclStorage);
	}
	
	/**
	 * Obtine permisiunile nodului ce reprezinta o entitate de continut.
	 * @param node nodul ce reprezinta entitatea
	 * @return obiectul ce tine permisiunile
	 * @throws RepositoryException
	 */
	public static ACL getAclFromNode(Node node) throws RepositoryException {
		String[] aclProperty = JackRabbitCommons.getPropertyValueAsStringArray(node, JackRabbitConstants.ACL_PROPERTY_NAME);
		return JackRabbitCommons.getAclFromJrValue(aclProperty);
	}
}