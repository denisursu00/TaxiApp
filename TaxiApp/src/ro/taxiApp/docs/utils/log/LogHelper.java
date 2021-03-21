package ro.taxiApp.docs.utils.log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ro.taxiApp.docs.config.environment.AppEnvironmentConfig;
import ro.taxiApp.docs.domain.security.SecurityManager;

public class LogHelper implements Serializable {
	
    private static final long serialVersionUID = 1L;

	private static final String LOG_CONFIG_FILE = "conf/log4j.properties";
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	
	static {
		
        PropertyConfigurator.configure(Loader.getResource(AppEnvironmentConfig.getFilePackagePathWithSuffix(LOG_CONFIG_FILE)));
		Logger.getLogger(LogHelper.class).info("Log4j a fost initializat folosind fisierul de configurare custom.");
		
	}

	private final Class<?> componentClass;
	private final Logger logger;
	
	private LogHelper(Class<?> componentClass) {
		this.componentClass = componentClass;
		this.logger = Logger.getLogger(componentClass);
	}
	
	/**
	 * Initializeaza Log4j pentru a fi folosit si in afara acestei clase.
	 * Metoda trebuie apelata cat de devreme posibil pentru ca componentele care nu folosesc LogHelper sa poata folosi
	 * Log4j.
	 * Aceasta metoda nu face practic nimic, insa asigura ca s-a executat blocul de initializare statica pentru
	 * configurarea Log4j folosind fisierul de configurare custom, dependent de mediul de rulare al aplicatiei. 
	 */
	public static void initLog4j() {
		
		Logger.getLogger(LogHelper.class).info("A rulat metoda ajutatoare pentru initializarea Log4j.");

		/*
		 * Trebuie sa initializez si bridge-ul a.i. sa pot redirectiona alte loggere
		 * catre cel folosit in aplicatie (in special logger-ul inclus in Java).
		 */
		SLF4JBridgeHandler.install();
	}
	
	/** Opreste Log4j pentru aplicatie. */
	public static void shutdownLog4j() {

		/*
		 * Dezinstalez bridge-ul inainte sa opresc Log4j, altfel va da exceptie
		 * cand se va redirectiona un mesaj de la ul alt logger (cel inclus in Java).
		 */
		SLF4JBridgeHandler.uninstall();
		
		LogManager.shutdown();
	}
	
	public static synchronized LogHelper getInstance(Class<?> component) {
		return new LogHelper(component);
	}
	
	public static String formatDateForLog(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(FORMAT_DATE).format(date);
	}
	
	/**
	 * Returneaza reprezentarea aplicatiei ca un SecurityManager, pentru a fi folosita ca un utilizator.
	 */
	private static SecurityManager getApplicationRepresentationAsSecurityManager() {
		return null;
	}
	
	private String getComponentClassRepresentationAsModule() {
		return componentClass.getName();
	}
	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	// >> LOGARE SPECIFICAND NIVELUL
	
	private LogMessage getLogMessage(String message, String module, String operation, SecurityManager userSecurity) {
		return new LogMessage(userSecurity, message, module, operation);
	}
	
	private void log(Level level, String message, Throwable exception, String module, String operation, SecurityManager userSecurity) {
		LogMessage logMessage = getLogMessage(message, module, operation, userSecurity);
		logger.log(level, logMessage, exception);
	}
	
	private void log(Level level, String message, String module, String operation, SecurityManager userSecurity) {
		LogMessage logMessage = getLogMessage(message, module, operation, userSecurity);
		logger.log(level, logMessage);
	}

	private void log(Level level, String message, Throwable exception, String operation, SecurityManager userSecurity) {
		log(level, message, exception, getComponentClassRepresentationAsModule(), operation, userSecurity);
	}
	
	private void log(Level level, String message, String operation, SecurityManager userSecurity) {
		log(level, message, getComponentClassRepresentationAsModule(), operation, userSecurity);
	}
	
	private void log(Level level, String message, Throwable exception, String module, String operation) {
		log(level, message, exception, module, operation, getApplicationRepresentationAsSecurityManager());
	}
	
	private void log(Level level, String message, String module, String operation) {
		log(level, message, module, operation, getApplicationRepresentationAsSecurityManager());
	}

	private void log(Level level, String message, Throwable exception, String operation) {
		log(level, message, exception, operation, getApplicationRepresentationAsSecurityManager());
	}
	
	private void log(Level level, String message, String operation) {
		log(level, message, operation, getApplicationRepresentationAsSecurityManager());
	}

