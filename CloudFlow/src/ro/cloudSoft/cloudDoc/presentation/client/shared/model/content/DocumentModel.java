package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;

public class DocumentModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = -3645545610816314412L;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_DOCUMENT_TYPE_ID = "documentTypeId";
	public static final String PROPERTY_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	public static final String PROPERTY_PARENT_FOLDER_ID = "parentFolderId";
	public static final String PROPERTY_DOCUMENT_NAME = "documentName";
	public static final String PROPERTY_DOCUMENT_DESCRIPTION = "documentDescription";
	public static final String PROPERTY_AUTHOR = "author";
	public static final String PROPERTY_CREATED = "created";
	public static final String PROPERTY_LAST_MODIFIED = "lastModified";
	public static final String PROPERTY_LOCKED_BY = "lockedBy";
	public static final String PROPERTY_DOCUMENT_METADATA_INSTANCES = "metadataInstances";
	public static final String PROPERTY_DOCUMENT_METADATA_COLLECTION_INSTANCES = "metadataCollectionInstances";
	public static final String PROPERTY_DOCUMENT_METADATA_COLLECTIONS = "collectionInstanceListMap";
	public static final String PROPERTY_ATTACHMENTS = "attachments";
	public static final String PROPERTY_NAMES_FOR_ATTACHMENTS_TO_DELETE = "namesForAttachmentsToDelete";
	public static final String PROPERTY_PERMISSIONS = "permissions";
	public static final String PROPERTY_HAS_STABLE_VERSION = "hasStableVersions";
	public static final String PROPERTY_VERSION_NUMBER = "versionNumber";
	public static final String PROPERTY_WORKFLOW_STATE_ID = "workflowStateId";
	
	public static final String PROPERTY_AUTHOR_NAME = "authorName";
	public static final String PROPERTY_LOCKER_NAME = "lockerName";
	
	public static final String PROPERTY_METADATA_INSTANCE_MAP = "metadataInstanceMap";
	
	private Long versionWorkflowStateId;
	
	public DocumentModel() {
	}
	
	public Map<Long, MetadataInstanceModel> getMetadataInstanceMap() {
		if (get(PROPERTY_METADATA_INSTANCE_MAP) == null) {
			Map<Long, MetadataInstanceModel> metadataInstanceMap = new HashMap<Long, MetadataInstanceModel>();
			List<MetadataInstanceModel> metadataInstanceList = getMetadataInstances();
			for (MetadataInstanceModel metadataInstance : metadataInstanceList) {
				metadataInstanceMap.put(metadataInstance.getMetadataDefinitionId(), metadataInstance);
			}
			set(PROPERTY_METADATA_INSTANCE_MAP, metadataInstanceMap);
		}
		return get(PROPERTY_METADATA_INSTANCE_MAP);
	}
	
	public void setMetadataCollectionInstances(List<MetadataCollectionInstanceModel> metadataCollectionInstances) {
		set(PROPERTY_DOCUMENT_METADATA_COLLECTION_INSTANCES, metadataCollectionInstances);
	}
	
	public List<MetadataCollectionInstanceModel> getMetadataCollectionInstances() {
		return get(PROPERTY_DOCUMENT_METADATA_COLLECTION_INSTANCES);
	}

	public String getId() {
		return get(PROPERTY_ID);
	}
	public void setId(String id) {
		set(PROPERTY_ID, id);
	}
	public Long getDocumentTypeId() {
		return get(PROPERTY_DOCUMENT_TYPE_ID);
	}
	public void setDocumentTypeId(Long id) {
		set(PROPERTY_DOCUMENT_TYPE_ID, id);
	}
	public String getDocumentLocationRealName() {
		return get(PROPERTY_DOCUMENT_LOCATION_REAL_NAME);
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		set(PROPERTY_DOCUMENT_LOCATION_REAL_NAME, documentLocationRealName);
	}
	public String getParentFolderId() {
		return get(PROPERTY_PARENT_FOLDER_ID);
	}
	public void setParentFolderId(String parentFolderId) {
		set(PROPERTY_PARENT_FOLDER_ID, parentFolderId);
	}
	public String getDocumentName() {
		return get(PROPERTY_DOCUMENT_NAME);
	}
	public void setDocumentName(String documentTypeName) {
		set(PROPERTY_DOCUMENT_NAME, documentTypeName);
	}
	public List<MetadataInstanceModel> getMetadataInstances() {
		return get(PROPERTY_DOCUMENT_METADATA_INSTANCES);
	}
	public void setMetadataInstances(List<MetadataInstanceModel> metadataInstances) {
		set(PROPERTY_DOCUMENT_METADATA_INSTANCES, metadataInstances);
	}
	public void setDocumentDescription(String documentDescription) {
		set(PROPERTY_DOCUMENT_DESCRIPTION, documentDescription);
	}
	public String getDocumentDescription() {
		return get(PROPERTY_DOCUMENT_DESCRIPTION);
	}
	public String getAuthor() {
		return get(PROPERTY_AUTHOR);
	}
	public void setAuthor(String author) {
		set(PROPERTY_AUTHOR, author);
	}
	public Date getCreated() {
		return get(PROPERTY_CREATED);
	}
	public void setCreated(Date created) {
		set(PROPERTY_CREATED, created);
	}
	public Date getLastModified() {
		return get(PROPERTY_LAST_MODIFIED);
	}
	public void setLastModified(Date lastModified) {
		set(PROPERTY_LAST_MODIFIED, lastModified);
	}
	public String getLockedBy() {
		return get(PROPERTY_LOCKED_BY);
	}
	public void setLockedBy(String lockedBy) {
		set(PROPERTY_LOCKED_BY, lockedBy);
	}
	public List<PermissionModel> getPermissions() {
		return get(PROPERTY_PERMISSIONS);
	}
	public void setPermissions(List<PermissionModel> permissions) {
		set(PROPERTY_PERMISSIONS, permissions);
	}
	public List<DocumentAttachmentModel> getAttachments() {
		return get(PROPERTY_ATTACHMENTS);
	}
	public void setAttachments(List<DocumentAttachmentModel> attachments) {
		set(PROPERTY_ATTACHMENTS, attachments);
	}
	public List<String> getNamesForAttachmentsToDelete() {
		return get(PROPERTY_NAMES_FOR_ATTACHMENTS_TO_DELETE);
	}
	public void setNamesForAttachmentsToDelete(List<String> namesForAttachmentsToDelete) {
		set(PROPERTY_NAMES_FOR_ATTACHMENTS_TO_DELETE, namesForAttachmentsToDelete);
	}
	public String getAuthorName() {
		return get(PROPERTY_AUTHOR_NAME);
	}
	public void setAuthorName(String authorName) {
		set(PROPERTY_AUTHOR_NAME, authorName);
	}
	public String getLockerName() {
		return get(PROPERTY_LOCKER_NAME);
	}
	public void setLockerName(String lockerName) {
		set(PROPERTY_LOCKER_NAME, lockerName);
	}
	public Boolean getHasStableVersion() {
		return get(PROPERTY_HAS_STABLE_VERSION);
	}
	public void setHasStableVersion(Boolean hasStableVersion) {
		set(PROPERTY_HAS_STABLE_VERSION, hasStableVersion);
	}
	public Integer getVersionNumber() {
		return get(PROPERTY_VERSION_NUMBER);
	}
	public void setVersionNumber(Integer versionNumber) {
		set(PROPERTY_VERSION_NUMBER, versionNumber);
	}
	public Long getWorkflowStateId() {
		return this.get(PROPERTY_WORKFLOW_STATE_ID);
	}
	public void setWorkflowStateId(Long workflowStateId) {
		this.set(PROPERTY_WORKFLOW_STATE_ID, workflowStateId);
	}
	public Long getVersionWorkflowStateId() {
		return versionWorkflowStateId;
	}
	public void setVersionWorkflowStateId(Long versionWorkflowStateId) {
		this.versionWorkflowStateId = versionWorkflowStateId;
	}
}