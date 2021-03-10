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

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.DateUtils;

public class ActiuniPeProiectNoteCDReportSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final ActiuniPeProiectReportFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	private final WorkflowService workflowService;
	
	public ActiuniPeProiectNoteCDReportSearcher(Repository repository, Credentials credentials,
			ActiuniPeProiectReportFilterModel filter, ArbConstants arbConstants,
			DocumentTypeService documentTypeService, WorkflowService workflowService) {
		this.repository = repository;
		this.credentials = credentials;
		this.filter = filter;
		this.arbConstants = arbConstants;
		this.documentTypeService = documentTypeService;
		this.workflowService = workflowService;
	}

	private Query createQuery(Session session, DocumentType documentType, String dataCdArbMetadataName, String denumireProiectMetadataName) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String dataCdArbMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, dataCdArbMetadataName);
		String denumireProiectMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, denumireProiectMetadataName);
		List<Long> workflowFinalStateIds = workflowService.getWorkflowFinalStateIdsByDocumentType(documentType.getId());
		
		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");
		
		if (filter.getDataInceput() != null) {
			queryBuilder.append(" AND document.[" + dataCdArbMetadataJrPropertyName + "] >= $dataInceput ");
		}
		if (filter.getDataSfarsit() != null) {
			queryBuilder.append(" AND document.[" + dataCdArbMetadataJrPropertyName + "] <= $dataSfarsit ");
		}
		if (filter.getProiectId() != null) {
			queryBuilder.append(" AND document.[" + denumireProiectMetadataJrPropertyName + "] = $proiectId ");	
		}
		if (CollectionUtils.isNotEmpty(workflowFinalStateIds)) {
			StringBuilder sb = new StringBuilder();
			for (Long stateId : workflowFinalStateIds) {
				if (sb.length() > 0) {
					sb.append(" OR ");
				}
				sb.append("document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] = '" + stateId.toString() + "'");
			}
			queryBuilder.append(" AND (").append(sb).append(")");
		}
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);	
		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));
		if (filter.getDataInceput() != null) {
			query.bindValue("dataInceput", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()))));
		}
		if (filter.getDataSfarsit() != null) {
			query.bindValue("dataSfarsit", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()))));
		}
		if (filter.getProiectId() != null) {
			query.bindValue("proiectId", session.getValueFactory().createValue(filter.getProiectId().toString()));
		}
		
		return query;
	}
	
	private void initContext() {
	}
	
	public List<Document> search(String documentLocationRealName)  {
		
		List<Document> documents = new LinkedList<>();
		
		Session session = null;		
		try {
			
			initContext();
			
			session = repository.login(credentials, documentLocationRealName);
			
			DocumentType notaCdCfDas = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDocumentTypeName());
			documents.addAll(getDocumentsBySpecificDocumentType(session, documentLocationRealName, notaCdCfDas, arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDataCdArbMetadataName(), arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDenumireProiectMetadataName()));
			
			DocumentType notaCdDirectori = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDDirectoriConstants().getDocumentTypeName());
			documents.addAll(getDocumentsBySpecificDocumentType(session, documentLocationRealName, notaCdDirectori, arbConstants.getDocumentNotaCDDirectoriConstants().getDataCdArbMetadataName(), arbConstants.getDocumentNotaCDDirectoriConstants().getDenumireProiectMetadataName()));
			
			DocumentType notaCdResponsabil = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDResponsabilConstants().getDocumentTypeName());
			documents.addAll(getDocumentsBySpecificDocumentType(session, documentLocationRealName, notaCdResponsabil, arbConstants.getDocumentNotaCDResponsabilConstants().getDataCdArbMetadataName(), arbConstants.getDocumentNotaCDResponsabilConstants().getDenumireProiectMetadataName()));
			
			DocumentType notaCdUseriArb = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDUseriARBConstants().getDocumentTypeName());
			documents.addAll(getDocumentsBySpecificDocumentType(session, documentLocationRealName, notaCdUseriArb, arbConstants.getDocumentNotaCDUseriARBConstants().getDataCdArbMetadataName(), arbConstants.getDocumentNotaCDUseriARBConstants().getDenumireProiectMetadataName()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
		return documents;
	}
	
	private List<Document> getDocumentsBySpecificDocumentType(Session session, String documentLocationRealName, DocumentType documentType, String dataCdArbMetadataName, String denumireProiectMetadataName) throws RepositoryException {
		
		List<Document> documents = new LinkedList<>();
		
		Query query = createQuery(session, documentType, dataCdArbMetadataName, denumireProiectMetadataName);
		QueryResult queryResult = query.execute();
		
		NodeIterator resultNodes = queryResult.getNodes();	
		while (resultNodes.hasNext()) {
			Node resultNode = resultNodes.nextNode();
			documents.add(DocumentCommons.getDocumentFromNodeWithoutAttachments(documentLocationRealName, resultNode, documentType));
		}
		
		return documents;
	}
}
