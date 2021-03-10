package ro.cloudSoft.cloudDoc.core.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class AppComponentsAvailabilityConstants implements InitializingBean {

	private boolean workflowGraphViewGeneratorEnabled;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowGraphViewGeneratorEnabled	
		);
	}
	
	public boolean isWorkflowGraphViewGeneratorEnabled() {
		return workflowGraphViewGeneratorEnabled;
	}
	
	public void setWorkflowGraphViewGeneratorEnabled(boolean workflowGraphViewGeneratorEnabled) {
		this.workflowGraphViewGeneratorEnabled = workflowGraphViewGeneratorEnabled;
	}
}