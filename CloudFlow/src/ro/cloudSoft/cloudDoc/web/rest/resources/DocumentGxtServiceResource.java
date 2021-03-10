package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.content.ImportedDocumentDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.Page;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PageRequest;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.SendDocumentToWorkflowRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.SendDocumentToWorkflowResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentAddBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AdminUpdateDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AdminUpdateDocumentBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCollectionValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentValidationResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentViewVersionBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetPagedDocumentsFromFolderRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.WorkflowGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentTypeGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.builders.model.content.DocumentViewModelsBuilder;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowStateConverter;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.ContentBusinessUtils;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentFilterModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.common.utils.PagingList;


@Component
@Path("/DocumentGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DocumentGxtServiceResource extends BaseResource {
	
	@Autowired
	private DocumentGxtService documentGxtService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DocumentTypeGxtService documentTypeGxtService;
	
	@Autowired
	private WorkflowGxtService workflowGxtService;
	
	@Autowired
	private NomenclatorService nomenclatorService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private CalendarService calendarService;
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private WorkflowInstanceDao workflowInstanceDao;
	
	@Autowired
	private ImportedDocumentDao importedDocumentDao;
	
	@POST
	@Path("/getPagedDocumentsFromFolder")
	public Page<DocumentViewModel> getPagedDocumentsFromFolder(PageRequest<GetPagedDocumentsFromFolderRequestModel> pageRequest) throws AppException {
		PagingList<Document> pagedDocuments = documentService.getPagedDocumentsFromFolder(
			pageRequest.getPayload().getDocumentLocationRealName(),
			pageRequest.getPayload().getFolderId(),
			pageRequest.getOffset(),
			pageRequest.getLimit(), 
			getSecurity());
		
		DocumentViewModelsBuilder builder = new DocumentViewModelsBuilder(
			pagedDocuments.getElements(),
			pageRequest.getPayload().isSameType(),
			documentTypeService,
			userService,
			nomenclatorService,
			groupService,
			documentService,
			calendarService,
			projectService,
			workflowInstanceDao,
			importedDocumentDao,
			
			getSecurity());
		
		return buildDocumentViewModelPage(builder.getViewModels(), pageRequest.getLimit(), pagedDocuments.getTotalCount());
    }
	
	@POST
	@Path("/getPagedDocuments")
	public Page<DocumentViewModel> getPagedDocuments(DocumentFilterModel filter) throws AppException {
		PagingList<Document> pagedDocuments = documentService.getPagedDocuments(filter, getSecurity());
		
		DocumentViewModelsBuilder builder = new DocumentViewModelsBuilder(
			pagedDocuments.getElements(),
			filter.isSameType(),
			documentTypeService,
			userService,
			nomenclatorService,
			groupService,
			documentService,
			calendarService,
			projectService,
			workflowInstanceDao,
			importedDocumentDao,
			getSecurity());
		
		return buildDocumentViewModelPage(builder.getViewModels(), filter.getPageSize(), pagedDocuments.getTotalCount());
    }

	private Page<DocumentViewModel> buildDocumentViewModelPage(List<DocumentViewModel> documents, int pageSize, int totalItems) {
		Page<DocumentViewModel> page = new Page<DocumentViewModel>();
		page.setItems(documents);
		page.setPageSize(pageSize);
		page.setTotalItems(totalItems);
		return page;
	}

	@POST
	@Path("/getDocumentVersions/{documentId}/{documentLocationRealName}")
	public List<DocumentVersionInfoViewModel> getDocumentVersions(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName ) throws PresentationException {
		return documentGxtService.getDocumentVersions(documentId, documentLocationRealName);
	}
	
	@POST
	@Path("/deleteDocument/{documentId}/{documentLocationRealName}")
	public void deleteDocument(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		documentGxtService.deleteDocument(documentId, documentLocationRealName);
	}
	
	@POST
	@Path("/getDocumentAddBundle/{documentTypeId}")
	public DocumentAddBundleModel getDocumentAddingInfo(@PathParam("documentTypeId") Long documentTypeId) throws PresentationException {
		
		clearTemporaryAttachments();
		
		DocumentTypeModel documentType = documentTypeGxtService.getDocumentTypeById(documentTypeId);
		
		WorkflowModel workflow = workflowGxtService.getWorkflowByDocumentType(documentTypeId);
		WorkflowStateModel currentState = workflowGxtService.getCurrentState(workflow, null);
		boolean canUserSend = workflowGxtService.checkSendingRights(workflow, null);
		
		DocumentAddBundleModel bundle = new DocumentAddBundleModel();
		bundle.setDocumentType(documentType);
		bundle.setWorkflow(workflow);
		bundle.setCurrentState(currentState);
		bundle.setCanUserSend(canUserSend);
		
		return bundle;
	}
	
	@POST
	@Path("/save")
	public String save(DocumentModel document) throws PresentationException, IOException {		
		return documentGxtService.save(document, document.getParentFolderId(), document.getDocumentLocationRealName(), document.getNamesForAttachmentsToDelete());
	}
	
	@POST
	@Path("/getDocumentViewOrEditBundle/{documentId}/{documentLocationRealName}")
	public ExtendedDocumentModel getDocumentViewOrEditBundle(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		clearTemporaryAttachments();
		return documentGxtService.getExtendedDocumentById(documentId, documentLocationRealName);
	}
	
	@POST
	@Path("/getAdminUpdateDocumentBundle/{documentId}/{documentLocationRealName}")
	public AdminUpdateDocumentBundleModel getAdminUpdateDocumentBundle(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		try {
			
			clearTemporaryAttachments();
			
			AdminUpdateDocumentBundleModel bundle = new AdminUpdateDocumentBundleModel();
			AdminUpdateDocumentModel document = documentService.getAdminUpdateDocument(new DocumentIdentifier(documentLocationRealName, documentId), getSecurity());
			bundle.setDocument(document);
			DocumentTypeModel documentType = documentTypeGxtService.getDocumentTypeById(document.getDocumentTypeId());
			bundle.setDocumentType(documentType);
			
			return bundle;
			
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/adminUpdateDocument")
	public void adminUpdateDocument(AdminUpdateDocumentModel document) throws PresentationException {
		try {
			
			clearTemporaryAttachments();
			
			documentService.updateDocument(document, getSecurity());
			
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	private void clearTemporaryAttachments() {
		try {
			documentGxtService.clearTemporaryAttachments();
		} catch (Exception e) {
			// log it with logger :)
			e.printStackTrace();
		}
	}
	
	@POST
	@Path("/getDocumentViewVersionBundle/{documentLocationRealName}/{documentId}/{versionNr}")
	public DocumentViewVersionBundleModel getDocumentViewVersionBundle(@PathParam("documentLocationRealName") String documentLocationRealName, @PathParam("documentId") String documentId, @PathParam("versionNr") String versionNr) throws PresentationException {
		
		clearTemporaryAttachments();
		
		DocumentModel documentModel = documentGxtService.getDocumentFromVersion(versionNr, documentId, documentLocationRealName);
		DocumentTypeModel documentTypeModel = documentTypeGxtService.getDocumentTypeById(documentModel.getDocumentTypeId());
		WorkflowModel workflowModel = workflowGxtService.getWorkflowForDocument(documentLocationRealName, documentId, documentTypeModel.getId());
		
		DocumentViewVersionBundleModel bundle = new DocumentViewVersionBundleModel();
		bundle.setDocument(documentModel);
		bundle.setDocumentType(documentTypeModel);
		bundle.setWorkflow(workflowModel);
		
		if (documentModel.getVersionWorkflowStateId() != null) {
			WorkflowState state = workflowService.getWorkflowStateById(documentModel.getVersionWorkflowStateId());
			WorkflowStateModel stateModel = WorkflowStateConverter.getModelFromWorkflowState(state);
			bundle.setWorkflowState(stateModel);
		}
		
		return bundle;
	}
	
	@POST
	@Path("/checkin")
	public String checkin(DocumentModel document) throws PresentationException {		
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
		Set<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = getDefinitionIdsForAutoNumberMetadataValuesToGenerate(documentType, document);
		return documentGxtService.checkin(document, documentType.isKeepAllVersions(), document.getParentFolderId(), document.getDocumentLocationRealName(), definitionIdsForAutoNumberMetadataValuesToGenerate, document.getNamesForAttachmentsToDelete());
	}
	
	private Set<Long> getDefinitionIdsForAutoNumberMetadataValuesToGenerate(DocumentType documentType, DocumentModel document) {
		Set<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = new HashSet<Long>();
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_AUTO_NUMBER)) {				
				for (MetadataInstanceModel metadataInstance : document.getMetadataInstances()) {
					if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinition.getId())) {
						if (metadataInstance.getValue() == null) {
							definitionIdsForAutoNumberMetadataValuesToGenerate.add(metadataDefinition.getId());
							break;
						}
					}
				}				
			}
		}
		return definitionIdsForAutoNumberMetadataValuesToGenerate;
	}
	
	@POST
	@Path("/checkout/{documentLocationRealName}/{documentId}")
	public void checkout(@PathParam("documentLocationRealName") String documentLocationRealName, @PathParam("documentId") String documentId) throws PresentationException {		
		documentGxtService.checkout(documentId, documentLocationRealName);
		clearTemporaryAttachments();
	}
	
	@POST
	@Path("/undoCheckout/{documentLocationRealName}/{documentId}")
	public void undoCheckout(@PathParam("documentLocationRealName") String documentLocationRealName, @PathParam("documentId") String documentId) throws PresentationException {		
		documentGxtService.undoCheckout(documentId, documentLocationRealName);
		clearTemporaryAttachments();
	}
	
	@POST
	@Path("/sendDocumentToWorkflow")
	public SendDocumentToWorkflowResponseModel sendDocumentToWorkflow(SendDocumentToWorkflowRequestModel request) throws PresentationException {
		
		SendDocumentToWorkflowResponseModel responseModel = new SendDocumentToWorkflowResponseModel();
		
		try {
			
			DocumentModel documentModel = request.getDocument();
			SecurityManager userSecurity = getSecurity();
			
			boolean isDocumentStable = true;
			if (StringUtils.isBlank(documentModel.getId())) {
				isDocumentStable = false;
			} else {
				Document document;
				try {
					document = documentService.getDocumentById(documentModel.getId(), documentModel.getDocumentLocationRealName(), userSecurity);
				} catch (AppException ae) {
					throw PresentationExceptionUtils.getPresentationException(ae);
				}
				boolean canUserAccessLockedDocument = ContentBusinessUtils.canUserAccessLockedDocument(document, userSecurity);
				if (canUserAccessLockedDocument) {
					isDocumentStable = false;
			    }
			}
			
			if (!request.isUiSendConfirmed() && StringUtils.isBlank(request.getManualAssignmentDestinationId())) {
				boolean needUiSendConfirmation = documentService.checkForNeedUiSendConfirmation(userSecurity, documentModel, request.getTransitionName());
				responseModel.setNeedUiSendConfirmation(needUiSendConfirmation);
			}
			
			if (!responseModel.isNeedUiSendConfirmation()) {
				if (!isDocumentStable) {
					DocumentType documentType = documentTypeService.getDocumentTypeById(request.getDocument().getDocumentTypeId());
					
					Set<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = getDefinitionIdsForAutoNumberMetadataValuesToGenerate(documentType, request.getDocument());			
					documentModel = documentGxtService.checkinAndGetDocument(documentModel, documentType.isKeepAllVersions(), 
							documentModel.getParentFolderId(), documentModel.getDocumentLocationRealName(), 
							definitionIdsForAutoNumberMetadataValuesToGenerate, documentModel.getNamesForAttachmentsToDelete());
					clearTemporaryAttachments();
				}
			
				WorkflowInstanceResponseModel response = documentGxtService.sendDocumentToWorkflow(request.getWorkflowId(), request.getTransitionName(), request.getManualAssignmentDestinationId(), documentModel);
				response.setDocument(documentModel);
				
				responseModel.setWorkflowInstanceResponse(response);
			}
			
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
		return responseModel;
	}
	
	@POST
	@Path("/existDocumentsOfType/{documentTypeId}")
	public boolean existDocumentsOfType(@PathParam("documentTypeId") Long documentTypeId) throws PresentationException {
		return documentGxtService.existDocumentsOfType(documentTypeId);
	}
	
	@POST
	@Path("/validateDocument")
	public DocumentValidationResponseModel validate(DocumentValidationRequestModel requestModel) throws PresentationException {
		try {
			return documentService.validateDocument(requestModel, getSecurity());
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
	
	@POST
	@Path("/validateDocumentCollection")
	public DocumentValidationResponseModel validateCollection(DocumentCollectionValidationRequestModel requestModel) throws PresentationException {
		try {
			return documentService.validateDocumentCollection(requestModel, getSecurity());
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
	
	@POST
	@Path("/getDocumentName/{documentLocationRealName}/{documentId}")
	public String getDocumentName(@PathParam("documentLocationRealName") String documentLocationRealName, @PathParam("documentId") String documentId) throws PresentationException {
		try {
			return documentService.getDocumentName(documentLocationRealName, documentId);
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
	
	@POST
	@Path("/autocompleteMetadata")
	public AutocompleteMetadataResponseModel autocompleteMetadata(AutocompleteMetadataRequestModel request) throws PresentationException {
		try {
			return documentService.autocompleteMetadata(request, getSecurity());
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
	
	@GET
	@Path("/exportDocument")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response exportDocument(@QueryParam(value = "documentLocationRealName") String documentLocationRealName, 
			@QueryParam(value = "documentId") String documentId, 
			@QueryParam(value = "templateName") String templateName, 
			@QueryParam(value = "exportType") ExportType exportType) throws PresentationException {
		try {
			DownloadableFile downloadableFile = documentService.exportDocument(documentLocationRealName, documentId, templateName, exportType, getSecurity(), false);
			return buildDownloadableFileResponse(downloadableFile);
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
	
	@GET
	@Path("/downloadDocumentAttachment")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response exportDocument(@QueryParam(value = "documentLocationRealName") String documentLocationRealName, 
			@QueryParam(value = "documentId") String documentId, 
			@QueryParam(value = "attachmentName") String attachmentName, 
			@QueryParam(value = "versionNumber") Integer versionNumber) throws PresentationException {
		try {
			DownloadableFile downloadableFile = documentService.getDocumentAttachmentAsDownloadableFile(documentLocationRealName, documentId, attachmentName, versionNumber, getSecurity());
			return buildDownloadableFileResponse(downloadableFile);
		} catch (AppException e) {
			throw new PresentationException(e.getCode().name());
		}
	}
}