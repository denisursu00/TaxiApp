package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.utils.content.DocumentLogAttributesPrinter;

import com.google.common.collect.Lists;

public class DocumentLogAttributes {

	private String documentLocationRealName;
	private String documentId;
	private Long documentTypeId;
	
	private List<String> parentFolderNames = Lists.newArrayList();
	
	private String documentName;
	
	private Date createdDate;
	private Long authorUserId;
	
	@Override
	public String toString() {
		return DocumentLogAttributesPrinter.asString(this);
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public String getDocumentId() {
		return documentId;
	}
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	public List<String> getParentFolderNames() {
		return parentFolderNames;
	}
	public String getDocumentName() {
		return documentName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public Long getAuthorUserId() {
		return authorUserId;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public void setParentFolderNames(List<String> parentFolderNames) {
		this.parentFolderNames = parentFolderNames;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setAuthorUserId(Long authorUserId) {
		this.authorUserId = authorUserId;
	}
}