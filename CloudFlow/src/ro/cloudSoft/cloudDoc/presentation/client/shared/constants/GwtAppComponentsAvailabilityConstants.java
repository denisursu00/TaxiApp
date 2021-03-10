package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtAppComponentsAvailabilityConstants implements IsSerializable {

	private boolean workflowGraphViewGeneratorEnabled;

	@SuppressWarnings("unused")
	private GwtAppComponentsAvailabilityConstants() {
		// Constructor necesar pentru serializare
	}
	
	public GwtAppComponentsAvailabilityConstants(boolean workflowGraphViewGeneratorEnabled) {
		this.workflowGraphViewGeneratorEnabled = workflowGraphViewGeneratorEnabled;
	}
	
	public boolean isWorkflowGraphViewGeneratorEnabled() {
		return workflowGraphViewGeneratorEnabled;
	}
}