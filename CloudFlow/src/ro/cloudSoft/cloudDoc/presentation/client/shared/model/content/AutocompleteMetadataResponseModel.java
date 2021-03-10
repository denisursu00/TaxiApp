package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class AutocompleteMetadataResponseModel {

	private MetadataInstanceModel metadataInstance;
	private MetadataCollectionInstanceModel metadataCollectionInstance;
	
	public MetadataInstanceModel getMetadataInstance() {
		return metadataInstance;
	}
	public void setMetadataInstance(MetadataInstanceModel metadataInstance) {
		this.metadataInstance = metadataInstance;
	}
	public MetadataCollectionInstanceModel getMetadataCollectionInstance() {
		return metadataCollectionInstance;
	}
	public void setMetadataCollectionInstance(MetadataCollectionInstanceModel metadataCollectionInstance) {
		this.metadataCollectionInstance = metadataCollectionInstance;
	}
}
