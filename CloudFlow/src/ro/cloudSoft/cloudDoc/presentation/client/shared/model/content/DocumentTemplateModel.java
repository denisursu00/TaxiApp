package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class DocumentTemplateModel {

	private String name;
	private String description;
	private String documentTypeId;
	private String exportAvailabilityExpression;
	private byte[] data;
	
	public DocumentTemplateModel(String name, String description, byte[] data) {
		this.name = name;
		this.description = description;
		this.data = data;
	}
	
	public DocumentTemplateModel(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public DocumentTemplateModel(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getExportAvailabilityExpression() {
		return exportAvailabilityExpression;
	}
	public void setExportAvailabilityExpression(String exportAvailabilityExpression) {
		this.exportAvailabilityExpression = exportAvailabilityExpression;
	}
}
