package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkflowModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_PROCESS_DEFINITION_ID = "processDefinitionId";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESCRIPTION = "description";
	public static final String PROPERTY_DOCUMENT_TYPES = "documentTypes";
	public static final String PROPERTY_DISPLAY_DOCUMENT_TYPES = "displayDocumentTypes";
	public static final String PROPERTY_SUPERVISORS = "supervisors";
	public static final String PROPERTY_TRANSITIONS = "transitions";
	
	public static final String PROPERTY_BASE_VERSION_WORKFLOW_ID = "baseVersionWorkflowId";
	public static final String PROPERTY_SOURCE_VERSION_WORKFLOW_ID = "sourceVersionWorkflowId";
	
	public static final String PROPERTY_VERSION_NUMBER = "versionNumber";
	
	public WorkflowModel() {
	}
	
	public void setId(Long id){
		set(PROPERTY_ID, id);
	}
	
	public Long getId(){
		return (Long)get(PROPERTY_ID);
	}
	
	public void setProcessDefinitionId(String processDefinitionId){
		set(PROPERTY_PROCESS_DEFINITION_ID, processDefinitionId);
	}
	
	public String getProcessDefinitionId(){
		return (String)get(PROPERTY_PROCESS_DEFINITION_ID);
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return (String)get(PROPERTY_NAME);
	}
	
	public void setDescription(String description){
		set(PROPERTY_DESCRIPTION, description);
	}
	
	public String getDescription(){
		return (String)get(PROPERTY_DESCRIPTION);
	}
	
	public void setDocumentTypes(List<DocumentTypeModel> documentTypes){
		set(PROPERTY_DOCUMENT_TYPES, documentTypes);
		if (documentTypes != null && documentTypes.size() > 0){
			StringBuilder displayDocumentTypes = new StringBuilder("");
			for(DocumentTypeModel dtm : documentTypes){
				displayDocumentTypes.append(dtm.getName() + ", ");
			}
			displayDocumentTypes.delete(displayDocumentTypes.length() - 2, displayDocumentTypes.length());
			setDisplayDocumentTypes(displayDocumentTypes.toString());
		}
	}
	
	public List<DocumentTypeModel> getDocumentTypes(){
		return get(PROPERTY_DOCUMENT_TYPES);
	}
	
	public void setDisplayDocumentTypes(String displayDocumentTypes){
		set(PROPERTY_DISPLAY_DOCUMENT_TYPES, displayDocumentTypes);
	}
	
	public String getDisplayDocumentTypes(){
		return (String)get(PROPERTY_DISPLAY_DOCUMENT_TYPES);
	}
	
	public void setSupervisors(List<OrganizationEntityModel> supervisors){
		set(PROPERTY_SUPERVISORS, supervisors);
	}
	
	public List<OrganizationEntityModel> getSupervisors(){
		return get(PROPERTY_SUPERVISORS);
	}
	
	public void setTransitions(List<WorkflowTransitionModel> transitions){
		set(PROPERTY_TRANSITIONS, transitions);
	}
	
	public List<WorkflowTransitionModel> getTransitions(){
		return get(PROPERTY_TRANSITIONS);
	}
	
	public Long getBaseVersionWorkflowId() {
		return get(PROPERTY_BASE_VERSION_WORKFLOW_ID);
	}

	public void setBaseVersionWorkflowId(Long baseVersionWorkflowId) {
		set(PROPERTY_BASE_VERSION_WORKFLOW_ID, baseVersionWorkflowId);
	}

	public Long getSourceVersionWorkflowId() {
		return get(PROPERTY_SOURCE_VERSION_WORKFLOW_ID);
	}
	
	public void setSourceVersionWorkflowId(Long sourceVersionWorkflowId) {
		set(PROPERTY_SOURCE_VERSION_WORKFLOW_ID, sourceVersionWorkflowId);
	}
	
	public void setVersionNumber(Integer versionNumber) {
		set(PROPERTY_VERSION_NUMBER, versionNumber);
	}
	
	public Integer getVersionNumber() {
		return get(PROPERTY_VERSION_NUMBER);
	}
	
}
