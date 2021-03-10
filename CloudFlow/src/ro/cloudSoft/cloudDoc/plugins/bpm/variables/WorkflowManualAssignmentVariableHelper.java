package ro.cloudSoft.cloudDoc.plugins.bpm.variables;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowPathWithManualAssignment;

import com.google.common.collect.Maps;

/**
 * 
 */
public class WorkflowManualAssignmentVariableHelper {
	
	private static String getVariableName(String transitionName, String finalStateCode) {
		
		if (StringUtils.isBlank(transitionName)) {
			throw new IllegalArgumentException("Numele tranzitiei NU poate fi nul sau gol.");
		}
		if (StringUtils.isBlank(finalStateCode)) {
			throw new IllegalArgumentException("Codul starii finale NU poate fi nul sau gol.");
		}
		
		return new StringBuilder()
			.append(WorkflowVariableNames.PREFIX_MANUAL_ASSIGNMENT_DESTINATION_USER_ID)
			.append(transitionName)
			.append(WorkflowVariableNames.SEPARATOR_MANUAL_ASSIGNMENT_DESTINATION_USER_ID_TRANSITION_NAME_FROM_FINAL_STATE_CODE)
			.append(finalStateCode)
			.toString();
	}
	
	/**
	 * Initializeaza variabilele legate de asignare manuala pentru fluxul asociat executiei date.
	 * 
	 * @param execution executia asociata fluxului
	 * @param pathsWithManualAssignment caile fluxului (tranzitie + stare finala) care sunt cu asignare manuala
	 */
	public static void initializeManualAssignmentVariables(OpenExecution execution,
			Collection<WorkflowPathWithManualAssignment> pathsWithManualAssignment) {
		
		Map<String, String> manualAssignmentVariables = Maps.newHashMap();
		for (WorkflowPathWithManualAssignment pathWithManualAssignment : pathsWithManualAssignment) {
			String variableName = getVariableName(pathWithManualAssignment.getTransitionName(), pathWithManualAssignment.getFinalStateCode());
			manualAssignmentVariables.put(variableName, null);
		}
		
		execution.setVariables(manualAssignmentVariables);
	}
	
	/**
	 * Seteaza ID-ul utilizatorului asignat manual pentru starea cu codul specificat.
	 * 
	 * @param processEngine motorul de procese jBPM
	 * @param processInstanceId ID-ul instantei de proces asociat fluxului
	 * @param incomingTransitionName numele tranzitiei prin care s-a ajuns in starea cu asignare manuala
	 * @param stateCode codul starii cu asignare manuala
	 * @param userId ID-ul utilizatorului asignat manual
	 */
	public static void setManuallyAssignedUserId(ProcessEngine processEngine, String processInstanceId, String incomingTransitionName, String stateCode, Long userId) {
		
		String variableName = getVariableName(incomingTransitionName, stateCode);
		String manuallyAssignedUserIdAsString = userId.toString();
		
		processEngine.getExecutionService().setVariable(processInstanceId, variableName, manuallyAssignedUserIdAsString);
	}
	
	/**
	 * Verifica daca task-ul corespunzator starii date este cu asignare manuala sau nu. Returneaza da / nu.
	 * 
	 * @param processEngine motorul de procese jBPM
	 * @param processInstanceId ID-ul instantei de proces asociat fluxului
	 * @param incomingTransitionName numele tranzitiei prin care s-a ajuns in starea cu asignare manuala
	 * @param stateCode codul starii cu asignare manuala
	 */
	public static boolean isTaskWithManualAssignment(ProcessEngine processEngine, String processInstanceId, String incomingTransitionName, String stateCode) {
		
		String neededVariableName = getVariableName(incomingTransitionName, stateCode);
		Set<String> variableNames = processEngine.getExecutionService().getVariableNames(processInstanceId);
		
		for (String variableName : variableNames) {
			if (variableName.equals(neededVariableName)) {
				return true;
			}
		}
		
		return false;		
	}
	
	/**
	 * Verifica daca task-ul corespunzator starii date (cu asignare manuala) este asignat deja.
	 * 
	 * @param processEngine motorul de procese jBPM
	 * @param processInstanceId ID-ul instantei de proces asociat fluxului
	 * @param incomingTransitionName numele tranzitiei prin care s-a ajuns in starea cu asignare manuala
	 * @param stateCode codul starii cu asignare manuala
	 */
	public static boolean isTaskWithManualAssignmentAssigned(ProcessEngine processEngine, String processInstanceId, String incomingTransitionName, String stateCode) {
		String variableName = getVariableName(incomingTransitionName, stateCode);
		String manuallyAssignedUserIdAsString = (String) processEngine.getExecutionService().getVariable(processInstanceId, variableName);
		return (manuallyAssignedUserIdAsString != null);		
	}

	/**
	 * Returneaza ID-ul utilizatorului asignat manual pentru starea cu codul specificat
	 * SAU null daca acesta nu a fost setat.
	 * 
	 * @param execution executia asociata fluxului
	 * @param incomingTransitionName numele tranzitiei prin care s-a ajuns in starea cu asignare manuala
	 * @param stateCode codul starii cu asignare manuala
	 */
	public static Long getManuallyAssignedUserId(OpenExecution execution, String incomingTransitionName, String stateCode) {
		
		String variableName = getVariableName(incomingTransitionName, stateCode);
		String manuallyAssignedUserIdAsString = (String) execution.getVariable(variableName);
		
		if (manuallyAssignedUserIdAsString != null) {
			return Long.valueOf(manuallyAssignedUserIdAsString);
		} else {
			return null;
		}
	}
}