package ro.taxiApp.docs.config;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.google.common.collect.Sets;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.common.utils.SpringHibernateUtils;
import ro.taxiApp.docs.exporters.HibernateSchemaSqlExporter;

/**
 * 
 */
public class CustomHibernateAnnotationSessionFactoryBean extends AnnotationSessionFactoryBean implements InitializingBean {
	
	private Set<String> basePackagesToScan = Sets.newHashSet();
	private HibernateSchemaSqlExporter schemaSqlExporter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			basePackagesToScan,
			schemaSqlExporter
		);
		super.afterPropertiesSet();
	}
	
	@Override
	protected void postProcessAnnotationConfiguration(AnnotationConfiguration configuration) throws HibernateException {
		SpringHibernateUtils.addAnnotatedClassesFromBasePackages(configuration, basePackagesToScan);
		schemaSqlExporter.doExport(configuration);		
		super.postProcessAnnotationConfiguration(configuration);
	}
	
	public void setBasePackagesToScan(Set<String> basePackagesToScan) {
		this.basePackagesToScan = basePackagesToScan;
	}
	public void setSchemaSqlExporter(HibernateSchemaSqlExporter schemaSqlExporter) {
		this.schemaSqlExporter = schemaSqlExporter;
	}
}