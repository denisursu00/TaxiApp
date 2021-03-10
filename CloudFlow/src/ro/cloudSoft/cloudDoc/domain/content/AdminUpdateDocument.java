package ro.cloudSoft.cloudDoc.domain.content;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class AdminUpdateDocument {
	
	private String documentLocationRealName;
	private String id;
	private Long documentTypeId;
	private String name;
	private String description;
	private List<MetadataInstance> metadataInstances;
	private Map<Long, List<CollectionInstance>> collectionInstanceListMap = Maps.newLinkedHashMap();
	
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
	public String getName() {
		return name;
	}
	public void setName(String documentName) {
		this.name = documentName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String documentDescription) {
		this.description = documentDescription;
	}
	public List<MetadataInstance> getMetadataInstances() {
		return metadataInstances;
	}
	public void setMetadataInstances(List<MetadataInstance> metadataInstances) {
		this.metadataInstances = metadataInstances;
	}
	public Map<Long, List<CollectionInstance>> getCollectionInstanceListMap() {
		return collectionInstanceListMap;
	}
	public void setCollectionInstanceListMap(Map<Long, List<CollectionInstance>> collectionInstanceListMap) {
		this.collectionInstanceListMap = collectionInstanceListMap;
	}
}
