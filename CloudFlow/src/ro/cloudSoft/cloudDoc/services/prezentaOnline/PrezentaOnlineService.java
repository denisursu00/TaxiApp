package ro.cloudSoft.cloudDoc.services.prezentaOnline;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.DocumentPrezentaOnlineModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.ParticipantiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.PrezentaMembriiReprezentantiComisieGl;

public interface PrezentaOnlineService {

	List<DocumentPrezentaOnlineModel> getAllDocumentsPrezenta(SecurityManager userSecurity);

	List<PrezentaMembriiReprezentantiComisieGl> getAllMembriiReprezentantiByComisieGlId(Long comisieGlId) throws AppException;

	void saveParticipant(PrezentaMembriiReprezentantiComisieGl prezentaMembriiReprezentantiComisieGl);

	ParticipantiModel getAllParticipantiByDocument(String documentId, String documentLocationRealName) throws AppException;

	void deleteParticipant(Long id);

	void finalizarePrezentaByDocument(String documentId, String documentLocationRealName);
	
	boolean isPrezentaFinalizataByDocument(String documentId, String documentLocationRealName);
	
	void importaPrezentaOnlineByDocument(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

}
