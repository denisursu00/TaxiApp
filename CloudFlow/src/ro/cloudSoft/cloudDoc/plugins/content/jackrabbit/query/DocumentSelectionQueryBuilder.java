package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query;

import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilter;

public class DocumentSelectionQueryBuilder {
	
	private static String PLACEHOLDER_DOCUMENT_TYPE_ID = "documentTypeId";
	private static String PLACEHOLDER_DOCUMENT_NAME = "documentName";
	
	private final Session session;
	private final DocumentSelectionSearchFilter filter;
	
	public DocumentSelectionQueryBuilder(Session session, DocumentSelectionSearchFilter filter) {
		this.session = session;
		this.filter = filter;
	}
	
	private String createQueryString() {	
		try {
			StringBuilder queryBuilder = new StringBuilder();
			
			String nodeType = JackRabbitConstants.DOCUMENT_NODE_TYPE;
			
			queryBuilder.append(" SELECT document.* FROM [" + nodeType + "] AS document "); 
			queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + nodeType + "'");
			queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $" + PLACEHOLDER_DOCUMENT_TYPE_ID);
			if (StringUtils.isNotBlank(filter.getDocumentName())) {
				queryBuilder.append(" AND UPPER(document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_NAME + "]) LIKE $" + PLACEHOLDER_DOCUMENT_NAME);
			}		
			
			return queryBuilder.toString();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public Query getQuery() {
		try {
						
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			
			String queryAsString = createQueryString();
			Query query = queryManager.createQuery(queryAsString.toString(), Query.JCR_SQL2);		
			
			query.bindValue(PLACEHOLDER_DOCUMENT_TYPE_ID, session.getValueFactory().createValue(this.filter.getDocumentTypeId().toString()));
			if (StringUtils.isNotBlank(filter.getDocumentName())) {
				query.bindValue(PLACEHOLDER_DOCUMENT_NAME, session.getValueFactory().createValue("%" + this.filter.getDocumentName().toUpperCase() + "%"));
			}
			
			return query;
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
