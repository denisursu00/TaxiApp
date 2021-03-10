package ro.cloudSoft.cloudDoc.presentation.server.converters.log;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;

public class LogEntryConverter {

	public static LogEntryModel getModelFromLogEntry(LogEntry logEntry) {
    	
		LogEntryModel logEntryModel = new LogEntryModel();
		
		logEntryModel.setId(logEntry.getId());
		
		logEntryModel.setTime(logEntry.getTime());
		logEntryModel.setLevel(LogLevelConverter.getForGwt(logEntry.getLevel()));
		
		logEntryModel.setModule(logEntry.getModule());
		logEntryModel.setOperation(logEntry.getOperation());
		
		logEntryModel.setActorType(LogActorTypeConverter.getForGwt(logEntry.getActorType()));
		logEntryModel.setActorDisplayName(logEntry.getActorDisplayName());
		logEntryModel.setUserId(logEntry.getUserId());
		
		logEntryModel.setMessage(logEntry.getMessage());
		logEntryModel.setException(logEntry.getException());
		
		return logEntryModel;
    }

    public static List<LogEntryModel> getModelsFromLogList(List<LogEntry> leList){
    	List<LogEntryModel> models = new ArrayList<LogEntryModel>();
    	for (LogEntry le : leList){
    		LogEntryModel model = getModelFromLogEntry(le);
    		models.add(model);
    	}
    	return models;
    }   
}