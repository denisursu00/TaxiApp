package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public enum ExportType {

	PDF(".pdf"),
	DOCX(".docx");
	
	private String fileExtension;
	
	private ExportType(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
}
