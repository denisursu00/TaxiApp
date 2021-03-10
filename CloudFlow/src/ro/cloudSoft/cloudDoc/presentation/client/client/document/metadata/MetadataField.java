package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

public interface MetadataField {
	
	String getMetadataDefinitionName();
	
	List<String> getMetadataValues();
	
	void setMetadataValues(List<String> values);
	
	void setMetadataValues(String... values);
	
	void setMandatory(boolean mandatory);
	
	void setRestrictedOnEdit(boolean restrictedOnEdit);
	
	boolean isRestrictedOnEdit();
	
	/**
	 * Se apeleaza la adaugarea unui document.
	 * 
	 * @param startState starea de pornire, daca tipul de document are un flux asociat, sau null daca nu are
	 */
	void onAddDocument(WorkflowStateModel startState);

	/**
	 * Se apeleaza la editarea unui document.
	 * 
	 * @param currentState starea curenta, daca tipul de document are un flux asociat, sau null daca nu are
	 */
	void onEditDocument(WorkflowStateModel currentState);
}