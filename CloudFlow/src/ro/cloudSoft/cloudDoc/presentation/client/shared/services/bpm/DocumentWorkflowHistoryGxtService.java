package ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface DocumentWorkflowHistoryGxtService extends GxtServiceBase {

	List<DocumentHistoryViewModel> getDocumentHistory(String documentId) throws PresentationException;
}