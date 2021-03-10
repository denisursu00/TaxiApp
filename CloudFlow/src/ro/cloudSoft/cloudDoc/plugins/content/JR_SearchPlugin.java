package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSelectionSearchResultView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.SecurityCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query.DocumentSearchQueryBuilder;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query.DocumentSelectionQueryBuilder;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilter;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class JR_SearchPlugin extends JR_PluginBase implements SearchPlugin, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_SearchPlugin.class);
	
	private DocumentTypeDao documentTypeDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Document> findDocuments(DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity) throws AppException {
		List<Document> foundDocuments = new ArrayList<Document>();
		Session session = null;
		try {
			
			Map<Long, DocumentType> documentTypeById = new HashMap<>();
			DocumentType documentType = null;			
			if (CollectionUtils.isNotEmpty(documentSearchCriteria.getDocumentTypeIdList()) && documentSearchCriteria.getDocumentTypeIdList().size() == 1) {
				Long documentTypeId = documentSearchCriteria.getDocumentTypeIdList().get(0);
				documentType = documentTypeDao.find(documentTypeId);
				documentTypeById.put(documentTypeId, documentType);
			}
			
			String documentLocationRealName = documentSearchCriteria.getDocumentLocationRealName();
			session = repository.login(credentials, documentLocationRealName);
			
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(DocumentSearchQueryBuilder.getQuery(documentSearchCriteria, documentType, userSecurity), Query.XPATH);
			QueryResult queryResult = query.execute();
			NodeIterator resultNodes = queryResult.getNodes();
			
			while (resultNodes.hasNext()) {
				Node resultNode = resultNodes.nextNode();
				
				Long documentTypeId = DocumentCommons.getDocumentTypeId(resultNode);
				documentType = documentTypeById.get(documentTypeId);
				if (documentType == null) {
					documentType = documentTypeDao.find(documentTypeId);
					documentTypeById.put(documentTypeId, documentType);
				}
				
				if (resultNode.getPrimaryNodeType().getName().equals(JackRabbitConstants.DOCUMENT_NODE_TYPE)) {
					// Daca este document...
					if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, resultNode, userSecurity)) {
						Document document = DocumentCommons.getDocumentFromNode(documentLocationRealName, resultNode, documentType);
						foundDocuments.add(document);
					}
				} else if (resultNode.getPrimaryNodeType().getName().equals(JackRabbitConstants.VERSION_NODE_TYPE)) {
					// Daca este versiunea unui document...
					Node documentNode = resultNode.getParent();
					if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity)) {
						int versionNumber = Integer.parseInt(resultNode.getName().substring(JackRabbitConstants.VERSION_NODE_PREFIX.length()));
						Document document = DocumentCommons.getDocumentFromVersion(documentLocationRealName, versionNumber, documentNode, documentType);
						// TODO Poate nu trebuie afisat numarul versiunii la toate...
						//document.setName(document.getName() + " (" + versionNumber + ")");
						foundDocuments.add(document);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "findDocuments", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return foundDocuments;
	}
	
	@Override
	public List<DocumentSelectionSearchResultView> findDocumentsForSelection(DocumentSelectionSearchFilter filter, SecurityManager userSecurity) throws AppException {
		
		List<DocumentSelectionSearchResultView> foundDocuments = new ArrayList<>();
		
		Session session = null;
		try {
			
			// DocumentType documentType = documentTypeDao.find(filter.getDocumentTypeId());	
			
			session = repository.login(credentials, filter.getDocumentLocationRealName());
			
			DocumentSelectionQueryBuilder queryBuilder = new DocumentSelectionQueryBuilder(session, filter);
			Query query = queryBuilder.getQuery();
			QueryResult queryResult = query.execute();
			NodeIterator resultNodesIterator = queryResult.getNodes();
			
			while (resultNodesIterator.hasNext()) {
				
				Node resultNode = resultNodesIterator.nextNode();				
				if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, resultNode, userSecurity)) {
					
					DocumentSelectionSearchResultView view = new DocumentSelectionSearchResultView();
					view.setDocumentLocationRealName(filter.getDocumentLocationRealName());
					view.setDocumentId(resultNode.getIdentifier());
					view.setDocumentName(DocumentCommons.getName(resultNode));
					view.setAuthorId(DocumentCommons.getAuthorUserIdAsString(resultNode));
					view.setCreatedDate(DocumentCommons.getCreatedDateAsCalendar(resultNode).getTime());
					
					foundDocuments.add(view);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "findDocumentsForSelection", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return foundDocuments;
	}
	
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
}