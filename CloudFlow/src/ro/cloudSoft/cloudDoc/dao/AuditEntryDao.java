package ro.cloudSoft.cloudDoc.dao;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntry;
import ro.cloudSoft.cloudDoc.domain.audit.AuditSearchCriteria;
import ro.cloudSoft.common.utils.PagingList;

public interface AuditEntryDao {

	void save(AuditEntry auditEntry);
	
	PagingList<AuditEntry> searchAuditEntries(int offset, int pageSize, AuditSearchCriteria searchCriteria);
}