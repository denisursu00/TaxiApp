package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class PreviousAssignmentHandler extends BaseAssignmentHandler {

	private static final long serialVersionUID = 1L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(PreviousAssignmentHandler.class);
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		
		WorkflowExecutionService workflowExecutionService = getWorkflowExecutionService();
		
		String previouslyAssignedAssignee = workflowExecutionService.getPreviouslyAssignedAssignee(documentLocationRealName, documentId);
		if (previouslyAssignedAssignee == null) {
			
			String logMessage = "NU s-a gasit ultimul asignat, necesar pentru " +
				"rutare. ID-ul executiei este [" + execution.getId() + "].";
			LOGGER.error(logMessage, "asignarea de tip 'pozitia anterioara'");
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}
		
		assignToAssignee(execution, assignable, previouslyAssignedAssignee);
	}
	
	private WorkflowExecutionService getWorkflowExecutionService() {
		return SpringContextUtils.getBean("workflowExecutionService");
	}
}