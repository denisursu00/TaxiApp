package ro.cloudSoft.cloudDoc.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSelectionSearchResultViewModel;

public interface SearchService {
	
	List<Document> findDocuments(DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity) throws AppException;
	
	List<DocumentSimpleSearchResultsView> findDocumentsUsingSimpleSearch(
		DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity)
		throws AppException;
	
	DocumentAdvancedSearchResultsViewsWrapper findDocumentsUsingAdvancedSearch(
		DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity)
		throws AppException;
		
	List<DocumentSelectionSearchResultViewModel> findDocumentsForSelection(DocumentSelectionSearchFilterModel filterModel, SecurityManager userSecurity) throws AppException;
}