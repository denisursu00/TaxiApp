package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentVersionInfo;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AdminUpdateDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCollectionValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationResponseModel;
import ro.cloudSoft.common.utils.PagingList;

public interface DocumentService {

    public Document getDocumentById(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public void deleteDocument(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public void moveDocumentToFolder(String folderId, String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;
    
    /**
     * Returneaza o lista paginata de documente.
     * 
	 * @param documentLocationRealName numele 'real' al spatiului de lucru de unde se doresc documentele
     * @param folderId ID-ul folder-ului de unde se doresc documentele
     * @param offset index-ul de unde sa inceapa lista de documente
     * @param limit numarul maxim de documente de afisat in pagina
     * @param userSecurity datele utilizatorului curent
     */
    PagingList<Document> getPagedDocumentsFromFolder(String documentLocationRealName,
		String folderId, int offset, int limit, SecurityManager userSecurity)
		throws AppException;
    
    PagingList<Document> getPagedDocuments(DocumentFilterModel documentFilterModel, SecurityManager userSecurity)
    		throws AppException;

    public Document checkout(String documentId, String documentLocationRealName,
	    SecurityManager userSecurity) throws AppException;

    public Document undoCheckout(String documentId, String documentLocationRealName,
	    SecurityManager userSecurity) throws AppException;

	/**
	 * Face checkin la documentul dat.
	 * Daca documentul era nou, dupa checkin va avea ID-ul completat.
	 */
    public String checkin(Document document, Collection<Attachment> attachments, boolean isVersionable, String parentFolderId,
		String documentLocationRealName, Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate,
		List<String> namesForAttachmentsToDelete, SecurityManager userSecurity) throws AppException;

    public void setDocumentState(String documentId, String documentLocationRealName, String idWorkflowState) throws AppException;

	/**
	 * Salveaza documentul.
	 * Daca documentul era nou, dupa salvare va avea ID-ul completat.
	 */
    public String save(Document document, Collection<Attachment> attachments, String parentFolderId,
	    String documentLocationRealName, List<String> namesForAttachmentsToDelete, SecurityManager userSecurity)
    	throws AppException;
    
    public WorkflowInstanceResponse sendDocumentToWorkflow(Long workflowId, String transitionName,
		String manualAssignmentDestinationId, Document document, SecurityManager userSecurity)
		throws AppException;
    public WorkflowState getCurrentWorkflowState(Long workflowId, Document document, SecurityManager userSecurity) 
    		throws AppException;
    
	/**
	 * Returneaza atasamentul cu numele dat.
	 * Daca nu se gaseste, va returna null.
	 */
    public Attachment getAttachment(String documentId, String attachmentName,
	    String documentLocationRealName, SecurityManager userSecurity)
	    throws AppException;

	/**
	 * Returneaza atasamentul cu numele dat din versiunea documentului cu numarul dat.
	 * Daca nu se gaseste, va returna null.
	 */
    Attachment getAttachmentFromVersion(String documentId, Integer versionNumber, String attachmentName,
		String documentLocationRealName, SecurityManager userSecurity) throws AppException;

    public Document restore(String documentId, int versionNumber,
	    String documentLocationRealName, SecurityManager userSecurity)
	    throws AppException;

    public List<DocumentVersionInfo> getVersionInfos(String documentId,
	    String documentLocationRealName, SecurityManager userSecurity)
	    throws AppException;

    public void changePermissionsForWorkflow(String documentId,
	    String documentLocationRealName, String userSenderId,
	    String userReceiverId) throws AppException;

    public void changePermissionsForWorkflow(String documentId,
	    String documentLocationRealName, String userSenderId,
	    List<Long> userReceiverIds,
	    List<Long> organizationUnitReceiverIds, List<Long> groupReceiverIds)
	    throws AppException;
    
    public void addEditingPermissions(String documentId, String documentLocationRealName,
		Collection<Long> userIds, Collection<Long> organizationUnitIds, Collection<Long> groupIds)
    	throws AppException;
    
    public void makeReadersIfEditors(String documentId, String documentLocationRealName, Collection<Long> userIds) throws AppException;

    public Document getDocumentFromVersion(String versionNR, String documentId,
	    String documentLocationRealName, SecurityManager userSecurity)
	    throws AppException;

    public void setReadOnly(String documentLocationRealName, String documentId) throws AppException;

    public void addSupervisorPermission(String documentId,
	    String documentLocationRealName, List<String> supervisorsIds,
	    List<Long> ouSupervisorsIds, List<Long> groupSupervisorIds)
	    throws AppException;
    
    void addExtraViewers(String documentId, String documentLocationRealName, Collection<Long> userIds,
		Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException;
    
    boolean existDocumentsOfType(Long documentTypeId, SecurityManager userSecurity) throws AppException;
    
    /**
	 * Cauta toate documentele de tipul dat care are printre permisiuni utilizatorul
	 * cu ID-ul dat si inlocuieste permisiunea acestuia cu ID-ul utilizatorului nou.
	 */
	void replaceUserPermission(String documentLocationRealName,
		Long documentTypeId, Long oldUserId, Long newUserId)
		throws AppException;
	
	Document getDocumentForAutomaticAction(String documentLocationRealName, String documentId) throws AppException;
	
	void checkinForAutomaticAction(Document document, boolean isVersionable, SecurityManager userSecurity) throws AppException;
	
	DocumentLogAttributes getDocumentLogAttributes(String documentLocationRealName, String documentId);
	
	DocumentValidationResponseModel validateDocument(DocumentValidationRequestModel requestModel, SecurityManager userSecurity) throws AppException;
	
	DocumentValidationResponseModel validateDocumentCollection(DocumentCollectionValidationRequestModel requestModel, SecurityManager userSecurity) throws AppException;
	
	String getDocumentName(String documentLocationRealName, String documentId) throws AppException;

    public Document getDocumentById(String documentId, String documentLocationRealName) throws AppException;
    
    AutocompleteMetadataResponseModel autocompleteMetadata(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException;
    
    DownloadableFile exportDocument(String documentLocationRealName, String documentId, String templateName, ExportType exportType, SecurityManager userSecurity, boolean ingonreExtensionFromFileName) throws AppException;
    
    DownloadableFile getDocumentAttachmentAsDownloadableFile(String documentLocationRealName, String documentId, String attachmentName, Integer versionNumber, SecurityManager userSecurity) throws AppException;
    
    boolean isDocumentImported(DocumentIdentifier documentIdentifier);
    
    boolean checkForNeedUiSendConfirmation(SecurityManager userSecurity, DocumentModel documentModel, String transitionName) throws AppException;
    
    boolean isDocumentWorkflowFinished(DocumentIdentifier documentIdentifier);
    
    AdminUpdateDocumentModel getAdminUpdateDocument(DocumentIdentifier documentIdentifier, SecurityManager userSecurity) throws AppException;

    void updateDocument(AdminUpdateDocumentModel document, SecurityManager userSecurity) throws AppException;
}