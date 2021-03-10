package ro.cloudSoft.cloudDoc.log;

import java.beans.PropertyVetoException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import ro.cloudSoft.cloudDoc.domain.log.LogActorType;
import ro.cloudSoft.cloudDoc.domain.log.LogLevel;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.utils.log.LogMessage;
import ro.cloudSoft.common.utils.db.jdbc.JdbcUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Appender pt. Log4j folosind o baza de date
 * 
 * 
 */
public class DatabaseLog4jAppender extends AppenderSkeleton {
	
	/** Lungimea maxima admisa pentru coloana corespunzatoare mesajului de log */
	private static final int MAX_LENGTH_MESSAGE = 4000;
	
	private static String ACTOR_DISPLAY_NAME_APPLICATION = "(aplicatie)";
	
	private static final String SQL_QUERY_INSERT_LOG_ENTRY =
		"INSERT INTO log_entries (" +
		"	id, log_time, log_level, module, operation, actor_type, actor_display_name, user_id, message, exception" +
		") VALUES (" +
		"	nextval('LOGS_SEQUENCE'), ?, ?, ?, ?, ?, ?, ?, ?, ?" +
		")";
	
	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String dbUsername;
	private String dbPassword;
	
	private int initialDbConnectionsCount;
	private int minimumDbConnectionsCount;
	private int maximumDbConnectionsCount;
	
	private int idleDbConnectionTestPeriodInSeconds;
	private String dbConnectionTestQuery;
	
	private ComboPooledDataSource dataSource;
	
	@Override
	public void activateOptions() {
		
		dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass(jdbcDriverClassName);
		} catch (PropertyVetoException pve) {
			throw new RuntimeException(pve);
		}
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(dbUsername);
		dataSource.setPassword(dbPassword);
		
		dataSource.setInitialPoolSize(initialDbConnectionsCount);
		dataSource.setMinPoolSize(minimumDbConnectionsCount);
		dataSource.setMaxPoolSize(maximumDbConnectionsCount);
		
