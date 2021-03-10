package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;

import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

public class AvailableDocumentsForArchivingByWithoutWorkflowAndBeforeDateSearcher {

	private final Repository repository;
	private final Credentials credentials;
	private final Date beforeDate;
	private final List<Long> documentTypeIds;
	
	public AvailableDocumentsForArchivingByWithoutWorkflowAndBeforeDateSearcher(Repository repository, Credentials credentials, Set<Long> documentTypeIds, Date beforeDate) {
		this.repository = repository;
		this.credentials = credentials;
		this.beforeDate = beforeDate;
		this.documentTypeIds = new ArrayList<>(documentTypeIds);
	}

	private Query createQuery(Session session, String documentLocationRealName) throws RepositoryException {

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document ");
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] IS NULL ");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.ENTITY_PROPERTY_CREATED + "] < $beforeDate ");
		if (CollectionUtils.isNotEmpty(documentTypeIds)) {
			queryBuilder.append(" AND (");
			for (int i = 0; i < documentTypeIds.size(); i++) {				
				if (i > 0) {
					queryBuilder.append(" OR ");
				}
				queryBuilder.append(" document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId" + i + " ");
			}
			queryBuilder.append(") ");
		}
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);
		query.bindValue("beforeDate", session.getValueFactory().createValue(DateUtils.toCalendar(beforeDate)));	
		if (CollectionUtils.isNotEmpty(documentTypeIds)) {
			for (int i = 0; i < documentTypeIds.size(); i++) {
				query.bindValue("documentTypeId" + i, session.getValueFactory().createValue(documentTypeIds.get(i).toString()));	
			}
		}
		return query;
	}

	private void initContext() {
	}

	public List<DocumentIdentifier> search(String documentLocationRealName) {

		List<DocumentIdentifier> documents = new LinkedList<>();

		Session session = null;
		try {

			initContext();

			session = repository.login(credentials, documentLocationRealName);

			Query query = createQuery(session, documentLocationRealName);
			QueryResult queryResult = query.execute();

			NodeIterator resultNodes = queryResult.getNodes();
			while (resultNodes.hasNext()) {
				Node resultNode = resultNodes.nextNode();
				documents.add(new DocumentIdentifier(documentLocationRealName, resultNode.getIdentifier()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return documents;
	}
}
