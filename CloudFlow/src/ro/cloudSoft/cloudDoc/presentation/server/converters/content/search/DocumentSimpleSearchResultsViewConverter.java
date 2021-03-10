package ro.cloudSoft.cloudDoc.presentation.server.converters.content.search;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;

import com.google.common.collect.Lists;

public class DocumentSimpleSearchResultsViewConverter {

	public static DocumentSimpleSearchResultsViewModel getModel(DocumentSimpleSearchResultsView searchResultsView) {
		
		if (searchResultsView == null) {
			return null;
		}
		
		DocumentSimpleSearchResultsViewModel searchResultsViewModel = new DocumentSimpleSearchResultsViewModel();
		
		AbstractDocumentSearchResultsViewConverter.populateModel(searchResultsView, searchResultsViewModel);
		
		searchResultsViewModel.setDocumentTypeName(searchResultsView.getDocumentTypeName());
		
		return searchResultsViewModel;
	}
	
	public static List<DocumentSimpleSearchResultsViewModel> getModels(Collection<DocumentSimpleSearchResultsView> searchResultsViews) {
		List<DocumentSimpleSearchResultsViewModel> searchResultsViewModels = Lists.newArrayListWithCapacity(searchResultsViews.size());
		for (DocumentSimpleSearchResultsView searchResultsView : searchResultsViews) {
			DocumentSimpleSearchResultsViewModel searchResultsViewModel = getModel(searchResultsView);
			searchResultsViewModels.add(searchResultsViewModel);
		}
		return searchResultsViewModels;
	}
}