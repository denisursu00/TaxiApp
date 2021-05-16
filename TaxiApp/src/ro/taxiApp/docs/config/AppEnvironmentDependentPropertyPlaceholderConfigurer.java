package ro.taxiApp.docs.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.google.common.collect.Lists;

import ro.taxiApp.common.utils.DependencyInjectionUtils;

/**
 * Permite incarcarea proprietatilor
 * 
 * 
 */
public class AppEnvironmentDependentPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

	private Collection<String> commonPropertyFilePackagePaths;
	private Collection<String> environmentDependentPropertyFilePackagePaths;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			commonPropertyFilePackagePaths,
			environmentDependentPropertyFilePackagePaths
		);
	}
	
	@Override
	protected void loadProperties(Properties properties) throws IOException {
		
		List<String> propertyFilePackagePaths = Lists.newLinkedList();
		
		propertyFilePackagePaths.addAll(commonPropertyFilePackagePaths);
		
		for (String environmentDependentPropertyFilePackagePath : environmentDependentPropertyFilePackagePaths) {
			propertyFilePackagePaths.add(environmentDependentPropertyFilePackagePath);
		}

		for (String propertyFilePackagePath : propertyFilePackagePaths) {
			InputStream propertyFileAsStream = getClass().getResourceAsStream(propertyFilePackagePath);
			properties.load(propertyFileAsStream);
			propertyFileAsStream.close();
		}
	}
	
	public void setCommonPropertyFilePackagePaths(Collection<String> commonPropertyFilePackagePaths) {
		this.commonPropertyFilePackagePaths = commonPropertyFilePackagePaths;
	}
	public void setEnvironmentDependentPropertyFilePackagePaths(Collection<String> environmentDependentPropertyFilePackagePaths) {
		this.environmentDependentPropertyFilePackagePaths = environmentDependentPropertyFilePackagePaths;
	}
}