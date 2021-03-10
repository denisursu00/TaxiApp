package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.organization.User;

@Entity
@Table(name = "DOCUMENT_ATTACHMENT_DETAIL")
public class DocumentAttachmentDetail {

	private Long id;
	
	private String documentLocationRealName;
	private String documentId;
	private String documentWorkflowStateCode;
	
	private String attachmentName;
	private User attachedBy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DOCUMENT_LOCATION_REAL_NAME", nullable = false)
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
	@Column(name = "DOCUMENT_ID", nullable = false)
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	@Column(name = "DOCUMENT_WORKFLOW_STATE_CODE", nullable = false)
	public String getDocumentWorkflowStateCode() {
		return documentWorkflowStateCode;
	}
	
	public void setDocumentWorkflowStateCode(String documentWorkflowStateCode) {
		this.documentWorkflowStateCode = documentWorkflowStateCode;
	}
	
	@Column(name = "ATTACHMENT_NAME", nullable = false)	
	public String getAttachmentName() {
		return attachmentName;
	}
	
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ATTACHED_BY", referencedColumnName = "org_entity_id", nullable = false)
	public User getAttachedBy() {
		return attachedBy;
	}
	
	public void setAttachedBy(User attachedBy) {
		this.attachedBy = attachedBy;
	}
}