	// << LOGARE SPECIFICAND NIVELUL
	
	// >> DEBUG
	
	public void debug(String message, Throwable exception, String module, String operation, SecurityManager userSecurity) {
		log(Level.DEBUG, message, exception, module, operation, userSecurity);
	}
	
	public void debug(String message, String module, String operation, SecurityManager userSecurity) {
		log(Level.DEBUG, message, module, operation, userSecurity);
	}

	public void debug(String message, Throwable exception, String operation, SecurityManager userSecurity) {
		log(Level.DEBUG, message, exception, operation, userSecurity);
	}
	
	public void debug(String message, String operation, SecurityManager userSecurity) {
		log(Level.DEBUG, message, operation, userSecurity);
	}
	
	public void debug(String message, Throwable exception, String module, String operation) {
		log(Level.DEBUG, message, exception, module, operation);
	}
	
	public void debug(String message, String module, String operation) {
		log(Level.DEBUG, message, module, operation);
	}

	public void debug(String message, Throwable exception, String operation) {
		log(Level.DEBUG, message, exception, operation);
	}
	
	public void debug(String message, String operation) {
		log(Level.DEBUG, message, operation);
	}

	// << DEBUG
	
	// >> INFO
	
	public void info(String message) {
		logger.info(message);
	}
	
	public void info(String message, Throwable exception, String module, String operation, SecurityManager userSecurity) {
		log(Level.INFO, message, exception, module, operation, userSecurity);
	}
	
	public void info(String message, String module, String operation, SecurityManager userSecurity) {
		log(Level.INFO, message, module, operation, userSecurity);
	}

	public void info(String message, Throwable exception, String operation, SecurityManager userSecurity) {
		log(Level.INFO, message, exception, operation, userSecurity);
	}
	
	public void info(String message, String operation, SecurityManager userSecurity) {
		log(Level.INFO, message, operation, userSecurity);
	}
	
	public void info(String message, Throwable exception, String module, String operation) {
		log(Level.INFO, message, exception, module, operation);
	}
	
	public void info(String message, String module, String operation) {
		log(Level.INFO, message, module, operation);
	}

	public void info(String message, Throwable exception, String operation) {
		log(Level.INFO, message, exception, operation);
	}
	
	public void info(String message, String operation) {
		log(Level.INFO, message, operation);
	}

	// << INFO
	
	// >> WARN
	
	public void warn(String message, Throwable exception, String module, String operation, SecurityManager userSecurity) {
		log(Level.WARN, message, exception, module, operation, userSecurity);
	}
	
	public void warn(String message, String module, String operation, SecurityManager userSecurity) {
		log(Level.WARN, message, module, operation, userSecurity);
	}

	public void warn(String message, Throwable exception, String operation, SecurityManager userSecurity) {
		log(Level.WARN, message, exception, operation, userSecurity);
	}
	
	public void warn(String message, String operation, SecurityManager userSecurity) {
		log(Level.WARN, message, operation, userSecurity);
	}
	
	public void warn(String message, Throwable exception, String module, String operation) {
		log(Level.WARN, message, exception, module, operation);
	}
	
	public void warn(String message, String module, String operation) {
		log(Level.WARN, message, module, operation);
	}

	public void warn(String message, Throwable exception, String operation) {
		log(Level.WARN, message, exception, operation);
	}
	
	public void warn(String message, String operation) {
		log(Level.WARN, message, operation);
	}

	// << WARN
	
	// >> ERROR
	
	public void error(String message, Throwable exception, String module, String operation, SecurityManager userSecurity) {
		log(Level.ERROR, message, exception, module, operation, userSecurity);
	}
	
	public void error(String message, String module, String operation, SecurityManager userSecurity) {
		log(Level.ERROR, message, module, operation, userSecurity);
	}

	public void error(String message, Throwable exception, String operation, SecurityManager userSecurity) {
		log(Level.ERROR, message, exception, operation, userSecurity);
	}
	
	public void error(String message, String operation, SecurityManager userSecurity) {
		log(Level.ERROR, message, operation, userSecurity);
	}
	
	public void error(String message, Throwable exception, String module, String operation) {
		log(Level.ERROR, message, exception, module, operation);
	}
	
	public void error(String message, String module, String operation) {
		log(Level.ERROR, message, module, operation);
	}

	public void error(String message, Throwable exception, String operation) {
		log(Level.ERROR, message, exception, operation);
	}
	
	public void error(String message, String operation) {
		log(Level.ERROR, message, operation);
	}
	
	// << ERROR
}