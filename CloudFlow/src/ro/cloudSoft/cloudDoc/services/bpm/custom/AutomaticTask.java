package ro.cloudSoft.cloudDoc.services.bpm.custom;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public abstract class AutomaticTask {
	
	public abstract void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException;

	protected SecurityManager getUserSecurity() {
		String applicationUsername = getBusinessConstants().getApplicationUserName();
		return getSecurityManagerFactory().getSecurityManager(applicationUsername);
	}

	private BusinessConstants getBusinessConstants() {
		return SpringUtils.getBean("businessConstants");
	}

	private SecurityManagerFactory getSecurityManagerFactory() {
		return SpringUtils.getBean("securityManagerFactory");
	}
}
