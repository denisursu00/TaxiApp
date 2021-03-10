package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesException;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DateUtils;

public class JR_DocumentUtilitiesPlugin extends JR_PluginBase implements DocumentUtilitiesPlugin {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DocumentUtilitiesPlugin.class);
	
	private DocumentLocationPlugin documentLocationPlugin;	
	private DocumentTypeService documentTypeService;
		
	
	@Override
	public List<String> getInfoDocumentsByName(String documentName) throws AppException {
		List<String> results = new ArrayList<String>();		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			for (String documentLocationRealName : documentLocationRealNames) {
				Session session = null;
				try {
					session = repository.login(credentials, documentLocationRealName);
					
					StringBuilder queryBuilder = new StringBuilder();					
					queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
					queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
					queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_NAME + "] = $documentName");
					
					QueryManager queryManager = session.getWorkspace().getQueryManager();		
					Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
					query.bindValue("documentName", session.getValueFactory().createValue(documentName));
					
					QueryResult queryResult = query.execute();
					
					NodeIterator resultNodes = queryResult.getNodes();	
					while (resultNodes.hasNext()) {
						Node resultNode = resultNodes.nextNode();
						
						String infoDocument = "DocumentName: [" + DocumentCommons.getName(resultNode) + "]" ;
						infoDocument += ", DocumentLocationRealName: [" + documentLocationRealName + "]";
						infoDocument += ", DocumentID: [" + resultNode.getIdentifier() + "]";
						infoDocument += ", DocumentTypeID: [" + DocumentCommons.getDocumentTypeId(resultNode) + "]";
						infoDocument += ", AuthorAsUserID: [" + DocumentCommons.getAuthorUserIdAsString(resultNode) + "]";
						infoDocument += ", CreatedDate: [" + DateFormatUtils.format(DocumentCommons.getCreatedDateAsCalendar(resultNode), "dd.MM.yyyy HH:mm:ss") + "]";
												
						results.add(infoDocument);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					JackRabbitCommons.logout(session);
				}
			}
		}
		return results;
	}
	
	@Override
	public void modifyDocument(String documentLocationRealName, String documentID, String newDocumentName, String newDocumentDescription, String metadataName, String newMetadataValue) throws AppUtilitiesException {
		Session session = null;
		try {
			
			if (StringUtils.isBlank(documentLocationRealName)) {
				throw new RuntimeException("documentLocationRealName cannot be null");
			}
			if (StringUtils.isBlank(documentID)) {
				throw new RuntimeException("documentID cannot be null");
			}
			
			session = repository.login(credentials, documentLocationRealName);
			
			Node documentNode = session.getNodeByIdentifier(documentID);
			if (documentNode == null) {
				throw new RuntimeException("Document node not found");
			}
			
			String documentName = DocumentCommons.getName(documentNode);
			
			StringBuilder messageChanges = new StringBuilder();
			if (StringUtils.isNotBlank(newDocumentName)) {
				documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME, newDocumentName);
				messageChanges.append("newDocumentName=[" + newDocumentName + "]");
			}
			
			if (StringUtils.isNotBlank(newDocumentDescription)) {
				documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, newDocumentDescription);
				messageChanges.append("newDocumentDescription=[" + newDocumentDescription + "]");
			}
			
			if (StringUtils.isNotBlank(metadataName) && StringUtils.isNotBlank(newMetadataValue)) {
				
				DocumentType documentType = documentTypeService.getDocumentTypeById(DocumentCommons.getDocumentTypeId(documentNode));
				MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(documentType, metadataName);
				String oldValue = null;
				
				if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_TEXT) || metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_TEXT_AREA)) {					
					oldValue = JackRabbitCommons.getPropertyValueAsString(documentNode, metadataDefinition.getJrPropertyName());
					documentNode.setProperty(metadataDefinition.getJrPropertyName(), newMetadataValue);					
				} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DATE)) {
					try {
						Date date = DateUtils.parse(newMetadataValue, FormatConstants.METADATA_DATE_FOR_SAVING);
						if (date == null) {
							throw new RuntimeException("not parsed");
						}						
					} catch (Exception e) {
						throw new RuntimeException("Invalid metadata value for Date: [" + e.getMessage() + "]");
					}
					oldValue = JackRabbitCommons.getPropertyValueAsString(documentNode, metadataDefinition.getJrPropertyName());
					documentNode.setProperty(metadataDefinition.getJrPropertyName(), newMetadataValue);				
				} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DATE_TIME)) {
					try {
						Date date = DateUtils.parse(newMetadataValue, FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
						if (date == null) {
							throw new RuntimeException("not parsed");
						}						
					} catch (Exception e) {
						throw new RuntimeException("Invalid metadata value for DateTime: [" + e.getMessage() + "]");
					}
					oldValue = JackRabbitCommons.getPropertyValueAsString(documentNode, metadataDefinition.getJrPropertyName());
					documentNode.setProperty(metadataDefinition.getJrPropertyName(), newMetadataValue);
				} else {
					throw new RuntimeException("Not implemented change value for type metadata type " + metadataDefinition.getMetadataType());
				}
				
				messageChanges.append(messageChanges.length() > 0 ? ", " : "");
				messageChanges.append("metadataName=[" + metadataName + "]");
				messageChanges.append(", oldMetadataValue=[" + oldValue + "]");
				messageChanges.append(", newMetadataValue=[" + newMetadataValue + "]");
			}
			
			if (messageChanges.length() > 0) {
				session.save();
				LOGGER.info("Successfully modified document - documentLocationRealName=[" + documentLocationRealName + "], documentID=[" + documentID + "], name=[" + documentName + "]. CHANGES: " + messageChanges.toString() + ".");
			} else {
				throw new RuntimeException("Nu s-a specificat cel putin document name sau metadata name si value.");
			}
		} catch (NoSuchWorkspaceException e) {
			throw new AppUtilitiesException("documentLocationRealName [" + documentLocationRealName + "] not found");
		} catch (Exception e) {e.printStackTrace();
			throw new AppUtilitiesException(e.getMessage());
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	public void setDocumentLocationPlugin(DocumentLocationPlugin documentLocationPlugin) {
		this.documentLocationPlugin = documentLocationPlugin;
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
}
