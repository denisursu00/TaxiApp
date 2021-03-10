package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;

/**
 * Handler de asignare care tine cont de tranzitia prin care s-a ajuns la task-ul curent.
 * 
 * 
 */
public class IncomingTransitionAwareAssignmentHandler implements AssignmentHandler {

	private static final long serialVersionUID = 1L;
	
	private Long workflowStateId;

	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		String lastTransitionName = WorkflowVariableHelper.getLastTransitionName(execution);
		AssignmentHandlerDispatcher assignmentHandlerDispatcher = getAssignmentHandlerDispatcher();
		AssignmentHandler assignmentHandler = assignmentHandlerDispatcher.getHandlerFor(lastTransitionName, workflowStateId);
		assignmentHandler.assign(assignable, execution);
	}
	
	private AssignmentHandlerDispatcher getAssignmentHandlerDispatcher() {
		return SpringContextUtils.getBean("assignmentHandlerDispatcher");
	}
	
	public void setWorkflowStateId(Long workflowStateId) {
		this.workflowStateId = workflowStateId;
	}
}