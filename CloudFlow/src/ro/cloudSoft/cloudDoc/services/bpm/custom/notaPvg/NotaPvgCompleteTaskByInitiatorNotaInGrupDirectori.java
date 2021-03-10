package ro.cloudSoft.cloudDoc.services.bpm.custom.notaPvg;

import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentNotaPvgConstants;
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
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.JbpmUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class NotaPvgCompleteTaskByInitiatorNotaInGrupDirectori extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		List<Task> tasks = JbpmUtils.getCurrentTasksForUser(getProcessEngine(), getUserSecurity().getUserId());
		for (Task task : tasks) {
			if (!task.getExecutionId().equals(workflowInstance.getProcessInstanceId())) {
				continue;
			}			
			
			String transitionName = null;
			if (isUserInGroup(workflowInstance)) {
				transitionName = getDocumentNotaPvgConstants().getMetadateCompletateVerificareDirectoriTransitionName();
			} else {
				transitionName = getDocumentNotaPvgConstants().getMetadateCompletateVerificareSuperiorTransitionName();
			}
			
			String assignee = AssigneeHelper.getAssigneeValueForUser(getUserSecurity().getUserId());
			getProcessEngine().getTaskService().assignTask(task.getId(), assignee);
			getProcessEngine().getTaskService().completeTask(task.getId(), transitionName);
			
			WorkflowVariableHelper.setSenderUserId(getProcessEngine(), task.getExecutionId(), getUserSecurity().getUserId());
		}
	}
	
	private boolean isUserInGroup(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException  {
		
		Document document = null;
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException ae) {
			throw new AutomaticTaskExecutionException(ae.getMessage(), ae);
		}
		
		DocumentType notaPvgDocumentType = getDocumentTypeDao().getDocumentTypeByName(getDocumentNotaPvgConstants().getDocumentTypeName());

		Long initiatorId = DocumentUtils.getMetadataUserValue(document, notaPvgDocumentType, getDocumentNotaPvgConstants().getInitiatorNotaMetadataName());
		Long groupId = DocumentUtils.getMetadataGroupValue(document, notaPvgDocumentType, getDocumentNotaPvgConstants().getGrupDirectoriMetadataName());
		
		return getGroupService().isUserInGroupWithId(initiatorId, groupId);
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
	
	public UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	public GroupService getGroupService() {
		return SpringUtils.getBean("groupService");
	}
	
	public DocumentNotaPvgConstants getDocumentNotaPvgConstants() {
		return SpringUtils.getBean("documentNotaPvgConstants");
	}
}
