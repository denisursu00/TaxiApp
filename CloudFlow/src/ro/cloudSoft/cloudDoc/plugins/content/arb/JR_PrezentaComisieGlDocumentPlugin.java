package ro.cloudSoft.cloudDoc.plugins.content.arb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.JR_PluginBase;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.DocumentPrezentaOnlineModel;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentLocationService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class JR_PrezentaComisieGlDocumentPlugin extends JR_PluginBase implements PrezentaComisieGlDocumentPlugin {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DecizieDeplasareDocumentPlugin.class);
	
	private DocumentLocationService documentLocationService;	
	private DocumentTypeService documentTypeService;
	private WorkflowService workflowService;
	private NomenclatorService nomenclatorService;
	
	private ArbConstants arbConstants;
	
	@Override
	public List<DocumentPrezentaOnlineModel> getPrezentaComisieGlDocumentsForPrezentaOnline(SecurityManager userSecurity) {
		List<DocumentPrezentaOnlineModel> results = new ArrayList<>();
		
		try {
			DocumentType documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
			String comisieGlMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());
			String dataInceputMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
			String dataSfarsitMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getDataSfarsitMetadataName());
			
			Workflow workflow = workflowService.getWorkflowByDocumentType(documentType.getId());
			WorkflowState prezentaComisieGlCompletarePrezentaWorkflowState = workflowService.getWorkflowState(workflow.getId(), arbConstants.getDocumentPrezentaComisieGlConstants().getCompletareMetadatePrezentaWorkflowStateCode());
			
			Set<String> documentLocations = documentLocationService.getAllDocumentLocationRealNames();
			
			if (CollectionUtils.isNotEmpty(documentLocations)) {
				for (String documentLocation : documentLocations) {
					List<DocumentPrezentaOnlineModel> prezente = getPrezenteComisieGl(documentLocation, documentType.getId(), comisieGlMetadataJrPropertyName, 
							dataInceputMetadataJrPropertyName, dataSfarsitMetadataJrPropertyName, prezentaComisieGlCompletarePrezentaWorkflowState.getId(), userSecurity);
					results.addAll(prezente);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", "getPrezentaComisieGlDocumentsForPrezentaOnline", userSecurity);
		} finally {
			
		}
		return results;
	}
	
	private List<DocumentPrezentaOnlineModel> getPrezenteComisieGl(String documentLocationRealName, Long prezentaComisieGlDocumentTypeId, String comisieGlMetadataJrPropertyName,
			String dataInceputMetadataJrPropertyName, String dataSfarsitMetadataJrPropertyName, Long prezentaComisieGlCompletarePrezentaWorkflowStateId, SecurityManager userSecurity) throws Exception {
		List<DocumentPrezentaOnlineModel> prezente = new ArrayList<>();
		
		Session session = null;		
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			
			StringBuilder queryBuilder = new StringBuilder();			
			queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
			queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
			queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");
			queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] = $workflowStateId ");
			
			Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
			query.bindValue("documentTypeId", session.getValueFactory().createValue(prezentaComisieGlDocumentTypeId.toString()));
			query.bindValue("workflowStateId", session.getValueFactory().createValue(prezentaComisieGlCompletarePrezentaWorkflowStateId.toString()));
			
			NodeIterator resultNodesIterator = query.execute().getNodes();
			
			Map<Long, String> nomenclatorComisiiGlUiAttrValuesAsMap = nomenclatorService
					.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);
			
			while (resultNodesIterator.hasNext()) {				
				Node resultNode = resultNodesIterator.nextNode();			
				DocumentPrezentaOnlineModel prezenta = new DocumentPrezentaOnlineModel();
				
				if (JackRabbitCommons.getPropertyValueAsString(resultNode, comisieGlMetadataJrPropertyName) != null) {
					Long comisieGlId = Long.parseLong(JackRabbitCommons.getPropertyValueAsString(resultNode, comisieGlMetadataJrPropertyName));
					prezenta.setComisieGl(nomenclatorComisiiGlUiAttrValuesAsMap.get(comisieGlId));
					prezenta.setComisieGlId(comisieGlId);
				}
				
				prezenta.setDataInceput(JackRabbitCommons.getPropertyValueAsDate(resultNode, dataInceputMetadataJrPropertyName));
				prezenta.setDataSfarsit(JackRabbitCommons.getPropertyValueAsDate(resultNode, dataSfarsitMetadataJrPropertyName));
				prezenta.setNumeDocument(DocumentCommons.getName(resultNode));	
				prezenta.setDocumentLocationRealName(documentLocationRealName);
				prezenta.setDocumentId(resultNode.getIdentifier());
									
				prezente.add(prezenta);
			}		
		} catch (Exception e) {
			throw e;
		} finally {
			JackRabbitCommons.logout(session);
		}
		return prezente;
	}

	
	public void setDocumentLocationService(DocumentLocationService documentLocationService) {
		this.documentLocationService = documentLocationService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

}
