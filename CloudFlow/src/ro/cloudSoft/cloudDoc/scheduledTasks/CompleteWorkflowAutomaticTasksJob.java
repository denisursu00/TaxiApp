package ro.cloudSoft.cloudDoc.scheduledTasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import ro.cloudSoft.cloudDoc.plugins.content.JR_DocumentLocationPlugin;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class CompleteWorkflowAutomaticTasksJob extends QuartzScheduledJobBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(CompleteWorkflowAutomaticTasksJob.class);
	
	private WorkflowExecutionService workflowExecutionService;
	
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}

	@Override
	public void doExecuteInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			workflowExecutionService.completeWorkflowAutomaticTasks();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Executed", "doExecuteInternal");
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
