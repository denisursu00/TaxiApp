package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public interface AuditGxtService extends GxtServiceBase {

	PagingLoadResult<AuditEntryModel> searchAuditEntries(PagingLoadConfig pagingLoadConfig,
		GwtAuditSearchCriteria searchCriteria) throws PresentationException;
}