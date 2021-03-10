import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";
import { MetadataCollectionInstanceModel } from "./metadata-collection-instance.model";
import { AdminUpdateDocumentModel } from "./admin-update-document.model";
import { DocumentTypeModel } from "./document-type.model";

@JsonObject
export class AdminUpdateDocumentBundleModel {

	@JsonProperty("document", AdminUpdateDocumentModel)
	public document: AdminUpdateDocumentModel = null;

	@JsonProperty("documentType", DocumentTypeModel)
	public documentType: DocumentTypeModel = null;
}