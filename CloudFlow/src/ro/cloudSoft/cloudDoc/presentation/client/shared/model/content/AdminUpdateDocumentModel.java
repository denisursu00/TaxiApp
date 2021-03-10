package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

public class AdminUpdateDocumentModel {
	
	private String documentLocationRealName;
	private String id;
	private Long documentTypeId;
	private String documentName;
	private String documentDescription;
	private List<MetadataInstanceModel> metadataInstances;
	private List<MetadataCollectionInstanceModel> metadataCollectionInstances;
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getDocumentDescription() {
		return documentDescription;
	}
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}
	public List<MetadataInstanceModel> getMetadataInstances() {
		return metadataInstances;
	}
	public void setMetadataInstances(List<MetadataInstanceModel> metadataInstances) {
		this.metadataInstances = metadataInstances;
	}
	public List<MetadataCollectionInstanceModel> getMetadataCollectionInstances() {
		return metadataCollectionInstances;
	}
	public void setMetadataCollectionInstances(List<MetadataCollectionInstanceModel> metadataCollectionInstances) {
		this.metadataCollectionInstances = metadataCollectionInstances;
	}
}
