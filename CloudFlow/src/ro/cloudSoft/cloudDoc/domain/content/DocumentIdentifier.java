package ro.cloudSoft.cloudDoc.domain.content;

import com.google.common.base.Objects;

/**
 * Contine proprietatile necesare pentru identificarea unui document.
 * 
 * 
 */
public class DocumentIdentifier {

	private final String documentLocationRealName;
	private final String documentId;
	
	public DocumentIdentifier(String documentLocationRealName, String documentId) {
		this.documentLocationRealName = documentLocationRealName;
		this.documentId = documentId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof DocumentIdentifier)) {
			return false;
		}
		
		DocumentIdentifier other = (DocumentIdentifier) obj;
		
		return (
			Objects.equal(getDocumentLocationRealName(), other.getDocumentLocationRealName()) &&
			Objects.equal(getDocumentId(), other.getDocumentId())
		);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(
			getDocumentLocationRealName(),
			getDocumentId()
		);
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public String getDocumentId() {
		return documentId;
	}
}