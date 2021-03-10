package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public interface LogGxtService extends GxtServiceBase {
	
	PagingLoadResult<LogEntryModel> searchLog(PagingLoadConfig pagingLoadConfig, GwtLogEntrySearchCriteria searchCriteria) throws PresentationException;
	
	void logException(String exceptionDetails);
}