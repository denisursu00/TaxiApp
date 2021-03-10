package ro.cloudSoft.cloudDoc.plugins.bpm;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface WorkflowPlugin {
	/**
	 * Aici o sa se construiasca un process definition si o sa se deployeze in contextul JBPM
	 */
	String createOrUpdateProcess(Workflow workflow, SecurityManager userSecurity) throws AppException;
	
	void removeProcess(String processDefinitionId, SecurityManager userSecurity);
	
	WorkflowState getStartState(Workflow workflow);
}