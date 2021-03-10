import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";
import { MetadataCollectionInstanceModel } from "./metadata-collection-instance.model";

@JsonObject
export class AutocompleteMetadataResponseModel {
	
	@JsonProperty("metadataInstance", MetadataInstanceModel)
	public metadataInstance: MetadataInstanceModel = null;
	
	@JsonProperty("metadataCollectionInstance", MetadataCollectionInstanceModel)
	public metadataCollectionInstance: MetadataCollectionInstanceModel = null;
}