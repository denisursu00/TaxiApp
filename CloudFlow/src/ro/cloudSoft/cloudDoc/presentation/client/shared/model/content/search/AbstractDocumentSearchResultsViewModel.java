package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AbstractDocumentSearchResultsViewModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_DOCUMENT_ID = "documentId";
	public static final String PROPERTY_DOCUMENT_NAME = "documentName";
	public static final String PROPERTY_DOCUMENT_CREATED_DATE = "documentCreatedDate";
	public static final String PROPERTY_DOCUMENT_AUTHOR_DISPLAY_NAME = "documentAuthorDisplayName";

	public static final String PROPERTY_WORKFLOW_NAME = "workflowName";
	public static final String PROPERTY_WORKFLOW_CURRENT_STATE_NAME = "workflowCurrentStateName";
	public static final String PROPERTY_WORKFLOW_SENDER_DISPLAY_NAME = "workflowSenderDisplayName";
	
	public String getDocumentId() {
		return get(PROPERTY_DOCUMENT_ID);
	}
	public String getDocumentName() {
		return get(PROPERTY_DOCUMENT_NAME);
	}
	public Date getDocumentCreatedDate() {
		return get(PROPERTY_DOCUMENT_CREATED_DATE);
	}
	public String getDocumentAuthorDisplayName() {
		return get(PROPERTY_DOCUMENT_AUTHOR_DISPLAY_NAME);
	}
	public String getWorkflowName() {
		return get(PROPERTY_WORKFLOW_NAME);
	}
	public String getWorkflowCurrentStateName() {
		return get(PROPERTY_WORKFLOW_CURRENT_STATE_NAME);
	}
	public String getWorkflowSenderDisplayName() {
		return get(PROPERTY_WORKFLOW_SENDER_DISPLAY_NAME);
	}
	public void setDocumentId(String documentId) {
		set(PROPERTY_DOCUMENT_ID, documentId);
	}
	public void setDocumentName(String documentName) {
		set(PROPERTY_DOCUMENT_NAME, documentName);
	}
	public void setDocumentCreatedDate(Date documentCreatedDate) {
		set(PROPERTY_DOCUMENT_CREATED_DATE, documentCreatedDate);
	}
	public void setDocumentAuthorDisplayName(String documentAuthorDisplayName) {
		set(PROPERTY_DOCUMENT_AUTHOR_DISPLAY_NAME, documentAuthorDisplayName);
	}
	public void setWorkflowName(String workflowName) {
		set(PROPERTY_WORKFLOW_NAME, workflowName);
	}
	public void setWorkflowCurrentStateName(String workflowCurrentStateName) {
		set(PROPERTY_WORKFLOW_CURRENT_STATE_NAME, workflowCurrentStateName);
	}
	public void setWorkflowSenderDisplayName(String workflowSenderDisplayName) {
		set(PROPERTY_WORKFLOW_SENDER_DISPLAY_NAME, workflowSenderDisplayName);
	}
}