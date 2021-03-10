package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class GetPagedDocumentsFromFolderRequestModel {

	private String documentLocationRealName;
	private String folderId;
	private boolean sameType;
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public boolean isSameType() {
		return sameType;
	}
	public void setSameType(boolean sameType) {
		this.sameType = sameType;
	}
}
