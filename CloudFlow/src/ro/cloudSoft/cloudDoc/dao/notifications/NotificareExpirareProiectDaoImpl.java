package ro.cloudSoft.cloudDoc.dao.notifications;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareProiect;

public class NotificareExpirareProiectDaoImpl extends HibernateDaoSupport implements NotificareExpirareProiectDao {

	@Override
	public boolean existByProjectIdAndDate(Long projectId, Date projectEndDate) {
		String query = ""
				+ "	SELECT notificare.id "
				+ "	FROM NotificareExpirareProiect notificare "
				+ "	WHERE notificare.project.id = ?"
				+ "		AND notificare.projectEndDate = ?";
		List result = getHibernateTemplate().find(query, projectId, projectEndDate);
		return result.size() > 0;
	}

	@Override
	public Long save(NotificareExpirareProiect notificare) {
		if (notificare.getId() == null) {
			return (Long) getHibernateTemplate().save(notificare);
		} else {
			getHibernateTemplate().saveOrUpdate(notificare);
			return notificare.getId();
		}
	}

}
