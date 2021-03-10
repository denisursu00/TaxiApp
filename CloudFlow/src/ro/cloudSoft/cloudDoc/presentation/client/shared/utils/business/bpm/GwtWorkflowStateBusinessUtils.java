package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.bpm;

public class GwtWorkflowStateBusinessUtils {
	
	/** Sir de caractere folosit pentru separarea codurilor starilor in care o metadata este obligatorie / restrictionata */
	public static final String SEPARATOR_STEPS = ";";

	public static boolean isStateFound(String stateCodesJoinedAsString, String codeForStateToFind) {
		String normalizedStateCodesJoinedAsString = (SEPARATOR_STEPS + stateCodesJoinedAsString + SEPARATOR_STEPS);
		return normalizedStateCodesJoinedAsString.contains(SEPARATOR_STEPS + codeForStateToFind + SEPARATOR_STEPS);
	}
}