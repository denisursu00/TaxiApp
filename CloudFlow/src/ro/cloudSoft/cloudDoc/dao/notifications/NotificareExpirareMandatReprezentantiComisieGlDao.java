package ro.cloudSoft.cloudDoc.dao.notifications;

import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareMandatReprezentantiComisieGl;

public interface NotificareExpirareMandatReprezentantiComisieGlDao {

	public boolean existByReprezentantComisieSauGlIdAndPersoanaId(Long reprezentantComisieSauGLId, Long persoanaId);
	public Long save(NotificareExpirareMandatReprezentantiComisieGl notificare);
	
}
