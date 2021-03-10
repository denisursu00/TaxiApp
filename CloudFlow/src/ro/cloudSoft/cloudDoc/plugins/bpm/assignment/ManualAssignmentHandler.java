package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.Transition;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowManualAssignmentVariableHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class ManualAssignmentHandler extends BaseAssignmentHandler {

	private static final long serialVersionUID = -4691766502996551993L;

	private final static LogHelper LOGGER = LogHelper.getInstance(ManualAssignmentHandler.class);
	
	private static final String ACTIVITY_TYPE_START = "start";
	
	private String incomingTransitionName;
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		if (StringUtils.isBlank(incomingTransitionName)) {
			String logMessage = "NU s-a specificat numele tranzitiei pe care s-a plecat de s-a ajuns la starea " +
				"curenta, nume necesar pentru asignarea manuala. Executia curenta are ID-ul [" + execution.getId() + "].";
			LOGGER.error(logMessage, "asignarea manuala");
			
			throw new AppException();
		}
		
		ExecutionImpl executionImpl = (ExecutionImpl) execution;
		
		Long manuallyAssignedUserId = WorkflowManualAssignmentVariableHelper.getManuallyAssignedUserId(execution, incomingTransitionName, executionImpl.getActivityName());
		if (manuallyAssignedUserId == null) {
			List<Transition> incomingTransitions = executionImpl.getActivity().getIncomingTransitions();
			Transition transition = incomingTransitions.get(0);
			if (transition.getSource().getType().equals(ACTIVITY_TYPE_START)) {
				/*
				 * daca activitatea de unde a venit este tip 'start' atunci nu 
				 * mai trebuie aruncata exceptie deoarece asignarea se face in
				 * metodele de pornire, completare task a procesului.
				 */
			} else {
				
				String logMessage = "NU s-a gasit ID-ul utilizatorului asignat manual printre " +
					"variabilele fluxului. Executia curenta are ID-ul [" + execution.getId() + "].";
				LOGGER.error(logMessage, "asignarea manuala");
				
				throw new AppException();
			}
		} else {
			assignToUser(execution, assignable, manuallyAssignedUserId);
		}
	}
	
	public void setIncomingTransitionName(String incomingTransitionName) {
		this.incomingTransitionName = incomingTransitionName;
	}
}