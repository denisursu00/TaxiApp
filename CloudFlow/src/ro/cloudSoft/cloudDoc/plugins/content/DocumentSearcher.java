package ro.cloudSoft.cloudDoc.plugins.content;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import io.jsonwebtoken.lang.Collections;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.services.content.DocumentFilterModel;
import ro.cloudSoft.cloudDoc.services.content.MetadataFilterModel;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.hibernate.QueryUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class DocumentSearcher {
	
	private static final String PLACEHOLDER_PREFIX = "$";
	private static final String CREATED_DATE_DE_LA_PLACEHOLDER_NAME = "createdDateDeLa";
	private static final String CREATED_DATE_PANA_LA_PLACEHOLDER_NAME = "createdDatePanaLa";
	
	private final Repository repository;
	private final Credentials credentials;
	private final DocumentFilterModel filter;
	private final  List<String> documentIdsFilter;
	private final DocumentTypeDao documentTypeDao;
	private Map<Long, MetadataDefinition> metadataDefinitionById;
	private Map<String, Object> placeholderValueByName;
	
	private DocumentType documentType;
	
	public DocumentSearcher(Repository repository, Credentials credentials, DocumentFilterModel filter, DocumentTypeDao documentTypeDao, List<String> documentIdsFilter) {
		this.repository = repository;
		this.credentials = credentials;
		this.filter = filter;
		this.documentTypeDao = documentTypeDao;
		this.documentIdsFilter = documentIdsFilter;
		initContext();
	}
	
	private Query createQuery(Session session, boolean isQueryForCountResults) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		if (isQueryForCountResults) {
			queryBuilder.append(" SELECT document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] FROM [" + JackRabbitConstants.FOLDER_NODE_TYPE + "] AS folder ");
		} else {
			queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.FOLDER_NODE_TYPE + "] AS folder ");
		}
		queryBuilder.append(" INNER JOIN [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document ON ISCHILDNODE(document, folder) ");
		queryBuilder.append(" WHERE folder.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] = '" + filter.getFolderId() + "' ");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		
		if(!Collections.isEmpty(documentIdsFilter) && StringUtils.isNotEmpty(filter.getStatusFilterValue())) {
			queryBuilder.append(" AND ( ");
			boolean isFirstCondition = true;
			for ( String docId: documentIdsFilter) {
				if (!isFirstCondition) {
					queryBuilder.append(" OR ");
				}
				queryBuilder.append(" document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] = '" + docId + "' ");
				isFirstCondition = false;
			}

			queryBuilder.append(" ) ");
		}
		
		appendMetadataFilters(queryBuilder);
		
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);	
		for (String placeholderName : placeholderValueByName.keySet()) {
			Object placeholderValue = placeholderValueByName.get(placeholderName);
			if (placeholderValue instanceof String) {
				query.bindValue(placeholderName, session.getValueFactory().createValue(((String)placeholderValue).toUpperCase()));
			} else if (placeholderValue instanceof BigDecimal){
				query.bindValue(placeholderName, session.getValueFactory().createValue((BigDecimal)placeholderValue));
			} else if (placeholderValue instanceof Calendar){
				query.bindValue(placeholderName, session.getValueFactory().createValue((Calendar)placeholderValue));
			} else {
				throw new RuntimeException("invalid filter value type:" + placeholderValue.getClass() + " of placeholder:" + placeholderName);
			}
		}
		
		return query;
	}
	
	private void appendMetadataFilters(StringBuilder queryBuilder) {
		if (StringUtils.isNotEmpty(filter.getNameFilterValue())) {
			queryBuilder.append(" AND UPPER(document.[" + JackRabbitConstants.ENTITY_PROPERTY_NAME + "]) LIKE '%" + filter.getNameFilterValue().toUpperCase() + "%' ");
		}
		if (StringUtils.isNotEmpty(filter.getAuthorFilterValue())) {
			queryBuilder.append(" AND UPPER(document.[" + JackRabbitConstants.ENTITY_PROPERTY_AUTHOR + "]) = '" + filter.getAuthorFilterValue().toUpperCase() + "'");
		}
		if (filter.getCreatedFilterValue() != null) {
			queryBuilder.append(" AND document.[" + JackRabbitConstants.ENTITY_PROPERTY_CREATED+ "] > " + PLACEHOLDER_PREFIX + CREATED_DATE_DE_LA_PLACEHOLDER_NAME);
			Calendar createdDateDeLaAsCalendar = Calendar.getInstance();
			createdDateDeLaAsCalendar.setTime(DateUtils.nullHourMinutesSeconds(filter.getCreatedFilterValue()));
			placeholderValueByName.put(CREATED_DATE_DE_LA_PLACEHOLDER_NAME, createdDateDeLaAsCalendar);
			queryBuilder.append(" AND document.[" + JackRabbitConstants.ENTITY_PROPERTY_CREATED+ "] < " + PLACEHOLDER_PREFIX + CREATED_DATE_PANA_LA_PLACEHOLDER_NAME);
			Calendar createdDatePanaLaAsCalendar = Calendar.getInstance();
			createdDatePanaLaAsCalendar.setTime(DateUtils.maximizeHourMinutesSeconds(filter.getCreatedFilterValue()));
			placeholderValueByName.put(CREATED_DATE_PANA_LA_PLACEHOLDER_NAME, createdDatePanaLaAsCalendar);
		}
		if (StringUtils.isNotEmpty(filter.getLockedFilterValue()) && Boolean.valueOf(filter.getLockedFilterValue())) { 
			queryBuilder.append(" AND document.[" + JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY + "] <> ''");
		} else if (StringUtils.isNotEmpty(filter.getLockedFilterValue()) && !Boolean.valueOf(filter.getLockedFilterValue())) {  
			queryBuilder.append(" AND document.[" + JackRabbitConstants.ENTITY_PROPERTY_LOCKED_BY + "] = ''");
		}
		for(MetadataFilterModel metadataFilter : filter.getMetadataFilters()) {
			MetadataDefinition metadataDefinition = metadataDefinitionById.get(metadataFilter.getMetadataId());
			switch(metadataDefinition.getMetadataType()) {
			case MetadataDefinition.TYPE_TEXT :  appendQueryContainsFilter(queryBuilder, metadataFilter, metadataDefinition); break;
			case MetadataDefinition.TYPE_TEXT_AREA :  appendQueryContainsFilter(queryBuilder, metadataFilter, metadataDefinition); break;
			case MetadataDefinition.TYPE_AUTO_NUMBER :  appendQueryContainsFilter(queryBuilder, metadataFilter, metadataDefinition); break;
			case MetadataDefinition.TYPE_DATE_TIME :  appendQueryDateTimeFilter(queryBuilder, metadataFilter, metadataDefinition); break;
			default:  appendQueryEqualsFilter(queryBuilder, metadataFilter, metadataDefinition); break;
			}
		}
		
	}

	private void appendQueryDateTimeFilter(StringBuilder queryBuilder, MetadataFilterModel metadataFilter, MetadataDefinition metadataDefinition) {
		String placeHolderNameDataDeLa = metadataDefinition.getName() + "DeLa";
		String placeHolderNameDataPanaLa = metadataDefinition.getName() + "PanaLa";
		Date filterValue = null;
		try {
			filterValue = DateUtils.parse(metadataFilter.getValue(), FormatConstants.METADATA_DATE_FOR_SAVING);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		queryBuilder.append(" AND document.[" + metadataDefinition.getJrPropertyName() + "] > " + PLACEHOLDER_PREFIX + placeHolderNameDataDeLa);
		Date filterValueDeLa = DateUtils.nullHourMinutesSeconds(filterValue);
		placeholderValueByName.put(placeHolderNameDataDeLa, DateFormatUtils.format(filterValueDeLa, FormatConstants.METADATA_DATE_TIME_FOR_SAVING));
		
		queryBuilder.append(" AND document.[" + metadataDefinition.getJrPropertyName() + "] < " + PLACEHOLDER_PREFIX + placeHolderNameDataPanaLa);
		Date filterValuePanaLa = DateUtils.maximizeHourMinutesSeconds(filterValue);
		placeholderValueByName.put(placeHolderNameDataPanaLa,  DateFormatUtils.format(filterValuePanaLa, FormatConstants.METADATA_DATE_TIME_FOR_SAVING));
		
	}

	private void appendQueryEqualsFilter(StringBuilder queryBuilder, MetadataFilterModel metadataFilter, MetadataDefinition metadataDefinition) {
		String placeHolderName = metadataDefinition.getName();
		queryBuilder.append(" AND UPPER(document.[" + metadataDefinition.getJrPropertyName() + "]) = " + PLACEHOLDER_PREFIX + placeHolderName);
		Object filterValue = metadataFilter.getValue();
		
		 if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NUMERIC)) {
			filterValue = new BigDecimal((String) filterValue);
		} 
		
		placeholderValueByName.put(placeHolderName, filterValue);
	}


	private void appendQueryContainsFilter(StringBuilder queryBuilder, MetadataFilterModel metadataFilter, MetadataDefinition metadataDefinition) {
		String placeHolderName = metadataDefinition.getName();
		queryBuilder.append(" AND UPPER(document.[" + metadataDefinition.getJrPropertyName() + "]) LIKE " + PLACEHOLDER_PREFIX + placeHolderName );
		placeholderValueByName.put(placeHolderName, "%" + metadataFilter.getValue().toString() + "%"); 
	}

	private void initContext() {
		List<Long> metadataDefIds = new ArrayList<>();
		metadataDefinitionById = new HashMap<>();
		filter.getMetadataFilters().forEach(metadataFilter -> {
			metadataDefIds.add(metadataFilter.getMetadataId());
		});
		if(!metadataDefIds.isEmpty()) {
			metadataDefinitionById = documentTypeDao.getMetadataDefinionsMapByIds(metadataDefIds);
		} 
		placeholderValueByName = new HashMap<>();
		documentType = documentTypeDao.find(filter.getDocumentTypeId());
	}
	
	public List<Document> search()  {
		
		List<Document> documents = new LinkedList<>();
		
		Session session = null;		
		try {
			
			initContext();
			
			session = repository.login(credentials, filter.getDocumentLocationRealName());
		
			Query query = createQuery(session, false);
			query.setLimit(filter.getPageSize());
			query.setOffset(filter.getOffset());
			QueryResult queryResult = query.execute();
			RowIterator rowIterator = queryResult.getRows();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.nextRow();
				Node resultNode = row.getNode("document");
				documents.add(DocumentCommons.getDocumentFromNodeWithoutAttachments(filter.getDocumentLocationRealName(), resultNode, documentType));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return documents;
	}
	
	public int getCount()  {
		
		Session session = null;		
		try {
			
			initContext();
			
			session = repository.login(credentials, filter.getDocumentLocationRealName());
		
			Query query = createQuery(session, true);
			QueryResult queryResult = query.execute();
			return Integer.parseInt("" + queryResult.getRows().getSize());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
}
