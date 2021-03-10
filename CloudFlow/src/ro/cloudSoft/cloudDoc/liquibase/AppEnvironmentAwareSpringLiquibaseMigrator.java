package ro.cloudSoft.cloudDoc.liquibase;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class AppEnvironmentAwareSpringLiquibaseMigrator implements ResourceLoaderAware, InitializingBean {

	private ResourceLoader resourceLoader;
	private DataSource dataSource;
	private String changelogFilePath;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		DependencyInjectionUtils.checkRequiredDependencies(
			dataSource,
			changelogFilePath
		);
		
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setResourceLoader(resourceLoader);
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog(changelogFilePath);
		springLiquibase.setContexts(AppEnvironmentConfig.SUFFIX_FOR_CURRENT_ENVIRONMENT);
		
		springLiquibase.afterPropertiesSet();
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void setChangelogFilePath(String changelogFilePath) {
		this.changelogFilePath = changelogFilePath;
	}
}