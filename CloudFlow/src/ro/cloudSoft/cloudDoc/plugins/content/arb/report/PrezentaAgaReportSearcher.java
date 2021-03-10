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

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.DateUtils;

public class PrezentaAgaReportSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final PrezentaAgaFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentPrezentaAgaTypeService;
	private DocumentType documentType;
	
	public PrezentaAgaReportSearcher(Repository repository, Credentials credentials, PrezentaAgaFilterModel filter,
			ArbConstants arbConstants, DocumentTypeService documentTypeService) {
		this.repository = repository;
		this.credentials = credentials;
		this.arbConstants = arbConstants;
		this.filter = filter;
		this.documentPrezentaAgaTypeService = documentTypeService;
	}
	
	private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String infoPrezentaMembriiMetadataCollectionJrPropertyName = DocumentTypeUtils.getMetadataCollectionJrPropertyName(documentType, arbConstants.getDocumentPrezentaAgaConstants().getPrezentaMembriiMetadataName());
		String prezentaMembriiMetadataCollectionName = arbConstants.getDocumentPrezentaAgaConstants().getPrezentaMembriiMetadataName();
		String dataInceputSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaAgaConstants().getDataInceputMetadataName());
		
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");	
		
		if (filter.getDocumentId() != null) {
			queryBuilder.append(" AND document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] = $documentId");	
		}
		if (filter.getBancaId() != null) {
			String intitutieOfPrezentaMembriiMetadataName = arbConstants.getDocumentPrezentaAgaConstants().getIntitutieOfPrezentaMembriiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoPrezentaMembriiMetadataCollectionJrPropertyName, 
					filter.getBancaId().toString(), prezentaMembriiMetadataCollectionName, intitutieOfPrezentaMembriiMetadataName, true));
		}
		if (filter.getFunctie() != null) {
			String functieOfPrezentaMembriiMetadataName = arbConstants.getDocumentPrezentaAgaConstants().getFunctieParticipantAcreditatInlocuitorOfPrezentaMembriiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithContainMatchFilterInCollectionProperty("document", documentType, infoPrezentaMembriiMetadataCollectionJrPropertyName, 
					filter.getFunctie(), prezentaMembriiMetadataCollectionName, functieOfPrezentaMembriiMetadataName, true));
		}
		
		if (filter.getDeLaDataInceput()!= null) {
			queryBuilder.append(" AND document.[" + dataInceputSedintaMetadataJrPropertyName + "] >= $dataSedintaDeLa");
		}
		if (filter.getPanaLaDataInceput() != null) {
			queryBuilder.append(" AND document.[" + dataInceputSedintaMetadataJrPropertyName + "] <= $dataSedintaPanaLa");
		}
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));
		if (filter.getDocumentId() != null) {
			query.bindValue("documentId", session.getValueFactory().createValue(filter.getDocumentId()));
		}
		if (filter.getDeLaDataInceput() != null) {
			query.bindValue("dataSedintaDeLa", session.getValueFactory().createValue(MetadataValueHelper.toDateTimeValue(DateUtils.nullHourMinutesSeconds(filter.getDeLaDataInceput()))));
		}
		if (filter.getPanaLaDataInceput() != null) {
			query.bindValue("dataSedintaPanaLa", session.getValueFactory().createValue(MetadataValueHelper.toDateTimeValue(DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaDataInceput()))));
		}
		return query;
	}

	private void initContext() {
		documentType = documentPrezentaAgaTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaAgaConstants().getDocumentTypeName());
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
