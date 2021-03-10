package ro.cloudSoft.cloudDoc.domain.content;

public enum TemplateType {
	JASPER_REPORTS(".zip"), 
	X_DOC_REPORT(".docx"), 
	EXPORT_TO_IARCHIVE_DOCUMENT_METADATA("EXPORT_TO_IARCHIVE_DOCUMENT_METADATA"), 
	EXPORT_TO_IARCHIVE_TABLE("EXPORT_TO_IARCHIVE_TABLE");

	private String fileExtension;

	private TemplateType(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileExtension() {
		return fileExtension;
	}
}
