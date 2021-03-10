package ro.cloudSoft.cloudDoc.services.log;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.log.LogDao;
import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

public class LogServiceImpl implements LogService, InitializingBean {
	
	private LogDao logDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			logDao
		);
	}

	@Override
	public PagingList<LogEntry> searchLog(int offset, int pageSize,
			LogEntrySearchCriteria searchCriteria) throws AppException {
		
		return logDao.searchLog(offset, pageSize, searchCriteria);
	}
	
	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
}