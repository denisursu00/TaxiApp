package ro.cloudSoft.cloudDoc.services.log;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.common.utils.PagingList;

public interface LogService {
	
	PagingList<LogEntry> searchLog(int offset, int pageSize,
		LogEntrySearchCriteria searchCriteria) throws AppException;
}