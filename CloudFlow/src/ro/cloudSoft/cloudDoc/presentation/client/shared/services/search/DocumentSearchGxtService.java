package ro.cloudSoft.cloudDoc.presentation.client.shared.services.search;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface DocumentSearchGxtService extends GxtServiceBase {
	
	public List<DocumentSimpleSearchResultsViewModel> findDocumentsUsingSimpleSearch(
		DocumentSearchCriteriaModel documentSearchCriteria) throws PresentationException;
		
	public DocumentAdvancedSearchResultsViewsWrapperModel findDocumentsUsingAdvancedSearch(
		DocumentSearchCriteriaModel documentSearchCriteria) throws PresentationException;

	public List<MyActivitiesViewModel> getMyActivites() throws PresentationException;
}