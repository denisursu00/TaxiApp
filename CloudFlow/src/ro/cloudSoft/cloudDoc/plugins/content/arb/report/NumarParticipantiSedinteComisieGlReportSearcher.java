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

import mx4j.log.Log;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.DateUtils;


public class NumarParticipantiSedinteComisieGlReportSearcher {

	private final Repository repository;
	private final Credentials credentials;
	private final NumarParticipantiSedinteComisieGlReportFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	
	private DocumentType documentType;
	
	public NumarParticipantiSedinteComisieGlReportSearcher(Repository repository, Credentials credentials,
			NumarParticipantiSedinteComisieGlReportFilterModel filter, ArbConstants arbConstants,
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
		String responsabilMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getResponsabilMetadataName());
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
		if (filter.getComisieId() != null) {
			queryBuilder.append(" AND document.[" + comisieGlMetadataJrPropertyName + "] = $comisieGlId");
		}
		if (filter.getBancaId() != null) {
			String intitutieOfInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
					filter.getBancaId().toString(), informatiiParticipantiMetadataCollectionName, intitutieOfInformatiiParticipantiMetadataName, false));
		}
		if (filter.getFunctie() != null) {
			String functieOfInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithContainMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
					filter.getFunctie(), informatiiParticipantiMetadataCollectionName, functieOfInformatiiParticipantiMetadataName, true));
		}
		if (filter.getDepartament() != null) {
			String departamentOfInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getDepartamentOfInformatiiParticipantiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithContainMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
					filter.getDepartament(), informatiiParticipantiMetadataCollectionName, departamentOfInformatiiParticipantiMetadataName, true));
		}
		if (filter.getCalitateMembru() != null) {
			String calitateMembruOfInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInformatiiParticipantiMetadataCollectionJrPropertyName, 
					filter.getCalitateMembru(), informatiiParticipantiMetadataCollectionName, calitateMembruOfInformatiiParticipantiMetadataName, false));
		}
		if (filter.getResponsabilId() != null) {
			queryBuilder.append(" AND document.[" + responsabilMetadataJrPropertyName + "] = $responsabilId");
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
		if (filter.getComisieId() != null) {
			query.bindValue("comisieGlId", session.getValueFactory().createValue(filter.getComisieId().toString()));
		}
		if (filter.getResponsabilId() != null) {
			query.bindValue("responsabilId", session.getValueFactory().createValue(filter.getResponsabilId().toString()));
		}
		
		return query;
	}
	
	private void initContext() {
		documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
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
