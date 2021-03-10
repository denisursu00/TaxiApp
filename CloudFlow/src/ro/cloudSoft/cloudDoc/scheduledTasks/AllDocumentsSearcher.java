package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;

public class AllDocumentsSearcher {

	private final Repository repository;
	private final Credentials credentials;
	private Map<Long, DocumentType> docTypeById;
	private Map<String, List<DocumentIdentifier>> excludeDocIdsByDocLocRealName;

	public AllDocumentsSearcher(Repository repository, Credentials credentials, Set<DocumentIdentifier> excludedDocIds, HashMap<Long, DocumentType> docTypeById) {
		this.repository = repository;
		this.credentials = credentials;
		this.docTypeById = docTypeById;
		this.excludeDocIdsByDocLocRealName = new HashMap<>();
		if (CollectionUtils.isNotEmpty(excludedDocIds)) {
			this.excludeDocIdsByDocLocRealName = excludedDocIds.stream().collect(Collectors.groupingBy(DocumentIdentifier::getDocumentLocationRealName));
		}
	}

	private Query createQuery(Session session, String documentLocationRealName) throws RepositoryException {

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document ");
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		List<DocumentIdentifier> excludeDocIds = excludeDocIdsByDocLocRealName.get(documentLocationRealName);
		if (CollectionUtils.isNotEmpty(excludeDocIds)) {
			for (DocumentIdentifier excludedId : excludeDocIds) {
				queryBuilder.append(" AND document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] <> '" + excludedId.getDocumentId() + "' ");
			}
		}
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);

		return query;
	}

	private void initContext() {

	}

	public List<Document> search(String documentLocationRealName) {

		List<Document> documents = new LinkedList<>();

		Session session = null;
		try {

			initContext();

			session = repository.login(credentials, documentLocationRealName);

			Query query = createQuery(session, documentLocationRealName);
			QueryResult queryResult = query.execute();

			NodeIterator resultNodes = queryResult.getNodes();
			while (resultNodes.hasNext()) {
				Node resultNode = resultNodes.nextNode();
				Long documentTypeId = DocumentCommons.getDocumentTypeId(resultNode);
				DocumentType documentType = docTypeById.get(documentTypeId);
				documents.add(DocumentCommons.getDocumentFromNode(documentLocationRealName, resultNode, documentType));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return documents;
	}
}
