package ro.cloudSoft.cloudDoc.presentation.server.converters.content.search;

import ro.cloudSoft.cloudDoc.domain.content.search.AbstractDocumentSearchResultsView;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.AbstractDocumentSearchResultsViewModel;

public class AbstractDocumentSearchResultsViewConverter {

	public static void populateModel(AbstractDocumentSearchResultsView searchResultsView, AbstractDocumentSearchResultsViewModel searchResultsViewModel) {
		
		searchResultsViewModel.setDocumentId(searchResultsView.getDocumentId());
		searchResultsViewModel.setDocumentName(searchResultsView.getDocumentName());
		searchResultsViewModel.setDocumentCreatedDate(searchResultsView.getDocumentCreatedDate());
		searchResultsViewModel.setDocumentAuthorDisplayName(searchResultsView.getDocumentAuthorDisplayName());

		searchResultsViewModel.setWorkflowName(searchResultsView.getWorkflowName());
		searchResultsViewModel.setWorkflowCurrentStateName(searchResultsView.getWorkflowCurrentStateName());
		searchResultsViewModel.setWorkflowSenderDisplayName(searchResultsView.getWorkflowSenderDisplayName());
	}
}