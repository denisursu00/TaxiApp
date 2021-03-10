package ro.cloudSoft.cloudDoc.presentation.server.converters.content.search;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;

public class DocumentAdvancedSearchResultsViewsWrapperConverter {

	public static DocumentAdvancedSearchResultsViewsWrapperModel getModel(DocumentAdvancedSearchResultsViewsWrapper searchResultsViewsWrapper) {
		
		if (searchResultsViewsWrapper == null) {
			return null;
		}
		
		DocumentAdvancedSearchResultsViewsWrapperModel searchResultsViewsWrapperModel = new DocumentAdvancedSearchResultsViewsWrapperModel();
		
		searchResultsViewsWrapperModel.setRepresentativeMetadataDefinitionLabelById(searchResultsViewsWrapper.getRepresentativeMetadataDefinitionLabelById());
		
		List<DocumentAdvancedSearchResultsViewModel> searchResultsViewModels = DocumentAdvancedSearchResultsViewConverter.getModels(searchResultsViewsWrapper.getSearchResultsViews());
		searchResultsViewsWrapperModel.setSearchResultsViews(searchResultsViewModels);
		
		return searchResultsViewsWrapperModel;
	}
}