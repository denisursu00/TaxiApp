package ro.cloudSoft.cloudDoc.services.content;

import java.util.Date;
import java.util.List;

public class DocumentFilterModel {
	
	private boolean isSameType;
	private int offset;
	private int pageSize;
	
	private String documentLocationRealName;
	private String folderId;
	private Long documentTypeId;
	
	private String nameFilterValue;

	private String authorFilterValue;

	private Date createdFilterValue;

	private String lockedFilterValue;
	
	private String statusFilterValue;
	
	private List<MetadataFilterModel> metadataFilters;

	public boolean isSameType() {
		return isSameType;
	}

	public void setSameType(boolean isSameType) {
		this.isSameType = isSameType;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public List<MetadataFilterModel> getMetadataFilters() {
		return metadataFilters;
	}

	public void setMetadataFilters(List<MetadataFilterModel> metadataFilters) {
		this.metadataFilters = metadataFilters;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getNameFilterValue() {
		return nameFilterValue;
	}

	public void setNameFilterValue(String nameFilterValue) {
		this.nameFilterValue = nameFilterValue;
	}

	public String getAuthorFilterValue() {
		return authorFilterValue;
	}

	public void setAuthorFilterValue(String authorFilterValue) {
		this.authorFilterValue = authorFilterValue;
	}

	public Date getCreatedFilterValue() {
		return createdFilterValue;
	}

	public void setCreatedFilterValue(Date createdFilterValue) {
		this.createdFilterValue = createdFilterValue;
	}

	public String getLockedFilterValue() {
		return lockedFilterValue;
	}

	public void setLockedFilterValue(String lockedFilterValue) {
		this.lockedFilterValue = lockedFilterValue;
	}

	public String getStatusFilterValue() {
		return statusFilterValue;
	}

	public void setStatusFilterValue(String statusFilterValue) {
		this.statusFilterValue = statusFilterValue;
	}
	
}
