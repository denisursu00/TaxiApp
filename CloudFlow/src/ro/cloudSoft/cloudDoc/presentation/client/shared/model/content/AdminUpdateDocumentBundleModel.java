package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class AdminUpdateDocumentBundleModel {

	private AdminUpdateDocumentModel document;
	private DocumentTypeModel documentType;
	
	public AdminUpdateDocumentModel getDocument() {
		return document;
	}
	public void setDocument(AdminUpdateDocumentModel document) {
		this.document = document;
	}
	
	public DocumentTypeModel getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentTypeModel documentType) {
		this.documentType = documentType;
	}
}
