package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.domain.validators.WorkflowValidator;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowPlugin;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.bpm.WorkflowCopyBuilder;
import ro.cloudSoft.cloudDoc.utils.log.LogConstants;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WorkflowServiceImpl implements WorkflowService, InitializingBean {

	private final static LogHelper LOGGER = LogHelper.getInstance(WorkflowServiceImpl.class);
	
	private static final Integer VERSION_NUMBER_FOR_NEW = 1;
	
	private DocumentTypeService documentTypeService;	
	
	private AuditService auditService;
	
	private WorkflowPlugin workflowPlugin;
	private WorkflowDao workflowDao;
	private WorkflowInstanceDao workflowInstanceDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
				
			documentTypeService,
			
			auditService,
			
			workflowPlugin,
			workflowDao,
			workflowInstanceDao
		);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteWorkflow(Long id, SecurityManager userSecurity) throws AppException
	{
		Workflow workflow = workflowDao.find(id);
		workflowPlugin.removeProcess(workflow.getProcessDefinitionId(), userSecurity);
		workflowDao.removeWorkflow( workflow );
		
		auditService.auditWorkflowOperation(userSecurity, workflow, AuditEntityOperation.DELETE);
	}

	@Override
	public List<Workflow> getAllWorkflows(SecurityManager userSecurity) throws AppException
	{
		List<Workflow> workflowList = null;
		try
		{
			workflowList = workflowDao.getAllWorkflows();
		}
		catch(Exception ex)
		{
			LOGGER.error("A aparut o eroare la citirea fluxurilor", ex, LogConstants.MODULE_BPM, LogConstants.OP_BPM_READ_ALL, userSecurity);
			throw new AppException();
		}
		return workflowList;
	}

	@Override
	public List<Workflow> getAllWorkflowsForDisplay(SecurityManager userSecurity) throws AppException
	{
		List<Workflow> workflowList = null;
		try
		{
			workflowList = workflowDao.getAllWorkflows();
		}
		catch(Exception ex)
		{
			LOGGER.error("A aparut o eroare la citirea fluxurilor", ex, LogConstants.MODULE_BPM, LogConstants.OP_BPM_READ_ALL, userSecurity);
			throw new AppException();
		}
		return workflowList;
	}

	@Override
	public Workflow getWorkflowById(Long id) {
		return workflowDao.find(id);
	}
	
	@Override
	public Workflow getWorkflowById(Long id, SecurityManager userSecurity) {
		Workflow workflow = getWorkflowById(id);
		auditService.auditWorkflowOperation(userSecurity, workflow, AuditEntityOperation.READ);
		return workflow;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveWorkflow(Workflow workflow, SecurityManager userSecurity) throws AppException {
		
		boolean isWorkflowNew = workflow.isNew();
		
		if (isWorkflowNew && (workflow.getVersionNumber() == null)) {
			workflow.setVersionNumber(VERSION_NUMBER_FOR_NEW);
		}
		
		new WorkflowValidator(documentTypeService, workflow).validate();
		
		try {
			
			workflow = this.workflowDao.saveAndGetWorkflow(workflow);
			String processDefinitionId = this.workflowPlugin.createOrUpdateProcess(workflow, userSecurity);
			workflow.setProcessDefinitionId(processDefinitionId);
			this.workflowDao.saveWorkflow(workflow);
			
			AuditEntityOperation operation = (isWorkflowNew) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
			auditService.auditWorkflowOperation(userSecurity, workflow, operation);
		} catch(Exception ex) {
			LOGGER.error("A aparut o eroare la cautarea fluxului", ex, LogConstants.MODULE_BPM, LogConstants.OP_BPM_UPDATE, userSecurity);
			throw new AppException();
		}

	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long createNewVersion(Long workflowId, SecurityManager userSecurity) throws AppException {
		
		Workflow workflow = getWorkflowById(workflowId);
		Workflow newVersionWorkflow = new WorkflowCopyBuilder(workflow).build();
		
		Workflow baseVersion = (workflow.getBaseVersion() != null) ? workflow.getBaseVersion() : workflow;
		newVersionWorkflow.setBaseVersion(baseVersion);
		Workflow sourceVersion = workflow;
		newVersionWorkflow.setSourceVersion(sourceVersion);
		
		Integer lastVersionNumber = workflowDao.getLastVersionNumberOfWorkflow(workflowId);
		Integer newVersionNumber = (lastVersionNumber + 1);
		newVersionWorkflow.setVersionNumber(newVersionNumber);
		
		saveWorkflow(newVersionWorkflow, userSecurity);
		
		auditService.auditWorkflowOperation(userSecurity, workflow, AuditEntityOperation.CREATE_VERSION);
		
		return newVersionWorkflow.getId();
	}

	@Override
	public List<WorkflowState> getStatesByDocumentTypeIds(List<Long> ids, SecurityManager userSecurity) throws AppException{
		return workflowDao.getStatesByDocumentTypeIds(ids);
	}

	@Override
	public Workflow getWorkflowByDocumentType(Long documentTypeId) {
		return workflowDao.getWorkflowByDocumentType(documentTypeId);
	}
	
	@Override
	public List<WorkflowState> getWorkflowStatesByDocumentType(Long documentTypeId) {
		Long idForLatestVersionOfWorkflow = workflowDao.getIdForLatestVersionOfWorkflowForDocumentType(documentTypeId);
		if (idForLatestVersionOfWorkflow == null) {
			return Collections.emptyList();
		}
		return workflowDao.getWorkflowStatesByWorkflowId(idForLatestVersionOfWorkflow);
	}
	
	@Override
	public Workflow getWorkflowForDocument(String documentLocationRealName, String documentId, Long documentTypeId) {
		Workflow workflowOfDocument = workflowDao.getWorkflowForDocument(documentLocationRealName, documentId);
		if (workflowOfDocument != null) {
			return workflowOfDocument;
		} else {
			return workflowDao.getWorkflowByDocumentType(documentTypeId);
		}
	}
	
	@Override
	public Workflow getWorkflowForDocument(String documentLocationRealName, String documentId) {
		return workflowDao.getWorkflowForDocument(documentLocationRealName, documentId);
	}
	
	@Override
	public Map<DocumentIdentifier, String> getWorkflowNameByDocumentIdentifier(Collection<DocumentIdentifier> documentIdentifiers) {
		return workflowDao.getWorkflowNameByDocumentIdentifier(documentIdentifiers);
	}

	@Override
	public WorkflowTransition getTransitionForDocument(String documentLocationRealName,
			String documentId, Long documentTypeId, String transitionName, String endStateCode) {
		
		if (workflowInstanceDao.hasActiveWorkflowInstance(documentLocationRealName, documentId)) {
			return workflowDao.getTransitionForDocument(documentLocationRealName, documentId, transitionName, endStateCode);
		} else {
			// Documentul nu a pornit inca pe flux sau e la inceput si instanta de flux inca nu exista.
			return workflowDao.getTransitionForDocumentType(documentTypeId, transitionName, endStateCode);
		}
	}
	
	@Override
	public WorkflowTransition getTransitionById(Long id) {
		return workflowDao.getTransitionById(id);
	}

	@Override
	public WorkflowTransition getTransition(String transitionName, Long finalStateId) {
		return workflowDao.getTransition(transitionName, finalStateId);
	}
	
	@Override
	public boolean hasInstances(Long workflowId) {
		return workflowDao.hasInstances(workflowId);
	}
	
	@Override
	public boolean workflowHasState(Long workflowId, String stateCode) {
		return workflowDao.hasState(workflowId, stateCode);
	}
	
	@Override
	public WorkflowState getWorkflowStateById(Long workflowStateId) {
		return workflowDao.getWorkflowStateById(workflowStateId);
	}
	
	@Override
	public WorkflowState getWorkflowState(Long workflowId, String workflowStateCode) {
		return workflowDao.getWorkflowState(workflowId, workflowStateCode);
	}
	
	@Transactional
	@Override
	public void deployImportedWorkflows() {
		try {
			List<Workflow> workflows = workflowDao.getAllWorkflows();
			if (CollectionUtils.isEmpty(workflows)) {
				return;
			}			
			for (Workflow workflow : workflows) {
				if (StringUtils.isBlank(workflow.getProcessDefinitionId())) {
					String processDefinitionId = workflowPlugin.createOrUpdateProcess(workflow, null);
					workflow.setProcessDefinitionId(processDefinitionId);
					workflowDao.saveWorkflow(workflow);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(WorkflowServiceImpl.class).error("n error occurred while deploying imported workflows.", e);
			throw new RuntimeException("An error occurred while deploying imported workflows.", e);
		}
	}
	
	@Override
	public List<WorkflowTransition> getOutgoingTransitionsFromState(Long stateId) {
		return workflowDao.getOutgoingTransitionsFromState(stateId);
	}
	
	@Override
	public List<Long> getWorkflowFinalStateIdsByDocumentType(Long documentTypeId) {
		List<Long> finalStateIds = new ArrayList<>();
		List<WorkflowState> states = getWorkflowStatesByDocumentType(documentTypeId);
		if (CollectionUtils.isNotEmpty(states)) {
			for (WorkflowState state : states) {
				if (state.getStateType().equals(WorkflowState.STATETYPE_STOP)) {
					finalStateIds.add(state.getId());
				}
			}
		}
		return finalStateIds;
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setWorkflowPlugin(WorkflowPlugin workflowPlugin) {
		this.workflowPlugin = workflowPlugin;
	}
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}
}