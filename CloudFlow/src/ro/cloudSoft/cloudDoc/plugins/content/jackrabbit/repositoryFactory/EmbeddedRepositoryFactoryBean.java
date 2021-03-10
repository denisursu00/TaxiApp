package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.repositoryFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;

/**
 * Factory bean ce creeaza un repository in momentul deploy-ului.
 * Cand se opreste aplicatia (sau server-ul), repository-ul se opreste in
 * mod corespunzator.
 */
public class EmbeddedRepositoryFactoryBean extends AbstractRepositoryFactoryBean {
	
	private static final String PACKAGE_PATH_CUSTOM_NODE_TYPES_CONFIG = "/ro/cloudSoft/cloudDoc/plugins/content/jackrabbit/config/customNodeTypes.cnd";

	private String configFilePath;
	private String repHomeDir;
	private Credentials credentials;
	private Map<String, String> systemProperties;
	
	private Repository repository;

	@Override
	public void afterPropertiesSet() throws Exception {
		registerSystemProperties();
		createRepository();
		registerCustomNodeTypes();
	}
	
	/**
	 * Integistreaza proprietatile necesare pt. JackRabbit la nivel de sistem (VM).
	 * Acest lucru este necesar pentru a putea folosi variabile in fisierul de
	 * configurare al repository-ului.
	 */
	private void registerSystemProperties() {
		for (Entry<String, String> property : systemProperties.entrySet()) {
			System.setProperty(property.getKey(), property.getValue());
		}
	}
	
	private void createRepository() throws RepositoryException {
		RepositoryConfig repositoryConfig = RepositoryConfig.create(configFilePath, repHomeDir);
		repository = RepositoryImpl.create(repositoryConfig);
	}
	
	private void registerCustomNodeTypes() throws RepositoryException, IOException, ParseException {
		Session session = null;
		try {
			session = repository.login(credentials);
			try (InputStream customNodeTypesCndStream = EmbeddedRepositoryFactoryBean.class.getResourceAsStream(PACKAGE_PATH_CUSTOM_NODE_TYPES_CONFIG);
					InputStreamReader customNodeTypesCndReader = new InputStreamReader(customNodeTypesCndStream);) {
				CndImporter.registerNodeTypes(customNodeTypesCndReader, session);
			}
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if (repository instanceof JackrabbitRepository) {
			((JackrabbitRepository) repository).shutdown();
        }		
	}

	@Override
	public Object getObject() throws Exception {
		return repository;
	}
	
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	public void setRepHomeDir(String repHomeDir) {
		this.repHomeDir = repHomeDir;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public void setSystemProperties(Map<String, String> systemProperties) {
		this.systemProperties = systemProperties;
	}
}