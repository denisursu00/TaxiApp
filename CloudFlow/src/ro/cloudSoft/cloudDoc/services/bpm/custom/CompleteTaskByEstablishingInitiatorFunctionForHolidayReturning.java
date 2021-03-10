package ro.cloudSoft.cloudDoc.services.bpm.custom;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.AssigneeHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CompleteTaskByEstablishingInitiatorFunctionForHolidayReturning extends AutomaticTask {
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {
		
		List<Task> tasks = getCurrentTasksForUser(getUserSecurity().getUserId());
		for (Task task : tasks) {
			if (!task.getExecutionId().equals(workflowInstance.getProcessInstanceId())) {
				continue;
			}			
			
			String transitionName = "Initiator Director executiv";
			DocumentMetadataValues values = getDocumentMetadataValues(workflowInstance);
			
			for (Group group : values.getInitiator().getGroups()) {
				if (group.getName().equals(values.getGrupPresedinteBom())) {
					transitionName = "Initiator Presedinte BOM";
				}
			}
			
			String assignee = AssigneeHelper.getAssigneeValueForUser(getUserSecurity().getUserId());
			getProcessEngine().getTaskService().assignTask(task.getId(), assignee);
			getProcessEngine().getTaskService().completeTask(task.getId(), transitionName);
			
			WorkflowVariableHelper.setSenderUserId(getProcessEngine(), task.getExecutionId(), getUserSecurity().getUserId());
		}
	}
	
	private DocumentMetadataValues getDocumentMetadataValues(WorkflowInstance workflowInstance) throws AppException {
		
		DocumentMetadataValues values = new DocumentMetadataValues();
		Document document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		
		List<MetadataInstance> documentMetadataInstanceList = document.getMetadataInstanceList();
		
		for (MetadataInstance metadataInstance : documentMetadataInstanceList) {
			metadataInstance.getMetadataDefinitionId();
			MetadataDefinition metadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
			
			if (metadataDefinition.getName().equals("grup_presedinte_bom")) {
				values.setGrupPresedinteBom(metadataInstance.getValue());
			}
			if (metadataDefinition.getName().equals("initiator_decizie_rechemare")) {
				Long initiatorId = MetadataValueHelper.getUserId(metadataInstance.getValue());
				values.setInitiator(getUserService().getUserById(initiatorId));
			}
		}
		
		return values;
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
	
	public UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	private class DocumentMetadataValues {
		
		private User initiator;
		private String grupPresedinteBom;
		
		public User getInitiator() {
			return initiator;
		}
		public void setInitiator(User initiator) {
			this.initiator = initiator;
		}
		public String getGrupPresedinteBom() {
			return grupPresedinteBom;
		}
		public void setGrupPresedinteBom(String grupPresedinteBom) {
			this.grupPresedinteBom = grupPresedinteBom;
		}
	}
}
