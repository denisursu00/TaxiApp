package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentSearchCriteriaModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_DOCUMENT_LOCATION_REAL_NAME= "documentLocationRealName";
	public static final String PROPERTY_CREATED_START = "createdStart";
	public static final String PROPERTY_CREATED_END = "createdEnd";
	public static final String PROPERTY_SEARCH_IN_VERSIONS = "searchInVersions";
	public static final String PROPERTY_DOCUMENT_TYPE_ID_LIST = "documentTypeIdList";
	public static final String PROPERTY_WORKFLOW_STATE_ID_LIST = "workflowStateIdList";
	public static final String PROPERTY_METADATA_SEARCH_CRITERIA_LIST = "metadataSearchCriteriaList";
	public static final String PROPERTY_COLLECTION_SEARCH_CRITERIA_LIST = "collectionSearchCriteriaList";
	
	public DocumentSearchCriteriaModel() {
		setDocumentTypeIdList(new ArrayList<Long>());
		setWorkflowStateIdList(new ArrayList<Long>());
		setMetadataSearchCriteriaList(new ArrayList<MetadataSearchCriteriaModel>());
		setCollectionSearchCriteriaList(new ArrayList<CollectionSearchCriteriaModel>());
	}
	
	public void setDocumentLocationRealName(String documentLocationRealName){
		set(PROPERTY_DOCUMENT_LOCATION_REAL_NAME, documentLocationRealName);
	}
	
	public String getDocumentLocationRealName(){
		return get(PROPERTY_DOCUMENT_LOCATION_REAL_NAME);
	}
	
	public void setCreatedStart(Date createdStart){
		set(PROPERTY_CREATED_START, createdStart);
	}
	
	public Date getCreatedStart(){
		return (Date)get(PROPERTY_CREATED_START);
	}
	
	public void setCreatedEnd(Date createdEnd){
		set(PROPERTY_CREATED_END, createdEnd);
	}
	
	public Date getCreatedEnd(){
		return (Date)get(PROPERTY_CREATED_END);
	}
	
	public void setSearchInVersions(Boolean searchInVersions){
		set(PROPERTY_SEARCH_IN_VERSIONS, searchInVersions);
	}
	
	public Boolean isSearchInVersions(){
		return get(PROPERTY_SEARCH_IN_VERSIONS);
	}
	
	public void setDocumentTypeIdList(List<Long> documentTypeIdList){
		set(PROPERTY_DOCUMENT_TYPE_ID_LIST, documentTypeIdList);
	}
	
	public List<Long> getDocumentTypeIdList(){
		return get(PROPERTY_DOCUMENT_TYPE_ID_LIST);
	}
	
	public void setWorkflowStateIdList(List<Long> workflowStateIdList){
		set(PROPERTY_WORKFLOW_STATE_ID_LIST, workflowStateIdList);
	}
	
	public List<Long> getWorkflowStateIdList(){
		return get(PROPERTY_WORKFLOW_STATE_ID_LIST);
	}
	
	public void setMetadataSearchCriteriaList(List<MetadataSearchCriteriaModel> metadataSearchCriteriaList){
		set(PROPERTY_METADATA_SEARCH_CRITERIA_LIST, metadataSearchCriteriaList);
	}
	
	public List<MetadataSearchCriteriaModel> getMetadataSearchCriteriaList(){
		return get(PROPERTY_METADATA_SEARCH_CRITERIA_LIST);
	}
	
	public void setCollectionSearchCriteriaList(List<CollectionSearchCriteriaModel> collectionSearchCriteriaList){
		set(PROPERTY_COLLECTION_SEARCH_CRITERIA_LIST, collectionSearchCriteriaList);
	}
	
	public List<CollectionSearchCriteriaModel> getCollectionSearchCriteriaList(){
		return get(PROPERTY_COLLECTION_SEARCH_CRITERIA_LIST);
	}
	
	public void addWorkflowStateId(Long workflowStateId){
		getWorkflowStateIdList().add(workflowStateId);
	}
	
	public void addDocumentTypeId(Long documentTypeId){
		getDocumentTypeIdList().add(documentTypeId);
	}
	
	public void addMetadataSearchCriteria(MetadataSearchCriteriaModel metadataSearchCriteriaModel){
		getMetadataSearchCriteriaList().add(metadataSearchCriteriaModel);
	}
	
	public void addCollectionSearchCriteria(CollectionSearchCriteriaModel collectionSearchCriteriaModel){
		getCollectionSearchCriteriaList().add(collectionSearchCriteriaModel);
	}
	
	// ------ inner classes	------
	
	public static class MetadataSearchCriteriaModel extends BaseModel {

		private static final long serialVersionUID = 1L;
		
		public static final String PROPERTY_METADATA_DEFINITION_ID = "metadataDefinitionId";
		public static final String PROPERTY_VALUE = "value";
		
		public MetadataSearchCriteriaModel() {
		}
		
		public MetadataSearchCriteriaModel(Long metadataDefinitionId, String value) {
			setMetadataDefinitionId(metadataDefinitionId);
			setValue(value);
		}
		
		public void setMetadataDefinitionId(Long metadataDefinitionId){
			set(PROPERTY_METADATA_DEFINITION_ID, metadataDefinitionId);
		}
		
		public Long getMetadataDefinitionId() {
			return get(PROPERTY_METADATA_DEFINITION_ID);
		}
		
		public void setValue(String value){
			set(PROPERTY_VALUE, value);
		}
		
		public String getValue() {
			return get(PROPERTY_VALUE);
		}
		
	}
	
	public static class CollectionSearchCriteriaModel extends BaseModel {

		private static final long serialVersionUID = 1L;
		
		public static final String PROPERTY_COLLECTION_DEFINITION_ID = "collectionDefinitionId";
		public static final String PROPERTY_METADATA_DEFINITION_ID = "metadataDefinitionId";
		public static final String PROPERTY_VALUE = "value";
		
		public CollectionSearchCriteriaModel() {
		}
		
		public CollectionSearchCriteriaModel(Long collectionDefinitionId, Long metadataDefinitionId, String value) {
			setCollectionDefinitionId(collectionDefinitionId);
			setMetadataDefinitionId(metadataDefinitionId);
			setValue(value);
		}
		
		public void setCollectionDefinitionId(Long collectionDefinitionId){
			set(PROPERTY_COLLECTION_DEFINITION_ID, collectionDefinitionId);
		}
		
		public Long getCollectionDefinitionId() {
			return get(PROPERTY_COLLECTION_DEFINITION_ID);
		}
		
		public void setMetadataDefinitionId(Long metadataDefinitionId){
			set(PROPERTY_METADATA_DEFINITION_ID, metadataDefinitionId);
		}
		
		public Long getMetadataDefinitionId() {
			return get(PROPERTY_METADATA_DEFINITION_ID);
		}
		
		public void setValue(String value){
			set(PROPERTY_VALUE, value);
		}
		
		public String getValue() {
			return get(PROPERTY_VALUE);
		}
		
	}
	
}
