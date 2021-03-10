package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.MimeTypeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;

public class DocumentTypeConverter {

	public static DocumentTypeModel getModelFromDocumentType(DocumentType documentType) {
		DocumentTypeModel documentTypeModel = new DocumentTypeModel();

		documentTypeModel.setId(documentType.getId());

		documentTypeModel.setName(documentType.getName());
		documentTypeModel.setDescription(documentType.getDescription());
		
		documentTypeModel.setKeepAllVersions(documentType.isKeepAllVersions());

		documentTypeModel.setAllowAnyInitiator(documentType.isAllUsersInitiators());
		ArrayList<OrganizationEntityModel> initiatorModels = new ArrayList<OrganizationEntityModel>();
		for (OrganizationEntity oe : documentType.getInitiators()) {
			OrganizationEntityModel oeModel = OrganizationEntityConverter.getModelFromOrganizationEntity(oe);
			initiatorModels.add(oeModel);
		}
		documentTypeModel.setInitiators(initiatorModels);

		documentTypeModel.setMandatoryAttachment(documentType.getMandatoryAttachment());
		
		documentTypeModel.setMandatoryAttachmentInStates(documentType.isMandatoryAttachmentInStates());
		documentTypeModel.setMandatoryAttachmentStates(documentType.getMandatoryAttachmentStates());
		
		documentTypeModel.setMandatoryAttachmentWhenMetadataHasValue(documentType.isMandatoryAttachmentWhenMetadataHasValue());
		documentTypeModel.setMetadataNameInMandatoryAttachmentCondition(documentType.getMetadataNameInMandatoryAttachmentCondition());
		documentTypeModel.setMetadataValueInMandatoryAttachmentCondition(documentType.getMetadataValueInMandatoryAttachmentCondition());

		ArrayList<MimeTypeModel> allowedMimeTypes = new ArrayList<MimeTypeModel>();
		for (MimeType mimeType : documentType.getAllowedMimeTypes()) {
			MimeTypeModel mimeTypeModel = MimeTypeConverter.getModelFromMimeType(mimeType);
			allowedMimeTypes.add(mimeTypeModel);
		}
		documentTypeModel.setAllowedAttachmentTypes(allowedMimeTypes);

		ArrayList<MetadataDefinitionModel> metadataDefinitionModels = new ArrayList<MetadataDefinitionModel>();
		// Parcurge si adauga metadatele "simple".
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			MetadataDefinitionModel metadataDefinitionModel = MetadataDefinitionConverter.getModelFromMetadataDefinition(metadataDefinition);
			metadataDefinitionModels.add(metadataDefinitionModel);
		}
		// Parcurge si adauga colectiile.
		for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
			MetadataCollectionDefinitionModel collectionMetadataDefinitionModel = MetadataCollectionDefinitionConverter.getModelFromMetadataCollectionDefinition(metadataCollection);
			metadataDefinitionModels.add(collectionMetadataDefinitionModel);
		}
		documentTypeModel.setMetadataDefinitions(metadataDefinitionModels);
		
		documentTypeModel.setParentDocumentLocationRealNameForDefaultLocation(documentType.getParentDocumentLocationRealNameForDefaultLocation());
		documentTypeModel.setFolderIdForDefaultLocation(documentType.getFolderIdForDefaultLocation());
				
		return documentTypeModel;
	}

	public static DocumentTypeModel getModelFromDocumentTypeForDisplay(DocumentType documentTypeForDisplay) {
		DocumentTypeModel documentTypeModelForDisplay = new DocumentTypeModel();

		documentTypeModelForDisplay.setId(documentTypeForDisplay.getId());
		documentTypeModelForDisplay.setName(documentTypeForDisplay.getName());
		documentTypeModelForDisplay.setDescription(documentTypeForDisplay.getDescription());

		return documentTypeModelForDisplay;
	}

	public static DocumentType getDocumentTypeFromModel(DocumentTypeModel documentTypeModel, GroupService groupService) {
		DocumentType documentType = new DocumentType();

		if (documentTypeModel.getId() != null) {
			documentType.setId(documentTypeModel.getId());
		}
		
		documentType.setName(documentTypeModel.getName());
		documentType.setDescription(documentTypeModel.getDescription());
		
		documentType.setKeepAllVersions(documentTypeModel.isKeepAllVersions());

		documentType.setAllUsersInitiators(documentTypeModel.isAllowAnyInitiator());
		Set<OrganizationEntity> initiators = new HashSet<OrganizationEntity>();
		for (OrganizationEntityModel model : documentTypeModel.getInitiators()) {
			OrganizationEntity initiator = OrganizationEntityConverter.getOrganizationEntityFromModel(model);
			initiators.add(initiator);
		}
		documentType.setInitiators(initiators);

		documentType.setMandatoryAttachment(documentTypeModel.isMandatoryAttachment());
		
		documentType.setMandatoryAttachmentInStates(documentTypeModel.isMandatoryAttachmentInStates());
		documentType.setMandatoryAttachmentStates(documentTypeModel.getMandatoryAttachmentStates());
		
		documentType.setMandatoryAttachmentWhenMetadataHasValue(documentTypeModel.isMandatoryAttachmentWhenMetadataHasValue());
		documentType.setMetadataNameInMandatoryAttachmentCondition(documentTypeModel.getMetadataNameInMandatoryAttachmentCondition());
		documentType.setMetadataValueInMandatoryAttachmentCondition(documentTypeModel.getMetadataValueInMandatoryAttachmentCondition());

		Set<MimeType> allowedMimeTypes = new HashSet<MimeType>();
		for (MimeTypeModel mimeTypeModel : documentTypeModel.getAllowedAttachmentTypes()) {
			MimeType mimeType = MimeTypeConverter.getMimeTypeFromModel(mimeTypeModel);
			allowedMimeTypes.add(mimeType);
		}
		documentType.setAllowedMimeTypes(allowedMimeTypes);

		List<MetadataDefinition> metadataDefinitions = Lists.newLinkedList();
		List<MetadataCollection> metadataCollectionDefinitions = Lists.newLinkedList();

		for (MetadataDefinitionModel metadataDefinitionModel : documentTypeModel.getMetadataDefinitions()) {
			if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
				MetadataCollectionDefinitionModel metadataCollectionDefinitionModel = (MetadataCollectionDefinitionModel) metadataDefinitionModel;
				MetadataCollection metadataCollectionDefinition = MetadataCollectionDefinitionConverter.getMetadataCollectionDefinitionFromModel(metadataCollectionDefinitionModel, groupService);
				metadataCollectionDefinitions.add(metadataCollectionDefinition);
			} else {
				MetadataDefinition metadataDefinition = MetadataDefinitionConverter.getMetadataDefinitionFromModel(metadataDefinitionModel, groupService);
				metadataDefinitions.add(metadataDefinition);
			}
		}

		documentType.setMetadataDefinitions(metadataDefinitions);
		documentType.setMetadataCollections(metadataCollectionDefinitions);
		
		documentType.setParentDocumentLocationRealNameForDefaultLocation(documentTypeModel.getParentDocumentLocationRealNameForDefaultLocation());
		documentType.setFolderIdForDefaultLocation(documentTypeModel.getFolderIdForDefaultLocation());
		
		return documentType;
	}
	
	public static List<DocumentTypeTemplate> getTemplatesFromModels(DocumentTypeModel documentTypeModel, String username) {
		List<DocumentTypeTemplate> templates = new ArrayList<DocumentTypeTemplate>();
		List<DocumentTypeTemplateModel> documentTypeTemplateModels = documentTypeModel.getTemplates();
		if (documentTypeModel.getTemplates() != null) {
			for (DocumentTypeTemplateModel documentTypeTemplateModel : documentTypeTemplateModels) {
				DocumentTypeTemplate documentTypeTemplate = new DocumentTypeTemplate();
				documentTypeTemplate.setName(documentTypeTemplateModel.getName());
				documentTypeTemplate.setDescription(documentTypeTemplateModel.getDescription());
				documentTypeTemplate.setExportAvailabilityExpression(documentTypeTemplateModel.getExportAvailabilityExpression());
				templates.add(documentTypeTemplate);
			}
		}
		return templates;
	}
}