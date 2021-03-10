package ro.cloudSoft.cloudDoc.domain.content.search;

import java.util.Date;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;

public class DocumentSearchResultsView {

	private String documentId;
	private String documentName;
	private String workflowSender;
	private String workflowCurrentStatus;
	private String workflowName;
	private String documentTypeName;
	private Date documentCreatedDate;
	private String documentAuthor;
	private Map<Long, MetadataInstance> documentMetadatasMap;
	
	public DocumentSearchResultsView() {
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getWorkflowSender() {
		return workflowSender;
	}

	public void setWorkflowSender(String workflowSender) {
		this.workflowSender = workflowSender;
	}

	public String getWorkflowCurrentStatus() {
		return workflowCurrentStatus;
	}

	public void setWorkflowCurrentStatus(String workflowCurrentStatus) {
		this.workflowCurrentStatus = workflowCurrentStatus;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public Date getDocumentCreatedDate() {
		return documentCreatedDate;
	}

	public void setDocumentCreatedDate(Date documentCreatedDate) {
		this.documentCreatedDate = documentCreatedDate;
	}

	public String getDocumentAuthor() {
		return documentAuthor;
	}

	public void setDocumentAuthor(String documentAuthor) {
		this.documentAuthor = documentAuthor;
	}

	public Map<Long, MetadataInstance> getDocumentMetadatasMap() {
		return documentMetadatasMap;
	}

	public void setDocumentMetadatasMap(
			Map<Long, MetadataInstance> documentMetadatasMap) {
		this.documentMetadatasMap = documentMetadatasMap;
	}
	
}
