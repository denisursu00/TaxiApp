package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentCreationInDefaultLocationViewModel implements IsSerializable {

	private Long documentTypeId;
	private String documentTypeName;

	private String parentDocumentLocationRealNameForDefaultLocation;
	private String folderIdForDefaultLocation;
	
	/**
	 * Constructor pentru serializare
	 */
	protected DocumentCreationInDefaultLocationViewModel() {}
	
	public DocumentCreationInDefaultLocationViewModel(Long documentTypeId, String documentTypeName,
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