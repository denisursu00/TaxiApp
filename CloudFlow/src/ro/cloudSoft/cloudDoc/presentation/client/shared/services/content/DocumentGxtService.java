package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface DocumentGxtService extends GxtServiceBase {

    String save(DocumentModel documentModel, String parentFolderId,
	    String documentLocationRealName,
	    List<String> namesForAttachmentsToDelete)
	    throws PresentationException, IOException;

    DocumentModel checkout(String documentId, String documentLocationRealName)
	    throws PresentationException;

    DocumentModel undoCheckout(String documentId,
	    String documentLocationRealName) throws PresentationException;

    String checkin(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName,
		Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete)
    	throws PresentationException;
    
    /**
	 * Returneaza o lista paginata de documente.
	 * 
	 * @param folderId ID-ul folder-ului de unde se doresc documentele
	 * @param sameType daca documentele sunt de acelasi tip (tipul specificat de folder)
	 * @param documentLocationRealName numele 'real' al spatiului de lucru de unde se doresc documentele
	 * @param pagingLoadConfig parametri de configurare pentru paginare
	 */
	PagingLoadResult<DocumentViewModel> getPagedDocumentsFromFolder(String folderId, boolean sameType,
		String documentLocationRealName, PagingLoadConfig pagingLoadConfig) throws PresentationException;

    DocumentModel getDocumentById(String documentId,
	    String documentLocationRealName) throws PresentationException;

    ExtendedDocumentModel getExtendedDocumentById(String documentId,
	    String documentLocationRealName) throws PresentationException;

    void moveDocument(String documentId, String folderDestinationId,
	    String documentLocationRealName) throws PresentationException;

    void deleteDocument(String documentId, String documentLocationRealName)
	    throws PresentationException;

    WorkflowInstanceResponseModel sendDocumentToWorkflow(Long workflowId,
	    String transitionName, String manualAssignmentDestinationId,
	    DocumentModel documentModel) throws PresentationException;

    List<DocumentVersionInfoViewModel> getDocumentVersions(String documentId,
	    String documentLocationRealName) throws PresentationException;

    DocumentModel getDocumentFromVersion(String versionNR, String documentId,
	    String documentLocationRealName) throws PresentationException;

    DocumentModel checkinAndGetDocument(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName,
		Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete) throws PresentationException;
    
	/**
	 * Goleste spatiul temporar pentru atasamente. Aceasta operatie este necesara inaintea adaugarii / modificarii
	 * unui document intrucat atasamentele temporare de la un document deschis anterior vor ramane daca utilizatorul
	 * nu salveaza documentul.
	 * @throws IOException 
	 */
	void clearTemporaryAttachments() throws PresentationException, IOException;
	
	boolean existDocumentsOfType(Long documentTypeId) throws PresentationException;
}