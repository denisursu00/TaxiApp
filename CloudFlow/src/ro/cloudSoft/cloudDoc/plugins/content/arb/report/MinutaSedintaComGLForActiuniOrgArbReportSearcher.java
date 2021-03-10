package ro.cloudSoft.cloudDoc.plugins.content.arb.report;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import ro.cloudSoft.cloudDoc.core.constants.CalendarConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;

public class MinutaSedintaComGLForActiuniOrgArbReportSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final DocumentIdentifier documentIdentifierPrezentaComGlfilter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	private DocumentType documentType;
	
	public MinutaSedintaComGLForActiuniOrgArbReportSearcher(Repository repository, Credentials credentials, DocumentIdentifier documentIdentifierPrezentaComGlfilter,
			ArbConstants arbConstants, DocumentTypeService documentTypeService) {
		this.repository = repository;
		this.credentials = credentials;
		this.arbConstants = arbConstants;
		this.documentIdentifierPrezentaComGlfilter = documentIdentifierPrezentaComGlfilter;
		this.documentTypeService = documentTypeService;
	}
	
	private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String denumireDocPrezentaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDenumireDocumentPrezentaComisieGlMetadataName());
		String locatieJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentMinutaSedintaComisieGLConstants().getLocatieMetadataName());
				
		
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");	
		queryBuilder.append(" AND document.[" + denumireDocPrezentaMetadataJrPropertyName + "] = $denumireDocPrezenta");	
		queryBuilder.append(" AND UPPER(document.[" + locatieJrPropertyName + "]) like '%" + CalendarConstants.CALENDAR_EVENT_LOCATION_VALUE_ARB.toUpperCase() + "%' ");	
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));
		query.bindValue("denumireDocPrezenta", session.getValueFactory().createValue(MetadataValueHelper.toMetadataValue(this.documentIdentifierPrezentaComGlfilter)));
		
		return query;
	}

	private void initContext() {
		String documentTypeName = arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDocumentTypeName();
		documentType = documentTypeService.getDocumentTypeByName(documentTypeName);
	}
	
	public List<Document> search(String documentLocationRealName)  {
		
		List<Document> documents = new LinkedList<>();
		
		Session session = null;		
		try {
			
			initContext();
			
			session = repository.login(credentials, documentLocationRealName);
		
			Query query = createQuery(session);
			QueryResult queryResult = query.execute();
			
			NodeIterator resultNodes = queryResult.getNodes();	
			while (resultNodes.hasNext()) {
				Node resultNode = resultNodes.nextNode();
				documents.add(DocumentCommons.getDocumentFromNodeWithoutAttachments(documentLocationRealName, resultNode, documentType));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return documents;
	}
}
