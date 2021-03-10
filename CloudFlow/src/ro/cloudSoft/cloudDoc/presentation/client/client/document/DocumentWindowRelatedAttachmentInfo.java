package ro.cloudSoft.cloudDoc.presentation.client.client.document;

public class DocumentWindowRelatedAttachmentInfo {

	private String attachmentName;
	private String documentLocationRealName;
	private String documentId;
	private Integer versionNumber;
	
	public String getAttachmentName() {
		return attachmentName;
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public String getDocumentId() {
		return documentId;
	}
	public Integer getVersionNumber() {
		return versionNumber;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}
}