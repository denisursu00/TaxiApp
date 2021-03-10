package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.repositoryFactory;

import java.util.Map;
import java.util.Set;

import javax.jcr.Credentials;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironment;
import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Sets;

/**
 * "Fabrica" de repository JackRabbit in functie de mediul de rulare al aplicatiei
 * 
 * 
 */
public class AppEnvironmentDependentRepositoryFactoryBean extends AbstractRepositoryFactoryBean implements InitializingBean {
	
	private static final Set<String> SUFFIXES_FOR_APP_ENVIRONMENTS_WITH_EMBEDDED =
		Sets.newHashSet(
			AppEnvironment.DEVELOPMENT.getSuffix(),
			AppEnvironment.TESTING.getSuffix(),
			AppEnvironment.STAGING.getSuffix(),
			AppEnvironment.PRODUCTION.getSuffix()
		);
	private static final Set<String> SUFFIXES_FOR_APP_ENVIRONMENTS_WITH_REMOTE =
		Sets.newHashSet(
		);

	private Credentials credentials;	
	
	private String url;	

	private String configFilePath;
	private String repHomeDir;
	private Map<String, String> systemProperties;	
	
	private AbstractRepositoryFactoryBean repositoryFactoryBean;

	@Override
	public void afterPropertiesSet() throws Exception {
			
		DependencyInjectionUtils.checkRequiredDependencies(
			
			credentials,
			
			url,
			
			configFilePath,
			repHomeDir,
			systemProperties
		);
		
		String suffixForCurrentEnvironment = AppEnvironmentConfig.SUFFIX_FOR_CURRENT_ENVIRONMENT;
		
		if (SUFFIXES_FOR_APP_ENVIRONMENTS_WITH_EMBEDDED.contains(suffixForCurrentEnvironment)) {
			
			EmbeddedRepositoryFactoryBean embeddedRepositoryFactoryBean = new EmbeddedRepositoryFactoryBean();
			
			embeddedRepositoryFactoryBean.setCredentials(credentials);
			embeddedRepositoryFactoryBean.setConfigFilePath(configFilePath);
			embeddedRepositoryFactoryBean.setRepHomeDir(repHomeDir);
			embeddedRepositoryFactoryBean.setSystemProperties(systemProperties);
			
			repositoryFactoryBean = embeddedRepositoryFactoryBean;			
		} else if (SUFFIXES_FOR_APP_ENVIRONMENTS_WITH_REMOTE.contains(suffixForCurrentEnvironment)) {
			
			RemoteRepositoryFactoryBean remoteRepositoryFactoryBean = new RemoteRepositoryFactoryBean();
			
			remoteRepositoryFactoryBean.setCredentials(credentials);
			remoteRepositoryFactoryBean.setUrl(url);
			
			repositoryFactoryBean = remoteRepositoryFactoryBean;
		} else {
			throw new IllegalStateException("Nu exista un tip de repository pentru mediul cu sufixul [" + suffixForCurrentEnvironment + "].");
		}
		
		repositoryFactoryBean.afterPropertiesSet();
	}
	
	@Override
	public void destroy() throws Exception {
		repositoryFactoryBean.destroy();
	}
	
	@Override
	public Object getObject() throws Exception {
		return repositoryFactoryBean.getObject();
	}
	
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	public void setRepHomeDir(String repHomeDir) {
		this.repHomeDir = repHomeDir;
	}
	public void setSystemProperties(Map<String, String> systemProperties) {
		this.systemProperties = systemProperties;
	}
}