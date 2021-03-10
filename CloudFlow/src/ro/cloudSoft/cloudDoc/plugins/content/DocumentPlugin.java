package ro.cloudSoft.cloudDoc.plugins.content;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.AdminUpdateDocument;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentVersionInfo;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.content.DocumentFilterModel;
import ro.cloudSoft.common.utils.PagingList;

public interface DocumentPlugin {

	Document getDocumentById(String id, String wrkspName, SecurityManager userSecurity) throws AppException;

	public void deleteDocument(String doc, String wrkspName, SecurityManager userSecurity) throws AppException;

	public void moveDocumentToFolder(String fol, String cont, String wrkspName, SecurityManager userSecurity) throws AppException;
	
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

	PagingList<Document> getPagedDocuments(DocumentFilterModel documentFilterModel, SecurityManager userSecurity, List<String> documentIdsFilter) throws AppException;

	public Document checkout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;
	
	Document undoCheckout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

	/**
	 * Face checkin la documentul dat.
	 * Daca documentul era nou, dupa checkin va avea ID-ul completat.
	 */
	String checkin(Document document, Collection<Attachment> attachments, boolean isVersionable,
		String parentFolderId, String documentLocationRealName, List<String> namesForAttachmentsToDelete,
		SecurityManager userSecurity) throws AppException;
	
	public void setDocumentState(String documentId, String wrkspName, String idWorkflowState) throws AppException;

	/**
	 * Salveaza documentul.
	 * Daca documentul era nou, dupa salvare va avea ID-ul completat.
	 */
	public String save(Document document, Collection<Attachment> attachments, String parentFolderId,
		String documentLocationRealName, List<String> namesForAttachmentsToDelete, SecurityManager userSecurity)
		throws AppException;
	
	/**
	 * Returneaza atasamentul cu numele dat.
	 * Daca nu se gaseste, va returna null.
	 */
	public Attachment getAttachment(String documentId, String attachmentName, String documentLocationRealName, SecurityManager userSecurity) throws AppException;

	/**
	 * Returneaza atasamentul cu numele dat din versiunea documentului cu numarul dat.
	 * Daca nu se gaseste, va returna null.
	 */
	Attachment getAttachmentFromVersion(String documentId, Integer versionNumber, String attachmentName,
		String documentLocationRealName, SecurityManager userSecurity) throws AppException;
	
	public Document restore(String documentId, int versionNumber, String documentLocationRealName, SecurityManager userSecurity) throws AppException;
	
	public List<DocumentVersionInfo> getVersionInfos(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException ;

	public void changePermissionsForWorkflow(String documentId, String documentLocationRealName, String userSenderId, List<Long> userReceiverIds, List<Long> organizationUnitReceiverIds, List<Long> groupReceiverIds) throws AppException;
	
	public void addEditingPermissions(String documentId, String documentLocationRealName, Collection<Long> userIds,
		Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException;
	
	public void makeReadersIfEditors(String documentId, String documentLocationRealName, Collection<Long> userIds) throws AppException;
	
	public Document getDocumentFromVersion(String versionNR, String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException;
	
	void setReadOnly(String documentLocationRealName, String documentId) throws AppException;

	public void addSupervisorPermission(String documentId,
		String documentLocationRealName, List<String> supervisorsIds,
		List<Long> ouSupervisorsIds, List<Long> groupSupervisorIds)
		throws AppException;
	
	void addExtraViewers(String documentId, String documentLocationRealName, Collection<Long> userIds,
		Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException;
	
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
	
	boolean existDocumentsByDocumentType(Long documentTypeId);
	
	String getDocumentName(String documentLocationRealName, String documentId) throws AppException;
	
	Document getDocumentById(String id, String wrkspName) throws AppException;
	
	List<DocumentIdentifier> getAvailableDocumentsForArchivingByNoWorkflowAndBeforeDate(Set<String> workspaceNames, Set<Long> documentTypeIds, Date beforeDate);
	
	void deleteDocument(DocumentIdentifier documentIdentifier) throws AppException;
	
	AdminUpdateDocument getAdminUpdateDocument(DocumentIdentifier documentIdentifier, SecurityManager userSecurity) throws AppException;
	
	void updateDocument(AdminUpdateDocument document, SecurityManager userSecurity) throws AppException;
}