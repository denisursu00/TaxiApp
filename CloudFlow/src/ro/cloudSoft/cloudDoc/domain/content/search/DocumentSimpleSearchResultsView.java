package ro.cloudSoft.cloudDoc.domain.content.search;

public class DocumentSimpleSearchResultsView extends AbstractDocumentSearchResultsView {

	private String documentTypeName;
	
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
}