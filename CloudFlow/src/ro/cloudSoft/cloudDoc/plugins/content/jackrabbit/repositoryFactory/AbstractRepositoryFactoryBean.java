package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.repositoryFactory;

import javax.jcr.Repository;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Clasa de baza ce contine interfetele comune necesare "fabricilor" de repository JackRabbit
 * 
 * 
 */
public abstract class AbstractRepositoryFactoryBean implements InitializingBean, DisposableBean, FactoryBean {
	
	@Override
	public Class<?> getObjectType() {
		return Repository.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
}