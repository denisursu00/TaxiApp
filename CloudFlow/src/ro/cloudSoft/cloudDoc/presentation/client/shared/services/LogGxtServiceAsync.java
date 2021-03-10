package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogGxtServiceAsync extends GxtServiceBaseAsync {

	public void searchLog(PagingLoadConfig pagingLoadConfig, GwtLogEntrySearchCriteria searchCriteria, AsyncCallback<PagingLoadResult<LogEntryModel>> callback);
	
	public void logException(String exceptionDetails, AsyncCallback<Void> callback);
}