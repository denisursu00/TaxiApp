package ro.cloudSoft.cloudDoc.presentation.server.converters.content.search;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsView;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewModel;

import com.google.common.collect.Lists;

public class DocumentAdvancedSearchResultsViewConverter {

	public static List<DocumentAdvancedSearchResultsViewModel> getModels(Collection<DocumentAdvancedSearchResultsView> searchResultsViews) {
		List<DocumentAdvancedSearchResultsViewModel> searchResultsViewModels = Lists.newArrayListWithCapacity(searchResultsViews.size());
		for (DocumentAdvancedSearchResultsView searchResultsView : searchResultsViews) {
			DocumentAdvancedSearchResultsViewModel searchResultsViewModel = getModel(searchResultsView);
			searchResultsViewModels.add(searchResultsViewModel);
		}
		return searchResultsViewModels;
	}
	
	public static DocumentAdvancedSearchResultsViewModel getModel(DocumentAdvancedSearchResultsView searchResultsView) {
		
		if (searchResultsView == null) {
			return null;
		}
		
		DocumentAdvancedSearchResultsViewModel searchResultsViewModel = new DocumentAdvancedSearchResultsViewModel();
		
		AbstractDocumentSearchResultsViewConverter.populateModel(searchResultsView, searchResultsViewModel);
		
		searchResultsViewModel.setDocumentMetadataInstanceDisplayValueByDefinitionId(searchResultsView.getDocumentMetadataInstanceDisplayValueByDefinitionId());
		
		return searchResultsViewModel;
	}
}