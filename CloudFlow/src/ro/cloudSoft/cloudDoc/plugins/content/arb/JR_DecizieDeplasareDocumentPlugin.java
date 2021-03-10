package ro.cloudSoft.cloudDoc.plugins.content.arb;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.JR_PluginBase;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.SecurityCommons;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentLocationService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class JR_DecizieDeplasareDocumentPlugin extends JR_PluginBase implements DecizieDeplasareDocumentPlugin {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DecizieDeplasareDocumentPlugin.class);
	
	private DocumentLocationService documentLocationService;	
	private DocumentTypeService documentTypeService;
	private WorkflowService workflowService;
	
	private ArbConstants arbConstants;
	
	@Override
	public List<NumarDecizieDeplasareModel> getNumarDeciziiDeplasariAprobateByReprezentant(Long reprezentantId, SecurityManager userSecurity) {
		List<NumarDecizieDeplasareModel> results = new ArrayList<>();
		try {
			DocumentType documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentDecizieDeplasare().getDocumentTypeName());
			String reprezentantMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentDecizieDeplasare().getReprezentantMetadataName());
			String numarDecizieMetadataJrPropertyName = DocumentTypeUtils.getMetadataJrPropertyName(documentType, arbConstants.getDocumentDecizieDeplasare().getNumarDecizieMetadataName());
			
			Workflow workflow = workflowService.getWorkflowByDocumentType(documentType.getId());
			WorkflowState decizieDeplasareAprobataWorkflowState = workflowService.getWorkflowState(workflow.getId(), arbConstants.getDocumentDecizieDeplasare().getDecizieDeplasareAprobataWorkflowStateCode());
			
			List<DocumentLocation> documentLocations = documentLocationService.getAllDocumentLocations(userSecurity);
			if (CollectionUtils.isNotEmpty(documentLocations)) {
				for (DocumentLocation documentLocation : documentLocations) {
					List<NumarDecizieDeplasareModel> numarDeciziiDeplasari = getNumarDeciziiDeplasariByReprezentantIntoWorkspace(documentLocation.getRealName(), documentType.getId(), 
							reprezentantMetadataJrPropertyName, reprezentantId, numarDecizieMetadataJrPropertyName, decizieDeplasareAprobataWorkflowState.getId(), userSecurity);
					results.addAll(numarDeciziiDeplasari);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", "getNumarDeciziiDeplasariByReprezentant", userSecurity);
		} finally {
			
		}
		return results;
	}
	
	private List<NumarDecizieDeplasareModel> getNumarDeciziiDeplasariByReprezentantIntoWorkspace(String documentLocationRealName, Long decizieDeplasareDocumentTypeId, 
			String reprezentantJrPropertyName, Long reprezentantId, String numarDecizieJrPropertyName, Long decizieDeplasareAprobataWorkflowStateId, SecurityManager userSecurity) throws Exception {
		
		List<NumarDecizieDeplasareModel> numarDeciziiDeplasari = new ArrayList<>();
		
		Session session = null;		
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			
			StringBuilder queryBuilder = new StringBuilder();			
			queryBuilder.append(" SELECT document.* FROM [" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "] AS document "); 
			queryBuilder.append(" WHERE document.[" + JackRabbitConstants.NODE_PROPERTY_PRIMARY_TYPE + "] = '" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'");
			queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + "] = $documentTypeId");
			queryBuilder.append(" AND document.[" + reprezentantJrPropertyName + "] = $reprezentantId");
			queryBuilder.append(" AND document.[" + JackRabbitConstants.DOCUMENT_PROP_WORKFLOW_STATE_ID + "] = $decizieDeplasareAprobataId");
			
			Query query = queryManager.createQuery(queryBuilder.toString(), Query.JCR_SQL2);		
			query.bindValue("documentTypeId", session.getValueFactory().createValue(decizieDeplasareDocumentTypeId.toString()));
			query.bindValue("reprezentantId", session.getValueFactory().createValue(reprezentantId.toString()));
			query.bindValue("decizieDeplasareAprobataId", session.getValueFactory().createValue(decizieDeplasareAprobataWorkflowStateId.toString()));
			
			NodeIterator resultNodesIterator = query.execute().getNodes();			
			while (resultNodesIterator.hasNext()) {				
				Node resultNode = resultNodesIterator.nextNode();
				if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, resultNode, userSecurity)) {					
					NumarDecizieDeplasareModel view = new NumarDecizieDeplasareModel();
					view.setDocumentLocationRealName(documentLocationRealName);
					view.setDocumentId(resultNode.getIdentifier());
					view.setNumarDecizie(JackRabbitCommons.getPropertyValueAsString(resultNode, numarDecizieJrPropertyName));					
					numarDeciziiDeplasari.add(view);
				}
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			JackRabbitCommons.logout(session);
		}
		return numarDeciziiDeplasari;
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
	
	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}
	
}
