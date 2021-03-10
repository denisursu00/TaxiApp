package ro.cloudSoft.cloudDoc.domain.bpm;

import java.util.Date;


public class TaskInstance {
	
	private String documentId;
	private String documentLocationRealName;
	private Long documentTypeId;
	private Long documentAuthorId;
	private String documentName;
	private Date documentCreatedDate;
	
	private String state;
	private Long senderUserId;
	private Workflow workflow;
	
	public TaskInstance() {
	}

	public Long getDocumentAuthorId() {
		return documentAuthorId;
	}
	public void setDocumentAuthorId(Long documentAuthorId) {
		this.documentAuthorId = documentAuthorId;
	}
	public Date getDocumentCreatedDate() {
		return documentCreatedDate;
	}
	public void setDocumentCreatedDate(Date documentCreatedDate) {
		this.documentCreatedDate = documentCreatedDate;
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
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getSenderUserId() {
		return senderUserId;
	}
	
	public void setSenderUserId(Long senderUserId) {
		this.senderUserId = senderUserId;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
}
