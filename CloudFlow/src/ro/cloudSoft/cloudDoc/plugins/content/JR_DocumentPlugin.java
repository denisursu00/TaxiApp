package ro.cloudSoft.cloudDoc.plugins.content;

import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.ATTACHMENT_NOT_FOUND;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.DOCUMENT_NOT_FOUND;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.DOCUMENT_VERSION_NOT_FOUND;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.INSUFFICIENT_RIGHTS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.AppConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.AdminUpdateDocument;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.ContentOperation;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLocation;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentVersionInfo;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.XPathQueryTemplate;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.DocumentCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.FolderCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.commons.SecurityCommons;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query.DocumentsForPermissionReplacementQueryBuilder;
import ro.cloudSoft.cloudDoc.scheduledTasks.AvailableDocumentsForArchivingByWithoutWorkflowAndBeforeDateSearcher;
import ro.cloudSoft.cloudDoc.services.content.DocumentFilterModel;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.PagingList;

public class JR_DocumentPlugin extends JR_PluginBase implements DocumentPlugin, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DocumentPlugin.class);
	private static final String WORKSPACE_COMISII_GL = "Comisii GL workspace";
	private static final String WORKSPACE_COMISII_GL_FOLDER_PREZENTE = "Prezente sedinte Comisii GL";
	private static final String WORKSPACE_COMISII_GL_FOLDER_MINUTE = "Minute sedinte Comisii GL";
	
	private DocumentTypeDao documentTypeDao;
	private JR_DocumentLocationPlugin jrDocumentLocationPlugin;
//	private DocumentType documentType;
	
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}	

	public void setJrDocumentLocationPlugin(JR_DocumentLocationPlugin jrDocumentLocationPlugin) {
		this.jrDocumentLocationPlugin = jrDocumentLocationPlugin;
	}
	
