package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search;

public class DocumentSimpleSearchResultsViewModel extends AbstractDocumentSearchResultsViewModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_DOCUMENT_TYPE_NAME = "documentTypeName";
	
	public String getDocumentTypeName() {
		return get(PROPERTY_DOCUMENT_TYPE_NAME);
	}
	public void setDocumentTypeName(String documentTypeName) {
		set(PROPERTY_DOCUMENT_TYPE_NAME, documentTypeName);
	}
}