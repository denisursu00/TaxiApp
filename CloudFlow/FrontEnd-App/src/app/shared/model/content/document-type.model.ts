import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationEntityModel } from "./../organization-entity.model";
import { MimeTypeModel } from "./mime-type.model";
import { MetadataDefinitionModel } from "./metadata-definition.model";
import { DocumentTemplateModel } from "./document-template.model";

@JsonObject
export class DocumentTypeModel {
	
	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String, true)
	public description: string = null;

	@JsonProperty("keepAllVersions", Boolean)
	public keepAllVersions: boolean = false;

	@JsonProperty("allowAnyInitiator", Boolean)
	public allowAnyInitiator: boolean = false;

	@JsonProperty("initiators", [OrganizationEntityModel], true)
	public initiators: OrganizationEntityModel[] = [];

	@JsonProperty("mandatoryAttachment", Boolean)
	public mandatoryAttachment: boolean  = false;

	@JsonProperty("mandatoryAttachmentInStates", Boolean)
	public mandatoryAttachmentInStates: boolean  = false;
	
	@JsonProperty("mandatoryAttachmentStates", String)
	public mandatoryAttachmentStates: string = null;

	@JsonProperty("mandatoryAttachmentWhenMetadataHasValue", Boolean)
	public mandatoryAttachmentWhenMetadataHasValue: boolean = false;

	@JsonProperty("metadataNameInMandatoryAttachmentCondition", String)
	public metadataNameInMandatoryAttachmentCondition: string = null;

	@JsonProperty("metadataValueInMandatoryAttachmentCondition", String)
	public metadataValueInMandatoryAttachmentCondition: string = null;

	@JsonProperty("allowedAttachmentTypes", [MimeTypeModel], true)
	public allowedAttachmentTypes: MimeTypeModel[] = [];

	@JsonProperty("metadataDefinitions", [MetadataDefinitionModel])
	public metadataDefinitions: MetadataDefinitionModel[] = [];

	@JsonProperty("templates", [DocumentTemplateModel], true)
	public templates: DocumentTemplateModel[] = [];

	@JsonProperty("namesForTemplatesToDelete", [String], true)
	public namesForTemplatesToDelete: string[] = [];
	
	@JsonProperty("parentDocumentLocationRealNameForDefaultLocation", String)
	public parentDocumentLocationRealNameForDefaultLocation: string = null;

	@JsonProperty("folderIdForDefaultLocation", String)
	public folderIdForDefaultLocation: string = null;
}