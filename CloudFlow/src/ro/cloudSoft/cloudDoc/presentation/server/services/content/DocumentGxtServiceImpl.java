package ro.cloudSoft.cloudDoc.presentation.server.services.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentAttachmentDetailDao;
import ro.cloudSoft.cloudDoc.dao.content.ImportedDocumentDao;
import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.DocumentVersionInfo;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentAttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtPermissionBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.server.builders.model.content.DocumentViewModelsBuilder;
import ro.cloudSoft.cloudDoc.presentation.server.converters.SecurityManagerConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowInstanceResponseConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowStateConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentTypeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentTypeTemplateConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DocumentGxtServiceImpl extends GxtServiceImplBase implements DocumentGxtService, InitializingBean {
	
	private DocumentService documentService;
	private DocumentTypeService documentTypeService;
	private UserService userService;
	private WorkflowExecutionService workflowExecutionService;
	private WorkflowService workflowService;
	private NomenclatorService nomenclatorService;
	private GroupService groupService;
	private CalendarService calendarService;
	private ProjectService projectService;
	private WorkflowInstanceDao workflowInstanceDao;
	private ImportedDocumentDao importedDocumentDao;
	private DocumentAttachmentDetailDao documentAttachmentDetailDao;
	
	private WorkflowConverter workflowConverter;
	
	private BusinessConstants businessConstants;
	private FileSystemAttachmentManager fileSystemAttachmentManager;
	
	public DocumentGxtServiceImpl() {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentService,
			documentTypeService,
			userService,
			workflowExecutionService,
			workflowService,
			nomenclatorService,
			groupService,
			projectService,
			businessConstants,
			fileSystemAttachmentManager
		);
	}

	@Override
	public String save(DocumentModel documentModel, String parentFolderId, String documentLocationRealName,
			List<String> namesForAttachmentsToDelete) throws PresentationException {
		
		// TODO Codul de mai jos este comentat pana se va rezolva cu securitatea, apoi trebuie revenit la implementare.
		// SessionAttachmentManager.removeAttachments(this.getSession(), namesForAttachmentsToDelete);
		// List<Attachment> attachments = SessionAttachmentManager.getAttachments(this.getSession());
		
		try {
			
			Document document = DocumentConverter.getDocumentFromModel(documentModel);
			List<Attachment> attachments = prepareAttachmentsFromModels(documentModel.getAttachments());
			
			String documentId = documentService.save(document, attachments, parentFolderId, documentLocationRealName, namesForAttachmentsToDelete, getSecurity());
			
			fileSystemAttachmentManager.remove(namesForAttachmentsToDelete, getSecurity().getUserUsername());
					
			return documentId;
			
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	private List<Attachment> prepareAttachmentsFromModels(List<DocumentAttachmentModel> attachmentModels) {
		List<Attachment> attachments = new LinkedList<>();
		if (CollectionUtils.isNotEmpty(attachmentModels)) {
			for (DocumentAttachmentModel attachmentModel : attachmentModels) {
				if (attachmentModel.getIsNew()) {
					Attachment attachment = fileSystemAttachmentManager.getAttachment(attachmentModel.getName(), getSecurity().getUserUsername());
					attachments.add(attachment);
				}
			}
		}
		return attachments;
	}
	
	@Override
	public DocumentModel checkout(String documentId, String documentLocationRealName) throws PresentationException {
		try {
			Document document = documentService.checkout(documentId, documentLocationRealName, getSecurity());
			DocumentModel documentModel = DocumentConverter.getModelFromDocument(document, documentAttachmentDetailDao);
			return documentModel;
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public DocumentModel undoCheckout(String documentId, String documentLocationRealName) throws PresentationException {
		try {
			Document document = documentService.undoCheckout(documentId, documentLocationRealName, getSecurity());
			DocumentModel documentModel = DocumentConverter.getModelFromDocument(document, documentAttachmentDetailDao);
			return documentModel;
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public String checkin(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName,
			Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete)
			throws PresentationException {
		
		//SessionAttachmentManager.removeAttachments(this.getSession(), namesForAttachmentsToDelete);
		//List<Attachment> attachments = SessionAttachmentManager.getAttachments(this.getSession());
			
		try {
			Document document = DocumentConverter.getDocumentFromModel(documentModel);
			
			List<Attachment> attachments = prepareAttachmentsFromModels(documentModel.getAttachments());
			
			String id = documentService.checkin(document, attachments, isVersionable, parentFolderId,
				documentLocationRealName, definitionIdsForAutoNumberMetadataValuesToGenerate,
				namesForAttachmentsToDelete, getSecurity());
			
			fileSystemAttachmentManager.remove(namesForAttachmentsToDelete, getSecurity().getUserUsername());	
			
			return id;			
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public PagingLoadResult<DocumentViewModel> getPagedDocumentsFromFolder(String folderId, boolean sameType,
			String documentLocationRealName, PagingLoadConfig pagingLoadConfig) throws PresentationException {
		
		try {
			PagingList<Document> pagedDocumentList = documentService.getPagedDocumentsFromFolder(
				documentLocationRealName, folderId, pagingLoadConfig.getOffset(),
				pagingLoadConfig.getLimit(), getSecurity());
			List<DocumentViewModel> documentViewModels = new DocumentViewModelsBuilder(pagedDocumentList.getElements(),
				sameType, this.documentTypeService, this.userService, this.nomenclatorService, this.groupService, this.documentService, this.calendarService, this.projectService,this.workflowInstanceDao, this.importedDocumentDao, this.getSecurity()).getViewModels();
			return new BasePagingLoadResult<DocumentViewModel>(documentViewModels,
				pagedDocumentList.getOffset(), pagedDocumentList.getTotalCount());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public DocumentModel getDocumentById(String documentId, String documentLocationRealName) throws PresentationException {
		try 
		{
			Document document = documentService.getDocumentById(documentId, documentLocationRealName, getSecurity());
			DocumentModel documentModel = DocumentConverter.getModelFromDocument(document, documentAttachmentDetailDao);
			return documentModel;
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public ExtendedDocumentModel getExtendedDocumentById(String documentId, String documentLocationRealName) throws PresentationException {
	    
		ExtendedDocumentModel extDocModel = new ExtendedDocumentModel();
	    
	    SecurityManager userSecurity = getSecurity();
		
	    try {
		    Document document = documentService.getDocumentById(documentId, documentLocationRealName, userSecurity);
		    DocumentModel documentModel = DocumentConverter.getModelFromDocument(document, documentAttachmentDetailDao);
		    extDocModel.setDocumentModel( documentModel );
		    
		    DocumentType docType = documentTypeService.getDocumentTypeById( documentModel.getDocumentTypeId() );
		    DocumentTypeModel documentTypeModel = DocumentTypeConverter.getModelFromDocumentType(docType);
		    List<DocumentTypeTemplate> templates = documentTypeService.getTemplates(documentModel.getDocumentTypeId());
		    List<DocumentTypeTemplateModel> templateModels = new ArrayList<DocumentTypeTemplateModel>();
		    if (CollectionUtils.isNotEmpty(templates)) {
		    	
		    	Map<String, MetadataWrapper> metadataWrapperByMetadataName = new HashMap<String, MetadataWrapper>(); 
				for (MetadataInstanceModel metadataInstanceModel : documentModel.getMetadataInstances()) {
					MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionById(docType, metadataInstanceModel.getMetadataDefinitionId());
					metadataWrapperByMetadataName.put(metadataDefinition.getName(), new MetadataWrapper(metadataDefinition.getMetadataType(), metadataInstanceModel.getValues()));
				}
				
		    	for (DocumentTypeTemplate template : templates) {		    		
		    		if (StringUtils.isNotBlank(template.getExportAvailabilityExpression())) {
		    			boolean available = ExpressionEvaluator.evaluateDocumentExpression(template.getExportAvailabilityExpression(), metadataWrapperByMetadataName);
		    			if (!available) {
		    				continue;
		    			}
		    		}		    		
					DocumentTypeTemplateModel templateModel = DocumentTypeTemplateConverter.getModelFromDocumentTypeTemplate(template);
					templateModels.add(templateModel);
			    }
		    }		    
		    documentTypeModel.setTemplates(templateModels);
	
		    extDocModel.setDocumentTypeModel(documentTypeModel);
		    
		    boolean isDocumentImported = false;
		    Workflow workflow = workflowService.getWorkflowForDocument(documentLocationRealName, documentId);
		    if (workflow == null) {
		    	isDocumentImported = documentService.isDocumentImported(new DocumentIdentifier(documentLocationRealName, documentId));
		    	if (!isDocumentImported) {
		    		workflow = workflowService.getWorkflowForDocument(documentLocationRealName, documentId, docType.getId());
		    	}
		    }
		    if (workflow != null){
		    	extDocModel.setWorkflowModel(workflowConverter.getModelFromWorkflow(workflow));
	
			    Boolean sendingRights = workflowExecutionService.checkSendingRights(document, workflow, userSecurity);
			    extDocModel.setSendingRights(sendingRights);		    
			    
			    WorkflowState workflowState = workflowExecutionService.getCurrentState(workflow, document, userSecurity);
			    extDocModel.setWorkflowStateModel(WorkflowStateConverter.getModelFromWorkflowState(workflowState));
		    } else {
		    	extDocModel.setSendingRights(false);
		    	extDocModel.setWorkflowModel(null);
		    }
		    
		    // TODO - Zona asta poate ar trebui refacuta.
		    
		    extDocModel.setCanUserEdit(false);
		    boolean isDocumentWorkflowFinished = documentService.isDocumentWorkflowFinished(new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId()));
		    if (!isDocumentImported && !isDocumentWorkflowFinished) {
		    	if (isUserAdmin(userSecurity)) {
			    	extDocModel.setCanUserEdit(true);
			    } else {		    	
			    	SecurityManagerModel userSecurityModel = SecurityManagerConverter.getModelFromSecurityManager(userSecurity, businessConstants);
			    	extDocModel.setCanUserEdit(GwtPermissionBusinessUtils.canEdit(documentModel.getPermissions(), userSecurityModel));
			    }
		    }
		    
		    boolean canUserAccessLockedDocument = false;		    
		    if (StringUtils.isNotBlank(document.getLockedBy())) {		    	
		    	if (isUserAdmin(userSecurity)) {
		    		canUserAccessLockedDocument = true;
		    	} else {
		    		canUserAccessLockedDocument = document.getLockedBy().equals(userSecurity.getUserIdAsString());
		    	}
		    }
		    extDocModel.setCanUserAccessLockedDocument(canUserAccessLockedDocument);
		    
	    } catch (AppException ae) {
	    	throw PresentationExceptionUtils.getPresentationException(ae);
	    }
	    
	    return extDocModel;
	}
	
	private boolean isUserAdmin(SecurityManager userSecurity) {
		String groupNameAdmins = businessConstants.getGroupNameAdmins();
		return userSecurity.getGroupNames().contains(groupNameAdmins);
	}

	@Override
	public void moveDocument(String documentId, String folderDestinationId, String documentLocationRealName) throws PresentationException {
		try {
			documentService.moveDocumentToFolder(folderDestinationId, documentId, documentLocationRealName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public void deleteDocument(String documentId, String documentLocationRealName) throws PresentationException {
		try {
			documentService.deleteDocument(documentId, documentLocationRealName, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	
	@Override
	public WorkflowInstanceResponseModel sendDocumentToWorkflow(Long workflowId, String transitionName,
			String manualAssignmentDestinationId, DocumentModel documentModel) throws PresentationException {
		
		Document document = DocumentConverter.getDocumentFromModel(documentModel);
		
		WorkflowInstanceResponse response = null;
		try {
			response = documentService.sendDocumentToWorkflow(workflowId,
				transitionName, manualAssignmentDestinationId, document, getSecurity());			
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}		
		return WorkflowInstanceResponseConverter.getResponseModelFromResponse(response);
	}
	
	@Override
	public List<DocumentVersionInfoViewModel> getDocumentVersions(String documentId, String documentLocationRealName ) throws PresentationException {
		List<DocumentVersionInfoViewModel> resultList = new ArrayList<DocumentVersionInfoViewModel>();
		List<DocumentVersionInfo> divList=new ArrayList<DocumentVersionInfo>();
		try {
			divList=documentService.getVersionInfos(documentId, documentLocationRealName, getSecurity());
			// Trebuie sa iau toate ID-urile utilizatorilor ale carui nume trebuie afisat.
			Set<Long> userIds = new HashSet<Long>();
			// Pentru fiecare document...
			for (DocumentVersionInfo div : divList) {
				// Ia ID-ul autorului.
				Long authorId = Long.valueOf(div.getAuthorId());
				// Adauga ID-ul autorului in set.
				userIds.add(authorId);
			}
			// Obtin o "harta" cu numele tuturor utilizatorilor de care am nevoie.
			Map<Long, String> userNameMap = userService.getUsersNameMap(userIds, getSecurity());
			// Pentru fiecare document...
			for (DocumentVersionInfo div : divList) {
				// Iau ID-ul autorului.
				Long authorId = Long.valueOf(div.getAuthorId());
				// Iau numele autorului.
				String authorName = userNameMap.get(authorId);
				// Setez numele autorului in document.
				DocumentVersionInfoViewModel result=new DocumentVersionInfoViewModel();
				result.setVersionNumber(div.getNumber().toString());
				result.setVersionAuthor(authorName);
				result.setVersionCreationDate(div.getDate().getTime());
				resultList.add(result);
			}
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		return resultList; 
	}
	
	@Override
	public DocumentModel getDocumentFromVersion(String versionNR, String documentId, String documentLocationRealName) throws PresentationException {
		try {
			Document document = documentService.getDocumentFromVersion(versionNR, documentId, documentLocationRealName, getSecurity());
			DocumentModel documentModel = DocumentConverter.getModelFromDocument(document, documentAttachmentDetailDao);
			return documentModel;
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public DocumentModel checkinAndGetDocument(DocumentModel documentModel, boolean isVersionable, String parentFolderId, String documentLocationRealName, 
			Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate, List<String> namesForAttachmentsToDelete) throws PresentationException {
		
		String documentId = this.checkin(documentModel, isVersionable, parentFolderId, documentLocationRealName,
			definitionIdsForAutoNumberMetadataValuesToGenerate, namesForAttachmentsToDelete);
		return this.getDocumentById(documentId, documentLocationRealName);	
	}
	
	/** {@inheritDoc} 
	 * @throws IOException */
	@Override
	public void clearTemporaryAttachments() throws PresentationException, IOException {

		// TODO Codul de mai jos este comentat pana se va rezolva cu securitatea, apoi trebuie revenit la implementare.
		// SessionAttachmentManager.clear(getSession());

		FileSystemAttachmentManager fileSystemAttachmentManager = SpringUtils.getBean("fileSystemAttachmentManager");
		fileSystemAttachmentManager.clear(getSecurity().getUserUsername());
		
	}
	
	@Override
	public boolean existDocumentsOfType(Long documentTypeId) throws PresentationException {
		try {
			return documentService.existDocumentsOfType(documentTypeId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}

	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setWorkflowConverter(WorkflowConverter workflowConverter) {
		this.workflowConverter = workflowConverter;
	}

	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}

	public void setFileSystemAttachmentManager(FileSystemAttachmentManager fileSystemAttachmentManager) {
		this.fileSystemAttachmentManager = fileSystemAttachmentManager;
	}

	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}

	public void setImportedDocumentDao(ImportedDocumentDao importedDocumentDao) {
		this.importedDocumentDao = importedDocumentDao;
	}
	public void setDocumentAttachmentDetailDao(DocumentAttachmentDetailDao documentAttachmentDetailDao) {
		this.documentAttachmentDetailDao = documentAttachmentDetailDao;
	}	
}