package ro.cloudSoft.cloudDoc.dao.log;

import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.common.utils.PagingList;

public interface LogDao {

	PagingList<LogEntry> searchLog(int offset, int pageSize, LogEntrySearchCriteria searchCriteria);
}