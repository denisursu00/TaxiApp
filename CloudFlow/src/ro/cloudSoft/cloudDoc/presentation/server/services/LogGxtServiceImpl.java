package ro.cloudSoft.cloudDoc.presentation.server.services;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.LogGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.log.LogEntryConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.log.LogEntrySearchCriteriaConverter;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.log.LogService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class LogGxtServiceImpl extends GxtServiceImplBase implements LogGxtService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(LogGxtServiceImpl.class);
	
	private LogService logService;
	
	public LogGxtServiceImpl() {
		super();
	}
	
	public LogGxtServiceImpl(LogService logSvc) {
		super();
		logService = logSvc;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			logService
		);
	}
	
	@Override
	public PagingLoadResult<LogEntryModel> searchLog(PagingLoadConfig pagingLoadConfig, GwtLogEntrySearchCriteria gwtSearchCriteria) throws PresentationException {
		LogEntrySearchCriteria searchCriteria = LogEntrySearchCriteriaConverter.getFromGwt(gwtSearchCriteria);
		PagingList<LogEntry> logEntriesPagingList = null;
		try {
			logEntriesPagingList = logService.searchLog(pagingLoadConfig.getOffset(), pagingLoadConfig.getLimit(), searchCriteria);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		List<LogEntryModel> logEntryModels = LogEntryConverter.getModelsFromLogList(logEntriesPagingList.getElements());
		return new BasePagingLoadResult<LogEntryModel>(logEntryModels, logEntriesPagingList.getOffset(), logEntriesPagingList.getTotalCount());
	}
	
	@Override
	public void logException(String exceptionDetails) {
		String logMessage = "A aparut o exceptie in interfata grafica: " + exceptionDetails;
		LOGGER.error(logMessage, "interfata grafica", "N/A", getSecurity());
	}
}