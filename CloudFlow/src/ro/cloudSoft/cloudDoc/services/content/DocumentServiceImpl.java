package ro.cloudSoft.cloudDoc.services.content;

import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.CANNOT_DELETE_DOCUMENT_ACTIVE_WORKFLOW_INSTANCE_EXISTS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.AppExceptionUtils;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentAttachmentDetailDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeTemplateDao;
import ro.cloudSoft.cloudDoc.dao.content.ImportedDocumentDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.AdminUpdateDocument;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentAttachmentDetail;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.DocumentValidationDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentVersionInfo;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.TemplateType;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.generators.AutoNumberMetadataValueGenerator;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;
import ro.cloudSoft.cloudDoc.plugins.bpm.WorkflowInstanceResponse;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentLocationPlugin;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AdminUpdateDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCollectionValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.bpm.GwtWorkflowStateBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentConverter;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata.AutocompleteMetadata;
import ro.cloudSoft.cloudDoc.services.jasperReports.JasperReportsConstants;
import ro.cloudSoft.cloudDoc.services.jasperReports.JasperReportsService;
import ro.cloudSoft.cloudDoc.services.jasperReports.JsonJRDataSourceBuilder;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.services.xDocReports.XDocReportsService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.MetadataValueFormatterForDocumentExport;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

