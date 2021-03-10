package ro.cloudSoft.cloudDoc.services.bpm.custom.cerereRechemare;

import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentCerereRechemareConstants;
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

public class CerereRechemareCompleteTaskByUserInGroup extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		List<Task> tasks = JbpmUtils.getCurrentTasksForUser(getProcessEngine(), getUserSecurity().getUserId());
		for (Task task : tasks) {
			if (!task.getExecutionId().equals(workflowInstance.getProcessInstanceId())) {
				continue;
			}			
			
			String transitionName = null;
			if (isUserInGroup(workflowInstance)) {
				transitionName = getDocumentCerereRechemareConstants().getInitiatorDirectorTransitionName();
			} else {
				transitionName = getDocumentCerereRechemareConstants().getInitiatorDiferitDeDirectorTransitionName();
			}
			
			String assignee = AssigneeHelper.getAssigneeValueForUser(getUserSecurity().getUserId());
			getProcessEngine().getTaskService().assignTask(task.getId(), assignee);
			getProcessEngine().getTaskService().completeTask(task.getId(), transitionName);
			
			WorkflowVariableHelper.setSenderUserId(getProcessEngine(), task.getExecutionId(), getUserSecurity().getUserId());
		}
	}
	
	private boolean isUserInGroup(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException  {
		
		String cerereRechemareDocumentTypeName = getDocumentCerereRechemareConstants().getDocumentTypeName();
		
		DocumentType cerereRechemareDocumentType = getDocumentTypeDao().getDocumentTypeByName(cerereRechemareDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}

		Long userRechematId = DocumentUtils.getMetadataUserValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getUserRechematMetadataName());
		Long groupId = DocumentUtils.getMetadataGroupValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getGrupDirectorMetadataName());
		
		return getGroupService().isUserInGroupWithId(userRechematId, groupId);
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
	
	public DocumentCerereRechemareConstants getDocumentCerereRechemareConstants() {
		return SpringUtils.getBean("documentCerereRechemareConstants");
	}
}
