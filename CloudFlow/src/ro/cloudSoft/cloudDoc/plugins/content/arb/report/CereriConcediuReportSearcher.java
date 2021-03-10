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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.report.ReportServiceImpl;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.DateUtils;

public class CereriConcediuReportSearcher {
	
	private final Repository repository;
	private final Credentials credentials;
	private final CereriConcediuReportFilterModel filter;
	private final ArbConstants arbConstants;
	private final DocumentTypeService documentTypeService;
	
	private final Long cererePersonalaAprobataWorkflowStateId;
	private final Long cerereNepersonalaAprobataWorkflowStateId;
	
	private DocumentType documentType;
	
	public CereriConcediuReportSearcher(Repository repository, Credentials credentials,
			CereriConcediuReportFilterModel filter, ArbConstants arbConstants,
			DocumentTypeService documentTypeService, Long cererePersonalaAprobataWorkflowStateId, Long cerereNepersonalaAprobataWorkflowStateId) {
		this.repository = repository;
		this.credentials = credentials;
		this.filter = filter;
		this.arbConstants = arbConstants;
		this.documentTypeService = documentTypeService;
		this.cererePersonalaAprobataWorkflowStateId = cererePersonalaAprobataWorkflowStateId;
		this.cerereNepersonalaAprobataWorkflowStateId = cerereNepersonalaAprobataWorkflowStateId;
	}
	
private Query createQuery(Session session) throws RepositoryException {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String dataInceputMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentCerereConcediuConstants().getDataInceputMetadataName());
		String tipConcediuPersonalMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentCerereConcediuConstants().getTipConcediuPersonalMetadataName());
		String tipConcediuNepersonalMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentCerereConcediuConstants().getTipConcediuNepersonalMetadataName());
		String beneficiarMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentCerereConcediuConstants().getBeneficiarConcediuMetadataName());

		queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
		queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
		queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");
		
		if (filter.getDeLaData() != null) {
			queryBuilder.append(" AND document.[" + dataInceputMetadataJrPropertyName + "] >= $deLaData");
		}
		if (filter.getPanaLaData() != null) {
			queryBuilder.append(" AND document.[" + dataInceputMetadataJrPropertyName + "] <= $panaLaData");
		}
		if (filter.getAngajatId() != null) {
			queryBuilder.append(" AND document.[" + beneficiarMetadataJrPropertyName + "] = $angajatId");
		}
		if (filter.getTipConcediu() != null) {
			queryBuilder.append(" AND ( document.[" + tipConcediuPersonalMetadataJrPropertyName + "] = $tipConcediuPersonal "
					+ " OR document.[" + tipConcediuNepersonalMetadataJrPropertyName + "] = $tipConcediuNepersonal )");
		}
		if (filter.getStatus() != null) {
			if (filter.getStatus().equals(CereriConcediuReportFilterModel.REPORT_CERERI_CONCEDIU_CONCEDIU_APROBAT)) {
				queryBuilder.append(" AND ( document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] = '" + cererePersonalaAprobataWorkflowStateId + "' "
						+ " OR document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] = '" + cerereNepersonalaAprobataWorkflowStateId + "' ) ");
			}
			if (filter.getStatus().equals(CereriConcediuReportFilterModel.REPORT_CERERI_CONCEDIU_CONCEDIU_RESPINS)) {
				queryBuilder.append(" AND ( document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] <> '" + cererePersonalaAprobataWorkflowStateId + "' "
						+ " AND document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] <> '" + cerereNepersonalaAprobataWorkflowStateId + "' ) ");
			}
		}
	
		
		QueryManager queryManager = session.getWorkspace().getQueryManager();		
		Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
		query.bindValue("documentTypeId", session.getValueFactory().createValue(documentType.getId().toString()));
		
		if (filter.getDeLaData() != null) {
			query.bindValue("deLaData", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(DateUtils.nullHourMinutesSeconds(filter.getDeLaData()))));
		}
		if (filter.getPanaLaData() != null) {
			query.bindValue("panaLaData", session.getValueFactory().createValue(MetadataValueHelper.toDateValue(DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()))));
		}
		if (filter.getAngajatId() != null) {
			query.bindValue("angajatId", session.getValueFactory().createValue(filter.getAngajatId().toString()));
		}
		if (filter.getTipConcediu() != null) {
			query.bindValue("tipConcediuPersonal", session.getValueFactory().createValue(filter.getTipConcediu()));
			query.bindValue("tipConcediuNepersonal", session.getValueFactory().createValue(filter.getTipConcediu()));
		}
		
		return query;
	}

	private void initContext() {
		documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentCerereConcediuConstants().getDocumentTypeName());
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
