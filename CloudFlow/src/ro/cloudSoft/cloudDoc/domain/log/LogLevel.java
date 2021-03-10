package ro.cloudSoft.cloudDoc.domain.log;

public enum LogLevel {

	DEBUG, INFO, WARN, ERROR;
	
	public static LogLevel ofLogFrameworkLevel(String logFrameworkLevel) {
		return valueOf(logFrameworkLevel);
	}
}