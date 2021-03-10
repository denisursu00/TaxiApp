package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants.DocumentLocationConstants;

public class DocumentLocationCommons {

	/**
	 * Obtine un nume unic pentru un document location nou.
	 * @return un nume unic pentru un document location nou
	 */
	public static String getUniqueName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		return "w" + sdf.format(new Date());
	}
	
	/**
	 * Completeaza proprietatile nodului descriere pentru document location cu
	 * valorile atributelor document location-ului.
	 * @param documentLocation document location-ul
	 * @param node nodul descriere pentru document location
	 * @throws RepositoryException
	 */
	public static void setDocumentLocationPropertiesToNode(DocumentLocation documentLocation, Node node) throws RepositoryException {
		node.setProperty(DocumentLocationConstants.PROPERTY_NAME, documentLocation.getName());
		node.setProperty(DocumentLocationConstants.PROPERTY_DESCRIPTION, documentLocation.getDescription());
	}
	
	/**
	 * Completeaza atributele document location-ului cu valorile proprietatilor
	 * nodului ce il reprezinta.
	 * @param documentLocation document location-ul
	 * @param node nodul descriere pentru document location
	 * @throws RepositoryException
	 */
	public static void populateDocumentLocationAttributesFromNode(DocumentLocation documentLocation, Node node) throws RepositoryException {
		documentLocation.setName(node.getProperty(DocumentLocationConstants.PROPERTY_NAME).getString());
		if (node.hasProperty(DocumentLocationConstants.PROPERTY_DESCRIPTION)) {
			documentLocation.setDescription(node.getProperty(DocumentLocationConstants.PROPERTY_DESCRIPTION).getString());
		}
	}
	
	/**
	 * Seteaza proprietatile nodului de acces pentru un document location.
	 * Acest nod se gaseste in workpace-ul pentru securitate si tine evidenta
	 * tuturor entitatilor care au acces la document location.
	 * @param acl permisiunile document location-ului
	 * @param node nodul din workpace-ul pentru securitate ce tine evidenta
	 * tuturor entitatilor care au acces la document location
	 * @throws RepositoryException
	 */
	public static void setAccessPropertiesToNode(ACL acl, Node node) throws RepositoryException {
		List<Permission> permissions = acl.getPermissions();
		String[] accessEntries = new String[permissions.size()];
		for (int i = 0; i < permissions.size(); i++) {
			Permission permission = permissions.get(i);
			String accessEntry = permission.getEntityType() + DocumentLocationConstants.SEPARATOR_SECURITY_ENTITY_TYPE_FROM_ID + permission.getEntityId();
			accessEntries[i] = accessEntry;
		}
		node.setProperty(DocumentLocationConstants.SECURITY_PROPERTY_ENTITIES_WITH_ACCESS, accessEntries);
	}
	
	/**
	 * Sterge nodul de acces al unui document location din workspace-ul pentru
	 * securitate. Ca efect, nimeni nu va mai putea vedea document location-ul.
	 * @param rootNode nodul radacina din workspace-ul pentru securitate
	 * @param realName numele real al document location-ului
	 * @throws RepositoryException
	 */
	public static void removeAccessNode(Node rootNode, String realName) throws RepositoryException {
		NodeIterator oldSecurityNodes = rootNode.getNodes(realName + DocumentLocationConstants.SEPARATOR_NAMES + "*");
		while (oldSecurityNodes.hasNext()) {
			Node oldSecurityNode = oldSecurityNodes.nextNode();
			if (oldSecurityNode.isNodeType(DocumentLocationConstants.SECURITY_NODE_TYPE)) {
				oldSecurityNode.remove();
			}
		}
	}
	
	public static String getName(Node descriptionNode) throws RepositoryException {
		return descriptionNode.getProperty(DocumentLocationConstants.PROPERTY_NAME).getString();
	}
	
}