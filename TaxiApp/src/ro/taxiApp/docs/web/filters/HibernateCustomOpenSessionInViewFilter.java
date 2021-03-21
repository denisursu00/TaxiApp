package ro.taxiApp.docs.web.filters;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

/**
 * Mentine o sesiune Hibernate activa pentru ca proprietatile cu fetch mode LAZY din clasele de mapare sa
 * poata fi accesate la cerere pe toata durata request-ului.
 * 
 */
public class HibernateCustomOpenSessionInViewFilter extends OpenSessionInViewFilter {
	
	@Override
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		/*
		 * Implicit, la folosirea acestui filtru, flush mode-ul e pus pe NEVER.
		 * Trebuie sa il punem pe AUTO pentru ca Hibernate sa salveze modificarile facute.
		 */
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}

	@Override
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		try {
			if (session != null && session.isOpen() && session.isConnected()) {
				session.flush();
			}
		} finally {
			super.closeSession(session, sessionFactory);
		}
	}
}