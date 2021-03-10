package ro.cloudSoft.cloudDoc.plugins.content;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class JR_PluginBase implements InitializingBean {

    protected SimpleCredentials credentials;
    protected Repository repository;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			credentials,
			repository
		);
	}

    public SimpleCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(SimpleCredentials credentials) {
        this.credentials = credentials;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}