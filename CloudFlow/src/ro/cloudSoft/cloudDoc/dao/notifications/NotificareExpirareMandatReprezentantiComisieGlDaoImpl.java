package ro.cloudSoft.cloudDoc.dao.notifications;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareMandatReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.domain.notifications.NotificareNrOrganismeBugetate;

public class NotificareExpirareMandatReprezentantiComisieGlDaoImpl extends HibernateDaoSupport implements NotificareExpirareMandatReprezentantiComisieGlDao{

	@Override
	public boolean existByReprezentantComisieSauGlIdAndPersoanaId(Long reprezentantComisieSauGLId, Long persoanaId) {
		String query = ""
				+ "	SELECT notificare.id "
				+ "	FROM NotificareExpirareMandatReprezentantiComisieGl notificare "
				+ "	WHERE notificare.reprezentantComisieSauGL.id = ?"
				+ "		AND notificare.persoana.id = ?";
		List result = getHibernateTemplate().find(query, reprezentantComisieSauGLId, persoanaId);
		return result.size() > 0;
	}

	@Override
	public Long save(NotificareExpirareMandatReprezentantiComisieGl notificare) {
		if (notificare.getId() == null) {
			return (Long) getHibernateTemplate().save(notificare);
		} else {
			getHibernateTemplate().saveOrUpdate(notificare);
			return notificare.getId();
		}
	}

}
