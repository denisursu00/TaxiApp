package ro.taxiApp.common.utils.hibernate;

import org.hibernate.Hibernate;

public class HibernateEntityUtils {

	public static boolean isOfType(Object entity, Class<?> type) {
		
		if (entity == null) {
			return false;
		}
		
		Class<?> realEntityClass = Hibernate.getClass(entity);
		
		return type.isAssignableFrom(realEntityClass);
	}
}