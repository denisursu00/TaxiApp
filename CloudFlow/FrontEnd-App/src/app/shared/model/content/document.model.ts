import { JsonObject, JsonProperty } from "json2typescript";
import { PermissionModel } from "./../permission.model";
import { DocumentAttachmentModel } from "./document-attachment.model";
import { MetadataInstanceModel } from "./metadata-instance.model";
import { MetadataCollectionInstanceModel } from "./metadata-collection-instance.model";
import { JsonDateConverter } from "./../../json-mapper";

@JsonObject
export class DocumentModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("documentTypeId", Number)
	public documentTypeId: number = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("parentFolderId", String)
	public parentFolderId: string = null;

	@JsonProperty("documentName", String)
	public documentName: string = null;

	@JsonProperty("metadataInstances", [MetadataInstanceModel])
	public metadataInstances: MetadataInstanceModel[] = [];
	
	@JsonProperty("documentDescription", String, true)
	public documentDescription: string = null;

	@JsonProperty("author", String, true)
	public author: string = null;

	@JsonProperty("created", JsonDateConverter, true)
	public created: Date = null;

	@JsonProperty("lastModified", JsonDateConverter, true)
	public lastModified: Date = null;

	@JsonProperty("lockedBy", String, true)
	public lockedBy: string = null;
	
	@JsonProperty("permissions", [PermissionModel], true)
	public permissions: PermissionModel[] = [];

	@JsonProperty("attachments", [DocumentAttachmentModel], true)
	public attachments: DocumentAttachmentModel[] = [];

	@JsonProperty("namesForAttachmentsToDelete", [String], true)
	public namesForAttachmentsToDelete: String[] = [];

	@JsonProperty("authorName", String, true)
	public authorName: string = null;

	@JsonProperty("lockerName", String, true)
	public lockerName: string = null;

	@JsonProperty("hasStableVersion", Boolean, true)
	public hasStableVersion: boolean = null;

	@JsonProperty("versionNumber", Number, true)
	public versionNumber: number = null;

	@JsonProperty("workflowStateId", Number, true)
	public workflowStateId: number = null;

	@JsonProperty("metadataCollectionInstances", [MetadataCollectionInstanceModel], true)
	public metadataCollectionInstances: MetadataCollectionInstanceModel[] = [];
}