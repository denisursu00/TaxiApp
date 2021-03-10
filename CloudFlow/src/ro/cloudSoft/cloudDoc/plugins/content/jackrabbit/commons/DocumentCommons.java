package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.value.BinaryImpl;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.core.ApplicationCodes;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.MimeType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

public class DocumentCommons {
	
	/**
	 * Valoarea numarului versiunii daca documentul nu are versiuni
	 * <br><br>
	 * NOTA: NU exista versiune propriu-zisa care sa aiba acest numar.
	 */
	public static final int VERSION_NUMBER_NO_VERSION = 0;
	
	public static void setDocumentPropertiesToNode(Document document, DocumentType documentType, Node node) throws RepositoryException {
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME, document.getName());
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, (document.getDescription() != null) ? document.getDescription() : "");
		node.setProperty(JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID, document.getDocumentTypeId().toString());
		// Daca documentul este nou, i se va seta autorul si data crearii.
		if (document.getId() == null) {
			node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR, document.getAuthor());
			node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED, document.getCreated());
		}
		node.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED, document.getLastUpdate());
		if (document.getWorkflowStateId() != null) {
			node.setProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID, document.getWorkflowStateId().toString());
		}		
		setDocumentMetadatasToNode(document.getMetadataInstanceList(), documentType.getMetadataDefinitions(), node);
		setDocumentMetadataCollectionsToNode(document.getCollectionInstanceListMap(), documentType.getMetadataCollections(), node);
	}
	
	public static void setDocumentMetadatasToNode(List<MetadataInstance> metadataInstances, List<? extends MetadataDefinition> metadataDefinitions, Node node) throws RepositoryException {
		
		if (CollectionUtils.isEmpty(metadataInstances)) {
			return;
		}
		
		Map<Long, MetadataDefinition> metadataDefinitionById = new HashMap<>();
		for (MetadataDefinition metadataDefinition : metadataDefinitions) {
			metadataDefinitionById.put(metadataDefinition.getId(), metadataDefinition);
		}
		
		for (MetadataInstance metadataInstance : metadataInstances) {
			MetadataDefinition metadataDefinition = metadataDefinitionById.get(metadataInstance.getMetadataDefinitionId());			
			if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_LIST)
					|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DOCUMENT)
					|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_PROJECT)) {				
				node.setProperty(metadataDefinition.getJrPropertyName(), JackRabbitCommons.preparePropertyValueAsMultipleFromMetadataInstance(metadataInstance));
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NUMERIC)) {
				BigDecimal value = null;
				if (StringUtils.isNotEmpty(metadataInstance.getValue())) {
					// TODO - 0016867: De revenit pentru eventuale ajustari la salvarea metadatelor numeric in JR
					value = new BigDecimal(metadataInstance.getValue());
				}
				node.setProperty(metadataDefinition.getJrPropertyName(), value);
			} else {
				node.setProperty(metadataDefinition.getJrPropertyName(), metadataInstance.getValue());
			}
		}
	}
	
	public static void setDocumentMetadataCollectionsToNode(Map<Long, List<CollectionInstance>> collectionInstanceMap, List<MetadataCollection> metadataCollectionDefinitions, Node node) throws RepositoryException {
		
		if (collectionInstanceMap == null || collectionInstanceMap.isEmpty()) {
			return;
		}
		
		Map<Long, MetadataCollection> metadataCollectionDefinitionById = new HashMap<>();
		for (MetadataCollection mcd : metadataCollectionDefinitions) {
			metadataCollectionDefinitionById.put(mcd.getId(), mcd);
		}
		
		for (Long collectionDefinitionId : collectionInstanceMap.keySet()) {
			
			List<String> propertyValues = new ArrayList<>();
						
			List<CollectionInstance> collectionInstances = collectionInstanceMap.get(collectionDefinitionId);
			long maxCollectionInstanceId = Long.valueOf(collectionDefinitionId + "0");
			for (CollectionInstance collectionInstance : collectionInstances) {
				if (StringUtils.isNotBlank(collectionInstance.getId())) {
					long currentCollectionId = Long.valueOf(collectionInstance.getId());
					if (currentCollectionId > maxCollectionInstanceId) {
						maxCollectionInstanceId = currentCollectionId;
					}
				}
			}
			
			for (CollectionInstance collectionInstance : collectionInstances) {
				/*
				 * Daca este o instanta de colectie noua (nu a mai fost salvata), atunci i se da un nou ID generat.
				 */
				if (collectionInstance.getId() == null) {
					/*
					 * ID-ul colectiei va fi format din ID-ul definitiei colectiei si numarul de ordine al colectiei 
					 * pentru a asigura unicitate globala.
					 */
					maxCollectionInstanceId = maxCollectionInstanceId + 1;
					collectionInstance.setId(String.valueOf(maxCollectionInstanceId));
				}
				
				for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
					if (CollectionUtils.isNotEmpty(metadataInstance.getValues())) {						
						for (int i = 0; i < metadataInstance.getValues().size(); i++) {
							String jrMetadataValue = buildJRMetadataValueOfCollectionMetadataValue(metadataInstance.getMetadataDefinitionId(), metadataInstance.getValues().get(i), collectionInstance.getId());
							propertyValues.add(jrMetadataValue);
						}
					}
				}
			}
			
			MetadataCollection collectioDefinition = metadataCollectionDefinitionById.get(collectionDefinitionId);
			node.setProperty(collectioDefinition.getJrPropertyName(), propertyValues.toArray(new String[propertyValues.size()]));
		}
	}
	
	private static String buildJRMetadataValueOfCollectionMetadataValue(Long metadataDefinitionId, String value, String collectionInstanceId) {
		StringBuilder propertyValue = new StringBuilder();		
		propertyValue.append(metadataDefinitionId.toString());
		propertyValue.append(JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES);
		propertyValue.append(value);
		propertyValue.append(JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES);
		propertyValue.append(collectionInstanceId);
		return propertyValue.toString();
	}
	
	
	public static Document getDocumentFromNode(String documentLocationRealName, Node documentNode, DocumentType documentType) throws RepositoryException {		
		return getDocumentFromNode(documentLocationRealName, documentNode, documentType, true);
	}
	
	public static Document getDocumentFromNodeWithoutAttachments(String documentLocationRealName, Node documentNode, DocumentType documentType) throws RepositoryException {		
		return getDocumentFromNode(documentLocationRealName, documentNode, documentType, false);
	}
	
	private static Document getDocumentFromNode(String documentLocationRealName, Node documentNode, DocumentType documentType, boolean withAttachments) throws RepositoryException {
		Document document = new Document();
		
		// Seteaza permisiunile.
		document.setAcl(SecurityCommons.getAclFromNode(documentNode));
		// Seteaza proprietatile.
		populateDocumentAttributesFromNode(document, documentType, documentNode);
		
		if (withAttachments) {
			// Ia numele atasamentelor.
			List<String> attachmentNames = getDocumentAttachmentNames(documentNode);
			document.setAttachmentNames(attachmentNames);
		}
		
		document.setDocumentLocationRealName(documentLocationRealName);
		
		return document;
	}
	
	/**
	 * Verifica daca documentul are versiuni.
	 * @param documentNode nodul documentului
	 * @return <code>true</code> daca documentul are versiuni,
	 * <code>false</code> altfel
	 * @throws RepositoryException
	 */
	public static boolean hasVersions(Node documentNode) throws RepositoryException {
		/*NodeIterator versionNodes = documentNode.getNodes(JackRabbitConstants.VERSION_NODE_PREFIX + "*");
		return (versionNodes.hasNext());*/
		return (getLastVersionNumber(documentNode) != VERSION_NUMBER_NO_VERSION);
	}
	
	/**
	 * Verifica daca exista versiunea cu numarul specificat pentru documentul reprezentat de nodul dat.
	 */
	public static boolean versionExists(Node documentNode, int versionNumber) throws RepositoryException {
		return (documentNode.hasNode(JackRabbitConstants.VERSION_NODE_PREFIX + versionNumber));
	}
	
	/**
	 * Ia datele din ultima versiune a unui document.
	 * Daca un document este in editare / blocat de catre un utilizator,
	 * ceilalti trebuie sa vada ultima versiune a documentului, nu cea in curs
	 * de editare avand modificarile utilizatorului.
	 * @param documentNode nodul documentului
	 * @return documentul avand proprietatile ultimei versiuni
	 * @throws RepositoryException
	 */
	public static Document getDocumentFromLatestVersion(String documentLocationRealName, Node documentNode, DocumentType documentType) throws RepositoryException {
		int lastVersionNumber = getLastVersionNumber(documentNode);
		return getDocumentFromVersion(documentLocationRealName, lastVersionNumber, documentNode, documentType);
	}
	
	public static void populateDocumentAttributesFromNode(Document document, DocumentType documentType, Node node) throws RepositoryException {
		document.setId(node.getIdentifier());
		document.setName(getName(node));
		document.setDescription(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION).getString());
		document.setDocumentTypeId(getDocumentTypeId(node));
		document.setAuthor(getAuthorUserIdAsString(node));
		document.setCreated(getCreatedDateAsCalendar(node));
		document.setLastUpdate(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED).getDate());
		if (node.hasProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY)) {
			document.setLockedBy(node.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY).getString());
		}
		document.setHasStableVersion(new Boolean(hasVersions(node)));
		if(node.hasProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID)) {
			document.setWorkflowStateId(Long.valueOf(node.getProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID).getString()));
		}		
		populateDocumentMetadatasFromNode(document, documentType, node);
		populateDocumentMetadataCollectionsFromNode(document, documentType, node);
	}
	
	public static List<MetadataInstance> getDocumentMetadatasFromNode(DocumentType documentType, Node node) {
		
		List<? extends MetadataDefinition> metadataDefinitions = documentType.getMetadataDefinitions();
		if (CollectionUtils.isEmpty(metadataDefinitions)) {
			return new ArrayList<>();
		}
		
		List<MetadataInstance> metadataInstances = new ArrayList<>();
		for (MetadataDefinition metadataDefinition : metadataDefinitions) {
			List<String> values = new LinkedList<>();
			if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_LIST) 
					|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DOCUMENT)
					|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_PROJECT)) {				
				values = JackRabbitCommons.getPropertyValueAsStringList(node, metadataDefinition.getJrPropertyName());				
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NUMERIC)) {
				BigDecimal decValue = JackRabbitCommons.getPropertyValueAsBigDecimal(node, metadataDefinition.getJrPropertyName());
				if (decValue != null) {
					// TODO - 0016867: De revenit pentru eventuale ajustari la salvarea metadatelor numeric in JR
					values.add(decValue.toPlainString());
				}				
			} else {
				String theValue = JackRabbitCommons.getPropertyValueAsString(node, metadataDefinition.getJrPropertyName());
				if (StringUtils.isNotBlank(theValue)) {
					values.add(theValue);
				}				
			}
			if (CollectionUtils.isNotEmpty(values)) {
				MetadataInstance metadataInstance = new MetadataInstance();
				metadataInstance.setMetadataDefinitionId(metadataDefinition.getId());	
				metadataInstance.setValues(values);
				metadataInstances.add(metadataInstance);
			}			
		}
		return metadataInstances;
	}
	
	private static void populateDocumentMetadatasFromNode(Document document, DocumentType documentType, Node node) {		
		document.setMetadataInstanceList(getDocumentMetadatasFromNode(documentType, node));
	}
	
	public static Map<Long, List<CollectionInstance>> getDocumentMetadataCollectionsFromNode(DocumentType documentType, Node node) {
		List<MetadataCollection> metadataCollections = documentType.getMetadataCollections();
		if (CollectionUtils.isEmpty(metadataCollections)) {
			return new HashMap<>();
		}		
		Map<Long, List<CollectionInstance>> collectionInstanceListMap = new HashMap<>();
		for (MetadataCollection metadataCollection : metadataCollections) {
			List<String> jrStringValues = JackRabbitCommons.getPropertyValueAsStringList(node, metadataCollection.getJrPropertyName());
			if (CollectionUtils.isEmpty(jrStringValues)) {
				continue;
			}
			collectionInstanceListMap.put(metadataCollection.getId(), getCollectionInstancesFromPropertyValues(jrStringValues));
		}	
		return collectionInstanceListMap;
	}	
	
	private static void populateDocumentMetadataCollectionsFromNode(Document document, DocumentType documentType, Node node) {			
		document.setCollectionInstanceListMap(getDocumentMetadataCollectionsFromNode(documentType, node));
	}
	
	private static List<CollectionInstance> getCollectionInstancesFromPropertyValues(List<String> collectionPropertyRawValues) {
		
		Map<String, List<MetadataInstance>> metadataInstanceListByCollectionInstanceId = new HashMap<>();
		
		for (String propertyValue : collectionPropertyRawValues) {
			
			int firstSeparatorIndex = propertyValue.indexOf(JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES);
			int lastSeparatorIndex = propertyValue.lastIndexOf(JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES);
			Long metadataDefinitionId = Long.valueOf(propertyValue.substring(0, firstSeparatorIndex));
			String metadataInstanceValue = propertyValue.substring(firstSeparatorIndex + 1, lastSeparatorIndex);
			String collectionInstanceId = propertyValue.substring(lastSeparatorIndex + 1);
			
			MetadataInstance metadataInstance = null;
			
			List<MetadataInstance> metadataInstanceList = metadataInstanceListByCollectionInstanceId.get(collectionInstanceId);
			if (metadataInstanceList == null) {
				metadataInstanceList = new ArrayList<MetadataInstance>();
				metadataInstanceListByCollectionInstanceId.put(collectionInstanceId, metadataInstanceList);
			}
			
			for (MetadataInstance mi: metadataInstanceList) {
				if (mi.getMetadataDefinitionId().equals(metadataDefinitionId)) {
					metadataInstance = mi;
					metadataInstance.addValue(metadataInstanceValue);
				}
			}			
			if (metadataInstance == null) {
				metadataInstance = new MetadataInstance(metadataDefinitionId, metadataInstanceValue);
			}
			
			metadataInstanceList.add(metadataInstance);			
		}
		
		List<CollectionInstance> collectionInstances = new ArrayList<>();
		for (String collectionInstanceId : metadataInstanceListByCollectionInstanceId.keySet()) {
			CollectionInstance collectionInstance = new CollectionInstance();
			collectionInstance.setId(collectionInstanceId);
			collectionInstance.setMetadataInstanceList(metadataInstanceListByCollectionInstanceId.get(collectionInstanceId));
			collectionInstances.add(collectionInstance);
		}
		
		/*
		 * Le sortam dupa id intrucat nu stim daca le mai avem in ordinea salvata (id-ul are valoare crescatoare dat de ordinea salvarii).
		 */
		collectionInstances.sort(new Comparator<CollectionInstance>() {
			@Override
			public int compare(CollectionInstance o1, CollectionInstance o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		return collectionInstances;
	}
	
	public static String getName(Node documentOrVersionNode) throws RepositoryException {
		return documentOrVersionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME).getString();
	}
	
	public static String getDescription(Node documentOrVersionNode) throws RepositoryException {
		return documentOrVersionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION).getString();
	}
	
	public static Long getDocumentTypeId(Node documentOrVersionNode) throws RepositoryException {
		String documentTypeIdAsString = documentOrVersionNode.getProperty(JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID).getString();
		Long documentTypeId = Long.valueOf(documentTypeIdAsString);
		return documentTypeId;
	}
	
	public static Calendar getCreatedDateAsCalendar(Node documentOrVersionNode) throws RepositoryException {
		return documentOrVersionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED).getDate();
	}
	
	public static String getAuthorUserIdAsString(Node documentOrVersionNode) throws RepositoryException {
		return documentOrVersionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR).getString();
	}
	
	public static void addAttachmentToDocumentNode(Node documentNode, Attachment attachment) throws RepositoryException {
		
		Node attachmentNode = documentNode.addNode(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + attachment.getName(), JackRabbitConstants.ATTACHMENT_NODE_TYPE);
		attachmentNode.setProperty(JackRabbitConstants.ATTACHMENT_PROPERTY_NAME, attachment.getName());
		
		Node contentNode = attachmentNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);
		contentNode.setProperty(JcrConstants.JCR_ENCODING, "UTF-8");
		contentNode.setProperty(JcrConstants.JCR_DATA, new BinaryImpl(attachment.getData()));
		MimeType mimeType = MimeType.getByFileName(attachment.getName());
		if (mimeType == null) {
			mimeType = MimeType.OCTET_STREAM;
		}
		contentNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType.getMimeType());
	}
	
	public static void deleteAttachments(Node documentNode, List<String> namesForAttachmentsToDelete) throws RepositoryException {
		NodeIterator attachmentNodes = documentNode.getNodes(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + "*");
		while (attachmentNodes.hasNext()) {
			Node attachmentNode = attachmentNodes.nextNode();
			String name = attachmentNode.getProperty(JackRabbitConstants.ATTACHMENT_PROPERTY_NAME).getString();
			if (namesForAttachmentsToDelete.contains(name)) {
				attachmentNode.remove();
			}
		}
	}
	
	/**
	 * Returneaza atasamentul din nodul documentului.
	 * Daca nu se gaseste atasament cu numele dat, va returna null.
	 */
	public static Attachment getAttachmentByName(Node documentNode, String attachmentName) throws RepositoryException, IOException {
		NodeIterator attachmentNodes = documentNode.getNodes(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + "*");
		while (attachmentNodes.hasNext()) {
			Node attachmentNode = attachmentNodes.nextNode();
			String name = attachmentNode.getProperty(JackRabbitConstants.ATTACHMENT_PROPERTY_NAME).getString();
			if (name.equals(attachmentName)) {				
				byte[] data = JackRabbitCommons.getContentFromAttachmentNode(attachmentNode);				
				return new Attachment(name, data);
			}
		}
		return null;
	}

	/**
	 * Returneaza atasamentul din ultima versiune a documentului.
	 * Daca nu se gaseste atasament cu numele dat, va returna null.
	 */
	public static Attachment getAttachmentFromLatestVersion(Node documentNode,
			String attachmentName) throws RepositoryException, IOException {
		
		int lastVersionNumber = getLastVersionNumber(documentNode);
		return getAttachmentFromVersion(documentNode, lastVersionNumber, attachmentName);
	}

	/**
	 * Returneaza atasamentul din versiunea cu numarul dat.
	 * Daca nu se gaseste atasament cu numele dat, va returna null.
	 */
	public static Attachment getAttachmentFromVersion(Node documentNode, int versionNumber, String attachmentName) throws RepositoryException, IOException {
		Node versionNode = documentNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + versionNumber);
		return getAttachmentByName(versionNode, attachmentName);
	}
	
	/**
	 * Seteaza la nivelul nodului document id-urile starilor de flux pe care este lansat documentul in cauza
	 */
	public static void setDocumentState(Node docNode,String idWorkflowState) throws RepositoryException
	{
		/*
		 * Setez ID-ul starii curente a documentului pe flux in nodul principal
		 * al documentului.
		 */
		docNode.setProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID, idWorkflowState);
		// Daca documentul are versiuni (incluzand versiunea stabila)...
		if (hasVersions(docNode)) {
			int lastVersionNumber = getLastVersionNumber(docNode);
			Node lastVersionNode = docNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + Integer.toString(lastVersionNumber));
			/*
			 * ID-ul starii curente trebuie salvat si in ultima versiune a
			 * documentului (pentru cautare).
			 */
			lastVersionNode.setProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID, idWorkflowState);
		}
	}
	/**
	 * de-blocheaza un document
	 */
	public static void unlock(Node documentNode) throws RepositoryException
	{
		documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY, "");
	}
	/**
	 * blocheaza un document
	 */
	public static void lock(Node documentNode, SecurityManager userSecurity) throws RepositoryException
	{
		documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY, userSecurity.getUserIdAsString());
		
	}
	/**
	 * verifica daca un document e blocat
	 */
	public static boolean isLocked(Node documentNode) throws RepositoryException
	{
		if (documentNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY).getString().length()>0)
			//locked
			return true;
		else
			return false;
	}
	/**
	 * returneaza id-ul userului care a blocat documentul
	 */
	public static String getLocker(Node documentNode) throws RepositoryException
	{
	   return documentNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY).getString();
	}
	
	/**
	 * Verifica daca documentul reprezentat prin nodul dat este blocat de utilizatorul cu datele specificate.
	 * Daca documentul NU este blocat, va returna false.
	 */
	public static boolean isLockedByUser(Node documentNode, SecurityManager userSecurity) throws RepositoryException {
		
		if (!isLocked(documentNode)) {
			return false;
		}
		
		return getLocker(documentNode).equals(userSecurity.getUserIdAsString());
	}
	
	/**
	 * Sterge toate versiunile unui document.
	 * @param documentNode nodul ce reprezinta documentul
	 * @throws RepositoryException
	 */
	public static void deleteAllVersions(Node documentNode) throws RepositoryException {
		NodeIterator versionNodes = documentNode.getNodes(JackRabbitConstants.VERSION_NODE_PREFIX + "*");
		while (versionNodes.hasNext()) {
			Node versionNode = versionNodes.nextNode();
			versionNode.remove();
		}
	}
	/**
	 * Adauga versiuni noi sau create prin restaurarea unor versiuni deja existente.
	 * @param documentNode nodul ce reprezinta documentul
	 * @param versionToBeRestoredNode- nodul versiune din care se face restaurarea; daca nu se face restaurare, acesta este null
	 * @param userSecurity utilizatorul curent
	 * @throws RepositoryException
	 */
	
	public static void addVersionNode(Node documentNode,Integer versionToBeRestoredNr, SecurityManager userSecurity, DocumentType documentType) throws RepositoryException 
	{
		//daca se face restaurare, restored=true si 
	    //daca documentul mai are versiuni, setez pentru acestea proprietatea VERSION_PROPERTY_LAST_STABLE_VERSION pe false
		setLastStableVersion(documentNode);
		
		int lastVersion = (getLastVersionNumber(documentNode) + 1);
		documentNode.setProperty(JackRabbitConstants.DOCUMENT_PROPERTY_LAST_VERSION_NUMBER, lastVersion);
		// Adauga un nou nod de tip versiune.
		Node versionNode = documentNode.addNode(JackRabbitConstants.VERSION_NODE_PREFIX + lastVersion, JackRabbitConstants.VERSION_NODE_TYPE);
			// Setare proprietati specifice versiunii.
		versionNode.setProperty(JackRabbitConstants.VERSION_PROPERTY_LAST_STABLE_VERSION, true);
		versionNode.setProperty(JackRabbitConstants.VERSION_PROPERTY_VERSION_AUTHOR, userSecurity.getUserIdAsString());
		//jcr:created pentru versiune va fi mereu data crearii documentului(se preia de la document)	
		versionNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED, documentNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED).getValue());
		
		//jcr:lastModify pentru versiune va fi mereau data crearii versiunii- vezi copyProperties
		
		if(versionToBeRestoredNr==null) { //adaugare de versiune noua
			versionNode.setProperty(JackRabbitConstants.VERSION_PROPERTY_VERSION_DESCRIPTION, ApplicationCodes.VERSION_NEW);
        	copyProperties(documentNode, versionNode, documentType);
		} else {
			//adaugare de versiune prin restaurare
			versionNode.setProperty(JackRabbitConstants.VERSION_PROPERTY_VERSION_DESCRIPTION, ApplicationCodes.VERSION_RESTORED);
			Node versionToBeRestoredNode = documentNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + versionToBeRestoredNr.intValue());
			copyProperties(versionToBeRestoredNode, versionNode, documentType);
		}
		if (documentNode.hasProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID)) {
			versionNode.setProperty(JackRabbitConstants.VERSION_PROPERTY_WORKFLOW_STATE_ID, documentNode.getProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID).getValue());
		}
	}

	public static void setLastStableVersion(Node documentNode) throws RepositoryException
	{
		//daca documentul mai are versiuni, setez pentru acestea proprietatea VERSION_PROPERTY_LAST_STABLE_VERSION pe false
		if(hasVersions(documentNode))
		{
			NodeIterator versionNodesIterator = documentNode.getNodes(JackRabbitConstants.VERSION_NODE_PREFIX + "*");
			while(versionNodesIterator.hasNext())
			{
			Node oldVersion =versionNodesIterator.nextNode();
			oldVersion.setProperty(JackRabbitConstants.VERSION_PROPERTY_LAST_STABLE_VERSION, false);
			}
		}
	}
	/**
	 * Returneaza numarul ultimei versiuni a unui document.
	 * @param documentNode nodul ce reprezinta documentul.
	 * @return numarul ultimii versiuni a unui document,
	 * sau valoarea constantei VERSION_NUMBER_NO_VERSION daca documentul nu are (inca) nici o versiune
	 * @throws RepositoryException
	 */
	public static int getLastVersionNumber(Node documentNode) throws RepositoryException {
		if (documentNode.hasProperty(JackRabbitConstants.DOCUMENT_PROPERTY_LAST_VERSION_NUMBER)) {
			return Integer.parseInt(documentNode.getProperty(JackRabbitConstants.DOCUMENT_PROPERTY_LAST_VERSION_NUMBER).getString());
		} else {
			return VERSION_NUMBER_NO_VERSION;
		}
	}
	
	public static Document getDocumentFromVersion(String documentLocationRealName, int versionNR, Node documentNode, DocumentType documentType) throws RepositoryException {
		
		Document document= new Document();
		
		Node versionNode = documentNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + versionNR);
		document.setVersionNumber(versionNR);
		if (versionNode.hasProperty(JackRabbitConstants.VERSION_PROPERTY_WORKFLOW_STATE_ID)) {
			String valueAsString = JackRabbitCommons.getPropertyValueAsString(versionNode, JackRabbitConstants.VERSION_PROPERTY_WORKFLOW_STATE_ID);
			if (StringUtils.isNotBlank(valueAsString)) {
				document.setVersionWorkflowStateId(Long.valueOf(valueAsString));
			}
		}
		// Seteaza proprietatile.
		populateDocumentAttributesFromNode(document, documentType, versionNode);
		//permisiunile se iau din nodul document pentru ca in versiuni nu se salveaza
		document.setAcl(SecurityCommons.getAclFromNode(documentNode));
		
		// Ia numele atasamentelor.
		List<String> attachmentNames = getDocumentAttachmentNames(versionNode);
		document.setAttachmentNames(attachmentNames);
		
		// ID-ul trebuie sa fie cel al documentului, nu al versiunii.
		document.setId(documentNode.getIdentifier());
		document.setLockedBy(documentNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY).getString());
		
		document.setDocumentLocationRealName(documentLocationRealName);
		
		return document;
	}
	
	private static List<String> getDocumentAttachmentNames(Node documentOrVersionNode) throws RepositoryException {
		List<String> attachmentNames = Lists.newLinkedList();
		NodeIterator attachmentNodes = documentOrVersionNode.getNodes(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + "*");
		while (attachmentNodes.hasNext()) {
			Node attachmentNode = attachmentNodes.nextNode();
			String attachmentName = attachmentNode.getProperty(JackRabbitConstants.ATTACHMENT_PROPERTY_NAME).getString();
			attachmentNames.add(attachmentName);
		}
		return attachmentNames;
	}
	
	/**
	 * Returneaza nodul documentului pentru care nodul dat este o versiune a documentului.
	 */
	public static Node getDocumentNodeOfVersionNode(Node versionNode) throws RepositoryException {
		
		if (!versionNode.isNodeType(JackRabbitConstants.VERSION_NODE_TYPE)) {
			String exceptionMessage = "Nodul cu calea [" + versionNode.getPath() + "] NU este un nod versiune pentru un document.";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		return versionNode.getParent();
	}

 	/**
	 * Copiaza toate proprietatile unui document dintr-un nod intr-un alt nod.
	 * @param sourceNode nodul sursa
	 * @param destinationNode nodul destinatie
	 */
	public static void copyProperties(Node sourceNode, Node destinationNode, DocumentType documentType) throws RepositoryException {
		// Seteaza toate proprietatile nodului document.
		destinationNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME, sourceNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME).getValue());
		destinationNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, sourceNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION).getValue());
		destinationNode.setProperty(JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID, sourceNode.getProperty(JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID).getValue());
		if (sourceNode.hasProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID)) {
			/*
			 * Daca nodul sursa are ID-ul starii, atunci si nodul destinatie
			 * va avea aceeasi valoare.
			 */
			destinationNode.setProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID, sourceNode.getProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID).getValue());
		} else if (destinationNode.hasProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID)) {
			/*
			 * Daca nodul sursa nu are ID-ul starii, dar nodul destinatie are
			 * un ID al starii, atunci cel din destinatie va trebui sa dispara.
			 */
			destinationNode.getProperty(JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID).remove();
		}
		destinationNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR, sourceNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_AUTHOR).getValue());
		destinationNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED, sourceNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_CREATED).getValue());
		destinationNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED, sourceNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED).getValue());
		
		List<? extends MetadataDefinition> metadataDefinitions = documentType.getMetadataDefinitions();
		if (CollectionUtils.isNotEmpty(metadataDefinitions)) {
			for (MetadataDefinition metadataDefinition : metadataDefinitions) {
				String propertyName = metadataDefinition.getJrPropertyName();				
				Property sourceProperty = JackRabbitCommons.getNodeProperty(sourceNode, propertyName);				
				if (sourceProperty != null) {
					if (sourceProperty.isMultiple()) {
						destinationNode.setProperty(propertyName, sourceProperty.getValues());
					} else {
						destinationNode.setProperty(propertyName, sourceProperty.getValue());
					}
				}
			}
		}
		
		List<MetadataCollection> metadataCollections = documentType.getMetadataCollections();
		if (CollectionUtils.isNotEmpty(metadataCollections)) {
			for (MetadataCollection metadataCollection : metadataCollections) {
				String propertyName = metadataCollection.getJrPropertyName();				
				Property sourceProperty = JackRabbitCommons.getNodeProperty(sourceNode, propertyName);				
				if (sourceProperty != null) {
					if (sourceProperty.isMultiple()) {
						destinationNode.setProperty(propertyName, sourceProperty.getValues());
					} else {
						destinationNode.setProperty(propertyName, sourceProperty.getValue());
					}
				}
			}
		}
		
		// Copiaza si atasamentele.
		copyAttachments(sourceNode, destinationNode);
	}

 	/**
	 * Copiaza toate atasamentele unui document dintr-un nod intr-un alt nod.
	 * @param sourceNode nodul sursa
	 * @param destinationNode nodul destinatie
	 */
	public static void copyAttachments(Node sourceNode, Node destinationNode) throws RepositoryException {
		
		// Sterge atasamentele nodului destinatie.
		removeAllDocumentAttachments(destinationNode);
		
		// Adauga atasamentele din sursa.
		NodeIterator sourceAttachmentNodes = sourceNode.getNodes(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + "*");
		while (sourceAttachmentNodes.hasNext()) {
			// Obtine un atasament din sursa.
			Node sourceAttachmentNode = sourceAttachmentNodes.nextNode();
			
			Attachment attachment = getAttachmentFromNode(sourceAttachmentNode);			
			addAttachmentToDocumentNode(destinationNode, attachment);
		}
	}
	
	private static void removeAllDocumentAttachments(Node documentNode) throws RepositoryException {
		NodeIterator oldAttachmentNodes = documentNode.getNodes(JackRabbitConstants.ATTACHMENT_NODE_PREFIX + "*");
		while (oldAttachmentNodes.hasNext()) {
			Node oldAttachmentNode = oldAttachmentNodes.nextNode();
			oldAttachmentNode.remove();
		}
	}
	
	private static Attachment getAttachmentFromNode(Node attachmentNode) {
		try {
			String name = attachmentNode.getProperty(JackRabbitConstants.ATTACHMENT_PROPERTY_NAME).getString();
			byte[] data = JackRabbitCommons.getContentFromAttachmentNode(attachmentNode);
			return new Attachment(name, data);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static DocumentLogAttributes getDocumentLogAttributes(String documentLocationRealName, Node documentNode) throws RepositoryException {
		
		DocumentLogAttributes documentLogAttributes = new DocumentLogAttributes();
		
		documentLogAttributes.setDocumentLocationRealName(documentLocationRealName);
		
		String documentId = documentNode.getIdentifier();
		documentLogAttributes.setDocumentId(documentId);
		
		Long documentTypeId = DocumentCommons.getDocumentTypeId(documentNode);
		documentLogAttributes.setDocumentTypeId(documentTypeId);
		
		List<String> parentFolderNames = Lists.newArrayList();
		Node currentNode = documentNode;
		try {
			while (true) {
				currentNode = currentNode.getParent();
				if (FolderCommons.isFolder(currentNode)) {
					String folderName = FolderCommons.getName(currentNode);
					parentFolderNames.add(folderName);
				} else {
					break;
				}
			}
		} catch (ItemNotFoundException infe) {
			// Nu mai exista noduri parinte.
		}
		documentLogAttributes.setParentFolderNames(parentFolderNames);
		
		String documentName = getName(documentNode);
		documentLogAttributes.setDocumentName(documentName);
		
		Calendar createdDateAsCalendar = getCreatedDateAsCalendar(documentNode);
		Date createdDate = createdDateAsCalendar.getTime();
		documentLogAttributes.setCreatedDate(createdDate);
		
		String authorUserIdAsString = getAuthorUserIdAsString(documentNode);
		Long authorUserId = Long.valueOf(authorUserIdAsString);
		documentLogAttributes.setAuthorUserId(authorUserId);
		
		return documentLogAttributes;
	}
}