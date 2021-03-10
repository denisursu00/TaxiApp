package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentTypeModel extends BaseModel implements IsSerializable {
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;

	private static final long serialVersionUID = 1692003290447993523L;
	
	public static final String PROPERTY_ID = "id";
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESCRIPTION = "description";
	
	public static final String PROPERTY_KEEP_ALL_VERSIONS = "keepAllVersions";
	
	public static final String PROPERTY_ALLOW_ANY_INITIATOR = "allowAnyInitiator";
	public static final String PROPERTY_INITIATORS = "initiators";
	
	public static final String PROPERTY_MANDATORY_ATTACHMENT = "mandatoryAttachment";
	
	public static final String PROPERTY_MANDATORY_ATTACHMENT_IN_STEPS = "mandatoryAttachmentInStates";
	public static final String PROPERTY_MANDATORY_ATTACHMENT_STEPS = "mandatoryAttachmentStates";
	
	public static final String PROPERTY_MANDATORY_ATTACHMENT_WHEN_METADATA_HAS_VALUE = "mandatoryAttachmentWhenMetadataHasValue";
	public static final String PROPERTY_METADATA_NAME_IN_MANDATORY_ATTACHMENT_CONDITION = "metadataNameInMandatoryAttachmentCondition";
	public static final String PROPERTY_METADATA_VALUE_IN_MANDATORY_ATTACHMENT_CONDITION = "metadataValueInMandatoryAttachmentCondition";
	
	public static final String PROPERTY_ALLOWED_ATTACHMENT_TYPES = "allowedAttachmentTypes";
	
	public static final String PROPERTY_METADATA_DEFINITIONS = "metadataDefinitions";
	
	public static final String PROPERTY_TEMPLATES = "templates";
	public static final String PROPERTY_NAMES_FOR_TEMPLATES_TO_DELETE = "namesForTemplatesToDelete";

	public static final String PROPERTY_PARENT_DOCUMENT_LOCATION_REAL_NAME_FOR_DEFAULT_LOCATION = "parentDocumentLocationRealNameForDefaultLocation";
	public static final String PROPERTY_FOLDER_ID_FOR_DEFAULT_LOCATION = "folderIdForDefaultLocation";
	
	public DocumentTypeModel() {}
	
	public DocumentTypeModel(String name) {
		setName(name);
	}
	
	public boolean isDefaultLocationSet() {
		return (
			GwtStringUtils.isNotBlank(getParentDocumentLocationRealNameForDefaultLocation()) &&
			GwtStringUtils.isNotBlank(getFolderIdForDefaultLocation())
		);
	}
	
	public Long getId() {
		return get(PROPERTY_ID);
	}	
	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}	
	public String getName() {
		return get(PROPERTY_NAME);
	}	
	public void setName(String name) {
		set(PROPERTY_NAME, name);
	}	
	public String getDescription() {
		return get(PROPERTY_DESCRIPTION);
	}	
	public void setDescription(String description) {
		set(PROPERTY_DESCRIPTION, description);
	}	
	public Boolean isKeepAllVersions() {
		return get(PROPERTY_KEEP_ALL_VERSIONS);
	}	
	public void setKeepAllVersions(Boolean keepAllVersions) {
		set(PROPERTY_KEEP_ALL_VERSIONS, keepAllVersions);
	}	
	public Boolean isAllowAnyInitiator() {
		return get(PROPERTY_ALLOW_ANY_INITIATOR);
	}	
	public void setAllowAnyInitiator(Boolean allowAnyInitiator) {
		set(PROPERTY_ALLOW_ANY_INITIATOR, allowAnyInitiator);
	}	
	public List<OrganizationEntityModel> getInitiators() {
		return get(PROPERTY_INITIATORS);
	}	
	public void setInitiators(List<OrganizationEntityModel> initiators) {
		set(PROPERTY_INITIATORS, initiators);
	}	
	public Boolean isMandatoryAttachment() {
		return get(PROPERTY_MANDATORY_ATTACHMENT);
	}	
	public void setMandatoryAttachment(Boolean mandatoryAttachment) {
		set(PROPERTY_MANDATORY_ATTACHMENT, mandatoryAttachment);
	}
	public boolean isMandatoryAttachmentInStates() {
		Boolean mandatoryAttachmentInStates = get(PROPERTY_MANDATORY_ATTACHMENT_IN_STEPS);
		return GwtBooleanUtils.isTrue(mandatoryAttachmentInStates);
	}	
	public void setMandatoryAttachmentInStates(boolean mandatoryAttachmentInStates) {
		set(PROPERTY_MANDATORY_ATTACHMENT_IN_STEPS, mandatoryAttachmentInStates);
	}
	public String getMandatoryAttachmentStates() {
		return get(PROPERTY_MANDATORY_ATTACHMENT_STEPS);
	}	
	public void setMandatoryAttachmentStates(String mandatoryAttachmentStates) {
		set(PROPERTY_MANDATORY_ATTACHMENT_STEPS, mandatoryAttachmentStates);
	}
	public boolean isMandatoryAttachmentWhenMetadataHasValue() {
		Boolean mandatoryAttachmentWhenMetadataHasValue = get(PROPERTY_MANDATORY_ATTACHMENT_WHEN_METADATA_HAS_VALUE);
		return GwtBooleanUtils.isTrue(mandatoryAttachmentWhenMetadataHasValue);
	}
	public void setMandatoryAttachmentWhenMetadataHasValue(boolean mandatoryAttachmentWhenMetadataHasValue) {
		set(PROPERTY_MANDATORY_ATTACHMENT_WHEN_METADATA_HAS_VALUE, mandatoryAttachmentWhenMetadataHasValue);
	}
	public String getMetadataNameInMandatoryAttachmentCondition() {
		return get(PROPERTY_METADATA_NAME_IN_MANDATORY_ATTACHMENT_CONDITION);
	}
	public void setMetadataNameInMandatoryAttachmentCondition(String metadataNameInMandatoryAttachmentCondition) {
		set(PROPERTY_METADATA_NAME_IN_MANDATORY_ATTACHMENT_CONDITION, metadataNameInMandatoryAttachmentCondition);
	}
	public String getMetadataValueInMandatoryAttachmentCondition() {
		return get(PROPERTY_METADATA_VALUE_IN_MANDATORY_ATTACHMENT_CONDITION);
	}
	public void setMetadataValueInMandatoryAttachmentCondition(String metadataValueInMandatoryAttachmentCondition) {
		set(PROPERTY_METADATA_VALUE_IN_MANDATORY_ATTACHMENT_CONDITION, metadataValueInMandatoryAttachmentCondition);
	}
	public List<MimeTypeModel> getAllowedAttachmentTypes() {
		return get(PROPERTY_ALLOWED_ATTACHMENT_TYPES);
	}	
	public void setAllowedAttachmentTypes(List<MimeTypeModel> allowedAttachmentTypes) {
		set(PROPERTY_ALLOWED_ATTACHMENT_TYPES, allowedAttachmentTypes);
	}	
	public List<MetadataDefinitionModel> getMetadataDefinitions() {
		return get(PROPERTY_METADATA_DEFINITIONS);
	}	
	public void setMetadataDefinitions(List<MetadataDefinitionModel> metadataDefinitions) {
		set(PROPERTY_METADATA_DEFINITIONS, metadataDefinitions);
	}
	public List<DocumentTypeTemplateModel> getTemplates() {
		return this.get(PROPERTY_TEMPLATES);
	}
	public void setTemplates(List<DocumentTypeTemplateModel> templates) {
		this.set(PROPERTY_TEMPLATES, templates);
	}
	public List<String> getNamesForTemplatesToDelete() {
		return this.get(PROPERTY_NAMES_FOR_TEMPLATES_TO_DELETE);
	}
	public void setNamesForTemplatesToDelete(List<String> namesForTemplatesToDelete) {
		this.set(PROPERTY_NAMES_FOR_TEMPLATES_TO_DELETE, namesForTemplatesToDelete);
	}
	public String getParentDocumentLocationRealNameForDefaultLocation() {
		return get(PROPERTY_PARENT_DOCUMENT_LOCATION_REAL_NAME_FOR_DEFAULT_LOCATION);
	}
	public void setParentDocumentLocationRealNameForDefaultLocation(String parentDocumentLocationRealNameForDefaultLocation) {
		set(PROPERTY_PARENT_DOCUMENT_LOCATION_REAL_NAME_FOR_DEFAULT_LOCATION, parentDocumentLocationRealNameForDefaultLocation);
	}
	public String getFolderIdForDefaultLocation() {
		return get(PROPERTY_FOLDER_ID_FOR_DEFAULT_LOCATION);
	}
	public void setFolderIdForDefaultLocation(String folderIdForDefaultLocation) {
		set(PROPERTY_FOLDER_ID_FOR_DEFAULT_LOCATION, folderIdForDefaultLocation);
	}
}