//	public void setDocumentType(DocumentType documentType) {
//		this.documentType = documentType;
//	}

	@Override
	public void afterPropertiesSet() throws Exception {		
		super.afterPropertiesSet();
	}
	
	@Override
	public void deleteDocument(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de stergere, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.DELETE, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat STERGEREA documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "], ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "deleteDocument", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Daca documentul este blocat, si nu de catre utilizatorul curent...
			if (DocumentCommons.isLocked(documentNode) && !SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
				throw new AppException(AppExceptionCodes.DOCUMENT_LOCKED_BY_ANOTHER_PERSON);
			}
			// Sterge nodul documentului.
			documentNode.remove();
			// Salveaza modificarile.
			session.save();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "deleteDocument", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public void moveDocumentToFolder(String destinationFolderId, String idForDocumentToMove, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);			
			// Obtinem nodul ce reprezinta documentul ce va fi mutat si calea acestuia.
			Node nodeForDocumentToMove = session.getNodeByIdentifier(idForDocumentToMove);
			String sourcePath = nodeForDocumentToMove.getPath();
			String nameForDocumentToMove = DocumentCommons.getName(nodeForDocumentToMove);
			/*
			 * Daca documentul este blocat de catre un alt utilizator,
			 * atunci nu are voie sa-l mute.
			 */
			if (DocumentCommons.isLocked(nodeForDocumentToMove) && !SecurityCommons.canAccessLockedDocument(nodeForDocumentToMove, userSecurity)) {
				throw new AppException(AppExceptionCodes.DOCUMENT_LOCKED_BY_ANOTHER_PERSON);
			}
			/*
			 * Obtinem nodul ce reprezinta folder-ul destinatie. Daca nu s-a
			 * trimis un ID, inseamna ca se va muta in radacina.
			 * In acelasi timp, construieste calea destinatie.
			 */
			Node destinationFolderNode = null;
			StringBuilder destinationPath = new StringBuilder();
			if (destinationFolderId != null) {
				destinationFolderNode = session.getNodeByIdentifier(destinationFolderId);
				destinationPath.append(destinationFolderNode.getPath());
			} else {
				destinationFolderNode = session.getRootNode();
			}
			destinationPath.append('/');
			destinationPath.append(nodeForDocumentToMove.getName());
			String destinationFolderName = FolderCommons.getName(destinationFolderNode);
			
			// Daca nu are permisiunile necesare, va arunca o exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.DELETE, nodeForDocumentToMove, userSecurity)
					&& !SecurityCommons.hasPermisssionForOperation(ContentOperation.CREATE_CHILD, destinationFolderNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat MUTAREA documentului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + idForDocumentToMove + "] si nume [" + nameForDocumentToMove + "]) in directorul cu ID [" + destinationFolderId + "] si numele [" + destinationFolderName + "], insa acesta NU are drepturi suficiente.", "moveDocumentToFolder", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}			
			// Muta documentul in destinatie.
			session.move(sourcePath, destinationPath.toString());
			// Salveaza modificarile.
			session.save();
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "moveDocumentToFolder", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public PagingList<Document> getPagedDocumentsFromFolder(String documentLocationRealName, String folderId, int offset, int limit, SecurityManager userSecurity) throws AppException {
		
		Session session = null;
		try {
			
			session = repository.login(credentials, documentLocationRealName);
			
			NodeIterator allDocumentNodes = session.getNodeByIdentifier(folderId).getNodes(JackRabbitConstants.DOCUMENT_NODE_PREFIX + "*");
			
			List<Node> visibleDocumentNodes = Lists.newArrayListWithExpectedSize((int) allDocumentNodes.getSize());
			
			while (allDocumentNodes.hasNext()) {
				Node documentNode = allDocumentNodes.nextNode();
				if (SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity)) {
					if (DocumentCommons.isLocked(documentNode)) {
						if (SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
							visibleDocumentNodes.add(documentNode);
						} else {
							if (DocumentCommons.hasVersions(documentNode)) {
								visibleDocumentNodes.add(documentNode);
							}
						}
					} else {
						visibleDocumentNodes.add(documentNode);
					}
				}
			}
			
			int totalVisibleDocumentCount = visibleDocumentNodes.size();
			
			while (offset != 0 && totalVisibleDocumentCount <= offset) {
				offset -= limit;
			}
			
			int beginIndex = offset;
			int endIndex = (totalVisibleDocumentCount >= (offset + limit)) ? ((offset + limit) - 1) : (totalVisibleDocumentCount - 1);
			
			List<Document> visibleDocumentsInRange = Lists.newLinkedList();
			Map<Long, DocumentType> documentTypeById = new HashMap<>();
			for (int index = 0; index < visibleDocumentNodes.size(); index++) {
				
				if (index > endIndex) {
					break;
				}
				
				if ((index >= beginIndex) && (index <= endIndex)) {
					
					Node documentNode = visibleDocumentNodes.get(index);
					
					Long documentTypeId = DocumentCommons.getDocumentTypeId(documentNode);
					DocumentType documentType = documentTypeById.get(documentTypeId);
					if (documentType == null) {
						documentType = documentTypeDao.find(documentTypeId);
						documentTypeById.put(documentTypeId, documentType);
					}
					
					if (DocumentCommons.isLocked(documentNode)) {
						if (SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
							Document document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
							visibleDocumentsInRange.add(document);
						} else {
							if (DocumentCommons.hasVersions(documentNode)) {
								Document document = DocumentCommons.getDocumentFromLatestVersion(documentLocationRealName, documentNode, documentType);
								visibleDocumentsInRange.add(document);
							}
						}
					} else {
						Document document = DocumentCommons.getDocumentFromLatestVersion(documentLocationRealName, documentNode, documentType);
						visibleDocumentsInRange.add(document);
					}
				}
			}
			
			return new PagingList<Document>(totalVisibleDocumentCount, offset, visibleDocumentsInRange);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getPagedDocumentsFromFolder", userSecurity);
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	
	@Override
	public PagingList<Document> getPagedDocuments(DocumentFilterModel filter, SecurityManager userSecurity, List<String> documentIdsFilter) {
		
		List<Document> documentResults ;
		
		DocumentSearcher documentSearcher = new DocumentSearcher(getRepository(), getCredentials(), filter, documentTypeDao, documentIdsFilter);
		documentResults = documentSearcher.search();
				
		return new PagingList<Document>(documentSearcher.getCount(), filter.getOffset(), documentResults);
	}

	@Override
	public Document checkout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Document document = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de editare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat operatia de CHECKOUT pentru documentul ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID ["+documentId+"] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "checkout", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			// Verifica starea blocarii documentului.
			if (DocumentCommons.isLocked(documentNode)) {
				// Daca documentul este blocat...
				if (!SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
					/*
					 * Daca documentul este blocat de catre alt utiliator,
					 * atunci nimeni nu mai poate edita acest document, iar
					 * utilizatorul va fi atentionat de acest lucru.
					 */
					throw new AppException(AppExceptionCodes.DOCUMENT_LOCKED_BY_ANOTHER_PERSON);
				}
			} else {
				// Daca documentul nu este blocat...
				/*
				 * Se marcheaza documentul ca fiind blocat de catre
				 * utilizatorul curent.
				 */
				DocumentCommons.lock(documentNode, userSecurity);
				// Salveaza starea documentului (blocat).
				session.save();
			}
			DocumentType  documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "checkout", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return document;
	}
	
	@Override
	public Document undoCheckout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Document document = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de editare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat operatia ANULARE CHECKOUT pentru documentul ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "undoCheckout", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Daca documentul nu este blocat, aceasta operatie este invalida.
			if (!DocumentCommons.isLocked(documentNode)) {
				throw new AppException(AppExceptionCodes.INVALID_OPERATION);
			}
			// Daca documentul este blocat de catre altcineva...
			if (!SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
				throw new AppException(AppExceptionCodes.DOCUMENT_LOCKED_BY_ANOTHER_PERSON);
			}
			// Daca documentul nu are versiuni, aceasta operatie este invalida.
			if (!DocumentCommons.hasVersions(documentNode)) {
				throw new AppException(AppExceptionCodes.INVALID_OPERATION);
			}
			// Obtine nodul ce reprezinta ultima versiune.
			Node lastVersionNode = documentNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + DocumentCommons.getLastVersionNumber(documentNode));
			/*
			 * Toate proprietatile documentului (inclusiv atasamentele) vor avea
			 * valorile ultimei versiuni.
			 * TODO Poate se gaseste o metoda mai "eleganta"...
			 */
			DocumentType documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			DocumentCommons.copyProperties(lastVersionNode, documentNode, documentType);
			// Deblocheaza documentul.
			DocumentCommons.unlock(documentNode);
			// Salveaza modificarile.
			session.save();
			
			document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
			document.setDocumentLocationRealName(documentLocationRealName);
			
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "undoCheckout", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return document;
	}

	@Override
	public String save(Document document, Collection<Attachment> attachments, String parentFolderId,
			String documentLocationRealName, List<String> namesForAttachmentsToDelete, SecurityManager userSecurity)
			throws AppException {
		
		String documentId = null;

		boolean isEdit = false;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			DocumentType documentType = documentTypeDao.find(document.getDocumentTypeId());
			
			Node documentNode = null;
			if (document.getId() == null) {
				// Daca nu exista documentul deja, va fi creat.
				/*
				 * Obtine parintele documentului. Daca a fost precizat un
				 * folder, atunci se va cauta acel folder; altfel, documentul va
				 * fi adaugat in radacina document location-ului.
				 */
				Node parentNode = null;
				if (parentFolderId == null) {
					parentNode = session.getRootNode();
				} else {
					parentNode = session.getNodeByIdentifier(parentFolderId);
				}
				String parentFolderName = FolderCommons.getName(parentNode);
				// Daca nu are drept de creare nod copil in parinte, va arunca exceptie.
				if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.CREATE_CHILD, parentNode, userSecurity)) {
					LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat ADAUGAREA unui document cu numele [" + document.getName() + "] in directorul ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + parentFolderId + "] si nume [" + parentFolderName + "]) , insa acesta NU are drepturi suficiente.", "save", userSecurity);
					throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
				}
				// Numele nodului documentului NU trebuie sa contina spatii.
				String documentNodeName = JackRabbitConstants.DOCUMENT_NODE_PREFIX + documentType.getId();
				// Adauga un nou nod pentru document.
				documentNode = parentNode.addNode(documentNodeName, JackRabbitConstants.DOCUMENT_NODE_TYPE);
				// Daca este document nou, atunci ii setez data crearii.
				document.setCreated(Calendar.getInstance());
				// Daca este document nou, atunci ii setez autorul.
				document.setAuthor(userSecurity.getUserIdAsString());
			} else {
				isEdit = true;
				documentNode = session.getNodeByIdentifier(document.getId());
			}
			// Setez data modificarii.
			document.setLastUpdate(Calendar.getInstance());
			// Daca este vorba de editare si nu are drept, va arunca exceptie.
			if (isEdit && !SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				String documentName = DocumentCommons.getName(documentNode);
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat EDITAREA documentului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta nu are drepturi suficiente", "save", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			/*
			 * TODO Daca este editare, inainte de a edita un document trebuie
			 * sa se dea checkout. Daca nu s-a dat, trebuie sa arunce exceptie.
			 */
			// Seteaza permisiunile documentului.
			SecurityCommons.setAclForNode(documentNode, document.getAcl());
			// Seteaza proprietatile documentului.
			DocumentCommons.setDocumentPropertiesToNode(document, documentType, documentNode);
			// Documentul ramane blocat pentru editare.
			DocumentCommons.lock(documentNode, userSecurity);
			// Seteaza istoricul documentului.
			//common.setDocumentHistory(document, documentNode, userSecurity);
			// Daca este vorba de editare, va sterge atasamentele eliminate.
			if (isEdit) {
				DocumentCommons.deleteAttachments(documentNode, namesForAttachmentsToDelete);
			}
			// Adauga atasamentele noi.
			for (Attachment attachment : attachments) {
				DocumentCommons.addAttachmentToDocumentNode(documentNode, attachment);
			}
			// Salveaza modificarile.
			session.save();
			// Iau ID-ul documentului.
			documentId = documentNode.getIdentifier();
			document.setId(documentId);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "save", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return documentId;
	}
	
	@Override
	public String checkin(Document document, Collection<Attachment> attachments, boolean isVersionable,
			String parentFolderId, String documentLocationRealName, List<String> namesForAttachmentsToDelete,
			SecurityManager userSecurity) throws AppException {
		
		boolean isEdit = (document.getId() != null);
		
		String documentId = save(document, attachments, parentFolderId,
			documentLocationRealName, namesForAttachmentsToDelete, userSecurity);
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			// Daca nu are drept de editare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat operatia de CHECKIN pentru documentul ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + documentId + "] si nume [" + document.getName() + "]) , insa acesta NU are drepturi suficiente.", "checkin", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Daca documentul nu este versionabil, se vor sterge toate versiunile.
			if (isEdit && !isVersionable) {
				DocumentCommons.deleteAllVersions(documentNode);
			}
			// Creeaza o noua versiune.
			DocumentType documentType = documentTypeDao.find(document.getDocumentTypeId());
			DocumentCommons.addVersionNode(documentNode, null, userSecurity, documentType);
			DocumentCommons.unlock(documentNode);
			// Salveaza modificarile.
			session.save();
			// Returneaza ID-ul documentului.
			return documentId;
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "checkin", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public Document getDocumentById(String documentId, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		Document document = null;

		Session session = null;
		
		List<Folder> foldersFromWorkspace = jrDocumentLocationPlugin.getAllFolders(documentLocationRealName, userSecurity);
		List<DocumentLocation> documentLocations = jrDocumentLocationPlugin.getAllDocumentLocations(userSecurity);

		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de vizualizare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity) && 
					!(documentLocations.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL)).findFirst().isPresent() && (
					foldersFromWorkspace.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL_FOLDER_MINUTE)).findFirst().isPresent() 
					|| foldersFromWorkspace.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL_FOLDER_PREZENTE)).findFirst().isPresent() ))) {
				
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "], ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "getDocumentById", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			DocumentType documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			
			// Verifica starea blocarii documentului.
			if (DocumentCommons.isLocked(documentNode)) {
				// Daca documentul este blocat...
				if (SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
					/*
					 * Daca utilizatorul curent poate accesa documentul blocat,
					 * atunci va primi efectiv documentul, avand ultimele
					 * modificari (pe care doar utilizatorul sau administratorii le pot vedea).
					 */
					document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
				} else if (DocumentCommons.hasVersions(documentNode)) {
					/*
					 * Daca documentul este blocat de catre alt utiliator si
					 * exista o versiune precedenta modificarilor "temporare",
					 * atunci utilizatorul o va vedea pe aceea.
					 */
					document = DocumentCommons.getDocumentFromLatestVersion(documentLocationRealName, documentNode, documentType);
				} else {
					/*
					 * Documentul este doar unul temporar pe care doar
					 * utilizatorul ce l-a creat ar trebui sa-l vada. Deci alt
					 * utilizator nu are ce cauta cu el.
					 */
					throw new AppException(DOCUMENT_NOT_FOUND);
				}
			} else {
				// Daca documentul nu este blocat, oricine il poate lua.
				document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentById", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return document;
	}

	@Override
	public Attachment getAttachment(String documentId, String attachmentName, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		// TODO Codul de mai jos este comentat pana se va rezolva cu securitatea, apoi trebuie revenit la implementare.
		/*
		Attachment attachment = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByUUID(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de vizualizare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA unui atasament cu numele [" + attachmentName + "] al documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "], ID [" + documentId +  "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "getAttachment", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			if (DocumentCommons.isLocked(documentNode) && !SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
				if (DocumentCommons.hasVersions(documentNode)) {
					// Ia atasamentul din ultima versiune stabila.
					attachment = DocumentCommons.getAttachmentFromLatestVersion(documentNode, attachmentName);
				} else {
					// Arunca exceptie intrucat atasamentul nu exista / nu este vizibil.
					throw new AppException(ATTACHMENT_NOT_FOUND);
				}
			} else {
				attachment = DocumentCommons.getAttachment(documentNode, attachmentName);
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAttachment", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return attachment;
		*/
		
		Attachment attachment = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			
			if (DocumentCommons.isLocked(documentNode)) {
				if (DocumentCommons.hasVersions(documentNode)) {
					// Ia atasamentul din ultima versiune stabila.
					attachment = DocumentCommons.getAttachmentFromLatestVersion(documentNode, attachmentName);
				} else {
					// Arunca exceptie intrucat atasamentul nu exista / nu este vizibil.
					throw new AppException(ATTACHMENT_NOT_FOUND);
				}
			} else {
				attachment = DocumentCommons.getAttachmentByName(documentNode, attachmentName);
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAttachment");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return attachment;
	}
	
	@Override
	public Attachment getAttachmentFromVersion(String documentId, Integer versionNumber, String attachmentName,
			String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		
		Attachment attachment = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			
			// Daca nu are drept de vizualizare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA UNUI ATASAMENT cu numele [" + attachmentName + "] din versiunea cu numarul " + versionNumber + "a documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "], ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "getAttachmentFromVersion", userSecurity);
				throw new AppException(INSUFFICIENT_RIGHTS);
			}
			
			if (DocumentCommons.versionExists(documentNode, versionNumber)) {
				attachment = DocumentCommons.getAttachmentFromVersion(documentNode, versionNumber, attachmentName);
			} else {
				
				DocumentLogAttributes documentLogAttributes = DocumentCommons.getDocumentLogAttributes(documentLocationRealName, documentNode);
				
				String logMessage = "S-a cerut atasamentul cu numele [" + attachmentName + "] din versiunea " +
					"cu numarul [" + versionNumber + "] pentru documentul cu atributele: " + documentLogAttributes + ", " +
					"insa aceasta versiune a documentului NU exista.";
				LOGGER.error(logMessage, "getAttachmentFromVersion", userSecurity);
			
				throw new AppException(DOCUMENT_VERSION_NOT_FOUND);
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getAttachment", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return attachment;
	}
	
	@Override
	public List<DocumentVersionInfo> getVersionInfos(String documentId, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		List<DocumentVersionInfo> versionInfos = new ArrayList<DocumentVersionInfo>();
		
		Session session = null;
		
		List<Folder> foldersFromWorkspace = jrDocumentLocationPlugin.getAllFolders(documentLocationRealName, userSecurity);
		List<DocumentLocation> documentLocations = jrDocumentLocationPlugin.getAllDocumentLocations(userSecurity);
		
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de vizualizare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity) && 
					!(documentLocations.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL)).findFirst().isPresent() && (
					foldersFromWorkspace.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL_FOLDER_MINUTE)).findFirst().isPresent() 
					|| foldersFromWorkspace.stream().filter(o -> o.getName().equals(JR_DocumentPlugin.WORKSPACE_COMISII_GL_FOLDER_PREZENTE)).findFirst().isPresent() ))) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat VIZUALIZAREA VERSIUNILOR documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "getVersionInfos", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			//daca documentul mai are versiuni, setez pentru acestea proprietatea VERSION_PROPERTY_LAST_STABLE_VERSION pe false
			if(DocumentCommons.hasVersions(documentNode))
			{
				NodeIterator versionNodesIterator = documentNode.getNodes(JackRabbitConstants.VERSION_NODE_PREFIX + "*");
				while(versionNodesIterator.hasNext())
				{
					Node versionNode =versionNodesIterator.nextNode();
					Integer versionNumber = Integer.parseInt(versionNode.getName().substring(JackRabbitConstants.VERSION_NODE_PREFIX.length()));
					
					//se(versionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED).getValue().getString());
					DocumentVersionInfo dvi=new DocumentVersionInfo(versionNumber,versionNode.getProperty(JackRabbitConstants.VERSION_PROPERTY_VERSION_AUTHOR).getValue().getString(),versionNode.getProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED).getDate());
					versionInfos.add(dvi);
				}
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getVersionInfos", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return versionInfos;
	}
	
	@Override
	public Document restore(String documentId, int versionNumber, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		Document document = null;
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Verifica daca are drept de editare.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat RESTAURAREA documentului ( numele 'real' al spatiului de lucru [" + documentLocationRealName + "] , ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "restore", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			// Verifica daca documentul nu este blocat de catre altcineva.
			if (DocumentCommons.isLocked(documentNode) && !SecurityCommons.canAccessLockedDocument(documentNode, userSecurity)) {
				throw new AppException(AppExceptionCodes.DOCUMENT_LOCKED_BY_ANOTHER_PERSON);
			}
			// Gaseste versiunea documentului.
			Node versionNode = documentNode.getNode(JackRabbitConstants.VERSION_NODE_PREFIX + versionNumber);
			// Restaureaza valorile documentului de la acea versiune.
			DocumentType documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			DocumentCommons.copyProperties(versionNode, documentNode, documentType);			
			/*
			 * Marcheaza faptul ca documentul a fost modificat (se va reflecta
			 * in data ultimei modificari).
			 */
			documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_LAST_MODIFIED, Calendar.getInstance());
			// Deblocheaza documentul.
			DocumentCommons.unlock(documentNode);
			/*
			 * Adauga o noua versiune, semn ca documentul a fost restaurat
			 * dintr-o versiune anterioara.
			 */
			DocumentCommons.addVersionNode(documentNode, versionNumber, userSecurity, documentType);
			// Salveaza modificarile.
			session.save();
			// Returneaza documentul cu valorile sale.
			document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "restore", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return document;
	}

	@Override
	public void setDocumentState(String documentId, String documentLocationName, String idWorkflowState) throws AppException {
		
		Session session = null;
		Node documentNode = null;
		try {
			session = repository.login(credentials, documentLocationName);
			documentNode = session.getNodeByIdentifier(documentId);
			/*
			 * Cand se trimite documentul mai departe, sender-ul devine doar
			 * viewer, iar receiver-ul devine editor (daca nu era inainte).
			 * Apoi se ajunge la aceasta metoda, iar utilizatorul curent va
			 * fi sender-ul (viewer), deci nu va mai avea drept de editare.
			 */
			/*if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.EDIT, documentNode, userSecurity)) {
				throw new ApplicationException(ApplicationExceptionCodes.INSUFFICIENT_RIGHTS);
			}*/
			DocumentCommons.setDocumentState(documentNode, idWorkflowState);
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "setDocumentState");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}

	@Override
	public void changePermissionsForWorkflow(String documentId, String documentLocationRealName, String userSenderId,
			List<Long> userReceiverIds, List<Long> organizationUnitReceiverIds, List<Long> groupReceiverIds)
			throws AppException {
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			// Obtine ACL-ul documentului.
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			
			/*
			 * Daca utilizatorul care a dat send a primit documentul prin intermediul unei asignari de tip
			 * unitate organizatorica sau grup, trebuie sa schimb permisiunea in reader SI pentru acea entitate
			 * organizatorica.
			 */
			SecurityUtils.setReadOnly(acl);
			
			// Obtin harta permisiunilor existente.
			Map<Integer, Map<String, String>> permissionMap = SecurityUtils.getPermissionMap(acl);
			/*
			 * Setez sender-ul ca cititor
			 * (fie ca exista sau nu in permisiunile existente).
			 */
			permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER).put(userSenderId, AppConstants.ACL_ROLE_READER);
			// Daca am receiveri de tip utilizator...
			if (userReceiverIds != null) {
				/*
				 * Setez fiecare receiver de tip utilizator ca editor 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (Long userReceiverId : userReceiverIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER).put(userReceiverId.toString(), AppConstants.ACL_ROLE_EDITOR);
				}
			}
			// Daca am receiveri de tip unitate organizatorica...
			if (organizationUnitReceiverIds != null) {
				/*
				 * Setez fiecare receiver de tip unitate organizatorica ca editor 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (Long organizationUnitReceiverId : organizationUnitReceiverIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT).put(organizationUnitReceiverId.toString(), AppConstants.ACL_ROLE_EDITOR);
				}
			}
			// Daca am receiveri de tip grup...
			if (groupReceiverIds != null) {
				/*
				 * Setez fiecare receiver de tip grup ca editor 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (Long groupReceiverId : groupReceiverIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_GROUP).put(groupReceiverId.toString(), AppConstants.ACL_ROLE_EDITOR);
				}
			}
			// Obtin noul ACL cu permisiunile nou modificate din structura folosita.
			ACL newAcl = SecurityUtils.getAclFromPermissionMap(permissionMap);
			// Setez noile permisiuni pe document.
			SecurityCommons.setAclForNode(documentNode, newAcl);
			// Salveaza modificarile.
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "changePermissionsForWorkflow");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void addEditingPermissions(String documentId, String documentLocationRealName, Collection<Long> userIds,
			Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException {
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			Node documentNode = session.getNodeByIdentifier(documentId);
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			Map<Integer, Map<String, String>> permissionMap = SecurityUtils.getPermissionMap(acl);
			
			Map<String, String> userPermissionRoleByUserIdAsString = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER);
			for (Long userId : userIds) {
				userPermissionRoleByUserIdAsString.put(userId.toString(), AppConstants.ACL_ROLE_EDITOR);
			}

			Map<String, String> organizationUnitPermissionRoleByOrganizationUnitIdAsString = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER);
			for (Long organizationUnitId : organizationUnitIds) {
				organizationUnitPermissionRoleByOrganizationUnitIdAsString.put(organizationUnitId.toString(), AppConstants.ACL_ROLE_EDITOR);
			}

			Map<String, String> groupPermissionRoleByGroupIdAsString = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER);
			for (Long groupId : groupIds) {
				groupPermissionRoleByGroupIdAsString.put(groupId.toString(), AppConstants.ACL_ROLE_EDITOR);
			}
			
			ACL newAcl = SecurityUtils.getAclFromPermissionMap(permissionMap);
			SecurityCommons.setAclForNode(documentNode, newAcl);
			
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "changePermissionsForWorkflow");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void makeReadersIfEditors(String documentId, String documentLocationRealName, Collection<Long> userIds) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			
			Node documentNode = session.getNodeByIdentifier(documentId);
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			Map<Integer, Map<String, String>> permissionMap = SecurityUtils.getPermissionMap(acl);
			
			Map<String, String> userPermissionRoleByUserIdAsString = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER);
			for (Long userId : userIds) {
				
				String userIdAsString = userId.toString();
				
				String currentPermissionRole = userPermissionRoleByUserIdAsString.get(userIdAsString);
				if ((currentPermissionRole != null) && currentPermissionRole.equals(AppConstants.ACL_ROLE_EDITOR)) {
					userPermissionRoleByUserIdAsString.put(userIdAsString, AppConstants.ACL_ROLE_READER);
				} else {
					
					DocumentLogAttributes documentLogAttributes = DocumentCommons.getDocumentLogAttributes(documentLocationRealName, documentNode);
					
					String logMessage = "Pentru documentul cu atributele: " + documentLogAttributes + ", s-a cerut transformarea " +
						"utilizatorului cu ID-ul [" + userId + "] din editor in reader, insa acest utilizator NU este editor.";
					LOGGER.warn(logMessage, "transformarea unui utilizator din editor in reader");
				}
			}
			
			ACL newAcl = SecurityUtils.getAclFromPermissionMap(permissionMap);
			SecurityCommons.setAclForNode(documentNode, newAcl);
			
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "changePermissionsForWorkflow");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void addSupervisorPermission(String documentId, String documentLocationRealName, List<String> supervisorsIds, 
			List<Long> ouSupervisorsIds, List<Long> groupSupervisorIds) throws AppException  {
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			// Obtine ACL-ul documentului.
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			// Obtin harta permisiunilor existente.
			Map<Integer, Map<String, String>> permissionMap = SecurityUtils.getPermissionMap(acl);
			
			// Daca am supervizori de tip utilizator...
			if (supervisorsIds != null) {
				/*
				 * Setez fiecare receiver de tip utilizator ca reader 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (String userReceiverId : supervisorsIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER).put(userReceiverId, AppConstants.ACL_ROLE_READER);
				}
			}
			// Daca am supervizori de tip unitate organizatorica...
			if (ouSupervisorsIds != null) {
				/*
				 * Setez fiecare receiver de tip unitate organizatorica ca reader 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (Long organizationUnitReceiverId : ouSupervisorsIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT).put(organizationUnitReceiverId.toString(), 
						AppConstants.ACL_ROLE_READER);
				}
			}
			// Daca am supervizori de tip grup...
			if (groupSupervisorIds != null) {
				/*
				 * Setez fiecare supervizori de tip grup ca reader 
				 * (fie ca exista sau nu in permisiunile existente).
				 */
				for (Long groupReceiverId : groupSupervisorIds) {
					permissionMap.get(AppConstants.ACL_ENTITY_TYPE_GROUP).put(groupReceiverId.toString(), AppConstants.ACL_ROLE_READER);
				}
			}
			// Obtin noul ACL cu permisiunile nou modificate din structura folosita.
			ACL newAcl = SecurityUtils.getAclFromPermissionMap(permissionMap);
			// Setez noile permisiuni pe document.
			SecurityCommons.setAclForNode(documentNode, newAcl);
			// Salveaza modificarile.
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "addSupervisorPermission");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void addExtraViewers(String documentId, String documentLocationRealName, Collection<Long> userIds,
			Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException {
		
		Session session = null;
		try {			
			session = repository.login(credentials, documentLocationRealName);
			
			Node documentNode = session.getNodeByIdentifier(documentId);
			
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			Map<Integer, Map<String, String>> permissionMap = SecurityUtils.getPermissionMap(acl);
			
			Map<String, String> userPermissionMap = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_USER);
			for (Long userId : userIds) {
				String userIdAsString = userId.toString();
				String existingPermissionForUser = userPermissionMap.get(userIdAsString);
				if (existingPermissionForUser == null) {
					userPermissionMap.put(userIdAsString, AppConstants.ACL_ROLE_READER);
				}
			}
			
			Map<String, String> organizationUnitPermissionMap = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT);
			for (Long organizationUnitId : organizationUnitIds) {
				String organizationUnitIdAsString = organizationUnitId.toString();
				String existingPermissionForOrganizationUnit = organizationUnitPermissionMap.get(organizationUnitIdAsString);
				if (existingPermissionForOrganizationUnit == null) {
					organizationUnitPermissionMap.put(organizationUnitIdAsString, AppConstants.ACL_ROLE_READER);
				}
			}
			
			Map<String, String> groupPermissionMap = permissionMap.get(AppConstants.ACL_ENTITY_TYPE_GROUP);
			for (Long groupId : groupIds) {
				String groupIdAsString = groupId.toString();
				String existingPermissionForGroup = groupPermissionMap.get(groupIdAsString);
				if (existingPermissionForGroup == null) {
					groupPermissionMap.put(groupIdAsString, AppConstants.ACL_ROLE_READER);
				}
			}
			
			ACL newAcl = SecurityUtils.getAclFromPermissionMap(permissionMap);
			SecurityCommons.setAclForNode(documentNode, newAcl);
			
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "addExtraViewers");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public Document getDocumentFromVersion(String versionNR, String documentId, String documentLocationRealName,
			SecurityManager userSecurity) throws AppException {
		
		Document document = null;
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			String documentName = DocumentCommons.getName(documentNode);
			// Daca nu are drept de vizualizare, va arunca exceptie.
			if (!SecurityCommons.hasPermisssionForOperation(ContentOperation.VIEW, documentNode, userSecurity)) {
				LOGGER.error("Utilizatorul [" + userSecurity.getDisplayName() + "] (cu ID [" + userSecurity.getUserIdAsString() + "]) a incercat CITIREA UNEI VERSIUNI  cu numarul " + versionNR + " a documentului ( cu numele 'real' al spatiului de lucru [" + documentLocationRealName + "], ID [" + documentId + "] si nume [" + documentName + "]) , insa acesta NU are drepturi suficiente.", "getDocumentFromVersion", userSecurity);
				throw new AppException(AppExceptionCodes.INSUFFICIENT_RIGHTS);
			}
			
			int versionNumber = Integer.parseInt(versionNR);
			if (DocumentCommons.hasVersions(documentNode) && DocumentCommons.versionExists(documentNode, versionNumber)) {
				DocumentType documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
				document = DocumentCommons.getDocumentFromVersion(documentLocationRealName, versionNumber, documentNode, documentType);
			} else {
				
				DocumentLogAttributes documentLogAttributes = DocumentCommons.getDocumentLogAttributes(documentLocationRealName, documentNode);
				
				String logMessage = "S-a cerut versiunea cu numarul [" + versionNumber + "] pentru documentul cu " +
					"atributele " + documentLogAttributes + ", insa aceasta versiune NU exista.";
				LOGGER.error(logMessage, "getDocumentFromVersion", userSecurity);
				
				throw new AppException(DOCUMENT_VERSION_NOT_FOUND);
			}
		} catch (AppException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentFromVersion", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
		
		return document;
	}
	
	@Override
	public void setReadOnly(String documentLocationRealName, String documentId) throws AppException {
		
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			// Obtine ACL-ul documentului.
			ACL acl = SecurityCommons.getAclFromNode(documentNode);
			// Transforma toate permisiunile in reader.
			SecurityUtils.setReadOnly(acl);
			// Seteaza noile permisiuni pe document.
			SecurityCommons.setAclForNode(documentNode, acl);
			// Salveaza modificarile.
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "setReadOnly");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void replaceUserPermission(String documentLocationRealName,
			Long documentTypeId, Long oldUserId, Long newUserId)
			throws AppException {
		
		Session session = null;
		try {
			
			String oldUserIdAsString = oldUserId.toString();
			String newUserIdAsString = newUserId.toString();
			
			session = repository.login(credentials, documentLocationRealName);
			
			String query = DocumentsForPermissionReplacementQueryBuilder.getQuery(documentTypeId);
			NodeIterator resultNodesAsIterator = XPathQueryTemplate.find(session, query);
			
			Set<String> idsForDocumentsWithReplacements = Sets.newHashSet();
			
			while (resultNodesAsIterator.hasNext()) {
				Node documentNode = resultNodesAsIterator.nextNode();
				ACL acl = SecurityCommons.getAclFromNode(documentNode);
				for (Permission permission : acl.getPermissions()) {
					if ((permission.getEntityType() == AppConstants.ACL_ENTITY_TYPE_USER)
							&& permission.getEntityId().equals(oldUserIdAsString)) {
						
						permission.setEntityId(newUserIdAsString);
						
						String documentId = documentNode.getIdentifier();
						idsForDocumentsWithReplacements.add(documentId);
					}
				}
				SecurityCommons.setAclForNode(documentNode, acl);
			}
			
			session.save();
			
			if (!idsForDocumentsWithReplacements.isEmpty()) {
				String logMessage = "S-au inlocuit permisiunile pentru documentele " +
					"de tipul cu ID-ul [" + documentTypeId + "], " +
					"din spatiul de lucru cu numele 'real' [" + documentLocationRealName + "]. " +
					"ID user vechi: [" + oldUserId + "]. ID user nou: [" + newUserId + "]. " +
					"ID-urile documentelor in care s-au inlocuit permisiunile: " +
					"[" + StringUtils.join(idsForDocumentsWithReplacements, ", ") + "].";
				LOGGER.info(logMessage, "replaceUserPermission");
			} else {
				String logMessage = "Nu s-au gasit documente de tipul cu ID-ul [" + documentTypeId + "], "
					+ "cu permisiuni de inlocuit in spatiul de lucru cu numele 'real' [" + documentLocationRealName + "]. " +
					"ID user vechi: [" + oldUserId + "]. ID user nou: [" + newUserId + "].";
				LOGGER.warn(logMessage, "replaceUserPermission");
			}
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "replaceUserPermission");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public Document getDocumentForAutomaticAction(String documentLocationRealName, String documentId) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			Node documentNode = session.getNodeByIdentifier(documentId);
			DocumentType  documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			Document document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
			return document;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentForAutomaticAction");
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public void checkinForAutomaticAction(Document document, boolean isVersionable, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, document.getDocumentLocationRealName());
			Node documentNode = session.getNodeByIdentifier(document.getId());
			
			document.setLastUpdate(Calendar.getInstance());
			
			SecurityCommons.setAclForNode(documentNode, document.getAcl());
			DocumentType  documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			DocumentCommons.setDocumentPropertiesToNode(document, documentType, documentNode);
			
			if (!isVersionable) {
				DocumentCommons.deleteAllVersions(documentNode);
			}
			DocumentCommons.addVersionNode(documentNode, null, userSecurity, documentType);
			
			DocumentCommons.unlock(documentNode);
			
			session.save();
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "checkinForAutomaticAction");
			throw new AppException();
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public DocumentLogAttributes getDocumentLogAttributes(String documentLocationRealName, String documentId) {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			Node documentNode = session.getNodeByIdentifier(documentId);
			return DocumentCommons.getDocumentLogAttributes(documentLocationRealName, documentNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public String getDocumentName(String documentLocationRealName, String documentId) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			try {
				Node documentNode = session.getNodeByIdentifier(documentId);
				return DocumentCommons.getName(documentNode);
			} catch (ItemNotFoundException infe) {
				throw new AppException(AppExceptionCodes.JR_ItemNotFoundException);
			} catch (Exception e) {
				throw e;
			}
		} catch (AppException appEx) {
			throw appEx;
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentName");
			throw new RuntimeException(e);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public boolean existDocumentsByDocumentType(Long documentTypeId) {
		throw new RuntimeException("not implemented");
	}
	
	
	@Override
	public Document getDocumentById(String documentId, String documentLocationRealName) throws AppException {
		Document document = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentLocationRealName);
			// Ia nodul documentului.
			Node documentNode = session.getNodeByIdentifier(documentId);
			
			DocumentType documentType = documentTypeDao.find(DocumentCommons.getDocumentTypeId(documentNode));
			document = DocumentCommons.getDocumentFromNode(documentLocationRealName, documentNode, documentType);
			
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDspDocumentById");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return document;
	}
	
	@Override
	public List<DocumentIdentifier> getAvailableDocumentsForArchivingByNoWorkflowAndBeforeDate(Set<String> workspaceNames, Set<Long> documentTypeIds, Date beforeDate) {
		if (CollectionUtils.isEmpty(workspaceNames)) {
			throw new RuntimeException("workspace names cannot be empty/null");
		}
		List<DocumentIdentifier> results = new LinkedList<>();		
		for (String workspaceName : workspaceNames) {
			AvailableDocumentsForArchivingByWithoutWorkflowAndBeforeDateSearcher searcher = new AvailableDocumentsForArchivingByWithoutWorkflowAndBeforeDateSearcher(repository, credentials, documentTypeIds, beforeDate);
			List<DocumentIdentifier> ids = searcher.search(workspaceName);
			if (CollectionUtils.isNotEmpty(ids)) {
				results.addAll(ids);
			}
		}
		return results;
	}
	
	@Override
	public void deleteDocument(DocumentIdentifier documentIdentifier) throws AppException {
		Session session = null;
		try {
			
			session = repository.login(credentials, documentIdentifier.getDocumentLocationRealName());
			
			Node documentNode = session.getNodeByIdentifier(documentIdentifier.getDocumentId());
			String documentName = DocumentCommons.getName(documentNode);
			
			documentNode.remove();
			session.save();
			
			LOGGER.info("Documentul [" + documentName + "] (documentLocationRealName: " +  documentIdentifier.getDocumentLocationRealName() + ", documentId: " + documentIdentifier.getDocumentId() + ") a fost sters.", "deleteDocument");
		
		} catch (ItemNotFoundException inotfe) {
			LOGGER.error("Exceptie - document negasit", inotfe, "deleteDocument(DocumentIdentifier)");		
			throw new AppException(AppExceptionCodes.JR_ItemNotFoundException);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "deleteDocument(DocumentIdentifier)");			
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
	
	@Override
	public AdminUpdateDocument getAdminUpdateDocument(DocumentIdentifier documentIdentifier, SecurityManager userSecurity) throws AppException {
		AdminUpdateDocument document = null;

		Session session = null;
		try {
			session = repository.login(credentials, documentIdentifier.getDocumentLocationRealName());
			
			Node documentNode = session.getNodeByIdentifier(documentIdentifier.getDocumentId());
			document = new AdminUpdateDocument();
			
			Long documentTypeId = DocumentCommons.getDocumentTypeId(documentNode);
			
			DocumentType documentType = documentTypeDao.find(documentTypeId);
			
			document.setDocumentLocationRealName(documentIdentifier.getDocumentLocationRealName());
			document.setId(documentNode.getIdentifier());
			document.setName(DocumentCommons.getName(documentNode));
			document.setDescription(DocumentCommons.getDescription(documentNode));
			document.setDocumentTypeId(documentTypeId);
			
			document.setMetadataInstances(DocumentCommons.getDocumentMetadatasFromNode(documentType, documentNode));
			document.setCollectionInstanceListMap(DocumentCommons.getDocumentMetadataCollectionsFromNode(documentType, documentNode));
			
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "getDocumentInfoForAdminUpdate");
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}

		return document;
	}
	
	@Override
	public void updateDocument(AdminUpdateDocument document, SecurityManager userSecurity) throws AppException {
		Session session = null;
		try {
			session = repository.login(credentials, document.getDocumentLocationRealName());
			
			DocumentType documentType = documentTypeDao.find(document.getDocumentTypeId());
			Node documentNode = session.getNodeByIdentifier(document.getId());
			
			documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME, document.getName());
			documentNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, (document.getDescription() != null) ? document.getDescription() : "");
			DocumentCommons.setDocumentMetadatasToNode(document.getMetadataInstances(), documentType.getMetadataDefinitions(), documentNode);
			DocumentCommons.setDocumentMetadataCollectionsToNode(document.getCollectionInstanceListMap(), documentType.getMetadataCollections(), documentNode);
			
			// Update last stable version.
			Node lastStableVersionNode = null;
			NodeIterator versionNodesIterator = documentNode.getNodes(JackRabbitConstants.VERSION_NODE_PREFIX + "*");
			while (versionNodesIterator.hasNext()) {
				Node versionNode = versionNodesIterator.nextNode();				
				boolean lastStableVersion = versionNode.getProperty(JackRabbitConstants.VERSION_PROPERTY_LAST_STABLE_VERSION).getBoolean();
				if (lastStableVersion) {
					lastStableVersionNode = versionNode;
					break;
				}
			}
			if (lastStableVersionNode != null) {
				lastStableVersionNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_NAME, document.getName());
				lastStableVersionNode.setProperty(JackRabbitConstants.ENTITY_PROPERTY_DESCRIPTION, (document.getDescription() != null) ? document.getDescription() : "");
				DocumentCommons.setDocumentMetadatasToNode(document.getMetadataInstances(), documentType.getMetadataDefinitions(), lastStableVersionNode);
				DocumentCommons.setDocumentMetadataCollectionsToNode(document.getCollectionInstanceListMap(), documentType.getMetadataCollections(), lastStableVersionNode);
			}
			
			session.save();
			
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "adminUpdateDocument", userSecurity);
			throw new AppException(AppExceptionCodes.APPLICATION_ERROR);
		} finally {
			JackRabbitCommons.logout(session);
		}
	}
}