package ro.cloudSoft.cloudDoc.dao.prezentaOnline;

import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineFinalizata;

public interface PrezentaOnlineFinalizataDao {
	
	Long save(PrezentaOnlineFinalizata entity);
	
	void deleteByDocument(String documentId, String documentLocationRealName);
	
	boolean existByDocument(String documentId, String documentLocationRealName);
	
	boolean isImportedByDocument(String documentId, String documentLocationRealName);
	
	PrezentaOnlineFinalizata getByDocument(String documentId, String documentLocationRealName);

}
