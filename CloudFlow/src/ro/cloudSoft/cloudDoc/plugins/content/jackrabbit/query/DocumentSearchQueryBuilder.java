package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.CollectionSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.MetadataSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants.QueryConstants;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class DocumentSearchQueryBuilder {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DocumentSearchQueryBuilder.class);
	
	/** numarul de milisecunde intr-o zi */
	private static final long MILISECONDS_IN_DAY = 24 * 60 * 60 * 1000;
	
	public static String getQuery(DocumentSearchCriteria criteria, DocumentType documentType, SecurityManager userSecurity) {
		
		String userId = userSecurity.getUserIdAsString();
		
		List<String> restrictions = new ArrayList<String>();
		
		StringBuilder nodeTypeRestriction = new StringBuilder();
		
		// cautare dupa documente blocate de utilizatorul curent
		StringBuilder lockedDocument = new StringBuilder();
		lockedDocument.append("(@jcr:primaryType = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "')");
		lockedDocument.append(" and ");
		lockedDocument.append("(");
		lockedDocument.append("(@" + JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY + " = '" + userId + "')");
		if (SecurityUtils.isUserAdmin(userSecurity)) {
			lockedDocument.append(" or ");
			lockedDocument.append("(");
			lockedDocument.append("(@" + JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY + " != '')");
			lockedDocument.append(" and ");
			lockedDocument.append("(@" + JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY + " != '" + userId + "')");
			lockedDocument.append(")");
		}
		lockedDocument.append(")");
		nodeTypeRestriction.append("(" + lockedDocument.toString() + ")");
		nodeTypeRestriction.append(QueryConstants.OR);
		// cautare dupa ultimele versiuni stabile ale documentelor
		String lastVersion = "@jcr:primaryType = '" + JackRabbitConstants.VERSION_NODE_TYPE + "' and @" + JackRabbitConstants.VERSION_PROPERTY_LAST_STABLE_VERSION + " = 'true'";
		nodeTypeRestriction.append("(" + lastVersion + ")");
		// cautare dupa alte versiuni ale documentelor (daca e specificat acest lucru)
		if (criteria.isSearchInVersions()) {
			nodeTypeRestriction.append(QueryConstants.OR);
			String otherVersion = "@jcr:primaryType = '" + JackRabbitConstants.VERSION_NODE_TYPE + "' and @" + JackRabbitConstants.VERSION_PROPERTY_LAST_STABLE_VERSION + " = 'false'";
			nodeTypeRestriction.append("(" + otherVersion + ")");
		}
		restrictions.add("(" + nodeTypeRestriction.toString() + ")");

		// restrictia pe data crearii
		StringBuilder createdRestriction = new StringBuilder();
		if (criteria.getCreatedStart() != null) {
			String createdStartRestriction = "@" + JackRabbitConstants.ENTITY_PROPERTY_CREATED + " >= xs:dateTime('" + getFormattedDate(criteria.getCreatedStart(), false) + "')";
			createdRestriction.append(createdStartRestriction);
		}
		if (criteria.getCreatedEnd() != null) {
			if (createdRestriction.length() > 0) {
				createdRestriction.append(QueryConstants.AND);
			}
			String createdEndRestriction = "@" + JackRabbitConstants.ENTITY_PROPERTY_CREATED + " <= xs:dateTime('" + getFormattedDate(criteria.getCreatedEnd(), true) + "')";
			createdRestriction.append(createdEndRestriction);
		}
		if (createdRestriction.length() > 0) {
			restrictions.add("(" + createdRestriction.toString() + ")");
		}

		// restrictia pe tipul documentului
		if (criteria.getDocumentTypeIdList() != null) {
			StringBuilder documentTypeIdsRestriction = new StringBuilder();
			for (Long documentTypeId : criteria.getDocumentTypeIdList()) {
				String documentTypeIdRestriction = "@" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + " = '" + documentTypeId.toString() + "'";
				documentTypeIdsRestriction.append(documentTypeIdRestriction + QueryConstants.OR);
			}
			if (documentTypeIdsRestriction.length() > 0) {
				documentTypeIdsRestriction.delete(documentTypeIdsRestriction.lastIndexOf(QueryConstants.OR), documentTypeIdsRestriction.length());
				restrictions.add("(" + documentTypeIdsRestriction.toString() + ")");
			}
		}
		
		// restrictia pe starea in care se afla documentul
		if (criteria.getWorkflowStateIdList() != null) {
			StringBuilder workflowStateIdsRestriction = new StringBuilder();
			for (Long workflowStateId : criteria.getWorkflowStateIdList()) {
				String workflowStateIdRestriction = "@" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + " = '" + workflowStateId.toString() + "'";
				workflowStateIdsRestriction.append(workflowStateIdRestriction + QueryConstants.OR);
			}
			if (workflowStateIdsRestriction.length() > 0) {
				workflowStateIdsRestriction.delete(workflowStateIdsRestriction.lastIndexOf(QueryConstants.OR), workflowStateIdsRestriction.length());
				restrictions.add("(" + workflowStateIdsRestriction.toString() + ")");
			}
		}
		
		// Numai daca e un singur tip de document selectat atunci are sens query-ul per metadate.
		
		if (CollectionUtils.isNotEmpty(criteria.getDocumentTypeIdList())) {
			
			// restrictiile pe metadate
			if (criteria.getMetadataSearchCriteriaList() != null) {
				
				Map<Long, MetadataDefinition> metadataDefinitionById = new HashMap<>();
				if (CollectionUtils.isNotEmpty(documentType.getMetadataDefinitions())) {
					for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
						metadataDefinitionById.put(metadataDefinition.getId(), metadataDefinition);
					}
				}
				
				Map<Long, List<String>> metadataRestrictionByDefId = new HashMap<>();				
				for (MetadataSearchCriteria metadataSearchCriteria : criteria.getMetadataSearchCriteriaList()) {					
					MetadataDefinition metadataDefinition = metadataDefinitionById.get(metadataSearchCriteria.getMetadataDefinitionId());
					if (metadataDefinition == null) {
						throw new RuntimeException("metadata definition not found for id [" + metadataSearchCriteria.getMetadataDefinitionId() + "]");
					}
					if (metadataRestrictionByDefId.get(metadataSearchCriteria.getMetadataDefinitionId()) == null) {
						metadataRestrictionByDefId.put(metadataSearchCriteria.getMetadataDefinitionId(), new ArrayList<>());
					}
					String metadataRestriction = getMetadataRestriction(metadataSearchCriteria, metadataDefinition);
					metadataRestrictionByDefId.get(metadataSearchCriteria.getMetadataDefinitionId()).add(metadataRestriction);
				}
				
				for (Long metadatadefId : metadataRestrictionByDefId.keySet()) {
					List<String> metadataRestrictions = metadataRestrictionByDefId.get(metadatadefId);
					if (metadataRestrictions.size() == 1) {
						restrictions.add(metadataRestrictions.get(0));
					} else {
						String multipleMetadataRestricion = "";
						for (int i = 0; i < metadataRestrictions.size(); i++) {
							if (multipleMetadataRestricion.length() > 0) {
								multipleMetadataRestricion += " or ";
							}
							multipleMetadataRestricion += metadataRestrictions.get(i);
						}
						restrictions.add("(" + multipleMetadataRestricion + ")");
					}
				}
				
			}
			
			// restrictiile pe metadatele din colectii
			if (criteria.getCollectionSearchCriteriaList() != null) {
				
				Map<Long, MetadataCollection> metadataCollectionById = new HashMap<>();
				if (CollectionUtils.isNotEmpty(documentType.getMetadataCollections())) {
					for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
						metadataCollectionById.put(metadataCollection.getId(), metadataCollection);
					}
				}
				
				for (CollectionSearchCriteria collectionSearchCriteria : criteria.getCollectionSearchCriteriaList()) {
					MetadataCollection metadataCollection = metadataCollectionById.get(collectionSearchCriteria.getCollectionDefinitionId());
					if (metadataCollection == null) {
						throw new RuntimeException("metadata collection not found for id [" + collectionSearchCriteria.getCollectionDefinitionId() + "]");
					}
					String collectionRestriction = getCollectionRestriction(collectionSearchCriteria, metadataCollection);
					restrictions.add(collectionRestriction);
				}
			}
		}
		
		// partea cu restrictii a interogarii
		StringBuilder queryRestriction = new StringBuilder();		
		for (String restriction : restrictions) {
			queryRestriction.append(restriction + QueryConstants.AND);
		}
		if (queryRestriction.length() > 0) {
			queryRestriction.delete(queryRestriction.lastIndexOf(QueryConstants.AND), queryRestriction.length());
			queryRestriction.insert(0, '[');
			queryRestriction.insert(queryRestriction.length(), ']');
		}
		
		String query = "//element(*, " + JackRabbitConstants.DOCUMENT_NODE_TYPE + ")" + queryRestriction.toString();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Se va executa urmatoarea interogare: " + query, "construirea query-ului pentru cautarea documentelor");
		}
		
		return query;
	}
	
	private static String getFormattedDate(Date date, boolean isEndDate) {
		if (isEndDate) {
			date.setTime(date.getTime() + MILISECONDS_IN_DAY - 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(QueryConstants.FORMAT_DATE);
		return sdf.format(date);
	}
	
	private static String getMetadataRestriction(MetadataSearchCriteria metadataSearchCriteria, MetadataDefinition metadataDefinition) {
		String metadataType = metadataDefinition.getMetadataType();
		if (metadataType.equals(MetadataDefinition.TYPE_NUMERIC)) {
			// TODO De vazut de ce nu merge filtrarea dupa cele numerice. Cred ca trebuie trecut la SQL2.
			return "@" + metadataDefinition.getJrPropertyName() + " = " + metadataSearchCriteria.getValue();
		} else if (metadataType.equals(MetadataDefinition.TYPE_AUTO_NUMBER) 
				|| metadataType.equals(MetadataDefinition.TYPE_DATE)
				|| metadataType.equals(MetadataDefinition.TYPE_DATE_TIME)
				|| metadataType.equals(MetadataDefinition.TYPE_MONTH)
				|| metadataType.equals(MetadataDefinition.TYPE_LIST)
				|| metadataType.equals(MetadataDefinition.TYPE_NOMENCLATOR)
				|| metadataType.equals(MetadataDefinition.TYPE_USER)
				|| metadataType.equals(MetadataDefinition.TYPE_GROUP)
				|| metadataType.equals(MetadataDefinition.TYPE_DOCUMENT)
				|| metadataType.equals(MetadataDefinition.TYPE_CALENDAR)
				|| metadataType.equals(MetadataDefinition.TYPE_PROJECT)) {			
			return "@" + metadataDefinition.getJrPropertyName() + " = '" + metadataSearchCriteria.getValue() + "'";
		} else if (metadataType.equals(MetadataDefinition.TYPE_TEXT) || metadataType.equals(MetadataDefinition.TYPE_TEXT_AREA)) {
			return "jcr:like(@" + metadataDefinition.getJrPropertyName() +", '%" + metadataSearchCriteria.getValue() + "%')";
		} else {
			throw new RuntimeException("tip de metadata necunoscut [" + metadataType + "]");
		}
	}
	
	private static String getCollectionRestriction(CollectionSearchCriteria criteria, MetadataCollection metadataCollection) {
		String metadataValueRestrict = criteria.getMetadataDefinitionId() + JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES + "%" + criteria.getValue() + "%";
		return "jcr:like(@" + metadataCollection.getJrPropertyName() + ", '" + metadataValueRestrict + "'";
	}
}