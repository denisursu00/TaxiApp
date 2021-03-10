package ro.cloudSoft.cloudDoc.utils.placeholderValueContexts;

import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;

public class JbpmExecutionDocumentAttributesPlaceholderValueContext implements PlaceholderValueContext {
	
	private static final String PLACEHOLDER_NAME_DOCUMENT_LOCATION_REAL_NAME = "DOCUMENT_LOCATION_REAL_NAME";
	private static final String PLACEHOLDER_NAME_DOCUMENT_ID = "DOCUMENT_ID";

	private final OpenExecution execution;
	
	public JbpmExecutionDocumentAttributesPlaceholderValueContext(OpenExecution execution) {
		this.execution = execution;
	}
	
	@Override
	public String getValue(String placeholderName) {
		if (placeholderName.equals(PLACEHOLDER_NAME_DOCUMENT_LOCATION_REAL_NAME)) {
			return WorkflowVariableHelper.getDocumentLocationRealName(execution);
		} else if (placeholderName.equals(PLACEHOLDER_NAME_DOCUMENT_ID)) {
			return WorkflowVariableHelper.getDocumentId(execution);
		} else {
			return PlaceholderValueContext.VALUE_WHEN_NOT_FOUND;
		}
	}
}