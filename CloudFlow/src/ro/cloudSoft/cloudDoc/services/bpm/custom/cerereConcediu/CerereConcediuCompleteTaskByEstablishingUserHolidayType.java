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
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CerereConcediuCompleteTaskByEstablishingUserHolidayType extends AutomaticTask {

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
			Long idInitiatorCerereConcediu = DocumentUtils.getMetadataGroupValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getInitiatorCerereConcediuMetadataName());
			
			String transitionName = null;
			if (idBeneficiarConcediu.equals(idInitiatorCerereConcediu)) {
				transitionName = getDocumentCerereConcediuConstants().getConcediuPersonalTransitionName();
			} else {
				transitionName = getDocumentCerereConcediuConstants().getConcediuNepersonalTransitionName();
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
	
	public UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	public DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	public DocumentCerereConcediuConstants getDocumentCerereConcediuConstants() {
		return SpringUtils.getBean("documentCerereConcediuConstants");
	}
}
