package ro.cloudSoft.cloudDoc.domain.content.search;

import java.util.Date;

public abstract class AbstractDocumentSearchResultsView {

	private String documentId;
	private String documentName;
	private Date documentCreatedDate;
	private String documentAuthorDisplayName;
	
	private String workflowName;
	private String workflowCurrentStateName;
	private String workflowSenderDisplayName;
	
	public String getDocumentId() {
		return documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public Date getDocumentCreatedDate() {
		return documentCreatedDate;
	}
	public String getDocumentAuthorDisplayName() {
		return documentAuthorDisplayName;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public String getWorkflowCurrentStateName() {
		return workflowCurrentStateName;
	}
	public String getWorkflowSenderDisplayName() {
		return workflowSenderDisplayName;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public void setDocumentCreatedDate(Date documentCreatedDate) {
		this.documentCreatedDate = documentCreatedDate;
	}
	public void setDocumentAuthorDisplayName(String documentAuthorDisplayName) {
		this.documentAuthorDisplayName = documentAuthorDisplayName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public void setWorkflowCurrentStateName(String workflowCurrentStateName) {
		this.workflowCurrentStateName = workflowCurrentStateName;
	}
	public void setWorkflowSenderDisplayName(String workflowSenderDisplayName) {
		this.workflowSenderDisplayName = workflowSenderDisplayName;
	}
}