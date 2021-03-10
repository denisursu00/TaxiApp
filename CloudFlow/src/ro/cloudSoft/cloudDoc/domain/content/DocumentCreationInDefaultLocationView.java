package ro.cloudSoft.cloudDoc.domain.content;

public class DocumentCreationInDefaultLocationView {

	private final Long documentTypeId;
	private final String documentTypeName;

	private final String parentDocumentLocationRealNameForDefaultLocation;
	private final String folderIdForDefaultLocation;
	
	public DocumentCreationInDefaultLocationView(Long documentTypeId, String documentTypeName,
			String parentDocumentLocationRealNameForDefaultLocation, String folderIdForDefaultLocation) {
		
		this.documentTypeId = documentTypeId;
		this.documentTypeName = documentTypeName;
		
		this.parentDocumentLocationRealNameForDefaultLocation = parentDocumentLocationRealNameForDefaultLocation;
		this.folderIdForDefaultLocation = folderIdForDefaultLocation;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public String getParentDocumentLocationRealNameForDefaultLocation() {
		return parentDocumentLocationRealNameForDefaultLocation;
	}

	public String getFolderIdForDefaultLocation() {
		return folderIdForDefaultLocation;
	}
}