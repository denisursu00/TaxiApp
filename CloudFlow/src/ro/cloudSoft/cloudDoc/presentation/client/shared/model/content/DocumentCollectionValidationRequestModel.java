package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class DocumentCollectionValidationRequestModel extends DocumentValidationRequestModel {
	
	private Long metadataCollectionDefinitionId;
	
	public Long getMetadataCollectionDefinitionId() {
		return metadataCollectionDefinitionId;
	}
	
	public void setMetadataCollectionDefinitionId(Long metadataCollectionDefinitionId) {
		this.metadataCollectionDefinitionId = metadataCollectionDefinitionId;
	}
}
