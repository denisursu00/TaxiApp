package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public class DocumentSelectionSearchFilter {
	
	private String documentLocationRealName;
	private Long documentTypeId;
	private String documentName;
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
}
