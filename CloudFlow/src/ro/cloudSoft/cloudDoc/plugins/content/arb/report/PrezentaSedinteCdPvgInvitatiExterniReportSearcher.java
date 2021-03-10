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

import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiExterniFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;

public class PrezentaSedinteCdPvgInvitatiExterniReportSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final PrezentaSedintaCdPvgInvitatiExterniFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentPrezentaCdPvgTypeService;
	
	private DocumentType documentType;
	
	public PrezentaSedinteCdPvgInvitatiExterniReportSearcher(Repository repository, Credentials credentials, PrezentaSedintaCdPvgInvitatiExterniFilterModel filter,
			ArbConstants arbConstants, DocumentTypeService documentTypeService) {
		this.repository = repository;
		this.credentials = credentials;
		this.arbConstants = arbConstants;
		this.filter = filter;
		this.documentPrezentaCdPvgTypeService = documentTypeService;
	}
	
	private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String tipSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getSedintaMetadataName());
		String dataSedintaMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName());
		String infoInvitatiMetadataCollectionJrPropertyName = DocumentTypeUtils.getMetadataCollectionJrPropertyName(documentType, arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName());
		
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

		String infoInvitatiExterniMetadataCollectionName = arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName();
		
		if (filter.getInstitutieInvitatId() != null) {
			String institutieInvitatofInfoInvExterniMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInstitutieInvitatOfInformatiiInvitatiExterniMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInvitatiMetadataCollectionJrPropertyName, 
					filter.getInstitutieInvitatId().toString(), infoInvitatiExterniMetadataCollectionName, institutieInvitatofInfoInvExterniMetadataName, false));
		} 		
		if (filter.getInvitatAcreditatId() != null) {
			String invitatAcreditatMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatAcreditatOfInformatiiInvitatiExterniMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithEqualMatchFilterInCollectionProperty("document", documentType, infoInvitatiMetadataCollectionJrPropertyName, 
					filter.getInvitatAcreditatId().toString(), infoInvitatiExterniMetadataCollectionName, invitatAcreditatMetadataName, false));
		}
		if (StringUtils.isNotBlank(filter.getInvitatInlocuitorNume())) {
			String numeInvitatInlocuitorMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithContainMatchFilterInCollectionProperty("document", documentType, infoInvitatiMetadataCollectionJrPropertyName, 
				filter.getInvitatInlocuitorNume(), infoInvitatiExterniMetadataCollectionName, numeInvitatInlocuitorMetadataName, true));
		}
		if (StringUtils.isNotBlank(filter.getInvitatInlocuitorPrenume())) {
			String prenumeInvitatInlocuitorMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName();
			queryBuilder.append(" AND " + JackRabbitCommons.getContainsWithContainMatchFilterInCollectionProperty("document", documentType, infoInvitatiMetadataCollectionJrPropertyName, 
				filter.getInvitatInlocuitorPrenume(), infoInvitatiExterniMetadataCollectionName, prenumeInvitatInlocuitorMetadataName, true));
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
		documentType = documentPrezentaCdPvgTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());
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
