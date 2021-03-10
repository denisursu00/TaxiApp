package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuditGxtServiceAsync extends GxtServiceBaseAsync {

	void searchAuditEntries(PagingLoadConfig pagingLoadConfig, GwtAuditSearchCriteria searchCriteria,
		AsyncCallback<PagingLoadResult<AuditEntryModel>> callback);
}