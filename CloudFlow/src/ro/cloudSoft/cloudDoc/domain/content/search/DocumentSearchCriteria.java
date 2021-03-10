package ro.cloudSoft.cloudDoc.domain.content.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentSearchCriteria {
	
	private String documentLocationRealName;

	private Date createdStart;
	private Date createdEnd;
	
	private boolean searchInVersions;
	
	private List<Long> documentTypeIdList;
	
	private List<Long> workflowStateIdList;
	
	private List<MetadataSearchCriteria> metadataSearchCriteriaList;
	private List<CollectionSearchCriteria> collectionSearchCriteriaList;
	
	public void addMetadataSearchCriteria(MetadataSearchCriteria metadataSearchCriteria) {
		if (metadataSearchCriteriaList == null) {
			metadataSearchCriteriaList = new ArrayList<MetadataSearchCriteria>();
		}
		metadataSearchCriteriaList.add(metadataSearchCriteria);
	}
	
	public void addCollectionSearchCriteria(CollectionSearchCriteria collectionSearchCriteria) {
		if (collectionSearchCriteriaList == null) {
			collectionSearchCriteriaList = new ArrayList<CollectionSearchCriteria>();
		}
		collectionSearchCriteriaList.add(collectionSearchCriteria);
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public Date getCreatedStart() {
		return createdStart;
	}
	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}
	public Date getCreatedEnd() {
		return createdEnd;
	}
	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}
	public boolean isSearchInVersions() {
		return searchInVersions;
	}
	public void setSearchInVersions(boolean searchInVersions) {
		this.searchInVersions = searchInVersions;
	}
	public List<Long> getDocumentTypeIdList() {
		return documentTypeIdList;
	}
	public void setDocumentTypeIdList(List<Long> documentTypeIdList) {
		this.documentTypeIdList = documentTypeIdList;
	}
	public List<Long> getWorkflowStateIdList() {
		return workflowStateIdList;
	}
	public void setWorkflowStateIdList(List<Long> workflowStateIdList) {
		this.workflowStateIdList = workflowStateIdList;
	}
	public List<MetadataSearchCriteria> getMetadataSearchCriteriaList() {
		return metadataSearchCriteriaList;
	}
	public void setMetadataSearchCriteriaList(List<MetadataSearchCriteria> metadataSearchCriteriaList) {
		this.metadataSearchCriteriaList = metadataSearchCriteriaList;
	}
	public List<CollectionSearchCriteria> getCollectionSearchCriteriaList() {
		return collectionSearchCriteriaList;
	}
	public void setCollectionSearchCriteriaList(List<CollectionSearchCriteria> collectionSearchCriteriaList) {
		this.collectionSearchCriteriaList = collectionSearchCriteriaList;
	}
	
	public static class MetadataSearchCriteria {
		
		private Long metadataDefinitionId;
		private String value;
		
		public MetadataSearchCriteria(Long metadataDefinitionId, String value) {
			this.metadataDefinitionId = metadataDefinitionId;
			this.value = value;
		}
		
		public Long getMetadataDefinitionId() {
			return metadataDefinitionId;
		}
		public String getValue() {
			return value;
		}
	}
	
	public static class CollectionSearchCriteria {
		
		private Long collectionDefinitionId;
		private Long metadataDefinitionId;
		private String value;
		
		public CollectionSearchCriteria(Long collectionDefinitionId, Long metadataDefinitionId, String value) {
			this.collectionDefinitionId = collectionDefinitionId;
			this.metadataDefinitionId = metadataDefinitionId;
			this.value = value;
		}
		
		public Long getCollectionDefinitionId() {
			return collectionDefinitionId;
		}
		public Long getMetadataDefinitionId() {
			return metadataDefinitionId;
		}
		public String getValue() {
			return value;
		}
	}
}