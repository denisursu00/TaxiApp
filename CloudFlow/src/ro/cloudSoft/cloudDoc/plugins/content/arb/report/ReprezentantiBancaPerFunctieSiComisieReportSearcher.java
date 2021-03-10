package ro.cloudSoft.cloudDoc.plugins.content.arb.report;

import java.util.LinkedList;
import java.util.List;
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

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DocumentIdentifierModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.DateUtils;

public class ReprezentantiBancaPerFunctieSiComisieReportSearcher {

	private final Repository repository;
	private final Credentials credentials;
	private final ReprezentantiBancaPerFunctieSiComisieReportFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	
	private DocumentType documentType;
	
	public ReprezentantiBancaPerFunctieSiComisieReportSearcher(Repository repository, Credentials credentials,
			ReprezentantiBancaPerFunctieSiComisieReportFilterModel filter, ArbConstants arbConstants,
			DocumentTypeService documentTypeService) {
		this.repository = repository;
		this.credentials = credentials;
		this.filter = filter;
		this.arbConstants = arbConstants;
		this.documentTypeService = documentTypeService;
	}

	private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String comisieGlMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());
		String dataSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
		String infoInformatiiParticipantiMetadataCollectionJrPropertyName = DocumentTypeUtils.getMetadataCollectionJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());
		String informatiiParticipantiMetadataCollectionName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");

		if (filter.getDataSedintaDeLa() != null) {
			queryBuilder.append(" AND document.[" + dataSedintaMetadataJrPropertyName + "] >= $dataSedintaDeLa");
		}
		if (filter.getDataSedintaPanaLa() != null) {
			queryBuilder.append(" AND document.[" + dataSedintaMetadataJrPropertyName + "] <= $dataSedintaPanaLa");
		}
		if (CollectionUtils.isNotEmpty(filter.getComisieId())) {
			queryBuilder.append(" AND  ( document.[" + comisieGlMetadataJrPropertyName + "] = '" + filter.getComisieId().get(0) + "' ");
			for (Long comisieId : filter.getComisieId()) {
				queryBuilder.append(" OR document.[" + comisieGlMetadataJrPropertyName + "] = '" + comisieId + "' ");
			}
			queryBuilder.append(" )");
		}
		if (CollectionUtils.isNotEmpty(filter.getInstitutieId())) {
			String intitutieOfInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName();
			queryBuilder.append(" AND ( " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
					filter.getInstitutieId().get(0).toString(), informatiiParticipantiMetadataCollectionName, intitutieOfInformatiiParticipantiMetadataName, false));
			for (Long institutieId : filter.getInstitutieId()) {
				queryBuilder.append(" OR " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
						institutieId.toString(), informatiiParticipantiMetadataCollectionName, intitutieOfInformatiiParticipantiMetadataName, false));
			}
			queryBuilder.append(" )");
		}
		if (CollectionUtils.isNotEmpty(filter.getDocuments())) {
			queryBuilder.append(" AND  ( document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] = '" + filter.getDocuments().get(0).getDocumentId() + "' ");
			for (DocumentIdentifierModel document : filter.getDocuments()) {
				queryBuilder.append(" OR document.[" + JackRabbitConstants.NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID + "] = '" + document.getDocumentId() + "' ");
			}
			queryBuilder.append(" )");
		}
			
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));

		if (filter.getDataSedintaDeLa() != null) {
			query.bindValue("dataSedintaDeLa", session.getValueFactory().createValue(MetadataValueHelper.toDateTimeValue(DateUtils.nullHourMinutesSeconds(filter.getDataSedintaDeLa()))));
		}
		if (filter.getDataSedintaPanaLa() != null) {
			query.bindValue("dataSedintaPanaLa", session.getValueFactory().createValue(MetadataValueHelper.toDateTimeValue(DateUtils.maximizeHourMinutesSeconds(filter.getDataSedintaPanaLa()))));
		}
		
		return query;
	}
	
	private void initContext() {
		documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
	}
	
	public List<Document> search(String documentLocationRealName)  {
		
		List<Document> documents = new LinkedList<>();
		
		if (CollectionUtils.isNotEmpty(filter.getDocuments()) && !filter.getDocuments().stream().filter(document -> document.getDocumentLocationRealName().equals(documentLocationRealName)).findFirst().isPresent()){
			return documents;
		}
		
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
