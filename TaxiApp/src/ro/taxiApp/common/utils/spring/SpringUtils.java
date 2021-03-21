package ro.taxiApp.common.utils.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * Contine metode utilitare pentru lucrul cu framework-ul Spring.
 * 
 * 
 */
public class SpringUtils {

	/**
	 * Returneaza bean-ul cu numele sau ID-ul dat.
	 * Daca nu se gaseste bean-ul, va arunca exceptie.
	 */
	public static <T> T getBean(String name) {
        ApplicationContext context = getApplicationContext();
    	@SuppressWarnings("unchecked")
        T bean = (T) context.getBean(name);
        return bean;
    }

	/**
	 * Returneaza bean-ul de tipul specificat. Se vor cauta si subclase ale tipului.
	 * Daca nu se gaseste nici un bean de acel tip sau daca se gasesc mai multe, va arunca exceptie.
	 */
	public static <T> T getBean(Class<T> beanClass) {
		
        ApplicationContext context = getApplicationContext();
        
        String[] beanNames = context.getBeanNamesForType(beanClass);
        if (beanNames.length == 0) {
        	throw new IllegalArgumentException("Nu exista bean de tipul [" + beanClass.getName() + "].");
        } else if (beanNames.length > 1) {
        	throw new IllegalArgumentException("Exista mai multe bean-uri de tipul [" + beanClass.getName() + "]: [" + StringUtils.join(beanNames, ", ") + "].");
        }
        
        // Exista un singur bean.
    	String beanName = beanNames[0];
    	
    	@SuppressWarnings("unchecked")
    	T bean = (T) context.getBean(beanName);
    	return bean;
	}
	
	private static ApplicationContext getApplicationContext() {
		return StaticSpringContextAccessorHelper.getApplicationContext();
	}
}