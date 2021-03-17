package ro.taxiApp.common.utils;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.google.common.collect.ImmutableSet;

/**
 * 
 */
public class SpringHibernateUtils {
	
	/** Tipurile de adnotari JPA & Hibernate folosite de Hibernate pentru a declara o entitate */
	private static final Set<TypeFilter> TYPE_FILTERS_HIBERNATE_ENTITY_ANNOTATIONS = ImmutableSet.<TypeFilter> of(
		new AnnotationTypeFilter(Entity.class, false),
		new AnnotationTypeFilter(Embeddable.class, false),
		new AnnotationTypeFilter(MappedSuperclass.class, false)
	);

	/**
	 * Adauga la configuratia Hibernate toate clasele adnotate corespunzator
	 * care se gasesc in pachetul specificat sau in subpachetele sale.
	 */
	public static void addAnnotatedClassesFromBasePackages(AnnotationConfiguration configuration, Collection<String> basePackagesToScan) {
		
		ClassPathScanningCandidateComponentProvider annotatedEntitiesScanner = new ClassPathScanningCandidateComponentProvider(false) {
			@Override
			protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
				
				// Implicit, metoda exclude clase abstracte, insa eu am nevoie si de ele.
				if (beanDefinition.getMetadata().isAbstract() && beanDefinition.getMetadata().isIndependent()) {
					return true;
				}
				
				return super.isCandidateComponent(beanDefinition);
			}
		};
		
		for (TypeFilter hibernateEntityAnnotation : TYPE_FILTERS_HIBERNATE_ENTITY_ANNOTATIONS) {
			annotatedEntitiesScanner.addIncludeFilter(hibernateEntityAnnotation);
		}
		
		for (String basePackageToScan : basePackagesToScan) {
			Set<BeanDefinition> beanDefinitions = annotatedEntitiesScanner.findCandidateComponents(basePackageToScan);			
			for (BeanDefinition beanDefinition : beanDefinitions) {				
				String className = beanDefinition.getBeanClassName();				
				try {
					Class<?> theClass = Class.forName(className);
					configuration.addAnnotatedClass(theClass);
				} catch (ClassNotFoundException cnfe) {
					throw new IllegalArgumentException("Nu exista clasa [" + className + "].");
				}
			}
		}
	}
}