package ro.cloudSoft.cloudDoc.presentation.server.services.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Lists;
import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.TemplateType;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentTypeGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentCreationInDefaultLocationViewConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentTypeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentTypeTemplateConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.MetadataDefinitionConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.utils.FileSystemDocumentTemplateManager;
import ro.cloudSoft.cloudDoc.utils.SessionTemplateManager;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DocumentTypeGxtServiceImpl extends GxtServiceImplBase implements DocumentTypeGxtService, InitializingBean {

	private DocumentTypeService documentTypeService;
	private GroupService groupService;
	private FileSystemDocumentTemplateManager fileSystemDocumentTemplateManager;
	
	public DocumentTypeGxtServiceImpl(DocumentTypeService documentTypeService, GroupService groupService, FileSystemDocumentTemplateManager fileSystemDocumentTemplateManager) {
		this.documentTypeService = documentTypeService;
		this.groupService = groupService;
		this.fileSystemDocumentTemplateManager = fileSystemDocumentTemplateManager;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentTypeService,
			groupService,
			fileSystemDocumentTemplateManager
		);
	}

	@Override
	public List<DocumentTypeModel> getAllDocumentTypesForDisplay() {
		List<DocumentType> allDocumentTypesForDisplay = documentTypeService.getAllDocumentTypesForDisplay();
		List<DocumentTypeModel> allDocumentTypeModelsForDisplay = new ArrayList<DocumentTypeModel>();
		for (DocumentType documentTypeForDisplay : allDocumentTypesForDisplay) {
			DocumentTypeModel documentTypeModelForDisplay = DocumentTypeConverter.getModelFromDocumentTypeForDisplay(documentTypeForDisplay);
			allDocumentTypeModelsForDisplay.add(documentTypeModelForDisplay);
		}
		return allDocumentTypeModelsForDisplay;
	}

	@Override
	public DocumentTypeModel getDocumentTypeById(Long id) {
		
		DocumentType documentType = documentTypeService.getDocumentTypeById(id, getSecurity());
		DocumentTypeModel documentTypeModel = DocumentTypeConverter.getModelFromDocumentType(documentType);
		
		List<DocumentTypeTemplate> templates = this.documentTypeService.getTemplates(id);
		List<DocumentTypeTemplateModel> templateModels = new ArrayList<DocumentTypeTemplateModel>();
		for (DocumentTypeTemplate template : templates) {
			DocumentTypeTemplateModel templateModel = DocumentTypeTemplateConverter.getModelFromDocumentTypeTemplate(template);
			templateModels.add(templateModel);
		}
		documentTypeModel.setTemplates(templateModels);
		
		return documentTypeModel;
	}
	
	@Override
	public void prepareForAddOrEdit() throws PresentationException {
		SessionTemplateManager.clear(getSession());
	}

	@Override
	public void saveDocumentType(DocumentTypeModel documentTypeModel) throws PresentationException {
		DocumentType documentType = DocumentTypeConverter.getDocumentTypeFromModel(documentTypeModel, groupService);
		try {
			
//			// Sterge template-urile cerute din spatiul temporar.
//			SessionTemplateManager.removeTemplates(this.getSession(), documentTypeModel.getNamesForTemplatesToDelete());
//			// Ia template-urile din spatiul temporar.
//			List<DocumentTypeTemplate> templates = SessionTemplateManager.getTemplates(this.getSession());

			List<DocumentTypeTemplate> templates = DocumentTypeConverter.getTemplatesFromModels(documentTypeModel, getSecurity().getUserUsername());
			List<DocumentTypeTemplate> templatesToSave = new ArrayList<>();
			for (DocumentTypeTemplate template : templates) {
				byte[] data = fileSystemDocumentTemplateManager.getTemplateAsByteArray(template.getName(), getSecurity().getUserUsername());
				if (data != null) {
					template.setData(data);
					if (template.getName().endsWith(TemplateType.X_DOC_REPORT.getFileExtension())) {
						template.setType(TemplateType.X_DOC_REPORT);
					} else if (template.getName().endsWith(TemplateType.JASPER_REPORTS.getFileExtension())) {
						template.setType(TemplateType.JASPER_REPORTS);
					} else {
						throw new RuntimeException("Invalid template file type for file name: " + template.getName());
					}
					templatesToSave.add(template);
				}
			}
			fileSystemDocumentTemplateManager.remove(documentTypeModel.getNamesForTemplatesToDelete(), getSecurity().getUserUsername());
			
			// Salveaza tipul de document.
			documentTypeService.saveDocumentType(documentType, templatesToSave,
				documentTypeModel.getNamesForTemplatesToDelete(), getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public void deleteDocumentType(Long id) throws PresentationException {
		try {
			documentTypeService.deleteDocumentType(id, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
public List<UserMetadataDefinitionModel> getUserMetadataDefinitions(List<Long> documentTypeIds) {
		
		List<UserMetadataDefinition> userMetadataDefinitions = documentTypeService.getUserMetadataDefinitions(documentTypeIds);
		List<UserMetadataDefinitionModel> userMetadataDefinitionModels = new ArrayList<UserMetadataDefinitionModel>();
		
		for (UserMetadataDefinition userMetadataDefinition : userMetadataDefinitions) {
			UserMetadataDefinitionModel userMetadataDefinitionModel = (UserMetadataDefinitionModel) MetadataDefinitionConverter.getModelFromMetadataDefinition(userMetadataDefinition);
			userMetadataDefinitionModels.add(userMetadataDefinitionModel);
		}
		
		return userMetadataDefinitionModels;
	}

	@Override
	public List<DocumentTypeModel> getAvailableDocumentTypes() {
		List<DocumentTypeModel> documentTypeModels = new ArrayList<DocumentTypeModel>();
		
		List<DocumentType> documentTypes = documentTypeService.getAvailableDocumentTypes(getSecurity());
		for (DocumentType documentType : documentTypes) {
			DocumentTypeModel documentTypeModel = DocumentTypeConverter.getModelFromDocumentTypeForDisplay(documentType);
			documentTypeModels.add(documentTypeModel);
		}
		
		return documentTypeModels;
	}
	
	@Override
	public List<DocumentCreationInDefaultLocationViewModel> getDocumentCreationInDefaultLocationViews() throws PresentationException {
		List<DocumentCreationInDefaultLocationView> views = documentTypeService.getDocumentCreationInDefaultLocationViews(getSecurity());
		List<DocumentCreationInDefaultLocationViewModel> viewModels = Lists.newArrayList();
		for (DocumentCreationInDefaultLocationView view : views) {
			DocumentCreationInDefaultLocationViewModel viewModel = DocumentCreationInDefaultLocationViewConverter.getModel(view);
			viewModels.add(viewModel);
		}
		return viewModels;
	}

	@Override
	public List<DocumentTypeModel> getAvailableDocumentTypesForSearch() {		
		List<DocumentType> documentTypes = documentTypeService.getAvailableDocumentTypesForSearch(getSecurity());
		List<DocumentTypeModel> documentTypeModels = Lists.newLinkedList();
		for (DocumentType documentType : documentTypes) {
			DocumentTypeModel documentTypeModel = DocumentTypeConverter.getModelFromDocumentTypeForDisplay(documentType);
			documentTypeModels.add(documentTypeModel);
		}
		return documentTypeModels;
	}

	@Override
	public List<DocumentTypeModel> getDocumentTypesWithNoWorkflow() {
		List<DocumentType> allDocumentTypesForDisplay = documentTypeService.getDocumentTypesWithNoWorkflow();
		List<DocumentTypeModel> allDocumentTypeModelsForDisplay = new ArrayList<DocumentTypeModel>();
		for (DocumentType documentTypeForDisplay : allDocumentTypesForDisplay) {
			DocumentTypeModel documentTypeModelForDisplay = DocumentTypeConverter.getModelFromDocumentType(documentTypeForDisplay);
			allDocumentTypeModelsForDisplay.add(documentTypeModelForDisplay);
		}
		return allDocumentTypeModelsForDisplay;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public void clearTemporaryTemplates() {
		try {
			fileSystemDocumentTemplateManager.clear(getSecurity().getUserUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}