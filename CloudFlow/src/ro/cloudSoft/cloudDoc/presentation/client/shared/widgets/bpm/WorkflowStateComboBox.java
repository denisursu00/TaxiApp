package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class WorkflowStateComboBox extends ComboBox<WorkflowStateModel> {

	public WorkflowStateComboBox() {
		setDisplayField(WorkflowStateModel.PROPERTY_NAME);
		setEditable(false);
		setStore(new ListStore<WorkflowStateModel>());
		setTriggerAction(TriggerAction.ALL);
	}
	
	public void resetPossibleStates() {
		getStore().removeAll();
	}
	
	public void setPossibleStates(List<WorkflowStateModel> workflowStates) {
		resetPossibleStates();
		getStore().add(workflowStates);
	}
	
	public void resetSelectedState() {
		setValue(null);
	}
	
	public void setSelectedStateByCode(String stateCode) {
		WorkflowStateModel stateWithCode = getStore().findModel(WorkflowStateModel.PROPERTY_CODE, stateCode);
		setValue(stateWithCode);
	}
	
	public String getCodeOfSelectedState() {
		WorkflowStateModel selectedState = getValue();
		return ((selectedState != null) ? selectedState.getCode() : null);
	}
}