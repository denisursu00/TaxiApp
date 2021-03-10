package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.Collections;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.bpm.GwtWorkflowStateBusinessUtils;

public class WorkflowStatesSelectionPopupField extends ListWithMultipleSelectionPopupField {
	
	public WorkflowStatesSelectionPopupField() {
		getSelectItemsWindow().setHeading(GwtLocaleProvider.getConstants().STATES());
	}

	public String getCodesForSelectedStatesAsString() {
		
		List<String> codesForSelectedStates = getValuesOfSelectedItems();
		String codesForSelectedStatesAsString = GwtStringUtils.join(codesForSelectedStates, GwtWorkflowStateBusinessUtils.SEPARATOR_STEPS);
		return codesForSelectedStatesAsString;
	}
	
	public void setCodesForSelectedStatesAsString(String codesForSelectedStatesAsString) {
		
		List<String> codesForSelectedStates = Collections.emptyList();		
		if (codesForSelectedStatesAsString != null) {
			codesForSelectedStates = GwtStringUtils.split(codesForSelectedStatesAsString, GwtWorkflowStateBusinessUtils.SEPARATOR_STEPS);
		}
		
		setSelectedItemsByValues(codesForSelectedStates);
	}
}