package ro.cloudSoft.cloudDoc.presentation.client.shared.services.search;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentSearchGxtServiceAsync extends GxtServiceBaseAsync {
	
	void findDocumentsUsingSimpleSearch(DocumentSearchCriteriaModel documentSearchCriteria,
		AsyncCallback<List<DocumentSimpleSearchResultsViewModel>> callback);
		
	void findDocumentsUsingAdvancedSearch(DocumentSearchCriteriaModel documentSearchCriteria,
		AsyncCallback<DocumentAdvancedSearchResultsViewsWrapperModel> callback);
	
	void getMyActivites(AsyncCallback<List<MyActivitiesViewModel>> callback);
}