package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class ApplicationAssignmentHandler extends BaseAssignmentHandler {

	private static final long serialVersionUID = 1L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(ApplicationAssignmentHandler.class);
	
	private Long workflowStateId;
	private String applicationUserName;
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		String lastTransitionName = WorkflowVariableHelper.getLastTransitionName(execution);
		
		WorkflowTransition transition = getWorkflowService().getTransition(lastTransitionName, workflowStateId);
		if (transition == null) {
			String exceptionMessage = "Nu s-a gasit tranzitia cu numele [" + lastTransitionName + "] si ID-ul starii finale [" + workflowStateId + "].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		User applicationUser = getUserService().getUserByUsername(getBusinessConstants().getApplicationUserName());
		if (applicationUser == null) {
			
			String logMessage = "NU s-a gasit ID-ul utilizatorului [" + applicationUserName + "] printre variabilele " +
				"fluxului. Executia curenta are ID-ul [" + execution.getId() + "].";
			LOGGER.error(logMessage, "application assignment");
			
			throw new AppException();
		}
		
		assignToUser(execution, assignable, applicationUser.getId());
	}
	
	public Long getWorkflowStateId() {
		return workflowStateId;
	}

	public void setWorkflowStateId(Long workflowStateId) {
		this.workflowStateId = workflowStateId;
	}

	public String getApplicationUserName() {
		return applicationUserName;
	}

	public void setApplicationUserName(String applicationUserName) {
		this.applicationUserName = applicationUserName;
	}
	
	public WorkflowService getWorkflowService() {
		return SpringContextUtils.getBean("workflowService");
	}
	
	public UserService getUserService() {
		return SpringContextUtils.getBean("userService");
	}
	
	public BusinessConstants getBusinessConstants() {
		return SpringContextUtils.getBean("businessConstants");
	}
}
