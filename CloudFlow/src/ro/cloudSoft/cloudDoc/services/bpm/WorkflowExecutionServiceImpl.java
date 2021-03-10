package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.DocumentWorkflowHistoryPlugin;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowExecutionPlugin;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowPlugin;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WorkflowExecutionServiceImpl implements WorkflowExecutionService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(WorkflowExecutionServiceImpl.class);
	
	private WorkflowExecutionPlugin workflowExecutionPlugin;
	private DocumentService documentService;
	private DocumentTypeDao documentTypeDao;
	private WorkflowInstanceDao workflowInstanceDao;
	private WorkflowDao workflowDao;
	private WorkflowPlugin workflowPlugin;
	private DocumentWorkflowHistoryPlugin documentWorkflowHistoryPlugin;
	private SecurityManagerFactory securityManagerFactory;	
	private BusinessConstants businessConstants;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowExecutionPlugin,
			documentService,
			documentTypeDao,
			workflowInstanceDao,
			workflowDao,
			workflowPlugin,
			documentWorkflowHistoryPlugin,
			securityManagerFactory,
			businessConstants
		);
	}
	
	public WorkflowExecutionServiceImpl() {
	}
	
	@Override
	public WorkflowInstance findProcessInstance(Long processInstanceId,
			SecurityManager userSecurity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkflowInstance> getAllWorkflowInstances(
			SecurityManager userSecurity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowInstance getWorkflowInstance(String documentId, SecurityManager userSecurity)
	{
		return workflowInstanceDao.findCurrentWorkflowInstances(documentId);
	}

	@Override
	public WorkflowInstanceResponse startWorkflowInstance(Long workflowId, Document document, String workspaceName, SecurityManager userSecurity) throws AppException {
		
		Map<String, MetadataWrapper> metadataWrapperByMetadataName = Maps.newHashMap();
		
		//pun in parameters toate metadatele de pe document
		List<MetadataInstance> metadatas = document.getMetadataInstanceList();
		for ( int i  = 0; metadatas != null && i < metadatas.size(); i++ )
		{
			/*
			 * TODO Poate ar fi mai rapid sa facem o lista cu definitiile
			 * de care avem nevoie si sa le luam pe toate odata
			 * (printr-o metoda din documentTypeDao).
			 */
			MetadataInstance  mi = metadatas.get( i );
			MetadataDefinition mdef = documentTypeDao.getMetadataDefinition( mi.getMetadataDefinitionId() );
			metadataWrapperByMetadataName.put(mdef.getName(), new MetadataWrapper(mdef.getMetadataType(), mi.getValues()));
		}
		
		Workflow theWorkflow = workflowDao.find(workflowId);
		if (theWorkflow == null) 
		{
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR); // TODO cod corespunzator
		}
		Set<OrganizationEntity> supervisors = theWorkflow.getSupervisors();
		
		if ( supervisors != null && supervisors.size() > 0 )
		{
        		List<Long> ouSupervisorsIds = new ArrayList<Long>();
        		List<Long> groupSupervisorIds = new ArrayList<Long>();
        		List<String> supervisorsIds = new ArrayList<String>();
        		
        		for ( OrganizationEntity e : supervisors )
        		{
        		    if ( e instanceof User )
        		    {
        			supervisorsIds.add(e.getId().toString());
        		    }
        		    else if ( e instanceof Group )
        		    {
        			groupSupervisorIds.add(e.getId());
        		    }
        		    else if ( e instanceof OrganizationUnit )
        		    {
        			ouSupervisorsIds.add(e.getId());
        		    }
        		}
        		
        		documentService.addSupervisorPermission(document.getId(), document.getDocumentLocationRealName(), 
        			supervisorsIds, ouSupervisorsIds, groupSupervisorIds);
		}
		
		WorkflowInstanceResponse response = workflowExecutionPlugin.startWorkflowInstance( theWorkflow, document, metadataWrapperByMetadataName, userSecurity );
		return response;
	}
	
	@Override
	public WorkflowInstanceResponse completeWorkflowInstanceTask(WorkflowInstance workflowInstance, String transitionName, 
			String manualAssignmentDestinationId, Document document, SecurityManager userSecurity) throws AppException {
		Map<String, MetadataWrapper> metadataMap = new HashMap<String, MetadataWrapper>();
		List<MetadataInstance> metadataInstances = document.getMetadataInstanceList();
		for (MetadataInstance metadataInstance : metadataInstances) {
			/*
			 * TODO Poate ar fi mai rapid sa facem o lista cu definitiile
			 * de care avem nevoie si sa le luam pe toate odata
			 * (printr-o metoda din documentTypeDao).
			 */
			MetadataDefinition metadataDefinition = documentTypeDao.getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
			metadataMap.put(metadataDefinition.getName(), new MetadataWrapper(metadataDefinition.getMetadataType(), metadataInstance.getValues()));
		}
		
		WorkflowInstanceResponse response = null;
		try
		{
			response = workflowExecutionPlugin.completeWorkflowInstanceTask(workflowInstance,
				transitionName, manualAssignmentDestinationId, metadataMap, userSecurity);
		}
		catch(Exception ex) {
			LOGGER.error("Exceptie", "completeWorkflowInstanceTask", userSecurity);
			throw new AppException();
		}
		return response;
	}

	@Override
	public List<TaskInstance> getCurrentTasks(SecurityManager userSecurity) throws AppException {
		return workflowExecutionPlugin.getCurrentTasks(userSecurity);
	}
	
	@Override
	public WorkflowState getCurrentState(Workflow workflow, Document document, SecurityManager userSecurity) throws AppException {
		if (document != null) {
			if (workflow != null) {
				WorkflowState currentActiveState = this.workflowExecutionPlugin.getCurrentState(workflow, document);
				if (currentActiveState != null) {
					return currentActiveState;
				} else {
					WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(document.getId());
					if (workflowInstance != null) {
						if (workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
							Map<String, WorkflowState> finalStateOfTypeStopByTransitionName = workflowDao.getFinalStateOfTypeStopByTransitionName(workflow.getId());
							WorkflowState stopStateOfInstance = documentWorkflowHistoryPlugin.getStopStateOfInstance(workflowInstance, finalStateOfTypeStopByTransitionName);
							if (stopStateOfInstance != null) {
								return stopStateOfInstance;
							} else {
								
								DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
								
								String logMessage = "Pentru documentul cu atributele: " + documentLogAttributes + ", exista o instanta de flux FINALIZATA " +
									"(cu ID-ul [" + workflowInstance.getId() + "] si numele fluxului [" + workflow.getName() + "]), insa NU s-a putut gasi " +
									"starea de tip STOP in care s-a terminat fluxul.";
								LOGGER.error(logMessage, "getCurrentState", userSecurity);
								
								throw new AppException();
							}
						} else {
							
							DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
							
							String logMessage = "Pentru documentul cu atributele: " + documentLogAttributes + ", exista o instanta de flux " +
								"(cu ID-ul [" + workflowInstance.getId() + "] si numele fluxului [" + workflow.getName() + "]), este activa " +
								"(NU e finalizata), insa NU s-a gasit task-ul pentru starea curenta activa.";
							LOGGER.error(logMessage, "getCurrentState", userSecurity);
							
							throw new AppException();
						}
					} else {
						WorkflowState startState = workflowPlugin.getStartState(workflow);
						return startState;
					}
				}
			} else {
				return null;
			}
		} else {
			if (workflow != null) {
				WorkflowState startState = workflowPlugin.getStartState(workflow);
				return startState;
			} else {
				return null;
			}
		}
	}
	
	@Override
	public boolean checkSendingRights(Document document, Workflow workflow, SecurityManager userSecurity) throws AppException {
		
		boolean noWorkflowAssociatedWithTypeOfDocument = (workflow == null);
		if (noWorkflowAssociatedWithTypeOfDocument) {
			return false;
		}
		
		boolean documentIsNew = (document == null);
		if (documentIsNew) {
			return true;
		}
			
		boolean documentHasWorkflowAndIsFinished = false;
		
		List<WorkflowInstance> workflowInstancesForDocument = workflowInstanceDao.findWorkflowInstances(document.getId());
		for (WorkflowInstance workflowInstance : workflowInstancesForDocument) {
			if (workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
				documentHasWorkflowAndIsFinished = true;
				break;
			}
		}
		
		if (documentHasWorkflowAndIsFinished) {
			return false;
		}

		if (document.isLocked()) {
			boolean isLockedByCurrentUser = document.getLockedByUserId().equals(userSecurity.getUserId());
			if (!isLockedByCurrentUser) {
				return false;
			}
		}
		
		WorkflowState currentState = getCurrentState(workflow, document, userSecurity);
		boolean documentHasActiveWorkflow = (
			(currentState != null) &&
			!currentState.getStateType().equals(WorkflowState.STATETYPE_START)
		);
		
		if (!documentHasActiveWorkflow) {
			return true;
		}
			
		boolean isDocumentStateAsUserTask = false;
		
		List<TaskInstance> currentTasksForUser = this.getCurrentTasks(userSecurity);
		for (TaskInstance task : currentTasksForUser) {
			// Daca documentul ii apare in lista de activitati, atunci poate trimite.
			if (task.getDocumentId().equals(document.getId())) {
				isDocumentStateAsUserTask = true;
				break;
			}
		}
		
		if (isDocumentStateAsUserTask) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public Map<String, TaskInstance> getCurrentTaskInstanceMap(List<Document> documents) {
		return workflowExecutionPlugin.getCurrentTaskInstanceMap(documents);
	}
	
	@Override
	public boolean hasActiveWorkflowInstance(String documentLocationRealName,
			String documentId, SecurityManager userSecurity) {
		return workflowInstanceDao.hasActiveWorkflowInstance(documentLocationRealName, documentId);
	}
	
	@Override
	public void endWorkflowInstanceIfActive(String documentLocationRealName, String documentId,
			String leavingTransitionNamePrefix, SecurityManager userSecurity) throws AppException {
		
		workflowExecutionPlugin.endWorkflowInstanceIfActive(documentLocationRealName,
			documentId, leavingTransitionNamePrefix, userSecurity);
	}
	
	@Override
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfUser(Long userId, Long replacementUserId, Collection<String> idsForTasksToIgnore) {
		return workflowExecutionPlugin.addReplacementAsAssignedToCurrentTasksOfUser(userId, replacementUserId, idsForTasksToIgnore);
	}
	
	@Override
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfOrganizationUnit(Long organizationUnitId, Long replacementUserId) {
		return workflowExecutionPlugin.addReplacementAsAssignedToCurrentTasksOfOrganizationUnit(organizationUnitId, replacementUserId);
	}
	
	@Override
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfGroup(Long groupId, Long replacementUserId) {
		return workflowExecutionPlugin.addReplacementAsAssignedToCurrentTasksOfGroup(groupId, replacementUserId);
	}
	
	@Override
	public void removeAssignedUsersFromDocumentAssociatedTask(Collection<Long> idsForAssignedUsersToRemove,
			String documentLocationRealName, String documentId) throws AppException {
		
		workflowExecutionPlugin.removeAssignedUsersFromDocumentAssociatedTask(
			idsForAssignedUsersToRemove, documentLocationRealName, documentId);
	}
	
	@Override
	public Map<DocumentIdentifier, String> getTaskIdByDocumentIdentifierForTasksAssignedToUser(Long userId) {
		return workflowExecutionPlugin.getTaskIdByDocumentIdentifierForTasksAssignedToUser(userId);
	}
	
	@Override
	public String getPreviouslyAssignedAssignee(String documentLocationRealName, String documentId) {
		WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(documentId);
		return documentWorkflowHistoryPlugin.getPreviouslyAssignedAssignee(workflowInstance);
	}
	
	@Override
	public void completeWorkflowAutomaticTasks() throws AppException {
		
		String applicationUsername = businessConstants.getApplicationUserName();
		SecurityManager userSecurity = securityManagerFactory.getSecurityManager(applicationUsername);
		
		List<TaskInstance> tasks = getCurrentTasks(userSecurity);		
		for (TaskInstance taskInstance : tasks) {
			WorkflowState workflowState = null;
			
			try {
				WorkflowInstance workflowInstance = getWorkflowInstance(taskInstance.getDocumentId(), userSecurity);
				Document document = documentService.getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), userSecurity);
				
				workflowState = getCurrentState(workflowInstance.getWorkflow(), document, userSecurity);
				completeWorkflowAutomaticTask(workflowInstance, workflowState, document, userSecurity);
				
			} catch (AutomaticTaskExecutionException ate) {
				String message = "A aparut o eroare la executarea clasei pentru task-ul automat cu "
						+ " ProcessDefinitionId [ " + taskInstance.getWorkflow().getProcessDefinitionId() + " ] avand classPath "
								+ " [" + workflowState.getClassPath() + "]. Mesaj eroare [" + ate.getMessage() + "]";
				LOGGER.error(message, ate, "Execute Automatic Task", userSecurity);
			} catch (Exception e) {
				String message = "A aparut o eroare la rularea unui task automat pentru documentul cu id [" + taskInstance.getDocumentId() + "] "
						+ " si cu ProcessDefinitionId [" + taskInstance.getWorkflow().getProcessDefinitionId() + "]. Mesaj eroare [" + e.getMessage() + "]";
				LOGGER.error(message, e, "Execute Automatic Task", userSecurity);
			}		
		}
    }
	
	@Transactional
	private void completeWorkflowAutomaticTask(WorkflowInstance workflowInstance, WorkflowState workflowState, Document document, SecurityManager userSecurity) throws Exception {		
		executeAutomaticTask(workflowInstance, workflowState);
		completeWorkflowInstanceTask(workflowInstance, null, null, document, userSecurity);
	}
    
    private void executeAutomaticTask(WorkflowInstance workflowInstance, WorkflowState workflowState) throws ClassNotFoundException, InstantiationException, IllegalAccessException, AutomaticTaskExecutionException, AppException {
    	
		String classPath = workflowState.getClassPath();
		
		if (StringUtils.isBlank(classPath)) {
			return;
		}
		
		Class<?> clazz = Class.forName(classPath);
		AutomaticTask automaticTask = (AutomaticTask) clazz.newInstance();
		automaticTask.execute(workflowInstance);
    }
	
    public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}
    public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	public void setWorkflowExecutionPlugin(WorkflowExecutionPlugin workflowExecutionPlugin) {
		this.workflowExecutionPlugin = workflowExecutionPlugin;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}
	public void setWorkflowPlugin(WorkflowPlugin workflowPlugin) {
		this.workflowPlugin = workflowPlugin;
	}
	public void setDocumentWorkflowHistoryPlugin(DocumentWorkflowHistoryPlugin documentWorkflowHistoryPlugin) {
		this.documentWorkflowHistoryPlugin = documentWorkflowHistoryPlugin;
	}
}