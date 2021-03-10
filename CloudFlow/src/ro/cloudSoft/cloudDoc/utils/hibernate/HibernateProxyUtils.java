package ro.cloudSoft.cloudDoc.utils.hibernate;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class HibernateProxyUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T unproxyIfNeeded(T object) {
		if (!(object instanceof HibernateProxy)) {
			return object;
		}
		LazyInitializer initializer = ((HibernateProxy) object).getHibernateLazyInitializer();
		initializer.initialize();
		return (T) initializer.getImplementation();
	}
}
