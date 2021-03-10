package ro.cloudSoft.cloudDoc.dao.notifications;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareNrOrganismeBugetate;

public class NotificareNrOrganismeBugetateDaoImpl extends HibernateDaoSupport implements NotificareNrOrganismeBugetateDao{

	@Override
	public long getNumarNotificariNrOrganismeBugetatePerAn(int an) {
		String query = ""
				+ "	SELECT count(notificare.id) "
				+ "	FROM NotificareNrOrganismeBugetate notificare "
				+ "	WHERE notificare.an = ?";
		List result = getHibernateTemplate().find(query, an);
		return (long) result.get(0);
	}

	@Override
	public Long saveNotificareNrOrganismeBugetate(NotificareNrOrganismeBugetate notificare) {
		return (Long) getHibernateTemplate().save(notificare);
		
	}

}
