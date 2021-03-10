package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.Transition;
import org.jbpm.pvm.internal.task.ParticipationImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.AssigneeHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowManualAssignmentVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowMetadataVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WorkflowExecutionJbpmPlugin implements WorkflowExecutionPlugin, InitializingBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(WorkflowExecutionJbpmPlugin.class);
	
	private static final String MODULE = "plugin-ul jBPM pentru executia fluxurilor";
	
	private static final String OPERATION_START_WORKFLOW = "pornirea unui flux";
	private static final String OPERATION_COMPLETE_TASK = "completarea unui task";

	private DocumentService documentService;

	private WorkflowInstanceDao workflowInstanceDao;
	private WorkflowDao workflowDao;
	
	private ProcessEngine processEngine;	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			documentService,
			
			workflowInstanceDao,
			workflowDao,
			
			processEngine
		);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public WorkflowInstanceResponse startWorkflowInstance(Workflow workflow, Document document,
			Map<String, MetadataWrapper> metadataWrapperByMetadataName, SecurityManager userSecurity)
			throws AppException {
		
		WorkflowInstanceResponse response = new WorkflowInstanceResponse();
		try {
			// variabilele puse pe flux
			Map<String, Object> workflowVariables = Maps.newHashMap();
			workflowVariables.putAll(WorkflowMetadataVariableHelper.getMetadataWrapperAsStringByVariableName(metadataWrapperByMetadataName));
			workflowVariables.putAll(WorkflowVariableHelper.prepareVariablesForNewProcessInstance(document, userSecurity));
			
			// pornirea procesului
			ProcessInstance processInstance = processEngine.getExecutionService()
				.startProcessInstanceById(workflow.getProcessDefinitionId(), workflowVariables);
			
			WorkflowInstance workflowInstance = new WorkflowInstance();
			workflowInstance.setDocumentId(document.getId());
			workflowInstance.setWorkspaceName(document.getDocumentLocationRealName());
			workflowInstance.setProcessInstanceId(processInstance.getId());
			workflowInstance.setStatus(WorkflowInstance.STATUS_RUNNING);
			workflowInstance.setWorkflow(workflow);
			workflowInstance.setInitiatorId(Long.parseLong(userSecurity.getUserIdAsString()));
			workflowInstanceDao.saveWorkflowInstance(workflowInstance);
			
			/*
			 * daca dupa pornirea procesului urmatorul task este unul cu asignare 
			 * manuala atunci trebuie sa specific in interfata acest lucru 
			 */
			Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).unassigned().uniqueResult();
			if (task != null) {
				String firstTransitionName = workflowInstanceDao.getFirstTransitionName(processInstance.getId(), task.getActivityName());
				if (WorkflowManualAssignmentVariableHelper.isTaskWithManualAssignment(processEngine, processInstance.getId(), firstTransitionName, task.getActivityName())) {
					/*
					 * asignez acest task userului curent, deoarece daca nu apuca sa
					 * asigneze pe altu, sa fie el asignat si apoi sa poata asigna cand 
					 * intra in 'activatile mele'
					 */
					processEngine.getTaskService().assignTask(task.getId(), userSecurity.getUserIdAsString());
					response.setManualAssignment(true);				
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie in timpul pornirii unui flux", e,
				MODULE, OPERATION_START_WORKFLOW, userSecurity);
			BpmExceptionUtils.handleJbpmException(e);
		}
		
		return response;
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public WorkflowInstanceResponse completeWorkflowInstanceTask(WorkflowInstance workflowInstance, String specifiedTransitionName,
			String specifiedManuallyAssignedUserIdAsString, Map<String, MetadataWrapper> metadataWrapperByMetadataName,
			SecurityManager userSecurity) throws AppException {
		
		// obiectul raspuns
		WorkflowInstanceResponse response = new WorkflowInstanceResponse();
		try {
			// Iau toate task-urile utilizatorului.
			List<Task> tasks = getCurrentTasksForUser(userSecurity.getUserId());
			tasks.addAll(getCurrentTasksForGroups(userSecurity.getGroupIds()));
			tasks.addAll(getCurrentTasksForOrganizationUnits(userSecurity.getOrganizationUnitIds()));
			
			if (CollectionUtils.isNotEmpty(tasks)) {
				String processInstanceId = workflowInstance.getProcessInstanceId();
				for (Task task : tasks) {
					// Daca task-ul este cel pe care il caut...
					if (task.getExecutionId().equals(processInstanceId)) {
						// Trebuie sa stiu daca s-a completat task-ul.
						boolean isTaskCompleted = false;
						
						String stateCode = task.getActivityName();
						
						String lastTransitionName = WorkflowVariableHelper.getLastTransitionName(processEngine, task.getExecutionId());
						
						/// manual assignment
						if (WorkflowManualAssignmentVariableHelper.isTaskWithManualAssignment(processEngine, processInstanceId, lastTransitionName, stateCode)) {
							if (!WorkflowManualAssignmentVariableHelper.isTaskWithManualAssignmentAssigned(processEngine, processInstanceId, lastTransitionName, stateCode)) {
								/*
								 * daca nu vine completat din interfata id user-ului destinatie
								 * atunci trebuie sa il semnalizez s-o faca
								 */
								if (specifiedManuallyAssignedUserIdAsString == null) {
									response.setManualAssignment(true);
									return response;
								} else {
									
									Long specifiedManuallyAssignedUserId = Long.valueOf(specifiedManuallyAssignedUserIdAsString);
									
									String assignee = AssigneeHelper.getAssigneeValueForUser(specifiedManuallyAssignedUserId);
									
									// asignare user pe task
									processEngine.getTaskService().assignTask(task.getId(), assignee);
									// update la variabila corespuzatoare cu asignarea manula a taskului									
									WorkflowManualAssignmentVariableHelper.setManuallyAssignedUserId(processEngine,
										processInstanceId, lastTransitionName, stateCode, specifiedManuallyAssignedUserId);
									
									// schimbarea permisiunilor pentru document
									documentService.changePermissionsForWorkflow(workflowInstance.getDocumentId(), 
											workflowInstance.getWorkspaceName(), 
											userSecurity.getUserIdAsString(), 
											specifiedManuallyAssignedUserIdAsString);
									return response;
								}
							}
						}
						//end
						
						// Daca utilizatorul a specificat tranzitia...
						if (specifiedTransitionName != null) {
							/// manual assignment
							// inainte sa completez taskul trebuie sa vad daca urmatorul 
							// task nu este cu asignare manuala.
							String nextTaskNameWithManualAssignment = getNextTaskNameWithManualAssignment(processInstanceId, task.getActivityName(), specifiedTransitionName);
							// urmatorul task e cu asignare manuala
							if (nextTaskNameWithManualAssignment != null) {
								// daca s-a ales din interfata id-ul destinatie
								if (specifiedManuallyAssignedUserIdAsString != null) {									
									Long specifiedManuallyAssignedUserId = Long.valueOf(specifiedManuallyAssignedUserIdAsString);
									WorkflowManualAssignmentVariableHelper.setManuallyAssignedUserId(processEngine,
										processInstanceId, specifiedTransitionName, nextTaskNameWithManualAssignment, specifiedManuallyAssignedUserId);
								}else {
									// setez in response ca este vorba de o asignare manuala
									response.setManualAssignment(true);
									// setez transitia care si-a ales-o anterior
									response.setChosenTransitionName(specifiedTransitionName);
									return response;
								}
							}
							// end
							
							// update la metadate
							WorkflowMetadataVariableHelper.setMetadataValues(processEngine, processInstanceId, metadataWrapperByMetadataName);
							
							// operatiunea are in vedere istoricul taskurilor si anume ca cel care completeaza
							// taskul sa fie pus in assignee la istoric, nu grupul sau ou-ul
							String assignee = AssigneeHelper.getAssigneeValueForUser(Long.valueOf(userSecurity.getUserIdAsString()));
							processEngine.getTaskService().assignTask(task.getId(), assignee);							
							
							// Completez task-ul, precizand tranzitia pe care sa mearga.
							processEngine.getTaskService().completeTask(task.getId(), specifiedTransitionName);
							// Task-ul s-a completat.
							isTaskCompleted = true;
						} else {
							// Iau instanta de proces.
							ProcessInstance processInstance = processEngine.getExecutionService().findProcessInstanceById(workflowInstance.getProcessInstanceId());
							// Iau definitia procesului (care cunoaste toate tranzitiile).
							ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).uniqueResult();
							// Iau numele tuturor tranzitiilor ce pleaca din task-ul meu.
							Set<String> outgoingTransitionNames = ((ProcessDefinitionImpl) processDefinition).findActivity(task.getActivityName()).getOutgoingTransitionsMap().keySet();
							
							String startStateCode = task.getActivityName();
							List<String> namesForTransitionsAvailableForAutomaticActionsOnly = workflowDao.getNamesForTransitionsAvailableForAutomaticActionsOnly(workflowInstance.getWorkflow().getId(), startStateCode);
							
							// Pentru fiecare tranzitie...
							for (String outgoingTransitionName : outgoingTransitionNames) {
								
								if (namesForTransitionsAvailableForAutomaticActionsOnly.contains(outgoingTransitionName)) {
									continue;
								}
								
								// Iau expresia conditiei pentru tranzitie.
								String conditionExpression = workflowDao.getTransitionRoutingConditionExpression(workflowInstance.getId(), outgoingTransitionName);
								// Daca tranzitia nu este conditionata...
								if (conditionExpression == null) {
									// Adaug tranzitia in lista cu cele valide.
									response.addCandidateTransitionName(outgoingTransitionName);
								} else {
									boolean result = ExpressionEvaluator.evaluateDocumentExpression(conditionExpression, metadataWrapperByMetadataName);
									if (result) {
										// Adaug tranzitia in lista cu cele valide.
										response.addCandidateTransitionName(outgoingTransitionName);
									}
								}
							}
							if (response.getCandidateTransitionNames().isEmpty()) {
								// TODO
								throw new AppException(AppExceptionCodes.NO_ROUTING_DIRECTION);
							}
							// Daca exista o singura tranzitie valida...
							if (response.getCandidateTransitionNames().size() == 1) {
								
								String transitionName = Iterables.getOnlyElement(response.getCandidateTransitionNames());
								
								/// manual assignment
								// inainte sa completez taskul trebuie sa vad daca urmatorul 
								// task nu este cu asignare manuala.
								String nextTaskNameWithManualAssignment = getNextTaskNameWithManualAssignment(
									processInstanceId, task.getActivityName(), transitionName);
								// urmatorul task e cu asignare manuala
								if (nextTaskNameWithManualAssignment != null) {
									// daca s-a ales din interfata id-ul destinatie
									if (specifiedManuallyAssignedUserIdAsString != null) {
										Long specifiedManuallyAssignedUserId = Long.valueOf(specifiedManuallyAssignedUserIdAsString);
										WorkflowManualAssignmentVariableHelper.setManuallyAssignedUserId(processEngine,
											processInstanceId, transitionName, nextTaskNameWithManualAssignment, specifiedManuallyAssignedUserId);
									} else {
										// setez in response ca este vorba de o asignare manuala
										response.setManualAssignment(true);
										// sterg tranzitiile din response deoarece nu trebuie
										response.getCandidateTransitionNames().clear();
										return response;
									}
								}
								// end
								
								// update la metadate
								WorkflowMetadataVariableHelper.setMetadataValues(processEngine, processInstanceId, metadataWrapperByMetadataName);
								
								// operatiunea are in vedere istoricul taskurilor si anume ca cel care completeaza
								// taskul sa fie pus in assignee la istoric, nu grupul sau ou-ul
								String assignee = AssigneeHelper.getAssigneeValueForUser(Long.valueOf(userSecurity.getUserIdAsString()));
								processEngine.getTaskService().assignTask(task.getId(), assignee);
								
								// Completez task-ul, precizand tranzitia pe care sa mearga (singura din lista).
								processEngine.getTaskService().completeTask(task.getId(), transitionName);
								// Golesc lista.
								response.getCandidateTransitionNames().clear();
								// Task-ul s-a completat.
								isTaskCompleted = true;
							}						
						}
						/*
						 * Iau instanta de proces din nou pentru ca s-ar putea
						 * sa se fi terminat instanta de proces.
						 */
						ProcessInstance processInstance = processEngine.getExecutionService().findProcessInstanceById(processInstanceId);
						// Daca s-a terminat instanta de proces...
						if (processInstance == null || processInstance.isEnded()) {
							// Marchez fluxul ca terminat.
							workflowInstance.setStatus(WorkflowInstance.STATUS_FINNISHED);
							workflowInstance.setFinishedDate(new Date());
							// Salvez fluxul.
							workflowInstanceDao.saveWorkflowInstance(workflowInstance);
							response.setWorkflowFinished(true);
						} else if (isTaskCompleted) {
							// Daca s-a terminat task-ul, update-ez ID-ul sender-ului.
							WorkflowVariableHelper.setSenderUserId(processEngine, processInstanceId, userSecurity.getUserId());
						}
						// Am gasit task-ul, deci nu mai trebuie sa caut prin lista.
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie la completarea unui task", e,
				MODULE, OPERATION_COMPLETE_TASK, userSecurity);
			BpmExceptionUtils.handleJbpmException(e);
		}
		return response;
	}
	
	/**
	 * Cauta daca urmatorul task este cu asignare manuala. Daca da atunci va returna
	 * numele task-ului, altfel va return null.
	 * @param processInstanceId - id-ul instantei de process
	 * @param currentActivityName - numele task-ului curent
	 * @param transitionName - numele tranzitie unde se duce de la task-ul curent
	 * @return String - numele task-ului cu asignare manuala sau null daca urmatorul 
	 * task nu este cu asignare manuala
	 */
	private String getNextTaskNameWithManualAssignment(String processInstanceId, 
			String currentActivityName, String transitionName) {		
		ProcessInstance processInstance = processEngine.getExecutionService().findProcessInstanceById(processInstanceId);
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).uniqueResult();
		ProcessDefinitionImpl processDefinitionImpl = (ProcessDefinitionImpl)processDefinition;
		List<Transition> transitionList = processDefinitionImpl.findActivity(currentActivityName).getOutgoingTransitions();
		if (transitionList != null) {
			for (Transition transition : transitionList) {
				/*
				 * verifica daca tranzitia este cea pe care va merge, adica cea
				 * trimisa ca parametru
				 */
				if (transition.getName().equals(transitionName)) {
					String transitionFinalStateCode = transition.getDestination().getName();					
					if (WorkflowManualAssignmentVariableHelper.isTaskWithManualAssignment(processEngine, processInstanceId, transition.getName(), transitionFinalStateCode)) {
						return transitionFinalStateCode;
					}
				}
			}
		}	
		return null;
	}
	
	@Override
	public WorkflowState getCurrentState(Workflow workflow, Document document) {
		// Ia instanta de flux.
		WorkflowInstance workflowInstance = this.workflowInstanceDao.getWorkflowInstance(workflow, document);
		// Daca nu exista, inseamnda ca documentul nu mai are stare.
		if (workflowInstance == null) {
			return null;
		}
		// Ia task-ul curent.
		Task currentTask = this.processEngine.getTaskService().createTaskQuery().processInstanceId(workflowInstance.getProcessInstanceId()).uniqueResult();
		/*
		 * Daca nu exista un task curent, inseamna ca fluxul s-a terminat, deci
		 * documentul nu mai are stare.
		 */
		if (currentTask == null) {
			return null;
		}
		// Returneaza starea curenta a documentului.
		return this.workflowDao.getWorkflowState(workflow.getId(), currentTask.getActivityName());
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public List<TaskInstance> getCurrentTasks(SecurityManager userSecurity) throws AppException {
		
		List<Task> list = getCurrentTasksForUser(userSecurity.getUserId());
		if (CollectionUtils.isNotEmpty(userSecurity.getOrganizationUnitIds())) {
			list.addAll(getCurrentTasksForOrganizationUnits(userSecurity.getOrganizationUnitIds()));
		}
		if (CollectionUtils.isNotEmpty(userSecurity.getGroupIds())) {
			list.addAll(getCurrentTasksForGroups(userSecurity.getGroupIds()));
		}
		
		List<TaskInstance> taskList = new ArrayList<TaskInstance>();
		
		for (int i = 0; list != null && i < list.size(); i++ ) {
			String processInstanceId = null;
			Task aTask = null;
			try {
				aTask = list.get(i);
				TaskInstance aTaskInstance = new TaskInstance();
				
				//pentru a seta documentul trebuie sa ajung la workflowinstance, deci trebuie sa ajung la processinstance
				String executionId = aTask.getExecutionId();
				Execution execution = processEngine.getExecutionService().findExecutionById( executionId );
				processInstanceId = execution.getProcessInstance().getId();
				
				//am processInstanceId, acum caut WorkflowInstance
				WorkflowInstance wkIns = workflowInstanceDao.findWorkflowInstanceByProcessInstanceId(processInstanceId);
				aTaskInstance.setDocumentLocationRealName(wkIns.getWorkspaceName());
				//Document document = documentService.getDocumentById(wkIns.getDocumentId(), wkIns.getWorkspaceName(), userSecurity);
					
				//aTaskInstance.setDocument(document);
				
				ExecutionImpl executionImpl = (ExecutionImpl) execution;
				
				Long documentAuthorId = WorkflowVariableHelper.getDocumentAuthorUserId(executionImpl);
				aTaskInstance.setDocumentAuthorId(documentAuthorId);
				Date documentCreatedDate = WorkflowVariableHelper.getDocumentCreatedDate(executionImpl);
				aTaskInstance.setDocumentCreatedDate(documentCreatedDate);
				String documentId = WorkflowVariableHelper.getDocumentId(executionImpl);
				aTaskInstance.setDocumentId(documentId);
				String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(executionImpl);
				aTaskInstance.setDocumentLocationRealName(documentLocationRealName);
				String documentName = WorkflowVariableHelper.getDocumentName(executionImpl);
				aTaskInstance.setDocumentName(documentName);
				Long documentTypeId = WorkflowVariableHelper.getDocumentTypeId(executionImpl);;
				aTaskInstance.setDocumentTypeId(documentTypeId);
				
				Long senderUserId = WorkflowVariableHelper.getSenderUserId(executionImpl);
				aTaskInstance.setSenderUserId(senderUserId);
				
				aTaskInstance.setWorkflow(wkIns.getWorkflow());				

				WorkflowState state = workflowDao.getWorkflowState(wkIns.getWorkflow().getId(), aTask.getName());
				aTaskInstance.setState(state.getName());
				
				taskList.add( aTaskInstance ); 
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					String message = "Nu s-a gasit instanta de flux asociata " +
						"instantei de proces jBPM cu ID-ul [" + processInstanceId + "].";
					LOGGER.warn(message, MODULE, "luarea task-urilor curente", userSecurity);
				} else {
					String taskId = (aTask != null) ?aTask.getId() : null;
					String logMessage = "Exceptie la luarea task-urilor curente. " +
						"Task-ul la care s-a ajuns este cu ID-ul [" + taskId + "].";
					LOGGER.error(logMessage, e, MODULE, "luarea task-urilor curente", userSecurity);
				}
			}
		}
		return taskList;
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Map<String, TaskInstance> getCurrentTaskInstanceMap(List<Document> documents) {
		/*
		 * aceasta metoda ia taskurile curente pentru un document care se poate
		 * afla pe un singur flux. Daca documentul se va afla pe mai multe fluxuri
		 * metoda trebuie reimplementata.
		 */
		Map<String, TaskInstance> result = new HashMap<String, TaskInstance>();		
		for (Document document : documents) {
			WorkflowInstance workflowInstance = workflowInstanceDao.findCurrentWorkflowInstances(document.getId());
			if (workflowInstance != null) {				
				Task currentTask = processEngine.getTaskService().createTaskQuery().processInstanceId(workflowInstance.getProcessInstanceId()).uniqueResult();
				if (currentTask != null) {
					TaskInstance taskInstance = new TaskInstance();
					taskInstance.setDocumentAuthorId(Long.valueOf(document.getAuthor()));
					taskInstance.setDocumentCreatedDate(document.getCreated().getTime());
					taskInstance.setDocumentId(document.getId());
					taskInstance.setDocumentLocationRealName(document.getDocumentLocationRealName());
					taskInstance.setDocumentName(document.getName());
					taskInstance.setDocumentTypeId(document.getDocumentTypeId());
					WorkflowState state = workflowDao.getWorkflowState(workflowInstance.getWorkflow().getId(), currentTask.getName());
					taskInstance.setState(state.getName());
					taskInstance.setWorkflow(workflowInstance.getWorkflow());
					taskInstance.setDocumentLocationRealName(workflowInstance.getWorkspaceName());
					String executionId = currentTask.getExecutionId();
					Execution execution = processEngine.getExecutionService().findExecutionById( executionId );
					Long senderUserId = WorkflowVariableHelper.getSenderUserId((ExecutionImpl) execution);
					taskInstance.setSenderUserId(senderUserId);
					result.put(document.getId(), taskInstance);
				}
			}
		}		
		return result;
	}
	
	private List<Task> getCurrentTasksForUser(Long userId) {
		List<Task> tasksForUser = new ArrayList<Task>();

		String asignee = AssigneeHelper.getAssigneeValueForUser(userId);
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().assignee(asignee).list();
		tasksForUser.addAll(tasks);
		List<Task> candidateTasks = processEngine.getTaskService().createTaskQuery().candidate(asignee).list();
		tasksForUser.addAll(candidateTasks);

		return tasksForUser;
	}
	
	private List<Task> getCurrentTasksForOrganizationUnits(Collection<Long> organizationUnitIds) {
		List<Task> tasksForOrganizationUnits = new ArrayList<Task>();

		for (Long organizationUnitId : organizationUnitIds) {
			
			String assignee = AssigneeHelper.getAssigneeValueForOrganizationUnit(organizationUnitId);
			
			List<Task> tasks = processEngine.getTaskService().createTaskQuery().assignee(assignee).list();
			tasksForOrganizationUnits.addAll(tasks);
			
			List<Task> candidateTasks = processEngine.getTaskService().createTaskQuery().candidate(assignee).list();
			tasksForOrganizationUnits.addAll(candidateTasks);
		}

		return tasksForOrganizationUnits;
	}

	private List<Task> getCurrentTasksForGroups(Collection<Long> groupIds) {
		List<Task> tasksForGroups = new ArrayList<Task>();

		for (Long groupId : groupIds) {
			String asignee = AssigneeHelper.getAssigneeValueForGroup(groupId);
			
			List<Task> tasks = processEngine.getTaskService().createTaskQuery().assignee(asignee).list();
			tasksForGroups.addAll(tasks);
			
			List<Task> candidateTasks = processEngine.getTaskService().createTaskQuery().candidate(asignee).list();
			tasksForGroups.addAll(candidateTasks);
		}

		return tasksForGroups;
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void endWorkflowInstanceIfActive(String documentLocationRealName, String documentId,
			String leavingTransitionNamePrefix, SecurityManager userSecurity) throws AppException {
		
		WorkflowInstance workflowInstance = workflowInstanceDao.findCurrentWorkflowInstances(documentId);
		if (workflowInstance == null) {
			return;
		}
		
		Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(workflowInstance.getProcessInstanceId()).uniqueResult();
		if (task == null) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);

			String logMessage = "Fluxul documentului cu atributele: " + documentLogAttributes + ", este activ, insa NU s-a gasit task in jBPM.";
			LOGGER.error(logMessage, "endWorkflowInstanceIfActive", userSecurity);
			
			throw new AppException();
		}
		
		ProcessInstance processInstance = processEngine.getExecutionService().findProcessInstanceById(workflowInstance.getProcessInstanceId());
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).uniqueResult();
		
		List<Transition> outgoingTransitions = ((ProcessDefinitionImpl) processDefinition).findActivity(task.getActivityName()).getOutgoingTransitions();

		String leavingTransitionName = null;
		for (Transition outgoingTransition : outgoingTransitions) {
			
			boolean hasProperName = outgoingTransition.getName().startsWith(leavingTransitionNamePrefix);
			boolean isDestinationaAStopState = BpmBusinessUtils.isStopState(outgoingTransition.getDestination());
			
			if (hasProperName) {
				if (isDestinationaAStopState) {
					leavingTransitionName = outgoingTransition.getName();
					break;
				} else {
					
					DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
					
					String logMessage = "Pentru fluxul documentului cu atributele: " + documentLogAttributes + ", tranzitia " +
						"cu numele [" + outgoingTransition.getName() + "]) ce pleaca din starea cu codul " +
						"[" + task.getActivityName() + "] NU duce spre o stare de tip STOP.";
					LOGGER.error(logMessage, "endWorkflowInstanceIfActive", userSecurity);
				}
			}
		}
		if (leavingTransitionName == null) {
			
			List<String> outgoingTransitionNames = Lists.newArrayListWithCapacity(outgoingTransitions.size());
			for (Transition outgoingTransition : outgoingTransitions) {
				outgoingTransitionNames.add(outgoingTransition.getName());
			}
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "Pentru fluxul documentului cu atributele: " + documentLogAttributes + ", NU s-a gasit " +
				"tranzitia necesara (avand prefixul [" + leavingTransitionNamePrefix + "]) care sa plece " +
				"din starea cu codul [" + task.getActivityName() + "]. Tranzitiile care pleaca din stare au numele: " +
				"[" + StringUtils.join(outgoingTransitionNames, ", ") + "].";
			LOGGER.error(logMessage, "endWorkflowInstanceIfActive", userSecurity);
			
			throw new AppException();
		}
		
		String assignee = AssigneeHelper.getAssigneeValueForUser(Long.valueOf(userSecurity.getUserIdAsString()));
		processEngine.getTaskService().assignTask(task.getId(), assignee);
		
		processEngine.getTaskService().completeTask(task.getId(), leavingTransitionName);
		
		processInstance = processEngine.getExecutionService().findProcessInstanceById(workflowInstance.getProcessInstanceId());
		if (processInstance == null || processInstance.isEnded()) {
			workflowInstance.setStatus(WorkflowInstance.STATUS_FINNISHED);
			workflowInstanceDao.saveWorkflowInstance(workflowInstance);
		}
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfUser(
			Long userId, Long replacementUserId, Collection<String> idsForTasksToIgnore) {
		
		Collection<Task> currentTasksOfUser = getCurrentTasksForUser(userId);
		return addReplacementAsAssignedToCurrentTasksOfEntity(currentTasksOfUser, replacementUserId, idsForTasksToIgnore);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfOrganizationUnit(Long organizationUnitId, Long replacementUserId) {
		Collection<Task> currentTasksOfOrganizationUnit = getCurrentTasksForOrganizationUnits(Collections.singleton(organizationUnitId));
		return addReplacementAsAssignedToCurrentTasksOfEntity(currentTasksOfOrganizationUnit, replacementUserId);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfGroup(Long groupId, Long replacementUserId) {
		Collection<Task> currentTasksOfGroup = getCurrentTasksForGroups(Collections.singleton(groupId));
		return addReplacementAsAssignedToCurrentTasksOfEntity(currentTasksOfGroup, replacementUserId);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void removeAssignedUsersFromDocumentAssociatedTask(Collection<Long> idsForAssignedUsersToRemove,
			String documentLocationRealName, String documentId) throws AppException {
		
		for (Long idForAssignedUserToRemove : idsForAssignedUsersToRemove) {
			
			Task documentAssociatedTaskAssignedToUser = getDocumentAssociatedTaskAssignedToUser(
				idForAssignedUserToRemove, documentLocationRealName, documentId);
			if (documentAssociatedTaskAssignedToUser == null) {
				
				DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
				
				String logMessage = "NU s-a gasit task-ul asociat documentului cu atributele: " + documentLogAttributes + ", " +
					"care sa fie asignat la utilizatorul cu ID-ul [" + idForAssignedUserToRemove + "].";
				LOGGER.error(logMessage, "inlaturarea unui utilizator asignat din task-ul asociat unui document");
				
				throw new AppException();
			}
			
			String assigneeForAssignedUserToRemove = AssigneeHelper.getAssigneeValueForUser(idForAssignedUserToRemove);
			if (isAssigneeCandidate(documentAssociatedTaskAssignedToUser, assigneeForAssignedUserToRemove)) {
				processEngine.getTaskService().removeTaskParticipatingUser(documentAssociatedTaskAssignedToUser.getId(),
					assigneeForAssignedUserToRemove, Participation.CANDIDATE);
			} else {
				
				String logMessage = "Utilizatorul pentru care s-a dorit inlaturarea asignarii (cel cu ID-ul [" + idForAssignedUserToRemove + "]) " +
					"NU este asignat CA SI CANDIDAT la task-ul cu ID-ul [" + documentAssociatedTaskAssignedToUser.getId() + "] si numele " +
					"[" + documentAssociatedTaskAssignedToUser.getName() + "], task care insa ii este asignat.";
				LOGGER.error(logMessage, "inlaturarea unui utilizator asignat din task-ul asociat unui document");
	
				throw new AppException();
			}
		}
	}
	
	private Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfEntity(
			Collection<Task> currentTasksOfEntity, Long replacementUserId) {
		
		return addReplacementAsAssignedToCurrentTasksOfEntity(currentTasksOfEntity,
			replacementUserId, Collections.<String> emptySet());
	}
	
	private Collection<DocumentIdentifier> addReplacementAsAssignedToCurrentTasksOfEntity(
			Collection<Task> currentTasksOfEntity, Long replacementUserId,
			Collection<String> idsForTasksToIgnore) {

		String replacementUserAssignee = AssigneeHelper.getAssigneeValueForUser(replacementUserId);
		Collection<DocumentIdentifier> documentIdentifiersForTasksWithAssignedReplacement = Sets.newHashSet();
		
		for (Task task : currentTasksOfEntity) {
			
			if (idsForTasksToIgnore.contains(task.getId())) {
				continue;
			}
			
			if (isAssigneeCandidate(task, replacementUserAssignee)) {
				continue;
			}
			
			processEngine.getTaskService().addTaskParticipatingUser(task.getId(), replacementUserAssignee, Participation.CANDIDATE);
			
			DocumentIdentifier documentIdentifierForTask = WorkflowVariableHelper.getDocumentIdentifier(processEngine, task);
			documentIdentifiersForTasksWithAssignedReplacement.add(documentIdentifierForTask);
		}
		
		return documentIdentifiersForTasksWithAssignedReplacement;
	}
	
	private boolean isAssigneeCandidate(Task task, String assignee) {
		
		if (!(task instanceof TaskImpl)) {
			String exceptionMessage = "Task-ul cu ID-ul [" + task.getId() + "] si numele " +
				"[" + task.getName() + "] NU este de tipul asteptat (" + TaskImpl.class.getName() + ").";
			throw new IllegalArgumentException(exceptionMessage);
		}
		TaskImpl taskImpl = (TaskImpl) task;
		
		Collection<ParticipationImpl> participants = taskImpl.getAllParticipants();
		for (ParticipationImpl participant : participants) {
			if (participant.getType().equals(Participation.CANDIDATE)
					&& (participant.getUserId() != null) 
					&& participant.getUserId().equals(assignee)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Task getDocumentAssociatedTaskAssignedToUser(Long userId, String documentLocationRealName, String documentId) {
		Collection<Task> currentTasksOfUser = getCurrentTasksForUser(userId);
		for (Task task : currentTasksOfUser) {
			DocumentIdentifier documentIdentifierForTask = WorkflowVariableHelper.getDocumentIdentifier(processEngine, task);
			if (documentIdentifierForTask.getDocumentLocationRealName().equals(documentLocationRealName)
					&& documentIdentifierForTask.getDocumentId().equals(documentId)) {
				return task;
			}
		}
		return null;
	}
	
	public Map<DocumentIdentifier, String> getTaskIdByDocumentIdentifierForTasksAssignedToUser(Long userId) {
		
		Collection<Task> currentTasksOfUser = getCurrentTasksForUser(userId);
		Map<DocumentIdentifier, String> taskIdByDocumentIdentifier = Maps.newHashMap();
		
		for (Task task : currentTasksOfUser) {
			DocumentIdentifier documentIdentifierForTask = WorkflowVariableHelper.getDocumentIdentifier(processEngine, task);
			taskIdByDocumentIdentifier.put(documentIdentifierForTask, task.getId());
		}
		
		return taskIdByDocumentIdentifier;
	}
	
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
	public WorkflowInstanceDao getWorkflowInstanceDao() {
		return workflowInstanceDao;
	}
	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}
	public WorkflowDao getWorkflowDao() {
		return workflowDao;
	}
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	public DocumentService getDocumentService() {
		return documentService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}