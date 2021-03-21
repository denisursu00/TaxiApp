package ro.taxiApp.docs.utils.hibernate;

import javax.persistence.Entity;

import org.apache.commons.lang.ClassUtils;
import org.hibernate.Hibernate;

public class EntityUtils {
	
	@SuppressWarnings("unchecked")
	public static <E> Class<E> getRealClass(E entity) {
		if (entity == null) {
			return null;
		}
		if (!isEntity(entity)) {
			throw new IllegalArgumentException("[" + entity + "] is not an entity.");
		}
		return Hibernate.getClass(entity);
	}
	
	@SuppressWarnings("unchecked")
	private static boolean isEntity(Object candidate) {
		if (candidate == null) {
			return false;
		}
		Class<?> candidateClass = candidate.getClass();
		if (isEntityClass(candidateClass)) {
			return true;
		}
		
		Iterable<Class<?>> candidateSuperclasses = ClassUtils.getAllSuperclasses(candidateClass);
		for (Class<?> candidateSuperclass : candidateSuperclasses) {
			if (isEntityClass(candidateSuperclass)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isEntityClass(Class<?> entity) {
		return (entity.getAnnotation(Entity.class) != null);
	}

}
