package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

public class DocumentValidationRequestModel {
	
	private Long documentTypeId;
	private String documentId;
	private String documentLocationRealName;
	private List<MetadataInstanceModel> metatadaInstances;
	
	public String getDocumentId() {
		return documentId;
	}
	
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	
	public void setMetatadaInstances(List<MetadataInstanceModel> metatadaInstances) {
		this.metatadaInstances = metatadaInstances;
	}
	
	public List<MetadataInstanceModel> getMetatadaInstances() {
		return metatadaInstances;
	}
}
