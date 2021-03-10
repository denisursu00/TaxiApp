package ro.cloudSoft.cloudDoc.services.bpm.custom.cerereConcediu;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentCerereConcediuConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.AssigneeHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CerereConcediuCompleteTaskByEstablishingUserFunctionForPersonalApplication extends AutomaticTask {
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {
		
		String cerereConcediuDocumentTypeName = getDocumentCerereConcediuConstants().getDocumentTypeName();
		
		DocumentType cerereConcediuDocumentType = getDocumentTypeDao().getDocumentTypeByName(cerereConcediuDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		List<Task> tasks = getCurrentTasksForUser(getUserSecurity().getUserId());
		for (Task task : tasks) {
			
			if (!task.getExecutionId().equals(workflowInstance.getProcessInstanceId())) {
				continue;
			}
			
			Long idBeneficiarConcediu = DocumentUtils.getMetadataUserValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getBeneficiarConcediuMetadataName());
			Long idGrupDirectori = DocumentUtils.getMetadataGroupValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getGrupDirectoriMetadataName());
			Long idGrupPe = DocumentUtils.getMetadataGroupValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getGrupPeMetadataName());
			
			String transitionName = getDocumentCerereConcediuConstants().getBeneficiarCererePersonalaDiferitDePEDirectorExecutivTransitionName();
			if (getUserGroupService().isUserInGroupWithId(idBeneficiarConcediu, idGrupDirectori)) {
				transitionName = getDocumentCerereConcediuConstants().getBeneficiarCererePersonalaDirectorExecutivTransitionName();
			} else if (getUserGroupService().isUserInGroupWithId(idBeneficiarConcediu, idGrupPe)) {
				transitionName = getDocumentCerereConcediuConstants().getBeneficiarCererePersonalaPETransitionName();
			}	

			String assignee = AssigneeHelper.getAssigneeValueForUser(getUserSecurity().getUserId());
			
			getProcessEngine().getTaskService().assignTask(task.getId(), assignee);
			getProcessEngine().getTaskService().completeTask(task.getId(), transitionName);
			
			WorkflowVariableHelper.setSenderUserId(getProcessEngine(), task.getExecutionId(), getUserSecurity().getUserId());
		}
	}
	
	private List<Task> getCurrentTasksForUser(Long userId) {
		List<Task> tasksForUser = new ArrayList<Task>();

		String asignee = AssigneeHelper.getAssigneeValueForUser(userId);
		
		List<Task> tasks = getProcessEngine().getTaskService().createTaskQuery().assignee(asignee).list();
		tasksForUser.addAll(tasks);
		List<Task> candidateTasks = getProcessEngine().getTaskService().createTaskQuery().candidate(asignee).list();
		tasksForUser.addAll(candidateTasks);

		return tasksForUser;
	}
	
	public ProcessEngine getProcessEngine() {
		return SpringUtils.getBean("jbpmProcessEngine");
	}
	
	public DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	public GroupService getUserGroupService() {
		return SpringUtils.getBean("groupService");
	}
	
	public DocumentCerereConcediuConstants getDocumentCerereConcediuConstants() {
		return SpringUtils.getBean("documentCerereConcediuConstants");
	}
}
