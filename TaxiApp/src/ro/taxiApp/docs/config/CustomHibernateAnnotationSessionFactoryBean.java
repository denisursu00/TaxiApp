package ro.taxiApp.docs.config;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.google.common.collect.Sets;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.common.utils.SpringHibernateUtils;

/**
 * 
 */
public class CustomHibernateAnnotationSessionFactoryBean extends AnnotationSessionFactoryBean implements InitializingBean {
	
	private Set<String> basePackagesToScan = Sets.newHashSet();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			basePackagesToScan
		);
		super.afterPropertiesSet();
	}
	
	@Override
	protected void postProcessAnnotationConfiguration(AnnotationConfiguration configuration) throws HibernateException {
		SpringHibernateUtils.addAnnotatedClassesFromBasePackages(configuration, basePackagesToScan);	
		super.postProcessAnnotationConfiguration(configuration);
	}
	
	public void setBasePackagesToScan(Set<String> basePackagesToScan) {
		this.basePackagesToScan = basePackagesToScan;
	}
}