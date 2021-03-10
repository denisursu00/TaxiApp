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
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;

public class PrezentaSedinteCdPvgInvitatiArbSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final PrezentaSedintaCdPvgInvitatiARBReportFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	
	private DocumentType documentType;
	
	public PrezentaSedinteCdPvgInvitatiArbSearcher(Repository repository, Credentials credentials, PrezentaSedintaCdPvgInvitatiARBReportFilterModel filter,
			ArbConstants arbConstants, DocumentTypeService documentTypeService) {
		this.repository = repository;
		this.credentials = credentials;
		this.arbConstants = arbConstants;
		this.filter = filter;
		this.documentTypeService = documentTypeService;
	}
	
	private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String tipSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getSedintaMetadataName());
		String dataSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName());
		String infoInvitatiArbMetadataJrPropertyName = DocumentTypeUtils.getMetadataCollectionJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName());
				
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");
		queryBuilder.append(" AND document.[" + tipSedintaMetadataJrPropertyName + "] = $tipSedinta");		
		if (filter.getDataSedintaDeLa() != null) {
			queryBuilder.append(" AND document.[" + dataSedintaMetadataJrPropertyName + "] >= $dataSedintaDeLa");
		}
		if (filter.getDataSedintaPanaLa() != null) {
			queryBuilder.append(" AND document.[" + dataSedintaMetadataJrPropertyName + "] <= $dataSedintaPanaLa");
		}
		if (filter.getInvitatArb() != null) {
			String infoInvitatiArbMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName();
			DocumentType prezentaCdPvgDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());
			MetadataDefinition metadataDefinitionInvitatArb = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType, infoInvitatiArbMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatArbOfInformatiiInvitatiArbMetadataName()); 
			String separator = JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES;
			queryBuilder.append(" AND document.[" + infoInvitatiArbMetadataJrPropertyName + "] like '%" +metadataDefinitionInvitatArb.getId() + separator + filter.getInvitatArb()+ "%'");
		}
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));
		query.bindValue("tipSedinta", session.getValueFactory().createValue(filter.getTipSedinta()));
		if (filter.getDataSedintaDeLa() != null) {
			query.bindValue("dataSedintaDeLa", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(filter.getDataSedintaDeLa())));
		}
		if (filter.getDataSedintaPanaLa() != null) {
			query.bindValue("dataSedintaPanaLa", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(filter.getDataSedintaPanaLa())));
		}
		
		return query;
	}
	
	private void initContext() {
		documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());
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
