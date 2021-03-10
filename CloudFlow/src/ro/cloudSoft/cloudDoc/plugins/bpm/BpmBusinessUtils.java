package ro.cloudSoft.cloudDoc.plugins.bpm;

import org.jbpm.pvm.internal.model.Activity;

/**
 * Contine metode utilitare legate de reguli de business din zona BPM.
 * 
 * 
 */
public class BpmBusinessUtils {
	
	private static final String ACTIVITY_TYPE_STOP = "end";

	/**
	 * Verifica daca task-ul a fost completat, pe baza raspunsului trimis.
	 * @param response raspunsul trimis utilizatorului
	 */
	public static boolean isTaskCompleted(WorkflowInstanceResponse response) {
		return (response.getCandidateTransitionNames().isEmpty() && !response.isManualAssignment());
	}
	
	/**
	 * Verifica daca starea reprezentata prin activitatea jBPM data este de tip STOP.
	 */
	public static boolean isStopState(Activity activity) {
		return (activity.getType().equals(ACTIVITY_TYPE_STOP));
	}
}