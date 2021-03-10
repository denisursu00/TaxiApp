package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;

public interface DocumentWorkflowHistoryService {
	
	/**
	 * Returneaza o lista de obiecte DocumentHistoryViewModel ce reprezinta 
	 * istoricul unui document lansat pe un flux.
	 */
	List <DocumentHistory> getDocumentHistory(String documentId) throws AppException;	
}