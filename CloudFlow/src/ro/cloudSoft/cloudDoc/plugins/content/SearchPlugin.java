package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSelectionSearchResultView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilter;

public interface SearchPlugin {
	
	List<Document> findDocuments(DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity) throws AppException;
	
	List<DocumentSelectionSearchResultView> findDocumentsForSelection(DocumentSelectionSearchFilter filter, SecurityManager userSecurity) throws AppException;
}