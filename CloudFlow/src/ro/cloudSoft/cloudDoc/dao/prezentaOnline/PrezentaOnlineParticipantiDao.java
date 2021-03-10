package ro.cloudSoft.cloudDoc.dao.prezentaOnline;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineParticipanti;

public interface PrezentaOnlineParticipantiDao {
	
	Long save(PrezentaOnlineParticipanti entity);
	
	void deleteById(Long id);
	
	PrezentaOnlineParticipanti findById(Long id);
	
	List<PrezentaOnlineParticipanti> getAllByDocument(String documentId, String documentLocationRealName);
	
	boolean existByDocument(String documentId, String documentLocationRealName, Long membruInstitutieId);

}
