package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentGxtServiceAsync extends GxtServiceBaseAsync {

    void save(DocumentModel documentModel, String parentFolderId,
	    String documentLocationRealName,
	    List<String> namesForAttachmentsToDelete,
	    AsyncCallback<String> callback);

    void checkout(String documentId, String documentLocationRealName,
	    AsyncCallback<DocumentModel> callback);

    void undoCheckout(String documentId, String documentLocationRealName,
	    AsyncCallback<DocumentModel> callback);

    void checkin(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName,
		Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete,
	    AsyncCallback<String> callback);
    
    /**
	 * Returneaza o lista paginata de documente.
	 * 
	 * @param folderId ID-ul folder-ului de unde se doresc documentele
	 * @param sameType daca documentele sunt de acelasi tip (tipul specificat de folder)
	 * @param documentLocationRealName numele 'real' al spatiului de lucru de unde se doresc documentele
	 * @param pagingLoadConfig parametri de configurare pentru paginare
	 * @param callback callback-ul pt. tratarea raspunsului de pe server
	 */
	void getPagedDocumentsFromFolder(String folderId, boolean sameType, String documentLocationRealName,
		PagingLoadConfig pagingLoadConfig, AsyncCallback<PagingLoadResult<DocumentViewModel>> callback);

    void getDocumentById(String documentId, String documentLocationRealName,
	    AsyncCallback<DocumentModel> callback);

    void getExtendedDocumentById(String documentId,
	    String documentLocationRealName,
	    AsyncCallback<ExtendedDocumentModel> callback);

    void moveDocument(String documentId, String folderDestinationId,
	    String documentLocationRealName, AsyncCallback<Void> callback);

    void deleteDocument(String documentId, String documentLocationRealName,
	    AsyncCallback<Void> callback);

    void getDocumentVersions(String documentId,
	    String documentLocationRealName,
	    AsyncCallback<List<DocumentVersionInfoViewModel>> callback);

    void getDocumentFromVersion(String versionNR, String documentId,
	    String documentLocationRealName,
	    AsyncCallback<DocumentModel> callback);

    void sendDocumentToWorkflow(Long workflowId, String transitionName,
	    String manualAssignmentDestinationId, DocumentModel documentModel,
	    AsyncCallback<WorkflowInstanceResponseModel> callback);

    void checkinAndGetDocument(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName,
		Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete,
		AsyncCallback<DocumentModel> callback);

	/**
	 * Goleste spatiul temporar pentru atasamente. Aceasta operatie este necesara inaintea adaugarii / modificarii
	 * unui document intrucat atasamentele temporare de la un document deschis anterior vor ramane daca utilizatorul
	 * nu salveaza documentul.
	 */
	void clearTemporaryAttachments(AsyncCallback<Void> callback);
	
	void existDocumentsOfType(Long documentTypeId, AsyncCallback<Boolean> callback);
}