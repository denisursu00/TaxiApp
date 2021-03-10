package ro.cloudSoft.cloudDoc.dao.notifications;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareNrOrganismeBugetate;

public interface NotificareNrOrganismeBugetateDao {
	
	public long getNumarNotificariNrOrganismeBugetatePerAn(int an);
	public Long saveNotificareNrOrganismeBugetate(NotificareNrOrganismeBugetate notificare);
}
