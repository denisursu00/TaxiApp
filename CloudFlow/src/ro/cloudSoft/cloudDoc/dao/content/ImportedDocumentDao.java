package ro.cloudSoft.cloudDoc.dao.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.ImportedDocument;

public interface ImportedDocumentDao {
	
	void save(ImportedDocument importedDocument);
	
	boolean isDocumentImported(DocumentIdentifier documentIdentifier);
	
	List<String> getDocumentIdsByDocumentLocationRealName(String documentLocationRealName);
}
