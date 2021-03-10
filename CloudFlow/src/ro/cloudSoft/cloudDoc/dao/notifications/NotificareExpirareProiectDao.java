package ro.cloudSoft.cloudDoc.dao.notifications;

import java.util.Date;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareProiect;

public interface NotificareExpirareProiectDao {

	public boolean existByProjectIdAndDate(Long projectId, Date projectEndDate);
	public Long save(NotificareExpirareProiect notificare);
}
