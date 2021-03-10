package ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface DocumentWorkflowHistoryGxtServiceAsync extends GxtServiceBaseAsync {
	
	 public  void getDocumentHistory(String documentId,AsyncCallback<List<DocumentHistoryViewModel> > callback); 
}