		dataSource.setIdleConnectionTestPeriod(idleDbConnectionTestPeriodInSeconds);
		dataSource.setPreferredTestQuery(dbConnectionTestQuery);
	}
	
	/**
	 * Se asigura ca mesajul nu depaseste lungimea maxima admisa.
	 * Daca mesajul depaseste lungimea maxima admisa, va fi trunchiat.
	 */
	private String normalizeMessage(String message) {
		return StringUtils.abbreviate(message, MAX_LENGTH_MESSAGE);
	}
	
	/** Completeaza interogarea asociata statement-ului cu parametri specifici unui mesaj log de aplicatie. */
	private void setParametersForAppSpecificMessage(PreparedStatement statement, LoggingEvent loggingEvent) throws SQLException {
		
		LogMessage appSpecificLogMessage = (LogMessage) loggingEvent.getMessage();
		
		Timestamp logTime = new Timestamp(System.currentTimeMillis());
		statement.setTimestamp(1, logTime);
		
		String logLevelFromLogFramework = loggingEvent.getLevel().toString();
		LogLevel logLevel = LogLevel.ofLogFrameworkLevel(logLevelFromLogFramework);
		statement.setString(2, logLevel.name());
		
		String module = loggingEvent.getLoggerName();
		if (appSpecificLogMessage.getModule() != null) {
			module = appSpecificLogMessage.getModule();
		}
		statement.setString(3, module);
		
		if (appSpecificLogMessage.getOperation() != null) {
			statement.setString(4, appSpecificLogMessage.getOperation());
		} else {
			statement.setNull(4, Types.VARCHAR);
		}
		
		LogActorType actorType = null;
		String actorDisplayName = null;
		Long userId = null;
		
		if (appSpecificLogMessage.getSecurityManager() != null) {
			actorType = LogActorType.USER;
			actorDisplayName = (appSpecificLogMessage.getSecurityManager().getDisplayName());
			userId = appSpecificLogMessage.getSecurityManager().getUserId();
		} else {
			actorType = LogActorType.APPLICATION;
			actorDisplayName = ACTOR_DISPLAY_NAME_APPLICATION;
		}
		
		statement.setString(5, actorType.name());
		statement.setString(6, actorDisplayName);
		if (userId != null) {
			statement.setLong(7, userId);
		} else {
			statement.setNull(7, Types.NUMERIC);
		}
		
		String message = appSpecificLogMessage.getMessage();
		setMessageToStatement(statement, 8, message);

		setExceptionToStatement(statement, 9, loggingEvent);
	}
	
	/** Completeaza interogarea asociata statement-ului cu parametri pentru un mesaj de log generic. */
	private void setParametersForOtherMessages(PreparedStatement statement, LoggingEvent loggingEvent) throws SQLException {

		Timestamp logTime = new Timestamp(System.currentTimeMillis());
		statement.setTimestamp(1, logTime);
		
		String logLevelFromLogFramework = loggingEvent.getLevel().toString();
		LogLevel logLevel = LogLevel.ofLogFrameworkLevel(logLevelFromLogFramework);
		statement.setString(2, logLevel.name());
		
		String module = loggingEvent.getLoggerName();
		statement.setString(3, module);
		
		statement.setNull(4, Types.VARCHAR);
		
		LogActorType actorType = LogActorType.APPLICATION;
		statement.setString(5, actorType.name());
		
		String actorDisplayName = ACTOR_DISPLAY_NAME_APPLICATION;
		statement.setString(6, actorDisplayName);

		statement.setNull(7, Types.NUMERIC);
		
		String message = loggingEvent.getMessage().toString();
		setMessageToStatement(statement, 8, message);
		
		setExceptionToStatement(statement, 9, loggingEvent);
	}
	
	private void setMessageToStatement(PreparedStatement statement, int messageIndex, String message) throws SQLException {
		String normalizedMessage = normalizeMessage(message);
		statement.setString(messageIndex, normalizedMessage);
	}
	
	private void setExceptionToStatement(PreparedStatement statement, int exceptionIndex, LoggingEvent loggingEvent) throws SQLException {
		if (loggingEvent.getThrowableInformation() != null) {			
			Throwable exception = loggingEvent.getThrowableInformation().getThrowable();
			String exceptionWithStackTraceAsString = ExceptionUtils.getFullStackTrace(exception);			
			statement.setCharacterStream(exceptionIndex, new StringReader(exceptionWithStackTraceAsString), exceptionWithStackTraceAsString.length());
		} else {
			statement.setNull(exceptionIndex, Types.CLOB);
		}
	}

	@Override
	protected synchronized void append(LoggingEvent loggingEvent) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(SQL_QUERY_INSERT_LOG_ENTRY);
			
			if (loggingEvent.getMessage() instanceof LogMessage) {
				setParametersForAppSpecificMessage(statement, loggingEvent);				
			} else {
				setParametersForOtherMessages(statement, loggingEvent);
			}
			
			statement.executeUpdate();
		} catch (SQLException sqle) {
			LogLog.error("Nu s-a putut insera un mesaj de log.", sqle);
		} finally {
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(connection);
		}
	}
	
	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void close() {
		dataSource.close();
		closed = true;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	
	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public void setInitialDbConnectionsCount(int initialDbConnectionsCount) {
		this.initialDbConnectionsCount = initialDbConnectionsCount;
	}
	public void setMinimumDbConnectionsCount(int minimumDbConnectionsCount) {
		this.minimumDbConnectionsCount = minimumDbConnectionsCount;
	}
	public void setMaximumDbConnectionsCount(int maximumDbConnectionsCount) {
		this.maximumDbConnectionsCount = maximumDbConnectionsCount;
	}
	public void setIdleDbConnectionTestPeriodInSeconds(int idleDbConnectionTestPeriodInSeconds) {
		this.idleDbConnectionTestPeriodInSeconds = idleDbConnectionTestPeriodInSeconds;
	}
	public void setDbConnectionTestQuery(String dbConnectionTestQuery) {
		this.dbConnectionTestQuery = dbConnectionTestQuery;
	}
}