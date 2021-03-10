package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class InitiatorAssignmentHandler extends BaseAssignmentHandler {

	private static final long serialVersionUID = 1L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(InitiatorAssignmentHandler.class);
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		Long initiatorUserId = WorkflowVariableHelper.getInitiatorUserId(execution);
		if (initiatorUserId == null) {
			
			String logMessage = "NU s-a gasit ID-ul initiatorului printre variabilele " +
				"fluxului. Executia curenta are ID-ul [" + execution.getId() + "].";
			LOGGER.error(logMessage, "asignarea initiatorului");
			
			throw new AppException();
		}
		
		assignToUser(execution, assignable, initiatorUserId);
	}
}