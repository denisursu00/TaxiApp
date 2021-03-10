package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class GetFolderPathRequestModel {

	private String documentLocationRealName;
	private String folderId;
	
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
}
