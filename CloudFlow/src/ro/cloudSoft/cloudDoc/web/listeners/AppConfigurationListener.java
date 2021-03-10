package ro.cloudSoft.cloudDoc.web.listeners;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.ApplicationVersionHolder;

public class AppConfigurationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		try {
			prepareApplicationVersion(sce);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Nothing.
	}
	
	private void prepareApplicationVersion(ServletContextEvent sce) throws Exception {
		String relativeVersionFile = "/version.txt";
		InputStream versionIs = sce.getServletContext().getResourceAsStream(relativeVersionFile);
		if (versionIs == null) {
			return;
		}
		byte[] versionAsBytes =  IOUtils.toByteArray(versionIs);
		if (versionAsBytes != null && versionAsBytes.length > 0) {
			String version = new String(versionAsBytes, "UTF-8");
			if (StringUtils.isNotBlank(version)) {
				ApplicationVersionHolder.setVersion(version.trim());
			}				
		}
	}
}
