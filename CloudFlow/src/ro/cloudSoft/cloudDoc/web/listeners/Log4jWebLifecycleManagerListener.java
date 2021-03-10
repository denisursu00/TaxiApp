package ro.cloudSoft.cloudDoc.web.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

/**
 * Gestioneaza Log4j pentru aplicatia web curenta.
 * <br><br>
 * NOTA: Acest listener trebuie sa fie primul declarat in "web.xml" pentru ca logger-ele initializate corect sa fie 
 * disponibile celorlalte componente ale aplicatiei. De asemenea, fiind primul listener declarat, Log4j se va opri
 * dupa ce s-au oprit celelalte componente.
 * 
 * 
 */
public class Log4jWebLifecycleManagerListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LogHelper.initLog4j();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LogHelper.shutdownLog4j();
	}
}