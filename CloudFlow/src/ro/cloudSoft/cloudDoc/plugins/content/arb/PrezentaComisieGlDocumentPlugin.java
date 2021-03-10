package ro.cloudSoft.cloudDoc.plugins.content.arb;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.DocumentPrezentaOnlineModel;

public interface PrezentaComisieGlDocumentPlugin {
	
	 List<DocumentPrezentaOnlineModel> getPrezentaComisieGlDocumentsForPrezentaOnline(SecurityManager userSecurity);

}
