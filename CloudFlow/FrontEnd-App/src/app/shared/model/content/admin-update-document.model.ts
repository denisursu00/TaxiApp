import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";
import { MetadataCollectionInstanceModel } from "./metadata-collection-instance.model";

@JsonObject
export class AdminUpdateDocumentModel {

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("documentTypeId", Number)
	public documentTypeId: number = null;	

	@JsonProperty("documentName", String)
	public documentName: string = null;

	@JsonProperty("documentDescription", String, true)
	public documentDescription: string = null;

	@JsonProperty("metadataInstances", [MetadataInstanceModel])
	public metadataInstances: MetadataInstanceModel[] = [];
	
	@JsonProperty("metadataCollectionInstances", [MetadataCollectionInstanceModel], true)
	public metadataCollectionInstances: MetadataCollectionInstanceModel[] = [];
}