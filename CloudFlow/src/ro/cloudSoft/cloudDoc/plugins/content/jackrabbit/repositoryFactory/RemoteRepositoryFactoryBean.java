package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.repositoryFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

public class RemoteRepositoryFactoryBean extends AbstractRepositoryFactoryBean {
	
	private static final String PACKAGE_PATH_CUSTOM_NODE_TYPES_CONFIG = "/ro/cloudSoft/cloudDoc/plugins/content/jackrabbit/config/customNodeTypes.xml";

	private String url;
	private Credentials credentials;
	
	private Repository repository;

	@Override
	public void afterPropertiesSet() throws Exception {
		createRepository();
		registerCustomNodeTypes();
	}
	
	private void createRepository() throws RepositoryException, MalformedURLException {
		if (true) {
			throw new RuntimeException("Acest tip de repository trebuie revizuit pentru a se folosi");
		}
		//repository = new URLRemoteRepository(url);
	}
	
	private void registerCustomNodeTypes() throws RepositoryException, IOException {
		
		Session session = null;
        try {
            session = repository.login(credentials);
            Workspace workspace = session.getWorkspace();
            JackrabbitNodeTypeManager nodeTypeManager = (JackrabbitNodeTypeManager) workspace.getNodeTypeManager();
            // Verifica daca s-au inregistrat nodurile deja.
            if (!nodeTypeManager.hasNodeType(JackRabbitConstants.DOCUMENT_NODE_TYPE)) {
                /*
                 * Daca nu s-au inregistrat deja, se citeste fisierul din
                 * proiect si se inregistreaza nodurile custom.
                 */
                InputStream customNodeTypesXml = EmbeddedRepositoryFactoryBean.class.getResourceAsStream(PACKAGE_PATH_CUSTOM_NODE_TYPES_CONFIG);
                nodeTypeManager.registerNodeTypes(customNodeTypesXml, NodeTypeManagerImpl.TEXT_XML);
                customNodeTypesXml.close();
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
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
}