public class DocumentServiceImpl implements DocumentService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DocumentServiceImpl.class);
	
	private WorkflowExecutionService workflowExecutionService;
	
	private AuditService auditService;
	
	private DocumentLocationPlugin documentLocationPlugin;
	private DocumentPlugin documentPlugin;
	
	private UserService userService;
	
	private SecurityManagerFactory securityManagerFactory;
	
	private BusinessConstants businessConstants;
	
	private AutoNumberMetadataValueGenerator autoNumberMetadataValueGenerator;

	private boolean timesheetsForLeavesIntegrationEnabled;
	
	private DocumentTypeDao documentTypeDao;
	private WorkflowService workflowService;
	
	private JasperReportsService jasperReportsService;
	private DocumentTypeTemplateDao documentTypeTemplateDao;
	
	private NomenclatorService nomenclatorService;
	private GroupService groupService;
	private DocumentService documentService;
	private CalendarService calendarService;
	private ProjectService projectService;
	private DocumentTypeService documentTypeService;
	
	private ImportedDocumentDao importedDocumentDao;
	private XDocReportsService xDocReportsService;
	private ArbConstants arbConstants;
	private WorkflowInstanceDao workflowInstanceDao;
	private DocumentAttachmentDetailDao documentAttachmentDetailDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			workflowExecutionService,
			
			auditService,
			
			documentLocationPlugin,
			documentPlugin,
			
			autoNumberMetadataValueGenerator,
			
			userService,
			
			securityManagerFactory,
			
			businessConstants,
			
			timesheetsForLeavesIntegrationEnabled,
			
			nomenclatorService,
			groupService,
			documentService,
			calendarService,
			projectService,
			documentTypeService,
			
			importedDocumentDao,
			xDocReportsService,
			arbConstants,
			workflowInstanceDao
		);
	}

    @Override
    public Document getDocumentById(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
        Document document = documentPlugin.getDocumentById(documentId, documentLocationRealName, userSecurity);
        auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.READ);
        return document;
    }

    @Override
    public void deleteDocument(String documentId, String documentLocationRealName,
    		SecurityManager userSecurity) throws AppException {
    	
    	if (workflowExecutionService.hasActiveWorkflowInstance(documentLocationRealName, documentId, userSecurity)) {
    		throw new AppException(CANNOT_DELETE_DOCUMENT_ACTIVE_WORKFLOW_INSTANCE_EXISTS);
    	}
    	
    	Document document = documentPlugin.getDocumentById(documentId, documentLocationRealName, userSecurity);
    	
        documentPlugin.deleteDocument(documentId, documentLocationRealName, userSecurity);
        
        documentAttachmentDetailDao.deleteAllOfDocument(new DocumentIdentifier(documentLocationRealName, documentId));
        
        auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.DELETE);
    }

    @Override
    public void moveDocumentToFolder(String folderId, String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	
        documentPlugin.moveDocumentToFolder(folderId, documentId, documentLocationRealName, userSecurity);
        
        Document document = documentPlugin.getDocumentById(documentId, documentLocationRealName, userSecurity);
        auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.MOVE);
    }
    
    @Override
    public PagingList<Document> getPagedDocumentsFromFolder(String documentLocationRealName,
    		String folderId, int offset, int limit, SecurityManager userSecurity)
    		throws AppException {
    	
    	return documentPlugin.getPagedDocumentsFromFolder(documentLocationRealName, folderId, offset, limit, userSecurity);
    }

    
    @Override
	public PagingList<Document> getPagedDocuments(DocumentFilterModel filter, SecurityManager userSecurity) throws AppException {
    	List<String> documentIdsFilter = Collections.EMPTY_LIST;
    	if (StringUtils.isNotEmpty(filter.getStatusFilterValue()) && filter.getDocumentTypeId() != null && filter.getDocumentLocationRealName() != null) {
    		if (filter.getStatusFilterValue().equals(WorkflowInstance.STATUS_IMPORTED)) {
    			documentIdsFilter = importedDocumentDao.getDocumentIdsByDocumentLocationRealName(filter.getDocumentLocationRealName());
    		} else {
    			documentIdsFilter = workflowInstanceDao.getDocumentIdsByDocTypeAndStatus(filter.getDocumentTypeId(), filter.getStatusFilterValue(), filter.getDocumentLocationRealName());
    		}
    		
    		if (CollectionUtils.isEmpty(documentIdsFilter)) {
    			return new PagingList<>(0, 0, Collections.EMPTY_LIST);
    		}
    	}
    	return documentPlugin.getPagedDocuments(filter, userSecurity, documentIdsFilter);
	}

	@Override
	public Document checkout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		checkDocumentAllowOperations(documentLocationRealName, documentId);
		Document document = documentPlugin.checkout(documentId, documentLocationRealName, userSecurity);
		auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.CHECK_OUT);
		return document;
	}
    
    @Override
    public Document undoCheckout(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	Document document = documentPlugin.undoCheckout(documentId, documentLocationRealName, userSecurity);
    	auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.UNDO_CHECK_OUT);
    	return document;
    }
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String checkin(Document document, Collection<Attachment> attachments, boolean isVersionable, String parentFolderId,
    		String documentLocationRealName, Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate,
    		List<String> namesForAttachmentsToDelete, SecurityManager userSecurity) throws AppException {
    	
    	checkDocumentAllowOperations(document.getDocumentLocationRealName(), document.getId());
    	
    	boolean isDocumentNew = document.isNew();
    	
    	autoNumberMetadataValueGenerator.populateDocumentWithGeneratedValues(document, definitionIdsForAutoNumberMetadataValuesToGenerate);
    	    	
    	String documentId = documentPlugin.checkin(document, attachments, isVersionable, parentFolderId,
			documentLocationRealName, namesForAttachmentsToDelete, userSecurity);
    	
    	updateDocumentAttachmentDetails(documentLocationRealName, documentId, document.getWorkflowStateId(), attachments, namesForAttachmentsToDelete, userSecurity);
    	
    	AuditEntityOperation operation = (isDocumentNew) ? AuditEntityOperation.CHECK_IN_NEW : AuditEntityOperation.CHECK_IN_EXISTING;
    	auditService.auditDocumentOperation(userSecurity, document, operation);
    	
    	return documentId;
    }

    @Override
    public void setDocumentState(String documentId, String documentLocationRealName, String idWorkflowState) throws AppException {
    	 documentPlugin.setDocumentState(documentId, documentLocationRealName, idWorkflowState);
    }

    @Override
    public String save(Document document, Collection<Attachment> attachments, String parentFolderId,
    	    String documentLocationRealName, List<String> namesForAttachmentsToDelete, SecurityManager userSecurity)
			throws AppException {
    	
    	checkDocumentAllowOperations(document.getDocumentLocationRealName(), document.getId());
    	
    	boolean isDocumentNew = document.isNew();
    	
    	String documentId = documentPlugin.save(document, attachments, parentFolderId, documentLocationRealName, namesForAttachmentsToDelete, userSecurity);
    	
    	updateDocumentAttachmentDetails(documentLocationRealName, documentId, document.getWorkflowStateId(), attachments, namesForAttachmentsToDelete, userSecurity);
    	
    	AuditEntityOperation operation = (isDocumentNew) ? AuditEntityOperation.SAVE_NEW : AuditEntityOperation.SAVE_EXISTING;
    	auditService.auditDocumentOperation(userSecurity, document, operation);
    	
    	return documentId;
    }
    
    private void updateDocumentAttachmentDetails(String documentLocationrealName, String documentId, Long workflowStateId, Collection<Attachment> newAttachments, List<String> namesForAttachmentsToDelete, SecurityManager userSecurity) {
    	
    	String documentWorkflowStateCode = null;
		if (workflowStateId != null) {
			WorkflowState documentWorkflowState = workflowService.getWorkflowStateById(workflowStateId);
			documentWorkflowStateCode = documentWorkflowState.getCode();
		}
		
		User user = userService.getUserById(userSecurity.getUserId());
		
		DocumentIdentifier documentIdentifier = new DocumentIdentifier(documentLocationrealName, documentId);
		if (CollectionUtils.isNotEmpty(namesForAttachmentsToDelete)) {
			for (String attachmentName : namesForAttachmentsToDelete) {
				documentAttachmentDetailDao.delete(documentIdentifier, attachmentName);
			}
		}
		
		if (CollectionUtils.isNotEmpty(newAttachments)) {
			for (Attachment attachment : newAttachments) {
				DocumentAttachmentDetail documentAttachmentDetail = new DocumentAttachmentDetail();
				documentAttachmentDetail.setAttachmentName(attachment.getName());
				documentAttachmentDetail.setAttachedBy(user);
				documentAttachmentDetail.setDocumentId(documentId);
				documentAttachmentDetail.setDocumentLocationRealName(documentLocationrealName);
				documentAttachmentDetail.setDocumentWorkflowStateCode(documentWorkflowStateCode);					
				documentAttachmentDetailDao.save(documentAttachmentDetail);
			}
		}
    }
    
    private void checkDocumentAllowOperations(String documentLocationRealName, String documentId) throws AppException {
    	boolean isDocumentNew = StringUtils.isBlank(documentId);
    	if (!isDocumentNew) {
    		DocumentIdentifier di = new DocumentIdentifier(documentLocationRealName, documentId);
    		if (isDocumentImported(di) || isDocumentWorkflowFinished(di)) {
    			throw new AppException(AppExceptionCodes.INVALID_OPERATION);
    		}
    	}
    }
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public WorkflowInstanceResponse sendDocumentToWorkflow(Long workflowId, String transitionName,
    		String manualAssignmentDestinationId, Document document, SecurityManager userSecurity)
			throws AppException {
    	
    	WorkflowInstanceResponse response = null;
		WorkflowInstance workflowInstance = workflowExecutionService.getWorkflowInstance(document.getId(), userSecurity);
		if (workflowInstance != null) {
			response = workflowExecutionService.completeWorkflowInstanceTask(workflowInstance, transitionName, manualAssignmentDestinationId, document, userSecurity);				
		} else {
			response = workflowExecutionService.startWorkflowInstance(workflowId, document, document.getDocumentLocationRealName(), userSecurity);
		}
		
		if (response.wasDocumentSent()) {
			auditService.auditDocumentOperation(userSecurity, document, AuditEntityOperation.SEND);
		}
		
		return response;
    }
    
    @Override
    public WorkflowState getCurrentWorkflowState(Long workflowId, Document document, SecurityManager userSecurity) throws AppException {
    	WorkflowInstance workflowInstance = workflowExecutionService.getWorkflowInstance(document.getId(), userSecurity);
		return workflowExecutionService.getCurrentState(workflowInstance.getWorkflow(), document, userSecurity);
    }

    @Override
	public Attachment getAttachment(String documentId, String attachmentName,
			String documentLocationRealName, SecurityManager userSecurity)
    		throws AppException {
    	
		return documentPlugin.getAttachment(documentId, attachmentName, documentLocationRealName, userSecurity);
	}
    
    @Override
    public Attachment getAttachmentFromVersion(String documentId, Integer versionNumber, String attachmentName,
			String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	
    	return documentPlugin.getAttachmentFromVersion(documentId, versionNumber, attachmentName, documentLocationRealName, userSecurity);
    }
    
    @Override
    public Document restore(String documentId, int versionNumber, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	return documentPlugin.restore(documentId, versionNumber, documentLocationRealName, userSecurity);
    }
    
    @Override
    public List<DocumentVersionInfo> getVersionInfos(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	return documentPlugin.getVersionInfos(documentId, documentLocationRealName, userSecurity);
    }

    @Override
    public void changePermissionsForWorkflow(String documentId, String documentLocationRealName, String userSenderId, String userReceiverId) throws AppException {
    	documentPlugin.changePermissionsForWorkflow(documentId, documentLocationRealName, userSenderId, Collections.singletonList(Long.valueOf(userReceiverId)), null, null);
    }
    
    @Override
    public void changePermissionsForWorkflow(String documentId, String documentLocationRealName, String userSenderId, List<Long> userReceiverIds, List<Long> organizationUnitReceiverIds, List<Long> groupReceiverIds) throws AppException {
    	documentPlugin.changePermissionsForWorkflow(documentId, documentLocationRealName, userSenderId, userReceiverIds, organizationUnitReceiverIds, groupReceiverIds);
    }
    
    @Override
    public void addEditingPermissions(String documentId, String documentLocationRealName, Collection<Long> userIds,
    		Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException {
    	
    	documentPlugin.addEditingPermissions(documentId, documentLocationRealName, userIds, organizationUnitIds, groupIds);
    }
    
    @Override
    public void makeReadersIfEditors(String documentId, String documentLocationRealName, Collection<Long> userIds) throws AppException {
    	documentPlugin.makeReadersIfEditors(documentId, documentLocationRealName, userIds);
    }
    
    @Override
    public void addSupervisorPermission(String documentId,
	    String documentLocationRealName, List<String> supervisorsIds,
	    List<Long> ouSupervisorsIds, List<Long> groupSupervisorIds)
	    throws AppException
    {
	documentPlugin.addSupervisorPermission(documentId, documentLocationRealName,
		supervisorsIds, ouSupervisorsIds, groupSupervisorIds);

    }
    
    @Override
    public void addExtraViewers(String documentId, String documentLocationRealName, Collection<Long> userIds,
    		Collection<Long> organizationUnitIds, Collection<Long> groupIds) throws AppException {
    	
    	documentPlugin.addExtraViewers(documentId, documentLocationRealName, userIds, organizationUnitIds, groupIds);
    }

    @Override
    public Document getDocumentFromVersion(String versionNR, String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
    	 return documentPlugin.getDocumentFromVersion(versionNR, documentId, documentLocationRealName, userSecurity);
    }
    
    @Override
    public void setReadOnly(String documentLocationRealName, String documentId) throws AppException {
    	this.documentPlugin.setReadOnly(documentLocationRealName, documentId);
    }
    
    @Override
    public boolean existDocumentsOfType(Long documentTypeId, SecurityManager userSecurity) throws AppException {
    	return documentLocationPlugin.existDocumentsOfType(documentTypeId, userSecurity);
    }
    
    @Override
    public void replaceUserPermission(String documentLocationRealName,
    		Long documentTypeId, Long oldUserId, Long newUserId)
    		throws AppException {
    	
    	documentPlugin.replaceUserPermission(documentLocationRealName, documentTypeId, oldUserId, newUserId);
    }
    
    @Override
    public Document getDocumentForAutomaticAction(String documentLocationRealName, String documentId) throws AppException {
    	return documentPlugin.getDocumentForAutomaticAction(documentLocationRealName, documentId);
    }
    
    @Override
    public void checkinForAutomaticAction(Document document, boolean isVersionable, SecurityManager userSecurity) throws AppException {
    	documentPlugin.checkinForAutomaticAction(document, isVersionable, userSecurity);
    }
    
    @Override
    public DocumentLogAttributes getDocumentLogAttributes(String documentLocationRealName, String documentId) {
    	return documentPlugin.getDocumentLogAttributes(documentLocationRealName, documentId);
    }
    
    @Override
    public DocumentValidationResponseModel validateDocument(DocumentValidationRequestModel requestModel, SecurityManager userSecurity) throws AppException {   		
    	DocumentType documentType = this.documentTypeDao.find(requestModel.getDocumentTypeId());
    	return this.validateMetadatas(requestModel.getDocumentLocationRealName(), requestModel.getDocumentId(), requestModel.getDocumentTypeId(), 
    			documentType.getMetadataDefinitions(), requestModel.getMetatadaInstances(), documentType.getValidationDefinitions(), userSecurity);
    }
    
    @Override
    public DocumentValidationResponseModel validateDocumentCollection(DocumentCollectionValidationRequestModel requestModel, SecurityManager userSecurity) throws AppException {    	
    	MetadataCollection metadataCollection = this.documentTypeDao.getMetadataCollectionDefinition(requestModel.getMetadataCollectionDefinitionId());    	
    	return this.validateMetadatas(requestModel.getDocumentLocationRealName(), requestModel.getDocumentId(), requestModel.getDocumentTypeId(), 
    			metadataCollection.getMetadataDefinitions(), requestModel.getMetatadaInstances(), metadataCollection.getValidationDefinitions(), userSecurity);
    }
    
    @Override
    public String getDocumentName(String documentLocationRealName, String documentId) throws AppException {
    	return documentPlugin.getDocumentName(documentLocationRealName, documentId);
    }
    
	private DocumentValidationResponseModel validateMetadatas(String documentLocationRealName, String documentId, Long documentTypeId,
    			List<? extends MetadataDefinition> metadataDefinitions, List<MetadataInstanceModel> metadataInstances,  
    			List<DocumentValidationDefinition> validationDefinitions, SecurityManager userSecurity) throws AppException {
    	
    	List<String> messages = new ArrayList<String>();    	
    	
    	if (CollectionUtils.isNotEmpty(validationDefinitions)) {
    		
    		WorkflowState currentState = null;    		
    		if (documentId == null) {
    			Workflow workflow = workflowService.getWorkflowByDocumentType(documentTypeId);
    			currentState = workflowExecutionService.getCurrentState(workflow, null, userSecurity);
    		} else {
    			Workflow workflow = workflowService.getWorkflowForDocument(documentLocationRealName, documentId, documentTypeId);
    			Document document = documentPlugin.getDocumentById(documentId, documentLocationRealName, userSecurity);
    			currentState = workflowExecutionService.getCurrentState(workflow, document, userSecurity);
    		}
    		
    		Map<String, MetadataWrapper> metadataWrapperByMetadataName = new HashMap<String, MetadataWrapper>(); 
    		for (MetadataInstanceModel metadataInstanceModel : metadataInstances) {
    			MetadataDefinition metadataDefinition = findMetadataDefinitionById(metadataDefinitions, metadataInstanceModel.getMetadataDefinitionId());
    			metadataWrapperByMetadataName.put(metadataDefinition.getName(), new MetadataWrapper(metadataDefinition.getMetadataType(), metadataInstanceModel.getValues()));
    		}
    		
    		if (!metadataWrapperByMetadataName.isEmpty()) {
    			for (DocumentValidationDefinition validationDefinition : validationDefinitions) {
    				boolean isValidation = false;
    				if (StringUtils.isBlank(validationDefinition.getValidationInStates())) {
    					isValidation = true;
    				} else {
    					if (currentState != null) {
        					isValidation = GwtWorkflowStateBusinessUtils.isStateFound(validationDefinition.getValidationInStates(), currentState.getCode());
        				} else {
        					throw new RuntimeException("Documentul/colectia are o validare pentru un pas din flux dar pasul nu a fost gasit.");
        				}
    				}
    				if (isValidation) {
    					boolean validationResult = ExpressionEvaluator.evaluateDocumentExpression(validationDefinition.getConditionExpression(), metadataWrapperByMetadataName);
            			if (!validationResult) {
            				messages.add(validationDefinition.getMessage());
            			}  
    				}			
        		}
    		}    		
    	}
    	
    	DocumentValidationResponseModel response = new DocumentValidationResponseModel();
    	response.setValid(CollectionUtils.isEmpty(messages));
    	response.setMessages(messages);
    	
    	return response;
    }
    
    private MetadataDefinition findMetadataDefinitionById(List<? extends MetadataDefinition> metadataDefintions, Long id) {
    	if (CollectionUtils.isNotEmpty(metadataDefintions)) {
    		for (MetadataDefinition metadataDefinition : metadataDefintions) {
    			if (metadataDefinition.getId().equals(id)) {
    				return metadataDefinition;
    			}
    		}
    	}
    	return null;
    }
    

    public Document getDocumentById(String documentId, String documentLocationRealName) throws AppException {
    	return documentPlugin.getDocumentById(documentId, documentLocationRealName);
    }
    
    @Override
    public AutocompleteMetadataResponseModel autocompleteMetadata(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException {
    	
    	// TODO - De aruncat AppException cu ceva cod mai specific - daca va fi nevoie.
    	
    	if (request.getTargetMetadataCollectionDefinitionId() == null) {
    		throw new AppException();
    	}
    	
    	MetadataCollection metadataCollection = documentTypeDao.getMetadataCollectionDefinition(request.getTargetMetadataCollectionDefinitionId());
		String className = metadataCollection.getClassNameForAutoCompleteWithMetadata();
		if (StringUtils.isBlank(className)) {
			throw new AppException();
		}
		try {
			Class<?> clazz = Class.forName(className);
			AutocompleteMetadata process = (AutocompleteMetadata) clazz.newInstance();
			return process.autocomplete(request, userSecurity);
		} catch (Exception e) {
			LOGGER.error("Exceptie la instantiere si rulare clasa custom [" + className + "]", "autocompleteMetadata", userSecurity);
			throw new AppException();
		}
    }
    
    @Override
    public DownloadableFile exportDocument(String documentLocationRealName, String documentId, String templateName, ExportType exportType, SecurityManager userSecurity, boolean ingonreExtensionFromFileName) throws AppException {
    	
//    	Document document = getDocumentById(documentId, documentLocationRealName, userSecurity);
    	Document document = getDocumentById(documentId, documentLocationRealName);
    	if (document == null) {
    		return null;
    	}
    	DocumentTypeTemplate documentTypeTemplate = null;
    	documentTypeTemplate = documentTypeTemplateDao.getByTemplateNameAndDocumentTypeId(templateName, document.getDocumentTypeId());
    	DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
    	
    	MetadataValueFormatterForDocumentExport metadataValueFormatter = new MetadataValueFormatterForDocumentExport(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
    	Map<String, Object> model;
		try {
			model = ExportDocumentDataHelper.buildExportMap(document, documentType, metadataValueFormatter, documentTypeTemplate.getType(), arbConstants);
		} catch (Exception e) {
			throw AppExceptionUtils.getAppExceptionFromExceptionCause(e);
		}

		if (documentTypeTemplate.getType().equals(TemplateType.JASPER_REPORTS)) {
			String repId = JasperReportsConstants.PREFIX_OF_REPORT_IDENTIFIER_FOR_DOCUMENT_TYPE_TEMPLATE + documentTypeTemplate.getId();
			return jasperReportsService.generate(repId, exportType, new JsonJRDataSourceBuilder(model), templateName + exportType.getFileExtension());
		} else if (documentTypeTemplate.getType().equals(TemplateType.X_DOC_REPORT)) {
			return xDocReportsService.generate(documentTypeTemplate.getData(), exportType, model, templateName + exportType.getFileExtension());
		} else {
			throw new RuntimeException("Unknown document type template type [" + documentTypeTemplate.getType() + "]");
		}
		
    }
    
    @Override
    public DownloadableFile getDocumentAttachmentAsDownloadableFile(String documentLocationRealName, String documentId,
    		String attachmentName, Integer versionNumber, SecurityManager userSecurity) throws AppException {
    	
    	Attachment attachment = null;
    	try {
			if (versionNumber != null) {
				attachment = getAttachmentFromVersion(documentId, versionNumber, attachmentName, documentLocationRealName, userSecurity);
			} else {
				attachment = getAttachment(documentId, attachmentName, documentLocationRealName, userSecurity);
			}
			
			if (attachment == null) {
				
				DocumentLogAttributes documentLogAttributes = getDocumentLogAttributes(documentLocationRealName, documentId);
				
				String message = "S-a incercat luarea din baza de date a atasamentului cu numele: [" + attachmentName + "], " +
					"pentru documentul cu atributele: " + documentLogAttributes + ", numarul versiunii [" + versionNumber + "], " +
					"insa acesta nu a fost gasit.";
				LOGGER.error(message, "luarea atasamentului din jr", userSecurity);
				
				throw new AppException(AppExceptionCodes.ATTACHMENT_NOT_FOUND);
			}
		} catch (AppException ae) {
			
			DocumentLogAttributes documentLogAttributes = getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String message = "Exceptie la luarea atasamentului cu numele: [" + attachmentName + "], pentru documentul "
					+ "cu atributele: " + documentLogAttributes;
			LOGGER.error(message, ae, "luarea atasamentului din jr", userSecurity);
			
			throw ae;
		}
    	
    	return new DownloadableFile(attachment.getName(), attachment.getData());
    }    
    
    @Override
    public boolean checkForNeedUiSendConfirmation(SecurityManager userSecurity, DocumentModel documentModel, String transitionName) throws AppException {
    	
    	WorkflowState currentState = null;    		
		if (documentModel.getId() == null) {
			Workflow workflow = workflowService.getWorkflowByDocumentType(documentModel.getDocumentTypeId());
			currentState = workflowExecutionService.getCurrentState(workflow, null, userSecurity);
		} else {
			Workflow workflow = workflowService.getWorkflowForDocument(documentModel.getDocumentLocationRealName(), documentModel.getId(), documentModel.getDocumentTypeId());
			Document document = documentPlugin.getDocumentById(documentModel.getId(), documentModel.getDocumentLocationRealName(), userSecurity);
			currentState = workflowExecutionService.getCurrentState(workflow, document, userSecurity);
		}
		
		List<WorkflowTransition> outgoingTransitions = workflowService.getOutgoingTransitionsFromState(currentState.getId());
		if (CollectionUtils.isEmpty(outgoingTransitions)) {
			return false;
		}
		
    	DocumentType documentType = documentTypeDao.find(documentModel.getDocumentTypeId());
		
		Map<String, MetadataWrapper> metadataWrapperByMetadataName = new HashMap<String, MetadataWrapper>(); 
		for (MetadataInstanceModel metadataInstanceModel : documentModel.getMetadataInstances()) {
			MetadataDefinition metadataDefinition = findMetadataDefinitionById(documentType.getMetadataDefinitions(), metadataInstanceModel.getMetadataDefinitionId());
			metadataWrapperByMetadataName.put(metadataDefinition.getName(), new MetadataWrapper(metadataDefinition.getMetadataType(), metadataInstanceModel.getValues()));
		}
		
		int transitionConfirmationRequiredCounter = 0;
		for (WorkflowTransition transition : outgoingTransitions) {
			if (transition.isUiSendConfirmation()) {
				if (StringUtils.isNotBlank(transitionName) && transition.getName().equals(transitionName)) {
					return true;
				} else {
					if (StringUtils.isNotBlank(transition.getRoutingCondition())) {
						boolean confirm = ExpressionEvaluator.evaluateDocumentExpression(transition.getRoutingCondition(), metadataWrapperByMetadataName);
						if (confirm) {
							transitionConfirmationRequiredCounter++;
						}
					} else {
						transitionConfirmationRequiredCounter++;
					}
				}
			}
		}
		
		if (transitionConfirmationRequiredCounter == 1) {
			return true;
		}
		
    	return false;
    }

    @Override
    public boolean isDocumentImported(DocumentIdentifier documentIdentifier) {
    	return importedDocumentDao.isDocumentImported(documentIdentifier);
    }
    
    @Override
    public boolean isDocumentWorkflowFinished(DocumentIdentifier documentIdentifier) {
    	return workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId());
    }
    
    @Override
    public AdminUpdateDocumentModel getAdminUpdateDocument(DocumentIdentifier documentIdentifier, SecurityManager userSecurity) throws AppException {
    	AdminUpdateDocument document = documentPlugin.getAdminUpdateDocument(documentIdentifier, userSecurity);
    	return DocumentConverter.getModelFromAdminUpdateDocument(document);
    }
    
    @Override
    public void updateDocument(AdminUpdateDocumentModel documentModel, SecurityManager userSecurity) throws AppException {
    	AdminUpdateDocument document = DocumentConverter.getAdminUpdateDocumentFromModel(documentModel);
    	documentPlugin.updateDocument(document, userSecurity);
    }
    
    public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setDocumentLocationPlugin(DocumentLocationPlugin documentLocationPlugin) {
		this.documentLocationPlugin = documentLocationPlugin;
	}
	public void setDocumentPlugin(DocumentPlugin documentPlugin) {
		this.documentPlugin = documentPlugin;
	}
	public void setAutoNumberMetadataValueGenerator(AutoNumberMetadataValueGenerator autoNumberMetadataValueGenerator) {
		this.autoNumberMetadataValueGenerator = autoNumberMetadataValueGenerator;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}
	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}
	public void setTimesheetsForLeavesIntegrationEnabled(boolean timesheetsForLeavesIntegrationEnabled) {
		this.timesheetsForLeavesIntegrationEnabled = timesheetsForLeavesIntegrationEnabled;
	}
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}	
	public void setJasperReportsService(JasperReportsService jasperReportsService) {
		this.jasperReportsService = jasperReportsService;
	}
	public void setDocumentTypeTemplateDao(DocumentTypeTemplateDao documentTypeTemplateDao) {
		this.documentTypeTemplateDao = documentTypeTemplateDao;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	
	public void setImportedDocumentDao(ImportedDocumentDao importedDocumentDao) {
		this.importedDocumentDao = importedDocumentDao;
	}

	public void setxDocReportsService(XDocReportsService xDocReportsService) {
		this.xDocReportsService = xDocReportsService;
	}

	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}
	
	public void setDocumentAttachmentDetailDao(DocumentAttachmentDetailDao documentAttachmentDetailDao) {
		this.documentAttachmentDetailDao = documentAttachmentDetailDao;
	}
}