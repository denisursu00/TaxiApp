package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class MoveFolderRequestModel {
	
	private String folderToMoveId;
	private String destinationFolderId;
	private String documentLocationRealName;
	
	public String getFolderToMoveId() {
		return folderToMoveId;
	}
	public void setFolderToMoveId(String folderToMoveId) {
		this.folderToMoveId = folderToMoveId;
	}
	public String getDestinationFolderId() {
		return destinationFolderId;
	}
	public void setDestinationFolderId(String destinationFolderId) {
		this.destinationFolderId = destinationFolderId;
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
}
