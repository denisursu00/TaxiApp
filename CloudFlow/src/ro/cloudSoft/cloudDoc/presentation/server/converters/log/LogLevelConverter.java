package ro.cloudSoft.cloudDoc.presentation.server.converters.log;

import ro.cloudSoft.cloudDoc.domain.log.LogLevel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogLevel;

public class LogLevelConverter {

	public static LogLevel getFromGwt(GwtLogLevel gwtLogLevel) {
		
		if (gwtLogLevel == null) {
			return null;
		}
		
		String gwtLogLevelName = gwtLogLevel.name();
		return LogLevel.valueOf(gwtLogLevelName);
	}
	
	public static GwtLogLevel getForGwt(LogLevel logLevel) {
		
		if (logLevel == null) {
			return null;
		}
		
		String logLevelName = logLevel.name();
		return GwtLogLevel.valueOf(logLevelName);
	}
}