package ro.cloudSoft.cloudDoc.presentation.server.converters.log;

import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogEntrySearchCriteria;

public class LogEntrySearchCriteriaConverter {

	public static LogEntrySearchCriteria getFromGwt(GwtLogEntrySearchCriteria gwtSearchCriteria) {
		
		LogEntrySearchCriteria searchCriteria = new LogEntrySearchCriteria();
		
		searchCriteria.setLevel(LogLevelConverter.getFromGwt(gwtSearchCriteria.getLevel()));
		
		searchCriteria.setTimeStartDate(gwtSearchCriteria.getTimeStartDate());
		searchCriteria.setTimeEndDate(gwtSearchCriteria.getTimeEndDate());
		
		searchCriteria.setModuleText(gwtSearchCriteria.getModuleText());
		searchCriteria.setOperationText(gwtSearchCriteria.getOperationText());
		
		searchCriteria.setActorType(LogActorTypeConverter.getFromGwt(gwtSearchCriteria.getActorType()));
		searchCriteria.setActorDisplayNameText(gwtSearchCriteria.getActorDisplayNameText());
		searchCriteria.setUserId(gwtSearchCriteria.getUserId());
		
		searchCriteria.setMessageText(gwtSearchCriteria.getMessageText());
		searchCriteria.setOperationText(gwtSearchCriteria.getOperationText());
		
		return searchCriteria;
	}
}